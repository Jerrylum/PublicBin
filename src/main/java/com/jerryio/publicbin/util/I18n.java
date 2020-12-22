package com.jerryio.publicbin.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import com.jerryio.publicbin.util.PluginLog;

import org.bukkit.command.CommandSender;

public class I18n {
    private static ResourceBundle res = null;
    private static ResourceBundle en = null;
    
    public static void load(String locale){
        try {
            en = ResourceBundle.getBundle("messages" , new Locale("en", "US"));

            if (locale.equals("en_US")) {
                res = en;
            } else {                
                if(locale.indexOf("_") == -1){
                    res = ResourceBundle.getBundle("messages" , new Locale(locale));
                }else{
                    String[] str = locale.split("_");
                    res = ResourceBundle.getBundle("messages" , new Locale(str[0],str[1]));
                }
            }
        } catch (Exception ex) {
            PluginLog.warn("Unknown language: \"" + locale + "\"");
        }
    }
    
    public static String t(String str, Object... obj){
        try {
            String format;

            try {
                format = res.getString(str);
            } catch (Exception ex) {
                format = en.getString(str);
            }

            MessageFormat messageFormat;

            try{
                messageFormat = new MessageFormat(format);
            }catch (IllegalArgumentException e){
                format = format.replaceAll("\\{(\\D*?)\\}", "\\[$1\\]");
                messageFormat = new MessageFormat(format);
            }

            return messageFormat.format(obj);
        } catch (Exception ex) {
            PluginLog.warn("Unknown key: \"" + str + "\"");
            
            return "";
        }
    }
    
    public static void sendMessage(CommandSender sender, String str, Object... obj) {
        sender.sendMessage(t(str, obj));
    }
}
