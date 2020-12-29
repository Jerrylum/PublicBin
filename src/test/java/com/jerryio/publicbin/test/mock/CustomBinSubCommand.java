package com.jerryio.publicbin.test.mock;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

import com.jerryio.publicbin.commands.BinSubCommand;

public class CustomBinSubCommand extends BinSubCommand {

    public CustomBinSubCommand(String name) {
        super(name);
    }
    
    public CustomBinSubCommand(String name, String... aliases) {
        super(name, aliases);
    }

    @Override
    public String getPossibleArguments() {
        return "<a> <b> <c> [d]";
    }

    @Override
    public int getMinimumArguments() {
        return 3;
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) throws CommandException {
        if (args[0].equals("TypeA"))
            throw new CommandException("Test");
        else
            throw new RuntimeException("Test");
    }

    @Override
    public List<String> getTutorial(CommandSender sender) {
        return Arrays.asList("Test Tutorial");
    }

}
