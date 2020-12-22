package com.jerryio.publicbin.commands.sub;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.jerryio.publicbin.PublicBinPlugin;
import com.jerryio.publicbin.commands.BinSubCommand;
import com.jerryio.publicbin.commands.Colors;
import com.jerryio.publicbin.commands.CommandValidator;
import com.jerryio.publicbin.commands.Strings;
import com.jerryio.publicbin.enums.ModeEnum;
import com.jerryio.publicbin.objects.BinManager;

public class ClearCommand extends BinSubCommand {
    
    public ClearCommand() {
        super("clear");
        setPermission(Strings.BASE_PERM + "command.clear.");
    }
    
    @Override
    public String getPermission() {
        return super.getPermission() + (isShareMode() ? "public" : "me");
    }
    
    @Override
    public String getPossibleArguments() {
        return isShareMode() ? "" : "[player]";
    }

    @Override
    public int getMinimumArguments() {
        return 0;
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) throws CommandException {
        BinManager manager = PublicBinPlugin.getBinManager();
        
        if (isShareMode()) {
            manager.getUsableBin(null).clear();
            sender.sendMessage(Colors.PRIMARY + "Cleared public bin");
        } else {
            if (args.length > 0) {
                Player target = Bukkit.getServer().getPlayer(args[0]);
                if (target == null) {                    
                    sender.sendMessage(Colors.ERROR + "Target not found");
                } else {
                    manager.getUsableBin(target).clear();
                    sender.sendMessage(Colors.PRIMARY + "Cleared " + target.getName() + "'s bin");
                }
                    
            } else {
                if (!CommandValidator.isPlayerSender(sender)) {
                    throw new CommandException("Only in-game players can use this command.");
                }
                manager.getUsableBin((Player) sender).clear();
                sender.sendMessage(Colors.PRIMARY + "Cleared your bin");
            }
        }
        // TODO
    }

    @Override
    public List<String> getTutorial() {
        if (isShareMode())
            return Arrays.asList(
                    "Clears the public bin.");
        else
            return Arrays.asList(
                    "Clears a player's trash bin. Clears your own trash bin",
                    "when the player name is not provided.");
    }
    
    private static boolean isShareMode() {
        return PublicBinPlugin.getPluginSetting().getMode() == ModeEnum.ShareMode;
    }

}
