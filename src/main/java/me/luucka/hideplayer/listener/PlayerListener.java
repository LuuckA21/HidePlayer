package me.luucka.hideplayer.listener;

import lombok.Getter;
import me.luucka.hideplayer.cache.PlayerCache;
import me.luucka.hideplayer.database.DatabaseManager;
import me.luucka.hideplayer.item.HideItem;
import me.luucka.hideplayer.item.ShowItem;
import me.luucka.hideplayer.manager.CooldownManager;
import me.luucka.hideplayer.manager.VisibilityManager;
import me.luucka.hideplayer.settings.HideSettings;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.annotation.AutoRegister;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.remain.Remain;

import java.util.ArrayList;
import java.util.Collection;

@AutoRegister
public final class PlayerListener implements Listener {

	@Getter
	private static final PlayerListener instance = new PlayerListener();

	private PlayerListener() {
		try {
			Class.forName("org.bukkit.event.player.PlayerSwapHandItemsEvent");
			Common.registerEvents(new SwapHandListener());
		} catch (final ClassNotFoundException ignored) {
		}
	}

	@EventHandler
	public void onJoin(final PlayerJoinEvent event) {
		Player player = event.getPlayer();

		// Load cache from DB
		DatabaseManager.getInstance().loadCache(player, cache -> {
			PlayerCache.addCache(cache);

			if (cache.isVisible()) {
				ShowItem.getInstance().give(player, HideSettings.Item.SLOT);
			} else {
				VisibilityManager.hidePlayers(player);
			}
		});

		Collection<Player> players = new ArrayList<>(Remain.getOnlinePlayers());
		players.remove(player);
		for (Player onlinePlayer : players) {
			PlayerCache.getFromId(onlinePlayer.getUniqueId()).ifPresent(
					cache -> {
						if (!cache.isVisible()) {
							onlinePlayer.hidePlayer(SimplePlugin.getInstance(), player);
						}
					}
			);
		}
	}

	@EventHandler
	public void onQuit(final PlayerQuitEvent event) {
		Player player = event.getPlayer();
		CooldownManager.clearCooldown(player.getUniqueId());
		PlayerCache.removeFromId(player.getUniqueId());
	}

	@EventHandler
	public void onInventoryClick(final InventoryClickEvent event) {
		if (checkTools(event.getCurrentItem())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onDrop(final PlayerDropItemEvent event) {
		if (checkTools(event.getItemDrop().getItemStack())) {
			event.setCancelled(true);
		}
	}

	private static boolean checkTools(ItemStack item) {
		return ShowItem.getInstance().isTool(item) || HideItem.getInstance().isTool(item);
	}

	static class SwapHandListener implements Listener {

		@EventHandler
		public void onSwapHand(final PlayerSwapHandItemsEvent event) {
			final ItemStack mainHand = event.getMainHandItem();
			final ItemStack offHand = event.getOffHandItem();

			if (mainHand == null && offHand == null) return;

			boolean isMainHand = false;
			if (mainHand != null) {
				if (checkTools(mainHand)) isMainHand = true;
			}

			boolean isOffHand = false;
			if (offHand != null) {
				if (checkTools(offHand)) isOffHand = true;
			}

			if (isMainHand || isOffHand) event.setCancelled(true);
		}
	}
}
