package com.jerryio.publicbin.objects;

import java.util.Set;

import org.bukkit.entity.Player;

public abstract class BinManager {
    
    public abstract Bin getUsableBin(Player p);
    
    public abstract Set<Bin> getAllBin();
}
