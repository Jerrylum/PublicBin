package com.jerryio.publicbin.commands;

import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.jerryio.publicbin.util.I18n;

public class CommandValidator {
    public static void notNull(Object obj, String string) throws CommandException {
        if (obj == null) {
            throw new CommandException(string);
        }
    }

    public static void isTrue(boolean b, String string) throws CommandException {
        if (!b) {
            throw new CommandException(string);
        }
    }

    public static int getInteger(String integer) throws CommandException {
        try {
            return Integer.parseInt(integer);
        } catch (NumberFormatException ex) {
            throw new CommandException(I18n.t("command-not-integer", integer));
        }
    }

    public static boolean isInteger(String integer) {
        try {
            Integer.parseInt(integer);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public static Player getPlayerSender(CommandSender sender) throws CommandException {
        if (sender instanceof Player) {
            return (Player) sender;
        } else {
            throw new CommandException(I18n.n(sender, "command-not-player"));
        }
    }

    public static boolean isPlayerSender(CommandSender sender) {
        return sender instanceof Player;
    }
}
