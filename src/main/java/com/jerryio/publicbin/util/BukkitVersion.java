package com.jerryio.publicbin.util;

import org.bukkit.Bukkit;

public class BukkitVersion {
    private static String mockBukkitVersion = null;
    
    public static boolean isSupport(String target) {
        String now = mockBukkitVersion != null ? mockBukkitVersion : Bukkit.getBukkitVersion().split("-")[0];
        String[] alpha = now.split("\\.");
        String[] beta = target.split("\\.");

        return Integer.parseInt(alpha[0]) > Integer.parseInt(beta[0]) || Integer.parseInt(alpha[1]) >= Integer.parseInt(beta[1]);
    }
    
    public static void setMockVersion(String now) {
        mockBukkitVersion = now;
    }    
    
}
