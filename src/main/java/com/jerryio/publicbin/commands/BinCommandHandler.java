package com.jerryio.publicbin.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.jerryio.publicbin.PublicBinPlugin;
import com.jerryio.publicbin.commands.sub.*;
import com.jerryio.publicbin.util.I18n;

public class BinCommandHandler implements CommandExecutor {

    private List<BinSubCommand> subCommands;
    private Map<Class<? extends BinSubCommand>, BinSubCommand> subCommandsByClass;

    public static BinCommandHandler load(PublicBinPlugin plugin) {
        BinCommandHandler rtn;
        plugin.getCommand("publicbin").setExecutor(rtn = new BinCommandHandler());
        return rtn;
    }

    private BinCommandHandler() {
        subCommands = new ArrayList<>();
        subCommandsByClass = new HashMap<>();

        registerSubCommand(new UseCommand());
        registerSubCommand(new ClearCommand());
        registerSubCommand(new ReloadCommand());
        registerSubCommand(new HelpCommand());
    }

    public void registerSubCommand(BinSubCommand subCommand) {
        subCommands.add(subCommand);
        subCommandsByClass.put(subCommand.getClass(), subCommand);
    }

    public List<BinSubCommand> getSubCommands() {
        return new ArrayList<>(subCommands);
    }

    public BinSubCommand getSubCommand(Class<? extends BinSubCommand> subCommandClass) {
        return subCommandsByClass.get(subCommandClass);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        BinSubCommand target = null;

        if (args.length == 0) {
            if (CommandValidator.isPlayerSender(sender)) {
                target = getSubCommand(UseCommand.class);
            } else {
                target = getSubCommand(HelpCommand.class);
            }
            args = new String[] { "" };
        } else {
            for (BinSubCommand subCommand : subCommands) {
                if (!subCommand.isValidTrigger(args[0]))
                    continue;

                if (!subCommand.hasPermission(sender)) {
                    I18n.sendMessage(sender, "command-no-permission");
                    return true;
                }

                target = subCommand;
                break;
            }
        }

        if (target == null) {
            I18n.sendMessage(sender, "command-unknown", label);
        } else {
            
            if (!target.hasPermission(sender)) {
                I18n.sendMessage(sender, "command-no-permission");
                return true;
            }

            if (args.length - 1 < target.getMinimumArguments()) {
                I18n.sendMessage(sender, "command-usage-suggestion", label, target.getName(),
                        target.getPossibleArguments());
                return true;
            }
            
            try {
                target.execute(sender, label, Arrays.copyOfRange(args, 1, args.length));
            } catch (CommandException e) {
                sender.sendMessage(Colors.ERROR + e.getMessage());
            } catch (Exception e) {
                I18n.sendMessage(sender, "command-exception");
                if (PublicBinPlugin.getPluginSetting().isDebug())
                    e.printStackTrace();
            }
        }

        return true;
    }
}
