package me.luucka.hideplayer.commands;

import me.luucka.hideplayer.HidePlayer;
import me.luucka.hideplayer.HidePlayerUser;
import me.luucka.hideplayer.PlayerVisibilityManager;
import me.luucka.hideplayer.items.ItemManager;
import me.luucka.hideplayer.utility.ChatUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdHide implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check if sender is a instanceof Player. Only players can run this command
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatUtils.message(HidePlayer.getPlugin().getMessagesYml().getConfig().getString("no-console")));
            return true;
        }
        // Cast sender in Player class
        Player player = (Player) sender;

        // Check cooldown
        if (!HidePlayer.getPlugin().cooldownManager(player)) {
            return true;
        }

        // Call method for hide all players
        PlayerVisibilityManager.hidePlayers(player);
        // Send messages to player
        player.sendMessage(ChatUtils.message(HidePlayer.getPlugin().getMessagesYml().getConfig().getString("hideall")));

        // Set visible status to false
        HidePlayerUser user = new HidePlayerUser(player);
        user.setVisible(false);

        player.getInventory().setItem(6, ItemManager.giveHideItem());
        return true;
    }
}
