package me.luucka.hideplayer.commands.subcommands;

import me.luucka.hideplayer.HidePlayer;
import me.luucka.hideplayer.commands.SubCommand;
import me.luucka.hideplayer.utility.ChatUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
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

        Player target = HidePlayer.getPlugin().getServer().getPlayerExact(args[1]);
        if (target == null) {
            sender.sendMessage(ChatUtils.message("&cPlayer not found"));
            return;
        }

        // ADD
        HidePlayer.getPlugin().getDataYml().getConfig().set(player.getUniqueId() + ".keepvisible", Arrays.asList(target.getUniqueId().toString()));
        HidePlayer.getPlugin().getDataYml().saveConfig();
        player.sendMessage(ChatUtils.message(HidePlayer.getPlugin().getMessagesYml().getConfig().getString("add-player")
                .replace("%player%", target.getDisplayName())));
    }

    @Override
    public List<String> getSubcommandArgs(CommandSender sender, String[] args) {
        return null;
    }
}
