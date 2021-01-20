package com.jerryio.publicbin.objects;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.entity.Item;
import org.bukkit.inventory.Inventory;

public class CollectDespawnStrategy extends Strategy {

    private List<Item> trackingItems;
    
    public CollectDespawnStrategy(BinManager manager) {
        super(manager);
        
        trackingItems = new ArrayList<>();
    }

    @Override
    public void tickCheck() {        
        Inventory inv = manager.getUsableBin(null).getInventory();
        int pos = inv.firstEmpty();
        
        if (pos == -1) { // no empty slot
            trackingItems.clear();
            return;
        }

        boolean needUpdate = false;
        
        Iterator<Item> i = trackingItems.iterator();
        while (i.hasNext()) {
           Item item = i.next();
           
           if (item.isDead()) { // despawn
               inv.setItem(pos, item.getItemStack());
               needUpdate = true;
               i.remove();
               
               pos = inv.firstEmpty();
               if (pos == -1) break;
           }
        }

        if (needUpdate) 
            manager.getUsableBin(null).requestUpdate();
    }

    public void track(Item item) {
        // no item.getPickupDelay() check
        if (!trackingItems.contains(item))
            trackingItems.add(item);
    }
    
    public void untrack(Item item) {
        trackingItems.remove(item);
    }
    
    
}
