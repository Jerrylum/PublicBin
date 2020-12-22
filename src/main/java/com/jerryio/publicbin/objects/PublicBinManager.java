package com.jerryio.publicbin.objects;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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

    @Override
    public Collection<Bin> getAllBin() {
        HashSet<Bin> rtn = new HashSet<Bin>();
        rtn.add(bin);
        return rtn;
    }
    
    public void onPlayerJoin(Player p) {
        // Nothing to do
    }
    
    public void onPlayerQuit(Player p) {
        // Nothing to do
    }
}
