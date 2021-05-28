package me.luucka.hideplayer;

import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerManager {

    public void showPlayers(Player player) {
        HidePlayer.getPlugin().getServer().getOnlinePlayers().forEach(p -> player.showPlayer(HidePlayer.getPlugin(), p));
    }

    public void hidePlayers(Player player) {
        HidePlayer.getPlugin().getServer().getOnlinePlayers().forEach(p -> player.hidePlayer(HidePlayer.getPlugin(), p));

        HidePlayer.getPlugin().getDataYml().getConfig().getStringList(player.getUniqueId() + ".keepvisible").forEach(uuid -> {
            Player keepPlayer = HidePlayer.getPlugin().getServer().getPlayer(UUID.fromString(uuid));
            if (keepPlayer != null) {
                player.showPlayer(HidePlayer.getPlugin(), keepPlayer);
            }
        });
    }
}
