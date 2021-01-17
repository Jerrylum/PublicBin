package com.jerryio.publicbin.objects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import com.jerryio.publicbin.PublicBinPlugin;
import com.jerryio.publicbin.disk.PluginSetting;
import com.jerryio.publicbin.enums.ModeEnum;

public abstract class BinManager {

    private BukkitTask scheduledTask;
    private List<Strategy> allClearStractegies;

    public static BinManager load(PublicBinPlugin plugin) {
        BinManager rtn = plugin.setting.getMode() == ModeEnum.ShareMode ? new PublicBinManager() : new PrivateBinManager();

        for (Player p : Bukkit.getServer().getOnlinePlayers())
            rtn.onPlayerJoin(p);

        return rtn;
    }

    public BinManager() {
        PublicBinPlugin plugin = PublicBinPlugin.getInstance();
        PluginSetting setting = PublicBinPlugin.getPluginSetting();
        
        allClearStractegies = new ArrayList<>();
        
        if (setting.isAutoDespawnEnabled())
            allClearStractegies.add(new CountdownDespawnStrategy(this));
        
        if (setting.isClearIntervalsEnabled())
            allClearStractegies.add(new ClearIntervalsStrategy(this));
        
        scheduledTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> doTickCheck(), 20, 20);

    }

    public void close() {
        for (Bin bin : getAllBin()) {
            bin.close();
        }

        if (scheduledTask != null)
            Bukkit.getScheduler().cancelTask(scheduledTask.getTaskId());
        
        scheduledTask = null;
    }

    private void doTickCheck() {        
        for (Strategy s : allClearStractegies)
            s.tickCheck();
    }

    public abstract Bin getUsableBin(Player p);

    public abstract Collection<Bin> getAllBin();

    public abstract void onPlayerJoin(Player p);

    public abstract void onPlayerQuit(Player p);

}
