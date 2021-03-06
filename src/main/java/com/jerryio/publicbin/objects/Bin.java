package com.jerryio.publicbin.objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import com.jerryio.publicbin.PublicBinPlugin;
import com.jerryio.publicbin.disk.PluginSetting;
import com.jerryio.publicbin.enums.OrderEnum;
import com.jerryio.publicbin.util.DateTime;
import com.jerryio.publicbin.util.PluginLog;

public abstract class Bin {
    protected Inventory inventory;
    protected ArrayList<BinItem> binItemList;
    protected ItemStack[] itemPosList;
    protected BukkitTask scheduledUpdateTask;

    private OrderEnum[] cacheAutoRemove;
    private OrderEnum[] cacheSmartGrouping;
    protected long requestCheckTime;

    public Bin(Inventory inventory) {
        this.inventory = inventory;
        getInventory().clear();
        clear();
    }

    public void clear() {
        binItemList = new ArrayList<BinItem>();

        doUpdateUI();
    }

    public void close() {
        getInventory().clear();

        // avoid ConcurrentModificationException
        while (getViewers().size() != 0)
            getViewers().get(0).closeInventory();

        if (scheduledUpdateTask != null)
            Bukkit.getScheduler().cancelTask(scheduledUpdateTask.getTaskId());
    }
    
    public void openInventory(HumanEntity player) {
        Bukkit.getScheduler().runTask(PublicBinPlugin.getInstance(), () -> player.openInventory(getInventory()));
    }    

    public Inventory getInventory() {
        return inventory;
    }

    public List<HumanEntity> getViewers() {
        return System.getProperty("MockTest") == null ? getInventory().getViewers() : Arrays.asList();
    }

    public boolean hasViewer() {
        return getViewers().size() != 0;
    }

    public void requestUpdate() {
        if (scheduledUpdateTask == null || scheduledUpdateTask.isCancelled())
            scheduledUpdateTask = Bukkit.getScheduler().runTask(PublicBinPlugin.getInstance(), () -> interactUpdate());
    }

    private void interactUpdate() {
        scheduledUpdateTask = null;

        boolean changed = doUpdateBinItemList();

        if (changed) {
            forceUpdate();
        }
    }

    public void timeUpdate(long now, int keepingTime) {
        doCountdownDespawnCheck(now, keepingTime);

        forceUpdate();
    }

    private void forceUpdate() {
        doRemoveWhenFull();

        doSmartGrouping();

        doUpdateUI();
        
        doUpdateInventoryViewer();
    }

    private BinItem getBySlotIdx(int slot) {
        for (BinItem target : binItemList) {
            if (target == null) {
                PluginLog.logDebug(Level.WARNING, "Found null " + binItemList.size());
                continue;
            }
            if (target.slot == slot)
                return target;
        }
        return null;
    }

    private void doCountdownDespawnCheck(long now, int keepingTime) {
        Iterator<BinItem> it = binItemList.iterator();
        while (it.hasNext()) {
            BinItem item = it.next();
            if (now >= item.placedTime + keepingTime)
                it.remove();
        }
    }

    private boolean doUpdateBinItemList() {
        boolean changed = false;
        ItemStack[] content = getInventory().getContents();
        ArrayList<BinItem> newBinItemList = new ArrayList<BinItem>();

        if (content.length != itemPosList.length) { // inventory size changed, this shouldn't happen
            binItemList = new ArrayList<BinItem>();
            return true;
        }

        for (int i = 0; i < content.length; i++) {
            ItemStack nowHere = content[i];
            
            if (nowHere != null) {
                BinItem binItem = getBySlotIdx(i);
                if (binItem != null && binItem.item.equals(nowHere)) {
                    newBinItemList.add(binItem);
                } else {
                    newBinItemList.add(new BinItem(nowHere, i));
                    changed = true;
                }
            }
        }

        if (binItemList.size() != newBinItemList.size())
            changed = true;

        binItemList = newBinItemList;

        return changed;
    }

    private void doRemoveWhenFull() {
        PluginSetting setting = PublicBinPlugin.getPluginSetting();
        if (!setting.isRmoveWhenFullEnabled())
            return;
        if (cacheAutoRemove == null)
            cacheAutoRemove = setting.getAutoRemovePrincipleList();

        Inventory inv = getInventory();

        PluginLog.logDebug(Level.INFO, "Inventory used " + binItemList.size() + " & " + setting.getFullThreshold() + " & max = " + inv.getSize());

        // Like, Selection sort algorithms

        while (binItemList.size() + setting.getFullThreshold() > inv.getSize()) {
            BinItem minScoreItem = null;

            for (BinItem target : binItemList) {
                if (minScoreItem == null || target.compareTo(minScoreItem, cacheAutoRemove) < 0) {
                    minScoreItem = target;
                }
            }

            if (minScoreItem == null)
                break; // No items can be removed, but still cannot meet the requirements

            binItemList.remove(minScoreItem);
            PluginLog.logDebug(Level.INFO, "Remove 1 item on slot " + minScoreItem.slot + " & count = " + binItemList.size());
        }

    }

    private void doSmartGrouping() {
        PluginSetting setting = PublicBinPlugin.getPluginSetting();
        if (!setting.isSmartGroupingEnabled())
            return;
        if (cacheSmartGrouping == null)
            cacheSmartGrouping = setting.getSmartGroupingPrincipleList();

        doGrouping();
        doSlotReorder();
    }

    private void doGrouping() {
        long now = DateTime.getTimestamp();

        for (int i = 0; i < binItemList.size(); i++) {
            BinItem mother = binItemList.get(i);
            int motherAmount = mother.item.getAmount();
            int max = mother.item.getMaxStackSize();

            if (motherAmount >= max)
                continue;

            Iterator<BinItem> it = binItemList.listIterator(i + 1);
            while (it.hasNext()) {
                BinItem target = it.next();
                if (!target.item.isSimilar(mother.item))
                    continue;

                int targetAmount = target.item.getAmount();
                if (motherAmount + targetAmount > max) {
                    mother.item.setAmount(max);
                    target.item.setAmount(motherAmount + targetAmount - max);

                    mother.placedTime = now;
                    target.placedTime = now;
                    break; // mother full, break;
                } else {
                    motherAmount += targetAmount;
                    mother.item.setAmount(motherAmount);
                    it.remove();

                    mother.placedTime = now;

                    if (motherAmount >= max)
                        break; // mother full, break;
                }
            }
        }
    }

    private void doSlotReorder() {
        final Comparator<BinItem> compare = new Comparator<BinItem>() {
            @Override
            public int compare(BinItem o1, BinItem o2) {
                return o1.compareTo(o2, cacheSmartGrouping) * -1; // descending order
            }
        };

        Collections.sort(binItemList, compare);

        for (int i = 0; i < binItemList.size(); i++) {
            binItemList.get(i).slot = i;
        }
    }

    private void doUpdateUI() {
        PluginSetting setting = PublicBinPlugin.getPluginSetting();
        Inventory inv = getInventory();
        ItemStack[] content = new ItemStack[inv.getSize()];

        requestCheckTime = Long.MAX_VALUE;
        int keepingTime = setting.getKeepingTime() * 1000; // in seconds

        for (BinItem target : binItemList) {
            content[target.slot] = target.item;
            requestCheckTime = Math.min(requestCheckTime, target.placedTime + keepingTime);
        }

        inv.setContents(content);
        itemPosList = content;
    }
    
    private void doUpdateInventoryViewer() {
        Iterator<HumanEntity> it = getViewers().iterator();
        
        try {
            while (it.hasNext())
                ((Player)it.next()).updateInventory();
        } catch (ConcurrentModificationException e) {
            PluginLog.logDebug(Level.INFO, "Iterator error while tring to update inventory viewer");
        }
    }
}
