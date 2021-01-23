package com.jerryio.publicbin.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

import com.jerryio.publicbin.PublicBinPlugin;
import com.jerryio.publicbin.objects.Bin;
import com.jerryio.publicbin.util.BukkitVersion;

public class MainListener implements Listener {

    public static MainListener load(PublicBinPlugin plugin) {
        MainListener rtn;
        Bukkit.getPluginManager().registerEvents(rtn = new MainListener(), plugin);

        return rtn;
    }

    // We process the click event when the player is allowed to.
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onInventoryClickEvent​(InventoryClickEvent event) {
        Bin usingBin = getInteractBin(event);
        if (usingBin == null)
            return;

        usingBin.requestUpdate();
    }

    // We process the click event when the player is allowed to.
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onInventoryDragEvent​(InventoryDragEvent event) {
        Bin usingBin = getInteractBin(event);
        if (usingBin == null)
            return;

        usingBin.requestUpdate();
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onInventoryOpenEvent​(InventoryOpenEvent event) {
        Player p = (Player) event.getPlayer();
        Bin usingBin = getInteractBin(event.getInventory(), p);
        if (usingBin == null)
            return;

        playBinSound(p, "open");
    }
    
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onInventoryCloseEvent​(InventoryCloseEvent event) {
        Player p = (Player) event.getPlayer();
        Bin usingBin = getInteractBin(event.getInventory(), p);
        if (usingBin == null)
            return;

        playBinSound(p, "close");
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void OnPlayerJoinEvent(PlayerJoinEvent event) {
        PublicBinPlugin.getBinManager().onPlayerJoin(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void OnPlayerQuitEvent(PlayerQuitEvent event) {
        PublicBinPlugin.getBinManager().onPlayerQuit(event.getPlayer());
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onItemDespawnEvent(ItemDespawnEvent event) {
        PublicBinPlugin.getBinManager().trackDroppedItem(event.getEntity());
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDamageEvent(EntityDamageEvent event) {
        Entity e = event.getEntity();
        if (e.getType() == EntityType.DROPPED_ITEM)
            PublicBinPlugin.getBinManager().trackDroppedItem((Item) e);
    }
    
    @EventHandler
    public void onEntityPickupItemEvent(EntityPickupItemEvent event) {
        PublicBinPlugin.getBinManager().untrackDroppedItem(event.getItem());
    }

    private Bin getInteractBin(InventoryInteractEvent event) {
        Inventory topInventory = event.getInventory();
        Player p = (Player) event.getWhoClicked();
        return getInteractBin(topInventory, p);
    }

    private Bin getInteractBin(Inventory topInventory, Player p) {
        Bin usableBin = PublicBinPlugin.getBinManager().getUsableBin(p);

        // Determine whether the user is using a trash bin (that they can use)
        if (usableBin.getInventory().equals(topInventory)) {
            return usableBin;
        } else {
            return null;
        }
    }
    
    private void playBinSound(Player p, String sub) {
        boolean verCheck = BukkitVersion.isSupport("1.15");
        p.playSound(p.getLocation(), (verCheck ? "block.barrel." : "block.shulker_box.") + sub, 1, 2f);
    }
}
