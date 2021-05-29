package me.luucka.hideplayer.commands.subcommands;

import me.luucka.hideplayer.HidePlayer;
import me.luucka.hideplayer.HidePlayerUser;
import me.luucka.hideplayer.commands.SubCommand;
import me.luucka.hideplayer.utility.ChatUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SubCmdKeepvisibleAdd extends SubCommand {

    @Override
    public String getName() {
        return "add";
    }

    @Override
    public String getDescription() {
        return "Add player in your Keepvisible list";
    }

    @Override
    public String getSyntax() {
        return "/keepvisible " + getName() + " <player>";
    }

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public boolean canOnlyPlayerUse() {
        return true;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (args.length < 2) {
            player.sendMessage(ChatUtils.message("&cUsage: " + getSyntax()));
            return;
        }

        // Check if target exists
        Player target = HidePlayer.getPlugin().getServer().getPlayerExact(args[1]);
        if (target == null) {
            player.sendMessage(ChatUtils.message(HidePlayer.getPlugin().getMessagesYml().getConfig().getString("player-not-found")));
            return;
        }

        // Check if you target yourself
        if (target.getName().equalsIgnoreCase(player.getName())) {
            player.sendMessage(ChatUtils.message(HidePlayer.getPlugin().getMessagesYml().getConfig().getString("add-yourself")));
            return;
        }

        HidePlayerUser user = new HidePlayerUser(player);

        if (user.isPlayerInKeepvisibleList(target.getUniqueId())) {
            player.sendMessage(ChatUtils.message(HidePlayer.getPlugin().getMessagesYml().getConfig().getString("already-added")
                    .replace("%player%", target.getDisplayName())));
            return;
        }

        user.addKeepvisiblePlayer(target.getUniqueId());
        player.sendMessage(ChatUtils.message(HidePlayer.getPlugin().getMessagesYml().getConfig().getString("add-player")
                .replace("%player%", target.getDisplayName())));
    }

    @Override
    public List<String> getSubcommandArgs(Player player, String[] args) {
        return null;
    }
}
