package com.jerryio.publicbin.commands.sub;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

import com.jerryio.publicbin.PublicBinPlugin;
import com.jerryio.publicbin.commands.BinSubCommand;
import com.jerryio.publicbin.commands.Strings;
import com.jerryio.publicbin.enums.ModeEnum;

public class UseCommand extends BinSubCommand {
    public UseCommand() {
        super("use");
        setPermission(Strings.BASE_PERM + "use");
    }
    
    @Override
    public String getPossibleArguments() {
        return "";
    }

    @Override
    public int getMinimumArguments() {
        return 0;
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) throws CommandException {
        sender.sendMessage("Open!!");
        // TODO
    }

    @Override
    public List<String> getTutorial() {
        return Arrays.asList("Opens the public bin or private bin.");
    }
    
    private static boolean isShareMode() {
        return PublicBinPlugin.getPluginSetting().getMode() == ModeEnum.ShareMode;
    }
}
