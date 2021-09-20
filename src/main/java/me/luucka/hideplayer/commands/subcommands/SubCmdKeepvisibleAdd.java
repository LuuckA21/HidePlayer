package me.luucka.hideplayer.commands.subcommands;

import me.luucka.hideplayer.HidePlayer;
import me.luucka.hideplayer.User;
import me.luucka.hideplayer.utility.Chat;
import me.luucka.lcore.commands.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SubCmdKeepvisibleAdd extends SubCommand {

    @Override
    public String name() {
        return "add";
    }

    @Override
    public String description() {
        return "Add player in your Keepvisible list";
    }

    @Override
    public String syntax() {
        return "/keepvisible " + name() + " <player>";
    }

    @Override
    public String permission() {
        return null;
    }

    @Override
    public boolean isOnlyPlayer() {
        return true;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (args.length < 2) {
            player.sendMessage(Chat.message("&cUsage: " + syntax()));
            return;
        }

        // Check if target exists
        Player target = HidePlayer.getPlugin().getServer().getPlayerExact(args[1]);
        if (target == null) {
            player.sendMessage(Chat.message(HidePlayer.yamlManager.cfg("messages").getString("player-not-found")));
            return;
        }

        // Check if you target yourself
        if (target.getName().equalsIgnoreCase(player.getName())) {
            player.sendMessage(Chat.message(HidePlayer.yamlManager.cfg("messages").getString("add-yourself")));
            return;
        }

        User user = new User(player);

        if (user.isPlayerInKeepvisibleList(target.getUniqueId())) {
            player.sendMessage(Chat.message(HidePlayer.yamlManager.cfg("messages").getString("already-added")
                    .replace("%player%", target.getDisplayName())));
            return;
        }

        user.addKeepvisiblePlayer(target.getUniqueId());
        player.sendMessage(Chat.message(HidePlayer.yamlManager.cfg("messages").getString("add-player")
                .replace("%player%", target.getDisplayName())));
    }

    @Override
    public List<String> getSubcommandArgs(CommandSender sender, String[] args) {
        return null;
    }
}
