package me.luucka.hideplayer.manager;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.luucka.hideplayer.settings.HideSettings;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CooldownManager {

	private static final Map<UUID, Long> cooldowns = new HashMap<>();

	/**
	 * Sets a cooldown for a player.
	 *
	 * @param playerUUID The UUID of the player.
	 * @param seconds    The cooldown duration in seconds.
	 */
	private static void setCooldown(UUID playerUUID, int seconds) {
		long cooldownEnd = System.currentTimeMillis() + (seconds * 1000L);
		cooldowns.put(playerUUID, cooldownEnd);
	}

	/**
	 * Handles cooldown logic. If the player is on cooldown, the cooldown is updated.
	 *
	 * @param player The player to check.
	 * @return True if the player was on cooldown (and cooldown updated), false otherwise.
	 */
	public static boolean handleCooldown(Player player) {
		UUID playerUUID = player.getUniqueId();

		if (player.hasPermission("hideplayer.bypass.cooldown")) {
			return false;
		}

		long currentTime = System.currentTimeMillis();

		if (cooldowns.containsKey(playerUUID)) {
			long cooldownEnd = cooldowns.getOrDefault(playerUUID, 0L);
			if (cooldownEnd == 0) return false;

			if (currentTime >= cooldownEnd) {
				// Update the cooldown time
				setCooldown(playerUUID, HideSettings.Cooldown.COOLDOWN);
				return false;
			} else {
				return true;
			}
		}

		setCooldown(playerUUID, HideSettings.Cooldown.COOLDOWN);
		return false;
	}

	/**
	 * Gets the remaining cooldown time for a player.
	 *
	 * @param playerUUID The UUID of the player.
	 * @return The remaining cooldown time in seconds. Returns 0 if no cooldown is active.
	 */
	public static int getRemainingCooldown(UUID playerUUID) {
		if (!cooldowns.containsKey(playerUUID)) {
			return 0;
		}

		long cooldownEnd = cooldowns.getOrDefault(playerUUID, 0L);
		long remainingTime = cooldownEnd - System.currentTimeMillis();

		return (int) (remainingTime / 1000) + 1;
	}

	/**
	 * Clears a player's cooldown.
	 *
	 * @param playerUUID The UUID of the player.
	 */
	public static void clearCooldown(UUID playerUUID) {
		cooldowns.remove(playerUUID);
	}
}
