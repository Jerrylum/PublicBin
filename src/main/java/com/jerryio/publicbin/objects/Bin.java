package com.jerryio.publicbin.objects;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public abstract class Bin {
    protected Inventory inventory;
    
    public Bin(Inventory inventory) {
        this.inventory = inventory;
    }
    
    public void clear() {
        
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
}
