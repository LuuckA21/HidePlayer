package me.luucka.hideplayer.cache;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.luucka.hideplayer.database.DatabaseManager;
import org.bukkit.entity.Player;
import org.mineacademy.fo.collection.SerializedMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayerCache {

	@Getter
	private static final Map<UUID, PlayerCache> cache = new HashMap<>();

	private final UUID uuid;
	private String name;
	private boolean visible;
	private long updated;

	public void updateCache(Player player, boolean visible) {
		name = player.getName();
		this.visible = visible;
		updated = System.currentTimeMillis();
		saveCache();
	}

	public void saveCache() {
		DatabaseManager.getInstance().saveCache(this);
	}

	public SerializedMap toMap() {
		return SerializedMap.ofArray(
				"UUID", uuid,
				"Name", name,
				"Visible", visible ? 1 : 0,
				"Updated", updated
		);
	}

	public static Optional<PlayerCache> getFromId(UUID uuid) {
		return Optional.ofNullable(cache.get(uuid));
	}

	public static void addCache(PlayerCache newCache) {
		cache.put(newCache.getUuid(), newCache);
	}

	public static void removeFromId(UUID uuid) {
		cache.remove(uuid);
	}

	public static PlayerCache fromDatabase(UUID uuid, String name, boolean visible, long updated) {
		return new PlayerCache(uuid, name, visible, updated);
	}

	public static PlayerCache fromPlayer(Player player) {
		return new PlayerCache(player.getUniqueId(), player.getName(), true, 0);
	}

	@Override
	public String toString() {
		return "PlayerCache: {[UUID: " + uuid + "], [Name: +" + name + "], [Visible: " + visible + "], [Updated: " + updated + "]}";
	}
}
