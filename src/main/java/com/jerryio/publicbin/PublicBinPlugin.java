package com.jerryio.publicbin;

import java.util.Arrays;
import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;

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
    }

    @Override
    public void onDisable() {
        binManager.close();

        PluginLog.info(I18n.t("plugin-disabled"));
    }

    public void onReload() {
        binManager.close();

        // reload config
        setting = PluginSetting.load(this);

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
        if (instance != null || System.getProperty("PublicBinLoaded") != null) {
            // important, don't use PluginLog here
            getLogger().log(Level.WARNING, I18n.t("plugin-singleton-warning"));
        }

        System.setProperty("PublicBinLoaded", "true");
        instance = this;
    }

    private void doConsoleIntial() {
        PluginLog.setLogger(getLogger());
    }

    private void doPrintDebugMsg() {
        PluginLog.info(I18n.t("plugin-enabled", setting.isDebug()));
        if (setting.isDebug()) { // optimization
            PluginLog.logDebug(Level.INFO, "Using mode = " + setting.getMode());
            PluginLog.logDebug(Level.INFO,
                    "Despawn = " + setting.isAutoDespawnEnabled() + " & time = " + setting.getKeepingTime());
            PluginLog.logDebug(Level.INFO, "Auto remove = " + setting.isRmoveWhenFullEnabled() + " & threshold = "
                    + setting.getKeepingTime() + " & order = " + Arrays.toString(setting.getRemoveOrderList()));
            PluginLog.logDebug(Level.INFO, "Smart grouping = " + setting.isSmartGroupingEnabled() + " & order = "
                    + Arrays.toString(setting.getSmartGroupingOrderList()));
        }
    }

}
