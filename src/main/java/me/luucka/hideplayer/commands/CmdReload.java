package me.luucka.hideplayer.commands;

import me.luucka.hideplayer.HidePlayer;
import me.luucka.hideplayer.utility.Chat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CmdReload implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!sender.hasPermission("hideplayer.reload")) {
            sender.sendMessage(Chat.message(HidePlayer.yamlManager.cfg("messages").getString("no-perm")));
            return true;
        }
        HidePlayer.getPlugin().reloadAllConfig();
        sender.sendMessage(Chat.message(HidePlayer.yamlManager.cfg("messages").getString("reload")));
        return true;
    }
}
