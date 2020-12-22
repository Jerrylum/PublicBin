package com.jerryio.publicbin.objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import com.jerryio.publicbin.PublicBinPlugin;
import com.jerryio.publicbin.disk.PluginSetting;
import com.jerryio.publicbin.enums.OrderEnum;
import com.jerryio.publicbin.util.PluginLog;

public abstract class Bin {
    protected Inventory inventory;
    protected ArrayList<BinItem> binItemList;
    protected ItemStack[] itemPosList;
    protected BukkitTask scheduledUpdateTask;
    protected BukkitTask scheduledDespawnTask;
    
    private OrderEnum[] cacheRemoveOrderList;
    private OrderEnum[] cacheSmartGrouping;
    private long requestCheckTime;

    public Bin(Inventory inventory) {
        inventory.clear();
        this.inventory = inventory;
        clear();
    }
    
    public void clear() {
        binItemList = new ArrayList<BinItem>();
        
        doUpdateUI();
    }
    
    public Inventory getInventory() {
        return inventory;
    }
    
    public List<HumanEntity> getViewers() {
        return getInventory().getViewers();
    }
    
    public boolean hasViewer() {
        return getViewers().size() != 0;
    }
    
    public void requestUpdate() {        
        if (scheduledUpdateTask == null || scheduledUpdateTask.isCancelled())
            scheduledUpdateTask = Bukkit.getScheduler().runTask(PublicBinPlugin.getInstance(), () -> interactUpdate());
    }
    
    protected void interactUpdate() {
        scheduledUpdateTask = null;
        
        boolean changed = doUpdateBinItemList();

        if (changed) {            
            forceUpdate();
        }
    }
    
    protected void forceUpdate() {
        doRemoveWhenFull();
        
        doSmartGrouping();
        
        doUpdateUI();
        
        doScheduledDespawnTask();
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
    
    private void doCountdownDespawnCheck() {
        scheduledDespawnTask = null;
        
        long now = new Date().getTime();
        int keepingTime = PublicBinPlugin.getPluginSetting().getKeepingTime() * 1000;
        
        Iterator<BinItem> it = binItemList.iterator();
        while (it.hasNext()) {
            BinItem item = it.next();
            if (now >= item.placedTime + keepingTime)
                it.remove();
        }
        
        forceUpdate();
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
        
        if (binItemList.size() != newBinItemList.size()) changed = true;
        
        binItemList = newBinItemList;
        
        
        return changed;
    }
    
    private void doRemoveWhenFull() {
        PluginSetting setting = PublicBinPlugin.getPluginSetting();
        if (!setting.isRmoveWhenFullEnabled()) return;
        if (cacheRemoveOrderList == null) cacheRemoveOrderList = setting.getRemoveOrderList();
        
        Inventory inv = getInventory();
        
        PluginLog.logDebug(Level.INFO, "Inventory used " + binItemList.size() + " & " + setting.getFullThreshold() + " & max = " + inv.getSize());
        
        // Like, Selection sort algorithms
        
        while (binItemList.size() + setting.getFullThreshold() > inv.getSize()) {
            BinItem minScoreItem = null;
            
            for (BinItem target : binItemList) {
                if (minScoreItem == null || target.compareTo(minScoreItem, cacheRemoveOrderList) < 0) {
                    minScoreItem = target;
                }
            }

            if (minScoreItem == null) break; // No items can be removed, but still cannot meet the requirements
            
            binItemList.remove(minScoreItem);
            PluginLog.logDebug(Level.INFO, "Remove 1 item on slot " + minScoreItem.slot + " & count = " + binItemList.size());
        }
        
    }
    
    private void doSmartGrouping() {
        PluginSetting setting = PublicBinPlugin.getPluginSetting();
        if (!setting.isSmartGroupingEnabled()) return;
        if (cacheSmartGrouping == null) cacheSmartGrouping = setting.getSmartGroupingOrderList();
        
        doGrouping();
        doSlotReorder();
    }
    
    private void doGrouping() {
        long now = new Date().getTime();

        for (int i = 0; i < binItemList.size(); i++) {
            BinItem mother = binItemList.get(i);
            int motherAmount = mother.item.getAmount();
            int max = mother.item.getMaxStackSize();
            
            if (motherAmount >= max) continue;

            Iterator<BinItem> it = binItemList.listIterator(i + 1);
            while (it.hasNext()) {
                BinItem target = it.next();
                if (!target.item.isSimilar(mother.item)) continue;
                
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

                    if (motherAmount >= max) break; // mother full, break;
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
    
    private void doScheduledDespawnTask() {
        PluginSetting setting = PublicBinPlugin.getPluginSetting();
        if (!setting.isAutoDespawnEnabled()) return;
        if (scheduledDespawnTask != null) scheduledDespawnTask.cancel();
        
        // covert to seconds, 1 second == 20 ticks, add 1 tick
        long delay = (long)((requestCheckTime - new Date().getTime()) / 1000.0 * 20 + 1);
        scheduledDespawnTask = Bukkit.getScheduler().runTaskLater(PublicBinPlugin.getInstance(), () -> doCountdownDespawnCheck(), delay);
    }
}
