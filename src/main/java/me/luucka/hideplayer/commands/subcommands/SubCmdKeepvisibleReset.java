package me.luucka.hideplayer.commands.subcommands;

import me.luucka.hideplayer.HidePlayer;
import me.luucka.hideplayer.HidePlayerUser;
import me.luucka.hideplayer.PlayerVisibilityManager;
import me.luucka.hideplayer.commands.SubCommand;
import me.luucka.hideplayer.utility.ChatUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SubCmdKeepvisibleReset extends SubCommand {

    @Override
    public String getName() {
        return "reset";
    }

    @Override
    public String getDescription() {
        return "Reset your Keepvisible list";
    }

    @Override
    public String getSyntax() {
        return "/keepvisible " + getName();
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

        HidePlayerUser user = new HidePlayerUser(player);
        user.resetKeepvisiblePlayer();
        PlayerVisibilityManager.hidePlayers(player);
        player.sendMessage(ChatUtils.message(HidePlayer.getPlugin().getMessagesYml().getConfig().getString("reset-list")));
    }

    @Override
    public List<String> getSubcommandArgs(Player player, String[] args) {
        return null;
    }
}
