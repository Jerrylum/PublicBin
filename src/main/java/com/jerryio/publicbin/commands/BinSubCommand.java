package com.jerryio.publicbin.commands;

import java.util.List;

import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

public abstract class BinSubCommand {
    private String name;
    private String permission;
    private String[] aliases;

    public BinSubCommand(String name) {
        this.name = name;
    }

    public BinSubCommand(String name, String... aliases) {
        this.name = name;
        this.aliases = aliases;
    }

    public String getName() {
        return name;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }

    public final boolean hasPermission(CommandSender sender) {
        if (getPermission() == null)
            return true;
        return sender.hasPermission(getPermission());
    }

    public abstract String getPossibleArguments();

    public abstract int getMinimumArguments();

    public abstract void execute(CommandSender sender, String label, String[] args) throws CommandException;

    public abstract List<String> getTutorial(CommandSender sender);

    public final boolean isValidTrigger(String name) {
        if (this.getName().equalsIgnoreCase(name)) {
            return true;
        }

        if (aliases != null) {
            for (String alias : aliases) {
                if (alias.equalsIgnoreCase(name)) {
                    return true;
                }
            }
        }

        return false;
    }

}
