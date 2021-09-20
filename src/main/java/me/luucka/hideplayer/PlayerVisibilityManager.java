package me.luucka.hideplayer;

import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerVisibilityManager {

    public static void showPlayers(Player player) {
        HidePlayer.getPlugin().getServer().getOnlinePlayers().forEach(p -> player.showPlayer(HidePlayer.getPlugin(), p));
    }

    public static void hidePlayers(Player player) {
        HidePlayer.getPlugin().getServer().getOnlinePlayers().forEach(p -> player.hidePlayer(HidePlayer.getPlugin(), p));

        User user = new User(player);

        user.getKeepvisibleList().forEach(uuid -> {
            Player keepPlayer = HidePlayer.getPlugin().getServer().getPlayer(UUID.fromString(uuid));
            if (keepPlayer != null) {
                player.showPlayer(HidePlayer.getPlugin(), keepPlayer);
            }
        });
    }
}
