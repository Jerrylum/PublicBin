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
import org.bukkit.entity.Player;

import com.jerryio.publicbin.PublicBinPlugin;
import com.jerryio.publicbin.commands.sub.*;

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
            args = new String[]{""};
        } else {
            for (BinSubCommand subCommand : subCommands) {
                if (!subCommand.isValidTrigger(args[0])) continue;
                    
                if (!subCommand.hasPermission(sender)) {
                    sender.sendMessage(Colors.ERROR + "You don't have permission.");
                    break;
                }
                
                if (args.length - 1 >= subCommand.getMinimumArguments()) {
                    target = subCommand;
                } else {
                    sender.sendMessage(Colors.ERROR + "Usage: /" + label + " " + subCommand.getName() + " " + subCommand.getPossibleArguments());
                }
            }
        }
        
        if (target == null) {
            sender.sendMessage(Colors.ERROR + "Unknown sub-command. Type \"/" + label + " help\" for a list of commands.");
        } else {
            try {
                target.execute(sender, label, Arrays.copyOfRange(args, 1, args.length));
            } catch (CommandException e) {
                sender.sendMessage(Colors.ERROR + e.getMessage());
            }
        }
        
        return true;
    }
}
