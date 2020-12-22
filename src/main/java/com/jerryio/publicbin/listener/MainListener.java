package com.jerryio.publicbin.listener;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;

import com.jerryio.publicbin.PublicBinPlugin;
import com.jerryio.publicbin.objects.Bin;

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
        if (usingBin == null) return;

        usingBin.requestUpdate();
    }
    
    // We process the click event when the player is allowed to.
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onInventoryDragEvent​(InventoryDragEvent event) {       
        Bin usingBin = getInteractBin(event);
        if (usingBin == null) return;

        usingBin.requestUpdate();
    }
    
    private Bin getInteractBin(InventoryInteractEvent event) {
        Inventory topInventory = event.getInventory();
        Player p = (Player)event.getWhoClicked();
        Bin usableBin = PublicBinPlugin.getBinManager().getUsableBin(p);
        
        // Determine whether the user is using a trash bin (that they can use)
        if (usableBin.getInventory().equals(topInventory)) {
            return usableBin;
        } else {
            return null;
        }
    }
}
