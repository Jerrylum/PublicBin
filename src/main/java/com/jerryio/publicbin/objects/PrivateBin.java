package com.jerryio.publicbin.objects;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.jerryio.publicbin.PublicBinPlugin;

public class PrivateBin extends Bin {

    public PrivateBin(Player p) {
        super(Bukkit.createInventory(p, 9 * PublicBinPlugin.getPluginSetting().getBinRow(), "Trash Bin (" + p.getName() + ")"));

    }

}
