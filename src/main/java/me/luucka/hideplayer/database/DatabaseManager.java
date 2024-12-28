package me.luucka.hideplayer.database;

import lombok.Getter;
import me.luucka.hideplayer.cache.PlayerCache;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.database.SimpleDatabase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;
import java.util.function.Consumer;

public class DatabaseManager extends SimpleDatabase {

	@Getter
	private final static DatabaseManager instance = new DatabaseManager();

	private DatabaseManager() {
		addVariable("table", "hideplayer");
	}

	@Override
	protected void onConnected() {
		if (!isSQLite()) {
			createTable(TableCreator.of("{table}")
					.addNotNull("UUID", "VARCHAR(64)")
					.add("Name", "TEXT")
					.add("Visible", "BOOLEAN")
					.add("Updated", "BIGINT")
					.setPrimaryColumn("UUID")
			);
		} else {
			update("CREATE TABLE IF NOT EXISTS `{table}` " +
					"(`UUID` text NOT NULL, " +
					"`Name` text, " +
					"`Visible` boolean, " +
					"`Updated` integer, " +
					"PRIMARY KEY (`UUID`));"
			);
		}
	}

	public void loadCache(Player player, Consumer<PlayerCache> callThisWithDataLoaded) {
		Valid.checkSync("Please call loadCache on the main Thread!");

		Common.runAsync(() -> {
			try (
					PreparedStatement preparedStatement = prepareStatement("SELECT * FROM {table} WHERE UUID = ?")
			) {
				preparedStatement.setString(1, player.getUniqueId().toString());
				ResultSet resultSet = preparedStatement.executeQuery();
				if (!resultSet.next()) {
					Common.runLater(() -> callThisWithDataLoaded.accept(PlayerCache.fromPlayer(player)));
					return;
				}
				final UUID uuid = UUID.fromString(resultSet.getString("UUID"));
				final String name = resultSet.getString("Name");
				final boolean visible = resultSet.getBoolean("Visible");
				final long updated = resultSet.getLong("Updated");
				Common.runLater(() -> callThisWithDataLoaded.accept(PlayerCache.fromDatabase(uuid, name, visible, updated)));
			} catch (Throwable t) {
				Common.error(t, "Unable to load player data for " + player.getName());
			}
		});
	}

	public void saveCache(PlayerCache cache) {
		Valid.checkSync("Please call saveCache on the main Thread!");

		Common.runAsync(() -> {
			try {
				SerializedMap map = SerializedMap.ofArray(cache.toMap());
				if (!isSQLite()) {
					insert(map);
				} else {
					// ONLY INTENDED TO BE USED WITH LOCAL STORAGE SUCH AS SQLITE
					final String columns = Common.join(map.keySet());
					final String values = Common.join(map.values(), ", ", value -> value == null || value.equals("NULL") ? "NULL" : "'" + value + "'");

					update("INSERT OR REPLACE INTO {table} (" + columns + ") VALUES (" + values + ");");
				}

			} catch (Throwable t) {
				Common.error(t, "Unable to save player data for " + cache.getName());
			}
		});
	}

//	public void pollCache(Player player, Consumer<PlayerCache> result) {
//		Valid.checkSync("Please call pollCache on the main Thread!");
//
//		Common.runAsync(() -> {
//			try (
//					PreparedStatement preparedStatement = prepareStatement("SELECT * FROM {table} WHERE UUID = ?")
//			) {
//				preparedStatement.setString(1, player.getUniqueId().toString());
//				ResultSet resultSet = preparedStatement.executeQuery();
//				if (!resultSet.next()) {
//					Common.runLater(() -> result.accept(null));
//					return;
//				}
//
//				UUID uuid = UUID.fromString(resultSet.getString("UUID"));
//				boolean visible = resultSet.getBoolean("Visible");
//				Common.runLater(() -> result.accept(new PlayerCache(uuid, visible)));
//			} catch (Throwable t) {
//				Common.error(t, "Unable to load player data for " + player.getName());
//			}
//		});
//	}
//
//	public void pollAllCache(Consumer<List<PlayerCache>> result) {
//		Valid.checkSync("Please call pollAllCache on the main Thread!");
//
//		Common.runAsync(() -> {
//			List<PlayerCache> data = new ArrayList<>();
//			try {
//				selectAll("{table}", resultSet -> data.add(new PlayerCache(UUID.fromString(resultSet.getString("UUID")), resultSet.getBoolean("Visible"))));
//			} catch (Throwable t) {
//				Common.error(t, "Unable to load all player data");
//			}
//			Common.runLater(() -> result.accept(data));
//		});
//	}
}
