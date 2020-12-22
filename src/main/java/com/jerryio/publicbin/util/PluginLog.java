package com.jerryio.publicbin.util;

import java.util.logging.Level;
import java.util.logging.Logger;

public class PluginLog {
    private static boolean debug = false;
    private static Logger logger = null;
    
    public static void setLogger(Logger logger) {
        PluginLog.logger = logger;
    }
    
    public static void setDebugEnabled(boolean enabled) {
        debug = enabled;
    }
    
    public static void log(Level level, String msg, Throwable thrown) {
        if (logger != null) {
            logger.log(level, msg, thrown);
        }
    }
    
    public static void log(Level level, String msg) {
        log(level, msg, null);
    }

    public static void logDebug(Level level, String msg, Throwable thrown) {
        if (debug) {
            log(level, "[Debug] " + msg, thrown);
        }
    }
    
    public static void logDebug(Level level, String msg) {
        logDebug(level, msg, null);
    }
    
    public static void info(String msg) {
        log(Level.INFO, msg);
    }
    
    public static void warn(String msg) {
        log(Level.WARNING, msg);
    }
}
