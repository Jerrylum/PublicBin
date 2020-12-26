package com.jerryio.publicbin.objects;

import java.util.Collection;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import com.jerryio.publicbin.PublicBinPlugin;
import com.jerryio.publicbin.disk.PluginSetting;
import com.jerryio.publicbin.enums.ModeEnum;
import com.jerryio.publicbin.util.DateTime;

public abstract class BinManager {

    private BukkitTask scheduledTask;
    private int cacheKeepingTime;

    public static BinManager load(PublicBinPlugin plugin) {
        BinManager rtn = plugin.setting.getMode() == ModeEnum.ShareMode ? new PublicBinManager() : new PrivateBinManager();

        for (Player p : Bukkit.getServer().getOnlinePlayers())
            rtn.onPlayerJoin(p);

        return rtn;
    }

    public BinManager() {
        PublicBinPlugin plugin = PublicBinPlugin.getInstance();
        PluginSetting setting = PublicBinPlugin.getPluginSetting();

        cacheKeepingTime = setting.getKeepingTime() * 1000;
        int minWaitTime = cacheKeepingTime / 1000 * 20;

        if (setting.isAutoDespawnEnabled())
            scheduledTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> doCountdownDespawnCheck(), minWaitTime, 20);

    }

    public void close() {
        for (Bin bin : getAllBin()) {
            bin.close();
        }

        if (scheduledTask != null)
            Bukkit.getScheduler().cancelTask(scheduledTask.getTaskId());
    }

    private void doCountdownDespawnCheck() {
        long now = DateTime.getTimestamp();

        for (Bin bin : getAllBin()) {
            if (now < bin.requestCheckTime)
                continue;

            bin.timeUpdate(now, cacheKeepingTime);
        }
    }

    public abstract Bin getUsableBin(Player p);

    public abstract Collection<Bin> getAllBin();

    public abstract void onPlayerJoin(Player p);

    public abstract void onPlayerQuit(Player p);

}
