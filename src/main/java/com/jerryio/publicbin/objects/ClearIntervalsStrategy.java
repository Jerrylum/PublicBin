package com.jerryio.publicbin.objects;

import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;

import com.jerryio.publicbin.PublicBinPlugin;
import com.jerryio.publicbin.disk.PluginSetting;
import com.jerryio.publicbin.enums.WarningEnum;
import com.jerryio.publicbin.util.DateTime;
import com.jerryio.publicbin.util.I18n;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class ClearIntervalsStrategy extends Strategy {
    private long timeStart = 0;
    private int cacheIntervalsTime;
    private WarningEnum cacheWarningType;
    private List<Integer> warningCounter;
    
    public ClearIntervalsStrategy(BinManager manager) {
        super(manager);

        
        PluginSetting setting = PublicBinPlugin.getPluginSetting();
        cacheIntervalsTime = setting.getClearIntervalsTime() * 1000;
        cacheWarningType = setting.getClearWarningMessageType();
        
        start();
    }

    @Override
    public void tickCheck() {
        long now = DateTime.getTimestamp();
        long gap = now - timeStart;
        
        Iterator<Integer> i = warningCounter.iterator();
        while (i.hasNext()) {
           Integer seconds = i.next();
           if ((cacheIntervalsTime - gap) <= (seconds * 1000)) {
               warning(seconds);
               i.remove();
           }
        }
        
        if (gap >= cacheIntervalsTime) {
            clear();
        }
    }
        
    private void start() {
        PluginSetting setting = PublicBinPlugin.getPluginSetting();
        
        timeStart = DateTime.getTimestamp();
        warningCounter = setting.getClearWarningPeriod();
    }

    private void clear() {
        for (Bin bin : manager.getAllBin()) {
            bin.getInventory().clear();
            bin.requestUpdate();
        }
        start();
    }
    
    private void warning(int seconds) {
        
        Bukkit.getOnlinePlayers().forEach(player -> {
            String msg = seconds != 0 ? I18n.n(player, "warning-clear-time-left", seconds) : I18n.n(player, "warning-clear-execute");
            
            if (cacheWarningType == WarningEnum.ACTIONBAR) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(msg));
            } else {
                player.sendMessage(msg);
            }
        });
    }
}
