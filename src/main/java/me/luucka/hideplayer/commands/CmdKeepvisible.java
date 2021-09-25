package me.luucka.hideplayer.commands;

import lombok.Getter;
import me.luucka.hideplayer.HidePlayer;
import me.luucka.hideplayer.commands.subcommands.SubCmdKeepvisibleAdd;
import me.luucka.hideplayer.commands.subcommands.SubCmdKeepvisibleRemove;
import me.luucka.hideplayer.commands.subcommands.SubCmdKeepvisibleReset;
import me.luucka.hideplayer.utility.Chat;
import me.luucka.lcore.commands.SubCommand;
import me.luucka.lcore.utils.ColorTranslate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

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
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length > 0) {
            for (SubCommand sub : subCommands) {
                if (args[0].equalsIgnoreCase(sub.name())) {
                    if (sub.isOnlyPlayer() && !(sender instanceof Player)) {
                        sender.sendMessage(Chat.message(HidePlayer.yamlManager.cfg("messages").getString("no-console")));
                        return true;
                    }
                    sub.perform(sender, args);
                }
            }
        } else {
            sender.sendMessage(Chat.message("&7&lHelp page"));
            for (SubCommand sub : subCommands) {
                sender.sendMessage(ColorTranslate.translate("&7\u25ba &a" + sub.syntax() + " &7- &b" + sub.description()));
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) {
            getSubCommands().forEach(subCmd -> suggestions.add(subCmd.name()));
            return suggestions;
        } else if (args.length == 2) {
            for (SubCommand subCmd : subCommands) {
                if (args[0].equalsIgnoreCase(subCmd.name())) {
                    return subCmd.getSubcommandArgs(sender, args);
                }
            }
        }

        return null;
    }
}
