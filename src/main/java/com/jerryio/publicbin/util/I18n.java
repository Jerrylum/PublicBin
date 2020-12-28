package com.jerryio.publicbin.util;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.jerryio.publicbin.PublicBinPlugin;

public class I18n {
    private static HashMap<String, ResourceBundle> langMap = new HashMap<String, ResourceBundle>();
    private static ResourceBundle defaultLang;
    
    public static void load(String locale){
        loadLanguage("en_US");
        loadLanguage("zh_CN");
        loadLanguage("zh_TW");
        defaultLang = langMap.get("en_us"); // lower cases
    }

    private static void loadLanguage(String locale) {
        try {
            String[] str = locale.split("_");
            langMap.put(locale.toLowerCase(), ResourceBundle.getBundle("messages" , new Locale(str[0],str[1])));
        } catch (Exception ex) {
            PluginLog.warn("Unknown language: \"" + locale + "\"");
        }
    }

    private static String p(String locale, String str, Object[] obj) {
        try {
            String format = langMap.getOrDefault(locale.toLowerCase(), defaultLang).getString(str);

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

    public static String t(String str, Object... obj) {
        return p(PublicBinPlugin.getPluginSetting().getLang(), str, obj);
    }

    public static String n(String locate, String str, Object... obj) {
        return p(locate, str, obj);
    }

    public static String n(CommandSender sender, String str, Object... obj) {
        if (sender instanceof Player) {
            String locate;
            if (BukkitVersion.isSupport("1.12"))
                locate = ((Player)sender).getLocale();
            else
                locate = PublicBinPlugin.getPluginSetting().getLang();
            return p(locate, str, obj);
        } else {
            return t(str, obj);
        }
    }

    public static void sendMessage(CommandSender sender, String str, Object... obj) {
        sender.sendMessage(n(sender, str, obj));
    }
}
