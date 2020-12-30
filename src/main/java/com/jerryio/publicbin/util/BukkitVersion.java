package com.jerryio.publicbin.util;

import org.bukkit.Bukkit;

public class BukkitVersion {
    private static String mockBukkitVersion = null;
    
    public static boolean isSupport(String target) {
        String now = mockBukkitVersion != null ? mockBukkitVersion : Bukkit.getBukkitVersion().split("-")[0];
        String[] alpha = now.split("\\.");
        String[] beta = target.split("\\.");
        
        int a0 = Integer.parseInt(alpha[0]), b0 = Integer.parseInt(beta[0]), a1 = Integer.parseInt(alpha[1]), b1 = Integer.parseInt(beta[1]);

        return a0 > b0 || (a0 == b0 && a1 >= b1);
    }
    
    public static void setMockVersion(String now) {
        mockBukkitVersion = now;
    }    
    
}
