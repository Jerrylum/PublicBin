package com.jerryio.publicbin.objects;

import org.bukkit.Bukkit;

import com.jerryio.publicbin.PublicBinPlugin;

public class PublicBin extends Bin {

    public PublicBin() {
        super(Bukkit.createInventory(null, 9 * PublicBinPlugin.getPluginSetting().getBinRow(), "Trash Bin (Public)"));

    }

}
