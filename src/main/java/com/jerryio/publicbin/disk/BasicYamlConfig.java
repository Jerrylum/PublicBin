package com.jerryio.publicbin.disk;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import com.jerryio.publicbin.PublicBinPlugin;
import com.jerryio.publicbin.util.PluginLog;

public class BasicYamlConfig {

    public static YamlConfiguration loadYaml(PublicBinPlugin plugin, String name) {
        // important, in order to keep all comment messages in the file, use this method to save the default config file
        
        File configFile = new File(plugin.getDataFolder(), name);
        if (!configFile.exists()) {
            plugin.getDataFolder().mkdirs();
            plugin.saveResource(name, true);
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
       
        return config;
    }
}
