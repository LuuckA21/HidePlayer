package me.luucka.hideplayer.storage;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import me.luucka.hideplayer.HidePlayer;
import me.luucka.lcore.file.YamlFileManager;

import java.util.HashMap;
import java.util.Map;

public final class StorageTypeManager {

    private static final HidePlayer PLUGIN = HidePlayer.getPlugin();

    private static final Map<String, String> CLASS_DRIVER = new HashMap<>();

    @Getter
    private static boolean usingDatabase;

    private static void loadClassDriverMap() {
        CLASS_DRIVER.put("mysql", "com.mysql.cj.jdbc.Driver");
        CLASS_DRIVER.put("mariadb", "org.mariadb.jdbc.Driver");
        CLASS_DRIVER.put("postgresql", "org.postgresql.Driver");
        CLASS_DRIVER.put("sqlite", "org.sqlite.JDBC");
        CLASS_DRIVER.put("h2", "org.h2.Driver");
    }

    public static HikariDataSource getDataSource(String storageType) {
        loadClassDriverMap();

        HikariConfig config;

        switch (storageType.toLowerCase()) {
            case "mysql":
            case "mariadb":
            case "postgresql":
                config = setNormalDBConnection(storageType);
                break;
            case "sqlite":
            case "h2":
                config = setFileDBConnection(storageType);
                break;
            default:
                config = setFileDBConnection("h2");
                PLUGIN.getConfig().set("storage.type", "h2");
                break;
        }

        usingDatabase = true;

        return new HikariDataSource(config);
    }

    private static HikariConfig setNormalDBConnection(String storageType) {
        HikariConfig config = new HikariConfig();

        config.setDriverClassName(CLASS_DRIVER.get(storageType));

        String host = PLUGIN.getConfig().getString("storage.settings.remote-db.host");
        String port = PLUGIN.getConfig().getString("storage.settings.remote-db.port");
        String database = PLUGIN.getConfig().getString("storage.settings.remote-db.database");
        String username = PLUGIN.getConfig().getString("storage.settings.remote-db.username");
        String password = PLUGIN.getConfig().getString("storage.settings.remote-db.password");

        config.setJdbcUrl("jdbc:" + storageType + "://" + host + ":" + port + "/" + database);
        config.setUsername(username);
        config.setPassword(password);

        return config;
    }

    private static HikariConfig setFileDBConnection(String storageType) {
        HikariConfig config = new HikariConfig();

        config.setDriverClassName(CLASS_DRIVER.get(storageType));

        config.setJdbcUrl("jdbc:" + storageType + ":" + PLUGIN.getDataFolder().getAbsolutePath() + "/" + PLUGIN.getConfig().getString("storage.settings." + storageType));

        return config;
    }

    public static void setYamlFile() {
        usingDatabase = false;
        YamlFileManager.addFile(new YamlFileManager(PLUGIN, "data"));
    }

}
