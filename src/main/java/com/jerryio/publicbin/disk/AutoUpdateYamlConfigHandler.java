package com.jerryio.publicbin.disk;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.bukkit.configuration.file.YamlConfiguration;

import com.jerryio.publicbin.PublicBinPlugin;
import com.jerryio.publicbin.util.PluginLog;
import com.jerryio.publicbin.util.Version;

public class AutoUpdateYamlConfigHandler extends BasicYamlConfigHandler {

    public static YamlConfiguration loadYaml(PublicBinPlugin plugin, String name) {
        // important, in order to keep all comment messages in the file, use this method to save the default config file
      
        boolean ok;
        CommentYamlConfiguration config = new CommentYamlConfiguration();
  
        File configFile = new File(plugin.getDataFolder(), name);
        boolean copyTemplate = !configFile.exists();
        
        if (copyTemplate) {
            plugin.getDataFolder().mkdirs();
            plugin.saveResource(name, true);
        }
          
        ok = BasicYamlConfigHandler.loadFromFile(config, configFile);
        if (!ok) return null;
          
        CommentYamlConfiguration template = new CommentYamlConfiguration();
        ok = BasicYamlConfigHandler.loadFromInputStream(template, plugin.getResource(name));
        if (!ok) return config; // maybe this version of the plugin do not support this file, just skip version check. 

        if (!copyTemplate) {
            String pluginVerStr = template.getString("version", "1.0.1");
            String configVerStr = config.getString("version", "1.0.1");
            Version pluginVer = new Version(pluginVerStr);
            Version configVer = new Version(configVerStr);
              
            if (pluginVer.compareTo(configVer) > 0) { // need to update the config file
                try {
                    PluginLog.info("Trying to update an older yaml file. File version = " + configVerStr);
                    
                    backupFile(configFile);
                      
                    config.addDefaultsWithComments(template);
                    config.options().copyDefaults(true);
                    config.set("version", pluginVerStr);
                    config.save(configFile);
                      
                    PluginLog.info("File updated successfully.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    PluginLog.warn("File update failed.");
                    return null;
                }
            }
        }
          
        
        return config;
    }
    
    public static File backupFile(File original) throws IOException {
        File backup = getNonExistsBackupFile(original);
        backup.getParentFile().mkdir();
        Files.copy(original.toPath(), backup.toPath());
        
        PluginLog.info("File backed up successfully. " + original.getName() + " --> backup/" + backup.getName());
        
        return backup;
    }
    
    public static File getNonExistsBackupFile(File original) {
        String name = original.getName();
        
        Date date = Calendar.getInstance().getTime();  
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
        String strDate = dateFormat.format(date);
        
        File backupFolder = new File(original.getParentFile(), "backup");
        
        File backup = new File(backupFolder, "backup " + strDate + " " + name);
        int idx = 1;
        while (backup.exists()) backup = new File(backupFolder, "backup " + strDate + " (" + (idx++) + ") " + name);
        
        return backup;
    }
}
