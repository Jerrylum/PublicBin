package com.jerryio.publicbin;

import java.io.File;
import java.util.Arrays;
import java.util.logging.Level;

import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import com.jerryio.publicbin.commands.BinCommandHandler;
import com.jerryio.publicbin.disk.PluginSetting;
import com.jerryio.publicbin.listener.MainListener;
import com.jerryio.publicbin.objects.BinManager;
import com.jerryio.publicbin.util.I18n;
import com.jerryio.publicbin.util.PluginLog;

public class PublicBinPlugin extends JavaPlugin {
    // Singleton pattern, The main instance of the plugin.
    private static PublicBinPlugin instance;

    public PluginSetting setting;

    public BinCommandHandler commandHandler;

    public BinManager binManager;

    private boolean cacheUsedReloadCommand;

    public PublicBinPlugin() {
        super();
    }

    protected PublicBinPlugin(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
    }

    @Override
    public void onEnable() {
        doSingletonPatternCheckAndInitial();

        doConsoleIntial();

        // initial config
        setting = PluginSetting.load(this);
        
        // initial language setting
        I18n.load(setting.getLang());

        // initial command handler
        commandHandler = BinCommandHandler.load(this);

        // initial bin manager
        binManager = BinManager.load(this);

        // initial event listener
        MainListener.load(this);

        // set console debug mode by config value
        PluginLog.setDebugEnabled(setting.isDebug());

        doPrintDebugMsg();
        
        doMetricsSend();
    }

    @Override
    public void onDisable() {
        binManager.close();

        PluginLog.info(I18n.t("plugin-disabled"));
    }

    public void onReload() {
        onReload(true);
    }
    
    public void onReload(boolean reloadConfig) {
        binManager.close();

        if (reloadConfig) {
            // reload config
            setting = PluginSetting.load(this);
        }

        // initial language setting
        I18n.load(setting.getLang());

        // initial bin manager
        binManager = BinManager.load(this);

        // set console debug mode by config value
        PluginLog.setDebugEnabled(setting.isDebug());

        doPrintDebugMsg();
    }


    public static PublicBinPlugin getInstance() {
        return instance;
    }

    public static PluginSetting getPluginSetting() {
        return instance.setting;
    }

    public static BinCommandHandler getCommandHandler() {
        return instance.commandHandler;
    }

    public static BinManager getBinManager() {
        return instance.binManager;
    }

    private void doSingletonPatternCheckAndInitial() {
        // Warn about plugin reloaders and the /reload command.
        cacheUsedReloadCommand = instance != null || System.getProperty("PublicBinLoaded") != null;

        System.setProperty("PublicBinLoaded", "true");
        instance = this;
    }

    private void doConsoleIntial() {
        PluginLog.setLogger(getLogger());
    }

    private void doPrintDebugMsg() {
        PluginLog.info(I18n.t("plugin-enabled", setting.isDebug()));
        
        if (cacheUsedReloadCommand) {
            PluginLog.log(Level.WARNING, I18n.t("plugin-singleton-warning"));
        }

        if (setting.isDebug()) { // optimization
            PluginLog.logDebug(Level.INFO, "Using mode = " + setting.getMode());
            PluginLog.logDebug(Level.INFO,
                    "Despawn = " + setting.isAutoDespawnEnabled() + " & time = " + setting.getKeepingTime());
            PluginLog.logDebug(Level.INFO, "Auto remove = " + setting.isRmoveWhenFullEnabled() + " & threshold = "
                    + setting.getKeepingTime() + " & order = " + Arrays.toString(setting.getAutoRemovePrincipleList()));
            PluginLog.logDebug(Level.INFO, "Smart grouping = " + setting.isSmartGroupingEnabled() + " & order = "
                    + Arrays.toString(setting.getSmartGroupingPrincipleList()));
        }
    }
    
    private void doMetricsSend() {
        try {
            Class.forName("com.google.gson.JsonElement");
            if (System.getProperty("MockTest") == null) {
                Metrics metrics = new Metrics(this, 9744);
                metrics.addCustomChart(new Metrics.SimplePie("using_mode", () ->  setting.getMode().toString()));
            }
        } catch (Exception e) {
            // nothing to do
        }        
    }

}
