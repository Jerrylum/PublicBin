package com.jerryio.publicbin.objects;

import org.bukkit.Bukkit;

public class PublicBin extends Bin {

    
    public PublicBin() {
        super(Bukkit.createInventory(null, 9 * 6, "Trash Bin (Public)"));        
        
    }

}
