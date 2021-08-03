package me.luucka.hideplayer.commands.subcommands;

import me.luucka.hideplayer.HidePlayer;
import me.luucka.hideplayer.HidePlayerUser;
import me.luucka.hideplayer.PlayerVisibilityManager;
import me.luucka.hideplayer.utility.ChatUtils;
import me.luucka.lcore.commands.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

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

        HidePlayerUser user = new HidePlayerUser(player);
        user.resetKeepvisiblePlayer();
        PlayerVisibilityManager.hidePlayers(player);
        player.sendMessage(ChatUtils.message(HidePlayer.getPlugin().getMessagesYml().getConfig().getString("reset-list")));
    }

    @Override
    public List<String> getSubcommandArgs(CommandSender sender, String[] args) {
        return null;
    }
}
