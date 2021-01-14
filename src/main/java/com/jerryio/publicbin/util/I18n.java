package com.jerryio.publicbin.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.jerryio.publicbin.PublicBinPlugin;
import com.jerryio.publicbin.disk.BasicYamlConfig;

public class I18n {
    private static HashMap<String, ResourceBundle> langMap = new HashMap<String, ResourceBundle>();
    private static YamlConfiguration customizeLang;
    private static ResourceBundle defaultLang;
    
    public static void load(PublicBinPlugin plugin) {
        loadCustomizeLanguageFile(plugin);
        loadLanguage("en_US");
        loadLanguage("zh_CN");
        loadLanguage("zh_TW");
        defaultLang = langMap.get("en_us"); // lower cases
    }

    private static void loadCustomizeLanguageFile(PublicBinPlugin plugin) {
        customizeLang = BasicYamlConfig.loadYaml(plugin, "messages.yml");
    }

    private static void loadLanguage(String locale) {
        try {
            String[] str = locale.split("_");
            langMap.put(locale.toLowerCase(), ResourceBundle.getBundle("messages" , new Locale(str[0],str[1]), new UTF8PropertiesControl()));
        } catch (Exception ex) {
            PluginLog.warn("Unknown language: \"" + locale + "\"");
        }
    }

    private static String p(String locale, String str, Object[] obj) {
        try {
            String format;
            
            if (customizeLang != null && customizeLang.isSet(str))
                format = customizeLang.getString(str);
            else
                format = langMap.getOrDefault(locale.toLowerCase(), defaultLang).getString(str);

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
    
    /**
     * Reads .properties files as UTF-8 instead of ISO-8859-1, which is the default on Java 8/below.
     * Java 9 fixes this by defaulting to UTF-8 for .properties files.
     */
    public static class UTF8PropertiesControl extends ResourceBundle.Control {
        public ResourceBundle newBundle(final String baseName, final Locale locale, final String format, final ClassLoader loader, final boolean reload) throws IOException {
            final String resourceName = toResourceName(toBundleName(baseName, locale), "properties");
            ResourceBundle bundle = null;
            InputStream stream = null;
            if (reload) {
                final URL url = loader.getResource(resourceName);
                if (url != null) {
                    final URLConnection connection = url.openConnection();
                    if (connection != null) {
                        connection.setUseCaches(false);
                        stream = connection.getInputStream();
                    }
                }
            } else {
                stream = loader.getResourceAsStream(resourceName);
            }
            if (stream != null) {
                try {
                    // use UTF-8 here, this is the important bit
                    bundle = new PropertyResourceBundle(new InputStreamReader(stream, StandardCharsets.UTF_8));
                } finally {
                    stream.close();
                }
            }
            return bundle;
        }
    }
}
