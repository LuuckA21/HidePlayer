package me.luucka.hideplayer.manager;

import me.luucka.hideplayer.HidePlugin;
import me.luucka.hideplayer.cache.PlayerCache;
import me.luucka.hideplayer.database.DatabaseManager;
import me.luucka.hideplayer.hook.Hooker;
import me.luucka.hideplayer.item.HideItem;
import me.luucka.hideplayer.item.ShowItem;
import me.luucka.hideplayer.settings.HideSettings;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Messenger;
import org.mineacademy.fo.debug.Debugger;
import org.mineacademy.fo.remain.Remain;
import org.mineacademy.fo.settings.SimpleLocalization;

import java.util.Optional;

public class VisibilityManager {

	public static void showPlayers(Player player) {
		Debugger.debug("show_players", "Show players from: " + player.getName());
		if (CooldownManager.handleCooldown(player)) {
			Messenger.error(
					player,
					SimpleLocalization.Commands.COOLDOWN_WAIT
							.replace(
									"{duration}",
									"" + CooldownManager.getRemainingCooldown(player.getUniqueId())
							)
			);
			return;
		}

		Remain.getOnlinePlayers().forEach(onlinePlayer -> player.showPlayer(HidePlugin.getInstance(), onlinePlayer));

		Optional<PlayerCache> optionalPlayerCache = PlayerCache.getFromId(player.getUniqueId());
		if (optionalPlayerCache.isPresent()) {
			PlayerCache playerCache = optionalPlayerCache.get();
			Debugger.debug("cache", "Cache present: " + playerCache);
			playerCache.updateCache(player, true);
		} else {
			Debugger.debug("cache", "Cache not present");
			DatabaseManager.getInstance().loadCache(player, cache -> {
				cache.updateCache(player, true);
				PlayerCache.addCache(cache);
			});
		}

		ShowItem.getInstance().give(player, HideSettings.Item.SLOT);
	}

	public static void hidePlayers(Player player) {
		Debugger.debug("hide_players", "Hide players from: " + player.getName());
		if (CooldownManager.handleCooldown(player)) {
			Messenger.error(
					player,
					SimpleLocalization.Commands.COOLDOWN_WAIT
							.replace(
									"{duration}",
									"" + CooldownManager.getRemainingCooldown(player.getUniqueId())
							)
			);
			return;
		}

		Remain.getOnlinePlayers().forEach(onlinePlayer -> player.hidePlayer(HidePlugin.getInstance(), onlinePlayer));
		Hooker.getPartyPlayers(player).forEach(uuid -> {
			player.showPlayer(HidePlugin.getInstance(), Remain.getPlayerByUUID(uuid));
		});

		Optional<PlayerCache> optionalPlayerCache = PlayerCache.getFromId(player.getUniqueId());
		if (optionalPlayerCache.isPresent()) {
			PlayerCache playerCache = optionalPlayerCache.get();
			Debugger.debug("cache", "Cache present: " + playerCache);
			playerCache.updateCache(player, false);
		} else {
			Debugger.debug("cache", "Cache not present");
			DatabaseManager.getInstance().loadCache(player, cache -> {
				cache.updateCache(player, false);
				PlayerCache.addCache(cache);
			});
		}

		HideItem.getInstance().give(player, HideSettings.Item.SLOT);
	}
}
