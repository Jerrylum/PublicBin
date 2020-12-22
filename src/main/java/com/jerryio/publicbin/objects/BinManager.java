package com.jerryio.publicbin.objects;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import com.jerryio.publicbin.PublicBinPlugin;
import com.jerryio.publicbin.disk.PluginSetting;

public abstract class BinManager {
    
    private BukkitTask scheduledTask;
    private int cacheKeepingTime;
    
    public BinManager() {
        PublicBinPlugin plugin = PublicBinPlugin.getInstance();
        PluginSetting setting = PublicBinPlugin.getPluginSetting();
        
        cacheKeepingTime = setting.getKeepingTime() * 1000;
        int minWaitTime = cacheKeepingTime / 1000 * 20;
        
        if (setting.isAutoDespawnEnabled())
            scheduledTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> doCountdownDespawnCheck(), minWaitTime, 20);
    }
    
    public void close() {
        for(Bin bin : getAllBin()) {
            bin.close();
        }
        
        if (scheduledTask != null)
            Bukkit.getScheduler().cancelTask(scheduledTask.getTaskId());
    }
    
    private void doCountdownDespawnCheck() {
        long now = new Date().getTime();
        
        for (Bin bin : getAllBin()) {
            if (now < bin.requestCheckTime) continue;
            
            bin.timeUpdate(now, cacheKeepingTime);
        }
    }
    
    public abstract Bin getUsableBin(Player p);
    
    public abstract Set<Bin> getAllBin();
}
