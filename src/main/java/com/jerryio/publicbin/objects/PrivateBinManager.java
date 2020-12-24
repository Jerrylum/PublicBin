package com.jerryio.publicbin.objects;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

public class PrivateBinManager extends BinManager {

    private HashMap<UUID, Bin> storage = new HashMap<UUID, Bin>();

    @Override
    public Bin getUsableBin(Player p) {
        if (storage.containsKey(p.getUniqueId()))
            return storage.get(p.getUniqueId());
        else
            return createNewBin(p);
    }

    @Override
    public Collection<Bin> getAllBin() {
        return storage.values();
    }

    public void onPlayerJoin(Player p) {
        // Nothing to do
    }

    public void onPlayerQuit(Player p) {
        storage.remove(p.getUniqueId());
    }
    
    private Bin createNewBin(Player p) {
        Bin rtn = new PrivateBin(p);
        storage.put(p.getUniqueId(), rtn);
        
        return rtn;
    }

}
