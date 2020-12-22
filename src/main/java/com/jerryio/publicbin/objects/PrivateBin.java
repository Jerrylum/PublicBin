package com.jerryio.publicbin.objects;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PrivateBin extends Bin {

    public PrivateBin(Player p) {
        super(Bukkit.createInventory(p, 9 * 6, "Trash Bin (" + p.getName() + ")"));        
        
    }

}
