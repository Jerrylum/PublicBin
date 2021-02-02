package com.jerryio.publicbin.util;

import org.bukkit.Bukkit;

public class BukkitVersion {
    private static String mockBukkitVersion = null;
    
    public static boolean isSupport(String target) {
        String now = mockBukkitVersion != null ? mockBukkitVersion : Bukkit.getBukkitVersion().split("-")[0];
        return (new Version(now).compareTo(new Version(target))) >= 0;
    }
    
    public static void setMockVersion(String now) {
        mockBukkitVersion = now;
    }    
    
}
