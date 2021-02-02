package com.jerryio.publicbin.update;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.bukkit.Bukkit;

import com.jerryio.publicbin.PublicBinPlugin;
import com.jerryio.publicbin.util.I18n;
import com.jerryio.publicbin.util.PluginLog;
import com.jerryio.publicbin.util.Version;

public class PublicBinUpdateChecker {
    
    private PublicBinPlugin cachePlugin;
    
    public PublicBinUpdateChecker(PublicBinPlugin plugin) {
        cachePlugin = plugin;        
    }
    
    public void checkForUpdates() {
        Bukkit.getScheduler().runTaskAsynchronously(cachePlugin, () -> checkThread());
    }
    
    public boolean analysisVersion(String compareRawText) {
        try {            
            Version compare = new Version(compareRawText);
            Version me = new Version(cachePlugin.getDescription().getVersion());
            
            return compare.compareTo(me) > 0;
        } catch(Exception ex) {
            return false;
        }
    }
    
    public void printMessage(String ver, boolean outdated) {
        if (outdated) {
            PluginLog.info(I18n.t("update-check-found", ver));
            PluginLog.info(I18n.t("update-website-link"));
        }
    }
    
    public void checkThread() {
        try {
            HttpsURLConnection con = (HttpsURLConnection) new URL("https://api.spigotmc.org/legacy/update.php?resource=87242").openConnection();
            con.setRequestMethod("GET");
            
            String ver = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
            printMessage(ver, analysisVersion(ver));
        } catch(Exception ex) {
            PluginLog.info(I18n.t("update-check-failed"));
        }
    }
    
}
