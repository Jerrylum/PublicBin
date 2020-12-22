package com.jerryio.publicbin.objects;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.jerryio.publicbin.PublicBinPlugin;
import com.jerryio.publicbin.util.I18n;

public class PrivateBin extends Bin {

    public PrivateBin(Player p) {
        super(Bukkit.createInventory(p, 9 * PublicBinPlugin.getPluginSetting().getBinRow(), I18n.t("private-bin-title")));

    }

}
