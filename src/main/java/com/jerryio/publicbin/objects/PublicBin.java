package com.jerryio.publicbin.objects;

import org.bukkit.Bukkit;

import com.jerryio.publicbin.PublicBinPlugin;
import com.jerryio.publicbin.util.I18n;

public class PublicBin extends Bin {

    public PublicBin() {
        super(Bukkit.createInventory(
                null, 
                9 * PublicBinPlugin.getPluginSetting().getBinRow(), 
                I18n.t("public-bin-title")));

    }

}
