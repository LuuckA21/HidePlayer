package me.luucka.hideplayer.commands;

import lombok.Getter;
import me.luucka.hideplayer.HidePlayer;
import me.luucka.hideplayer.commands.subcommands.SubCmdKeepvisibleAdd;
import me.luucka.hideplayer.commands.subcommands.SubCmdKeepvisibleRemove;
import me.luucka.hideplayer.commands.subcommands.SubCmdKeepvisibleReset;
import me.luucka.hideplayer.utility.ChatUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CmdKeepvisible implements TabExecutor {

    @Getter
    private final List<SubCommand> subCommands = new ArrayList<>();

    public CmdKeepvisible() {
        subCommands.add(new SubCmdKeepvisibleAdd());
        subCommands.add(new SubCmdKeepvisibleRemove());
        subCommands.add(new SubCmdKeepvisibleReset());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            for (SubCommand sub : subCommands) {
                if (args[0].equalsIgnoreCase(sub.getName())) {
                    if (sub.canOnlyPlayerUse() && !(sender instanceof Player)) {
                        sender.sendMessage(ChatUtils.message(HidePlayer.getPlugin().getMessagesYml().getConfig().getString("no-console")));
                        return true;
                    }
                    /*
                    if (!sender.hasPermission(sub.getPermission())) {
                        sender.sendMessage(ChatUtils.message(HidePlayer.getPlugin().getMessagesYml().getConfig().getString("no-perm")));
                        return true;
                    }*/
                    sub.perform(sender, args);
                }
            }
        } else {
            sender.sendMessage(ChatUtils.message("&7&lHelp page"));
            for (SubCommand sub : subCommands) {
                /*
                if (sender.hasPermission(sub.getPermission())) {
                    sender.sendMessage(ChatUtils.hexColor("&7\u25ba &a" + sub.getSyntax() + " &7- &b" + sub.getDescription()));
                }
                 */
                sender.sendMessage(ChatUtils.hexColor("&7\u25ba &a" + sub.getSyntax() + " &7- &b" + sub.getDescription()));
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) {
            getSubCommands().forEach(subCmd -> {
                /*
                if (sender.hasPermission(subCmd.getPermission())) {
                    suggestions.add(subCmd.getName());
                }*/
                suggestions.add(subCmd.getName());
            });
            return suggestions;
        } else if (args.length == 2) {
            for (SubCommand subCmd : subCommands) {
                if (args[0].equalsIgnoreCase(subCmd.getName())) {
                    return subCmd.getSubcommandArgs((Player) sender, args);
                }
            }
        }

        return null;
    }
}
