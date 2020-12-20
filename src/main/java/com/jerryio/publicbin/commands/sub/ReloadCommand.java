package com.jerryio.publicbin.commands.sub;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

import com.jerryio.publicbin.PublicBinPlugin;
import com.jerryio.publicbin.commands.BinSubCommand;
import com.jerryio.publicbin.commands.Strings;

public class ReloadCommand extends BinSubCommand {
    public ReloadCommand() {
        super("reload");
        setPermission(Strings.BASE_PERM + "command.reload");
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
        sender.sendMessage("Reload!!");
        // TODO
    }

    @Override
    public List<String> getTutorial() {
        return Arrays.asList("Reloads the config file.");
    }
}
