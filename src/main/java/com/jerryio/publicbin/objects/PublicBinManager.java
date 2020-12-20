package com.jerryio.publicbin.objects;

import org.bukkit.entity.Player;

public class PublicBinManager extends BinManager {

    private PublicBin bin;
    
    public PublicBinManager() {
        bin = new PublicBin();
    }

    @Override
    public Bin getUsableBin(Player p) {
        return bin;
    }
    
    
}
