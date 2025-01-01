package me.luucka.hideplayer;

import lombok.NonNull;
import me.luucka.hideplayer.cache.PlayerCache;
import me.luucka.hideplayer.database.DatabaseManager;
import me.luucka.hideplayer.hook.Hooker;
import me.luucka.hideplayer.settings.HideSettings;
import net.byteflux.libby.BukkitLibraryManager;
import net.byteflux.libby.Library;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mineacademy.fo.FileUtil;
import org.mineacademy.fo.TimeUtil;
import org.mineacademy.fo.debug.Debugger;
import org.mineacademy.fo.exception.FoException;
import org.mineacademy.fo.model.SimpleExpansion;
import org.mineacademy.fo.model.Variables;
import org.mineacademy.fo.plugin.SimplePlugin;

import java.util.Optional;

public final class HidePlugin extends SimplePlugin {

	private BukkitLibraryManager bukkitLibraryManager;

	@Override
	protected void onPluginStart() {
		bukkitLibraryManager = new BukkitLibraryManager(this);
		bukkitLibraryManager.addMavenCentral();
		loadDatabase();
	}

	@Override
	protected void onReloadablesStart() {
		Hooker.loadDependencies();
		
		Variables.addExpansion(new SimpleExpansion() {
			@Override
			protected String onReplace(@NonNull CommandSender sender, String identifier) {
				if (!(sender instanceof Player)) {
					return "";
				}
				Player player = (Player) sender;
				Optional<PlayerCache> optional = PlayerCache.getFromId(player.getUniqueId());
				if (!optional.isPresent()) {
					return "";
				}

				PlayerCache cache = optional.get();
				if ("visible".equals(identifier)) {
					return Boolean.toString(cache.isVisible());
				} else if ("visible_formatted".equals(identifier)) {
					return cache.isVisible() ? "yes" : "no";
				} else if ("updated".equals(identifier)) {
					return Long.toString(cache.getUpdated());
				} else if ("updated_formatted".equals(identifier)) {
					return TimeUtil.getFormattedDate(cache.getUpdated());
				}
				return "";
			}
		});
	}

	@Override
	protected void onPluginStop() {
		DatabaseManager.getInstance().close();
	}

	private void loadDatabase() {
		final String storageType = HideSettings.Storage.TYPE.toLowerCase();
		Debugger.debug("database", "Database storage type: " + storageType);
		String url;
		switch (storageType) {
			case "mysql":
			case "mariadb":
				loadHikari();
				if ("mysql".equalsIgnoreCase(storageType)) {
					loadMysql();
				} else {
					loadMariaDb();
				}
				url = String.format(
						"jdbc:%s://%s:%s/%s?useSSL=false&useUnicode=yes&characterEncoding=UTF-8&autoReconnect=true",
						storageType, HideSettings.Storage.Settings.HOST, HideSettings.Storage.Settings.PORT, HideSettings.Storage.Settings.DATABASE
				);
				Debugger.debug("database", "Database URL: " + url);
				DatabaseManager.getInstance().connect(url, HideSettings.Storage.Settings.USER, HideSettings.Storage.Settings.PASSWORD, null);
				break;
			case "sqlite":
				loadHikari();
				loadSqlite();
				url = "jdbc:sqlite:" + FileUtil.getOrMakeFile(HideSettings.Storage.Settings.SQLITE_FILE).getAbsolutePath();
				Debugger.debug("database", "Database URL: " + url);
				DatabaseManager.getInstance().connect(url);
				break;
			default:
				throw new FoException("Unsupported database type: " + HideSettings.Storage.TYPE.toLowerCase());
		}
	}

	private void loadHikari() {
		loadLibrary("com.zaxxer", "HikariCP", "5.1.0");
	}

	private void loadMariaDb() {
		loadLibrary("org.mariadb.jdbc", "mariadb-java-client", "3.5.1");
	}

	private void loadMysql() {
		loadLibrary("com.mysql", "mysql-connector-j", "9.1.0");
	}

	private void loadSqlite() {
		loadLibrary("org.xerial", "sqlite-jdbc", "3.47.1.0");
	}

	private void loadLibrary(final String groupId, final String artifactId, final String version) {
		bukkitLibraryManager.loadLibrary(
				Library.builder()
						.groupId(groupId)
						.artifactId(artifactId)
						.version(version)
						.build()
		);
	}
}
