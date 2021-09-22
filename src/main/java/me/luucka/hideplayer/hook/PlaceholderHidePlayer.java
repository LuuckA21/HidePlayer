package me.luucka.hideplayer.hook;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.luucka.hideplayer.HidePlayer;
import me.luucka.hideplayer.User;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlaceholderHidePlayer extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "hideplayer";
    }

    @Override
    public @NotNull String getAuthor() {
        return HidePlayer.getPlugin().getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getVersion() {
        return HidePlayer.getPlugin().getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier) {
        if (player == null) return "";

        User user = new User(player);

        if (identifier.equalsIgnoreCase("is_players_hidden")) {
            return user.getVisible() ? "No" : "Yes";
        }

        return "";
    }
}
