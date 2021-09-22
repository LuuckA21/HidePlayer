package me.luucka.hideplayer.commands.subcommands;

import me.luucka.hideplayer.HidePlayer;
import me.luucka.hideplayer.User;
import me.luucka.hideplayer.utility.Chat;
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
            player.sendMessage(Chat.message("&cUsage: " + syntax()));
            return;
        }

        String _name = args[1].toLowerCase();

        User user = new User(player);

        // Create List<OfflinePlayer> from UUID in keepvisible personal list
        List<OfflinePlayer> offlinePlayerList = new ArrayList<>();
        List<String> uuids = user.getKeepvisibleList();
        uuids.forEach(uuid -> {
            offlinePlayerList.add(HidePlayer.getPlugin().getServer().getOfflinePlayer(UUID.fromString(uuid)));
        });

        Map<String, UUID> nameUUID = new HashMap<>();
        offlinePlayerList.forEach(offlinePlayer -> {
            nameUUID.put(offlinePlayer.getName().toLowerCase(), offlinePlayer.getUniqueId());
        });

        if (!nameUUID.containsKey(_name)) {
            player.sendMessage(Chat.message(HidePlayer.yamlManager.cfg("messages").getString("player-not-in-list")));
            return;
        }

        user.removeKeepvisiblePlayer(nameUUID.get(_name));

        if (!user.getVisible()) {
            Player toHide = HidePlayer.getPlugin().getServer().getPlayer(nameUUID.get(_name));
            if (toHide != null) player.hidePlayer(HidePlayer.getPlugin(), toHide);
        }

        player.sendMessage(Chat.message(HidePlayer.yamlManager.cfg("messages").getString("remove-player")
                .replace("%player%", _name)));
    }

    @Override
    public List<String> getSubcommandArgs(CommandSender sender, String[] args) {
        if (args.length == 2) {
            User user = new User((Player) sender);
            List<String> suggestions = new ArrayList<>();

            user.getKeepvisibleList().forEach(uuid -> {
                suggestions.add(HidePlayer.getPlugin().getServer().getOfflinePlayer(UUID.fromString(uuid)).getName());
            });
            return suggestions;
        }
        return null;
    }
}
