package me.luucka.hideplayer.commands.subcommands;

import me.luucka.hideplayer.HidePlayer;
import me.luucka.hideplayer.HidePlayerUser;
import me.luucka.hideplayer.PlayerVisibilityManager;
import me.luucka.hideplayer.utility.ChatUtils;
import me.luucka.lcore.commands.SubCommand;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class SubCmdKeepvisibleRemove extends SubCommand {

    @Override
    public String name() {
        return "remove";
    }

    @Override
    public String description() {
        return "Remove player from your Keepvisible list";
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
            player.sendMessage(ChatUtils.message("&cUsage: " + syntax()));
            return;
        }

        HidePlayerUser user = new HidePlayerUser(player);

        // Create List<OfflinePlayer> from UUID in keepvisible personal list
        List<OfflinePlayer> offlinePlayerList = new ArrayList<>();
        List<String> uuids = user.getKeepvisibleList();
        uuids.forEach(uuid -> {
            offlinePlayerList.add(HidePlayer.getPlugin().getServer().getOfflinePlayer(UUID.fromString(uuid)));
        });

        Map<String, String> nameUUID = new HashMap<>();
        offlinePlayerList.forEach(offlinePlayer -> {
            nameUUID.put(offlinePlayer.getName().toLowerCase(), offlinePlayer.getUniqueId().toString());
        });

        if (!nameUUID.containsKey(args[1].toLowerCase())) {
            player.sendMessage(ChatUtils.message(HidePlayer.getPlugin().getMessagesYml().getConfig().getString("player-not-in-list")));
            return;
        }

        user.removeKeepvisiblePlayer(UUID.fromString(nameUUID.get(args[1].toLowerCase())));
        PlayerVisibilityManager.hidePlayers(player);
        player.sendMessage(ChatUtils.message(HidePlayer.getPlugin().getMessagesYml().getConfig().getString("remove-player")
                .replace("%player%", args[1])));
    }

    @Override
    public List<String> getSubcommandArgs(CommandSender sender, String[] args) {
        if (args.length == 2) {
            HidePlayerUser user = new HidePlayerUser((Player) sender);
            List<String> suggestions = new ArrayList<>();

            user.getKeepvisibleList().forEach(uuid -> {
                suggestions.add(HidePlayer.getPlugin().getServer().getOfflinePlayer(UUID.fromString(uuid)).getName());
            });
            return suggestions;
        }
        return null;
    }
}
