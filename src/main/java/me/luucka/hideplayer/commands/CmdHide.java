package me.luucka.hideplayer.commands;

import me.luucka.hideplayer.HidePlayer;
import me.luucka.hideplayer.PlayerVisibilityManager;
import me.luucka.hideplayer.User;
import me.luucka.hideplayer.utility.Chat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CmdHide implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Chat.message(HidePlayer.yamlManager.cfg("messages").getString("no-console")));
            return true;
        }

        User user = new User((Player) sender);

        // Check cooldown
        if (!HidePlayer.getPlugin().cooldownManager(user.getPlayer())) {
            return true;
        }

        if (user.getVisible()) {
            PlayerVisibilityManager.hidePlayers(user.getPlayer());
            user.getPlayer().sendMessage(Chat.message(HidePlayer.yamlManager.cfg("messages").getString("hideall")));
            if (HidePlayer.getPlugin().getConfig().getBoolean("item.enable")) {
                user.setHideItem();
            }
        }

        return true;
    }
}
