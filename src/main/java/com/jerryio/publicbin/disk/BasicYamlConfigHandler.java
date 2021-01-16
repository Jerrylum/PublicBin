package com.jerryio.publicbin.disk;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.jerryio.publicbin.PublicBinPlugin;
import com.jerryio.publicbin.util.PluginLog;

public class BasicYamlConfigHandler {

    public static YamlConfiguration loadYaml(PublicBinPlugin plugin, String name) {
        // important, in order to keep all comment messages in the file, use this method to save the default config file
        
        File configFile = new File(plugin.getDataFolder(), name);
        if (!configFile.exists()) {
            plugin.getDataFolder().mkdirs();
            plugin.saveResource(name, true);
        }

        YamlConfiguration config = new YamlConfiguration();
        
        if (loadFromFile(config, configFile))
            return config;
        else
            return null;
    }
    
    public static boolean loadFromFile(FileConfiguration yaml, File file) {
        try {
            yaml.load(file);
            return true;
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
            PluginLog.log(Level.WARNING, "The file \"" + file.getName() + "\" is not a valid YAML file! Please check it with a tool like http://yaml-online-parser.appspot.com/");
            return false;
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
            PluginLog.log(Level.WARNING, "The file \"" + file.getName() + "\" cannot be read! Was the file in use?");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            PluginLog.log(Level.WARNING, "Unhandled exception while reading the configuration!");
            return false;
        }
    }
    
    public static boolean loadFromInputStream(FileConfiguration yaml, InputStream stream) {
        try {
            yaml.load(new InputStreamReader(stream));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            PluginLog.log(Level.WARNING, "Unhandled exception while reading input stream!");
            return false;
        }
    }
    
    
}
