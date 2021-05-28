package me.luucka.hideplayer.commands;

import me.luucka.hideplayer.HidePlayer;
import me.luucka.hideplayer.items.ItemManager;
import me.luucka.hideplayer.utility.ChatUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdShow implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatUtils.message(HidePlayer.getPlugin().getMessagesYml().getConfig().getString("no-console")));
            return true;
        }
        Player player = (Player) sender;

        HidePlayer.getPlugin().getPlayerManager().showPlayers(player);
        player.sendMessage(ChatUtils.message(HidePlayer.getPlugin().getMessagesYml().getConfig().getString("showall")));

        HidePlayer.getPlugin().getDataYml().getConfig().set(player.getUniqueId() + ".visible", true);
        HidePlayer.getPlugin().getDataYml().saveConfig();

        player.getInventory().setItem(6, ItemManager.giveShowItem());
        return true;
    }
}
