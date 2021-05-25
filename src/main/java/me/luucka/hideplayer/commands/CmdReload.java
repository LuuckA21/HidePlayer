package me.luucka.hideplayer.commands;

import me.luucka.hideplayer.HidePlayer;
import me.luucka.hideplayer.utility.ChatUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CmdReload implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("hideplayer.reload")) {
            sender.sendMessage(ChatUtils.message(HidePlayer.getPlugin().getMessagesYml().getConfig().getString("no-perm")));
            return true;
        }
        HidePlayer.getPlugin().reloadAllConfig();
        sender.sendMessage(ChatUtils.message(HidePlayer.getPlugin().getMessagesYml().getConfig().getString("reload")));
        return true;
    }
}
