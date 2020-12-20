package com.jerryio.publicbin.commands.sub;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.jerryio.publicbin.PublicBinPlugin;
import com.jerryio.publicbin.commands.BinSubCommand;
import com.jerryio.publicbin.commands.Colors;
import com.jerryio.publicbin.commands.CommandValidator;
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
        if (!CommandValidator.isPlayerSender(sender)) {
            throw new CommandException("Only in-game players can use this command.");
        }
    
        Player p = (Player) sender;
        p.openInventory(PublicBinPlugin.getBinManager().getUsableBin(p).getInventory());
    }

    @Override
    public List<String> getTutorial() {
        return Arrays.asList("Opens the public bin or private bin.");
    }
}
