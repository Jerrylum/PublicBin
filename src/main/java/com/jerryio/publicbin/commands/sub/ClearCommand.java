package com.jerryio.publicbin.commands.sub;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

import com.jerryio.publicbin.PublicBinPlugin;
import com.jerryio.publicbin.commands.BinSubCommand;
import com.jerryio.publicbin.commands.Strings;
import com.jerryio.publicbin.enums.ModeEnum;

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
        sender.sendMessage("Clear!!");
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
