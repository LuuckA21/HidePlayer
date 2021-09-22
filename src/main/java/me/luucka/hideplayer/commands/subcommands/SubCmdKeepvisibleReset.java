package me.luucka.hideplayer.commands.subcommands;

import me.luucka.hideplayer.HidePlayer;
import me.luucka.hideplayer.User;
import me.luucka.hideplayer.utility.Chat;
import me.luucka.lcore.commands.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class SubCmdKeepvisibleReset extends SubCommand {

    @Override
    public String name() {
        return "reset";
    }

    @Override
    public String description() {
        return "Reset your Keepvisible list";
    }

    @Override
    public String syntax() {
        return "/keepvisible " + name();
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

        User user = new User(player);

        if (!user.getVisible()) {
            user.getKeepvisibleList().forEach(uuid -> {
                Player toRemove = HidePlayer.getPlugin().getServer().getPlayer(UUID.fromString(uuid));
                if (toRemove != null) player.hidePlayer(HidePlayer.getPlugin(), toRemove);
            });
        }

        user.resetKeepvisiblePlayer();

        player.sendMessage(Chat.message(HidePlayer.yamlManager.cfg("messages").getString("reset-list")));
    }

    @Override
    public List<String> getSubcommandArgs(CommandSender sender, String[] args) {
        return null;
    }
}
