package com.jerryio.publicbin.disk;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import com.jerryio.publicbin.PublicBinPlugin;
import com.jerryio.publicbin.enums.ModeEnum;
import com.jerryio.publicbin.enums.OrderEnum;
import com.jerryio.publicbin.util.I18n;
import com.jerryio.publicbin.util.PluginLog;

public class PluginSetting {
    
//    private PublicBinPlugin plugin;
    
    private YamlConfiguration config;

    public static PluginSetting load(PublicBinPlugin plugin) {
        // important, in order to keep all comment messages in the file, use this method to save the default config file
        
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            plugin.getDataFolder().mkdirs();
            plugin.saveResource("config.yml", true);
        }
        
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.load(configFile);
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
            PluginLog.log(Level.WARNING, "The configuration is not a valid YAML file! Please check it with a tool like http://yaml-online-parser.appspot.com/");
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            PluginLog.log(Level.WARNING, "I/O error while reading the configuration. Was the file in use?");
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            PluginLog.log(Level.WARNING, "Unhandled exception while reading the configuration!");
            return null;
        }

        return new PluginSetting(config);
    }
    
    private PluginSetting(YamlConfiguration config) {
        this.config = config;
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
        return config.getInt("countdown-despawn.time", 0);
    }
    
    public boolean isRmoveWhenFullEnabled() {
        return config.getBoolean("remove-when-full.enable", false);
    }
    
    public int getFullThreshold() {
        return Math.max(1, config.getInt("remove-when-full.threshold", 1));
    }
    
    public OrderEnum[] getRemoveOrderList() {
        return getOrderList("remove-when-full.order");
    }
    
    public boolean isSmartGroupingEnabled() {
        return config.getBoolean("smart-grouping.enable", false);
    }
    
    public OrderEnum[] getSmartGroupingOrderList() {
        return getOrderList("smart-grouping.order");
    }
    
    public boolean isDebug() {
        return config.getBoolean("debug", false);
    }
    
    private OrderEnum[] getOrderList(String path) {
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
