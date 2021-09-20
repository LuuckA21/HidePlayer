package me.luucka.hideplayer.commands;

import me.luucka.hideplayer.HidePlayer;
import me.luucka.hideplayer.PlayerVisibilityManager;
import me.luucka.hideplayer.User;
import me.luucka.hideplayer.utility.Chat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdShow implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check if sender is a instanceof Player. Only players can run this command
        if (!(sender instanceof Player)) {
            sender.sendMessage(Chat.message(HidePlayer.yamlManager.cfg("messages").getString("no-console")));
            return true;
        }
        // Cast sender in Player class
        Player player = (Player) sender;

        // Check cooldown
        if (!HidePlayer.getPlugin().cooldownManager(player)) {
            return true;
        }

        // Call method for show all players
        PlayerVisibilityManager.showPlayers(player);
        // Send messages to player
        player.sendMessage(Chat.message(HidePlayer.yamlManager.cfg("messages").getString("showall")));

        // Set visible status to true
        User user = new User(player);
        user.setVisible(true);

        if (HidePlayer.getPlugin().getConfig().getBoolean("item.enable")) {
            user.setShowItem();
        }
        return true;
    }
}
