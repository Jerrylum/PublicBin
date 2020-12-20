package com.jerryio.publicbin.commands.sub;

import java.util.ArrayList;
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

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.ComponentBuilder.FormatRetention;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class HelpCommand extends BinSubCommand {

    public HelpCommand() {
        super("help");
        setPermission(Strings.BASE_PERM + "command.help");
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
        sender.sendMessage("");
        sender.sendMessage(Strings.formatTitle("PublicBin Commands"));
        for (BinSubCommand subCommand : PublicBinPlugin.getCommandHandler().getSubCommands()) {
            String usage = "/" + label + " " + subCommand.getName() + (subCommand.getPossibleArguments().length() > 0 ? " " + subCommand.getPossibleArguments() : "");
            
            if (CommandValidator.isPlayerSender(sender)) {
                
                List<String> help = new ArrayList<>();
                help.add(Colors.PRIMARY + usage);
                for (String tutLine : subCommand.getTutorial()) {
                    help.add(Colors.SECONDARY_SHADOW + tutLine);
                }
                
                ((Player) sender).spigot().sendMessage(new ComponentBuilder(usage)
                    .color(ChatColor.AQUA)
                    .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, usage))
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(String.join("\n", help))))
                    .create());
                
            } else {
                sender.sendMessage(Colors.PRIMARY + usage);
            }
        }
        
        if (CommandValidator.isPlayerSender(sender)) {
            sendHoverTip((Player) sender);
        }
    }
    
    private static void sendHoverTip(Player player) {
        player.sendMessage("");
        player.spigot().sendMessage(new ComponentBuilder("TIP:").color(ChatColor.YELLOW).bold(true)
            .append(" Try to ", FormatRetention.NONE).color(ChatColor.GRAY)
            .append("hover").color(ChatColor.WHITE).underlined(true)
            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(ChatColor.LIGHT_PURPLE + "Hover on the commands to get info about them.")))
            .append(" or ", FormatRetention.NONE).color(ChatColor.GRAY)
            .append("click").color(ChatColor.WHITE).underlined(true)
            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(ChatColor.LIGHT_PURPLE + "Click on the commands to insert them in the chat.")))
            .append(" on the commands!", FormatRetention.NONE).color(ChatColor.GRAY)
            .create());
    }

    @Override
    public List<String> getTutorial() {
        return Arrays.asList("Shows this help menu.");
    }

}
