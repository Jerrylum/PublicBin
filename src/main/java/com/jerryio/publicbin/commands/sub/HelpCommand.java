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
import com.jerryio.publicbin.util.I18n;

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
        sender.sendMessage(Strings.formatTitle(I18n.n(sender, "command-help-title")));
        for (BinSubCommand subCommand : PublicBinPlugin.getCommandHandler().getSubCommands()) {
            String usage = "/" + label + " " + subCommand.getName() + (subCommand.getPossibleArguments().length() > 0 ? " " + subCommand.getPossibleArguments() : "");
            
            if (CommandValidator.isPlayerSender(sender)) {
                
                List<String> help = new ArrayList<>();
                help.add(Colors.PRIMARY + usage);
                for (String tutLine : subCommand.getTutorial(sender)) {
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
    
    private static void sendHoverTip(Player p) {
        p.sendMessage("");
        p.spigot().sendMessage(new ComponentBuilder(I18n.n(p, "command-help-tips")).color(ChatColor.YELLOW).bold(true)
            .append(I18n.n(p, "command-help-try"), FormatRetention.NONE).color(ChatColor.GRAY)
            .append(I18n.n(p, "command-help-hover")).color(ChatColor.WHITE).underlined(true)
            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(ChatColor.LIGHT_PURPLE + I18n.n(p, "command-help-hover-show"))))
            .append(I18n.n(p, "command-help-or"), FormatRetention.NONE).color(ChatColor.GRAY)
            .append(I18n.n(p, "command-help-click")).color(ChatColor.WHITE).underlined(true)
            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(ChatColor.LIGHT_PURPLE + I18n.n(p, "command-help-click-show"))))
            .append(I18n.n(p, "command-help-on-it"), FormatRetention.NONE).color(ChatColor.GRAY)
            .create());
    }

    @Override
    public List<String> getTutorial(CommandSender sender) {
        return Arrays.asList(I18n.n(sender, "command-help-tutorial"));
    }

}
