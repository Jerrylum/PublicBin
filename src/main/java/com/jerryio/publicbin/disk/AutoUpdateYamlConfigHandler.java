package com.jerryio.publicbin.disk;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.bukkit.configuration.file.YamlConfiguration;

import com.jerryio.publicbin.PublicBinPlugin;
import com.jerryio.publicbin.util.MD5Checksum;
import com.jerryio.publicbin.util.PluginLog;
import com.jerryio.publicbin.util.Version;

public class AutoUpdateYamlConfigHandler extends BasicYamlConfigHandler {

    public static YamlConfiguration loadYaml(PublicBinPlugin plugin, String name) {
        return AutoUpdateYamlConfigHandler.loadYaml(plugin, name, new String[] {});
    }
    
    public static YamlConfiguration loadYaml(PublicBinPlugin plugin, String name, String[] forceCopyFilesMD5) {
        FileYamlContent original = new FileYamlContent(plugin, name);
        boolean copyTemplate = !original.file().exists();
        if (copyTemplate) {
            plugin.getDataFolder().mkdirs();
            plugin.saveResource(name, true);
        }
        
        
        original = new FileYamlContent(plugin, name);
        if (original.config == null)
            return null;
        
        
        InputStreamYamlContent template = new InputStreamYamlContent(plugin, name);
        if (template.config == null)
            return original.config; // maybe this version of the plugin do not support this file, just skip version check.
        
        
        if (!copyTemplate) {
            String pluginVerStr = template.config.getString("version", "1.0.1");
            String configVerStr = original.config.getString("version", "1.0.1");
            Version pluginVer = new Version(pluginVerStr);
            Version configVer = new Version(configVerStr);
            
            if (pluginVer.compareTo(configVer) > 0) { // need to update the config file
                try {
                    PluginLog.info("Trying to update file \"" + name + "\". File version = " + configVerStr);
                    
                    backupFile(original.file());
                      
                    updateFile(original, template, forceCopyFilesMD5, pluginVerStr);
                      
                    PluginLog.info("File updated successfully.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    PluginLog.warn("File update failed.");
                    return null;
                }
            }
        }
        
        return original.config;
    }
    
    public static File backupFile(File original) throws IOException {
        File backup = getNonExistsBackupFile(original);
        backup.getParentFile().mkdir();
        Files.copy(original.toPath(), backup.toPath());
        
        PluginLog.info("File backed up successfully. " + original.getName() + " --> backup/" + backup.getName());
        
        return backup;
    }
    
    public static void updateFile(
            FileYamlContent original,
            InputStreamYamlContent template,
            String[] forceCopyFilesMD5,
            String version) throws Exception {
        
        String checksum = MD5Checksum.createStringChecksum(original.stream());
        boolean found = false;
        
        for (int i = 0; i < forceCopyFilesMD5.length; i++) {
            if (forceCopyFilesMD5[i].equals(checksum)) {
                found = true;
                break;
            }
        }
        
        if (found) {
            PluginLog.info("Copying new content.");
            
            Files.copy(template.stream(), original.file().toPath(), StandardCopyOption.REPLACE_EXISTING);
        } else {
            PluginLog.info("Patching new content.");
            
            original.config.addDefaultsWithComments(template.config);
            original.config.options().copyDefaults(true);
            original.config.set("version", version);
            original.config.save(original.file());
        }
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
    
    private static abstract class YamlContent {
        public CommentYamlConfiguration config;
        public PublicBinPlugin plugin;
        public String name;
        
        public YamlContent(PublicBinPlugin plugin, String name) {
            this.plugin = plugin;
            this.name = name;
            this.config = new CommentYamlConfiguration();
        }
    }
    
    private static class FileYamlContent extends YamlContent {        
        public FileYamlContent(PublicBinPlugin plugin, String name) {
            super(plugin, name);
            
            if (!file().exists() || !BasicYamlConfigHandler.loadFromFile(config, file())) this.config = null;
        }
        
        public File file() {
            return new File(plugin.getDataFolder(), name);
        }

        public InputStream stream() throws Exception {
            return new FileInputStream(file());
        }
    }
    
    private static class InputStreamYamlContent extends YamlContent {      
        public InputStreamYamlContent(PublicBinPlugin plugin, String name) {
            super(plugin, name);
            
            if (!BasicYamlConfigHandler.loadFromInputStream(config, stream())) this.config = null;
        }
        
        public InputStream stream() {
            return plugin.getResource(name);
        }
    }
}
