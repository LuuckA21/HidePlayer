package me.luucka.hideplayer;

import me.luucka.hideplayer.utility.Chat;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerVisibilityManager {

    public static void showPlayers(Player player) {
        HidePlayer.getPlugin().getServer().getOnlinePlayers().forEach(p -> player.showPlayer(HidePlayer.getPlugin(), p));

        // Set visible status to true
        User user = new User(player);
        user.setVisible(true);
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

        user.setVisible(false);
    }
}
