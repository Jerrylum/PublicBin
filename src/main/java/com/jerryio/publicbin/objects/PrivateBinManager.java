package com.jerryio.publicbin.objects;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

public class PrivateBinManager extends BinManager {

    private HashMap<UUID, Bin> storage = new HashMap<UUID, Bin>();

    @Override
    public Bin getUsableBin(Player p) {
        return storage.getOrDefault(p.getUniqueId(), null);
    }

    @Override
    public Collection<Bin> getAllBin() {
        return storage.values();
    }

    public void onPlayerJoin(Player p) {
        storage.put(p.getUniqueId(), new PrivateBin(p));
    }

    public void onPlayerQuit(Player p) {
        storage.remove(p.getUniqueId());
    }

}
