package me.luucka.hideplayer.manager;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class VisibilityManager {

	@Getter
	private static VisibilityManager instance = new VisibilityManager();

	public void updateVisibility(final Player player, final boolean show) {
		// Handle the cooldown
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

		if (show) {
			showAllPlayers(player);
			ShowItem.getInstance().give(player, HideSettings.Item.SLOT);
		} else {
			hideAllPlayers(player);
			HideItem.getInstance().give(player, HideSettings.Item.SLOT);
		}

		// Get and update the cache
		Optional<PlayerCache> optionalPlayerCache = PlayerCache.getFromId(player.getUniqueId());
		if (optionalPlayerCache.isPresent()) {
			PlayerCache playerCache = optionalPlayerCache.get();
			Debugger.debug("cache", "Cache present: " + playerCache);
			playerCache.updateCache(player, show);
		} else {
			// Need to retrive data from DB or create a new cache
			Debugger.debug("cache", "Cache not present");
			DatabaseManager.getInstance().loadCache(player, cache -> {
				cache.updateCache(player, show);
				PlayerCache.addCache(cache);
			});
		}
	}

	public void showAllPlayers(Player player) {
		Remain.getOnlinePlayers().forEach(onlinePlayer -> player.showPlayer(HidePlugin.getInstance(), onlinePlayer));
	}

	public void hideAllPlayers(Player player) {
		Remain.getOnlinePlayers().forEach(onlinePlayer -> player.hidePlayer(HidePlugin.getInstance(), onlinePlayer));

		Hooker.getPartyMembers(player).forEach(uuid -> {
			player.showPlayer(HidePlugin.getInstance(), Remain.getPlayerByUUID(uuid));
		});

		Hooker.getFriends(player).forEach(uuid -> {
			player.showPlayer(HidePlugin.getInstance(), Remain.getPlayerByUUID(uuid));
		});
	}
}
