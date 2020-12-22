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
import com.jerryio.publicbin.util.I18n;

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
            I18n.sendMessage(sender, "command-clear-public-bin");
        } else {
            if (args.length > 0) {
                Player target = Bukkit.getServer().getPlayer(args[0]);
                if (target == null) {                    
                    I18n.sendMessage(sender, "command-target-404");
                } else {
                    manager.getUsableBin(target).clear();
                    I18n.sendMessage(sender, "command-clear-player-bin", target.getName());
                }
                    
            } else {
                Player p = CommandValidator.getPlayerSender(sender); 
                manager.getUsableBin(p).clear();
                I18n.sendMessage(sender, "command-clear-your-bin");
            }
        }
    }

    @Override
    public List<String> getTutorial() {
        if (isShareMode())
            return Arrays.asList(I18n.t("command-clear-tutorial-public"));
        else
            return Arrays.asList(I18n.t("command-clear-tutorial-private").split("\n"));
    }
    
    private static boolean isShareMode() {
        return PublicBinPlugin.getPluginSetting().getMode() == ModeEnum.ShareMode;
    }

}
