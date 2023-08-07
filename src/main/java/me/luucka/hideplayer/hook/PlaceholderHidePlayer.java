package me.luucka.hideplayer.hook;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlaceholderHidePlayer extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "hideplayer";
    }

    @Override
    public @NotNull String getAuthor() {
//        return HidePlayer.getPlugin().getDescription().getAuthors().toString();
        return "";
    }

    @Override
    public @NotNull String getVersion() {
//        return HidePlayer.getPlugin().getDescription().getVersion();
        return "";
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier) {
//        if (player == null) return "";
//
//        User user = new User(player);
//
//        if (identifier.equalsIgnoreCase("is_players_hidden")) {
//            return HidePlayer.yamlManager.cfg("messages").getString("is-players-hidden." + (user.getVisible() ? "false" : "true"));
//        }

        return "";
    }
}
