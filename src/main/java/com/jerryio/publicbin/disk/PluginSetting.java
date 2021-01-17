package com.jerryio.publicbin.disk;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.configuration.file.YamlConfiguration;

import com.jerryio.publicbin.PublicBinPlugin;
import com.jerryio.publicbin.enums.ModeEnum;
import com.jerryio.publicbin.enums.OrderEnum;
import com.jerryio.publicbin.enums.WarningEnum;
import com.jerryio.publicbin.util.I18n;
import com.jerryio.publicbin.util.PluginLog;

public class PluginSetting {
    
    public static final String[] OLD_CONFIG_MD5_CHECKSUMS = {
            "1a3144fbbd4835c947010725b681e6c5"
    };

    private YamlConfiguration config;

    public static PluginSetting load(PublicBinPlugin plugin) {
        return new PluginSetting(AutoUpdateYamlConfigHandler.loadYaml(plugin, "config.yml", OLD_CONFIG_MD5_CHECKSUMS));
    }
    
    private PluginSetting(YamlConfiguration config) {
        if (config != null) {
            this.config = config;
        } else {
            this.config = new YamlConfiguration();
        }
    }
    
    @Deprecated
    public YamlConfiguration getConfig() {
        return config;
    }
    
    public String getLang() {
        return config.getString("lang", "en_US"); 
    }
    
    public ModeEnum getMode() {
        return "share".equalsIgnoreCase(config.getString("mode")) ? ModeEnum.ShareMode : ModeEnum.SeparateMode; 
    }
    
    public int getBinRow() {
        return Math.min(Math.max(1, config.getInt("size", 6)), 6);
    }
    
    public boolean isAutoDespawnEnabled() {
        return config.getBoolean("countdown-despawn.enable", false);
    }
    
    public int getKeepingTime() {
        return config.getInt("countdown-despawn.time", 300);
    }
    
    public boolean isClearIntervalsEnabled() {
        return config.getBoolean("clear-intervals.enable", false);
    }
    
    public int getClearIntervalsTime() {
        return config.getInt("clear-intervals.time", 600);
    }
    
    public WarningEnum getClearWarningMessageType() {
        return "actionbar".equalsIgnoreCase(config.getString("clear-intervals.warnings.type")) ? WarningEnum.ACTIONBAR : WarningEnum.TYPE; 
    }
    
    public List<Integer> getClearWarningPeriod() {
        return config.getIntegerList("clear-intervals.warnings.period"); 
    }
    
    public boolean isRmoveWhenFullEnabled() {
        return config.getBoolean("remove-when-full.enable", false);
    }
    
    public int getFullThreshold() {
        return Math.max(1, config.getInt("remove-when-full.threshold", 1));
    }
    
    public OrderEnum[] getAutoRemovePrincipleList() {
        return getPrincipleList("remove-when-full.order");
    }
    
    public boolean isSmartGroupingEnabled() {
        return config.getBoolean("smart-grouping.enable", false);
    }
    
    public OrderEnum[] getSmartGroupingPrincipleList() {
        return getPrincipleList("smart-grouping.order");
    }
    
    public boolean isDebug() {
        return config.getBoolean("debug", false);
    }
    
    private OrderEnum[] getPrincipleList(String path) {
        List<String> raw = config.getStringList(path);
        List<OrderEnum> rtn = new ArrayList<OrderEnum>();
        
        for (int i = 0; i < raw.size(); i++) {
            String target = raw.get(i);
            
            try {
                char[] alpha = target.toLowerCase().toCharArray();
                alpha[0] = Character.toUpperCase(alpha[0]);
                rtn.add(OrderEnum.valueOf(new String(alpha)));
            }catch (Exception e) {
                PluginLog.log(Level.WARNING, I18n.t("config-unknown-sorting-param", target));
            }
            
        }

        return rtn.toArray(new OrderEnum[rtn.size()]);
    }

}
