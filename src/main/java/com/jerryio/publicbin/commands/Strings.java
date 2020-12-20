package com.jerryio.publicbin.commands;

public class Strings {
    public static final String BASE_PERM = "publicbin.";

    public static String formatTitle(String input) {
        return "" + Colors.PRIMARY_SHADOW + Colors.BOLD + "----- " + input + Colors.PRIMARY_SHADOW + Colors.BOLD + " -----";
    }
}
