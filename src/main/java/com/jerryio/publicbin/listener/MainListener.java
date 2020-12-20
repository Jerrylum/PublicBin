package com.jerryio.publicbin.listener;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;

import com.jerryio.publicbin.PublicBinPlugin;
import com.jerryio.publicbin.util.PluginLog;

public class MainListener implements Listener {

    public static MainListener load(PublicBinPlugin plugin) {
        MainListener rtn;
        Bukkit.getPluginManager().registerEvents(rtn = new MainListener(), plugin);

        return rtn;
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClickEvent​(InventoryClickEvent event) {
        if (event.isCancelled()) return;
//        PluginLog.log(Level.INFO, event.getAction() + " | " 
//                + event.getClick() + " | " 
//                + event.getInventory().getSize()+ " | "
//                + event.getClickedInventory().getSize() + " | "
//                + event.getView().getTitle() + " | "
//                + event.getSlot() + " | "
//                + (event.getCurrentItem() != null));
    }
}
