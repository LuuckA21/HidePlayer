package me.luucka.hideplayer.storage;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import me.luucka.hideplayer.HidePlayer;
import me.luucka.hideplayer.files.FileManager;

public class StorageManager {

    @Getter private static String storageType;
    @Getter private static boolean usingDatabase;

    public static void setStorageType() {
        storageType = HidePlayer.getPlugin().getConfig().getString("storage-type");

        HikariConfig config = new HikariConfig();
        if (storageType.equalsIgnoreCase("mysql")) {
            String host = HidePlayer.getPlugin().getConfig().getString("mysql.host");
            String port = HidePlayer.getPlugin().getConfig().getString("mysql.port");
            String database = HidePlayer.getPlugin().getConfig().getString("mysql.database");
            String username = HidePlayer.getPlugin().getConfig().getString("mysql.username");
            String password = HidePlayer.getPlugin().getConfig().getString("mysql.password");

            config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
            config.setUsername(username);
            config.setPassword(password);
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

            HidePlayer.getPlugin().setHikari(new HikariDataSource(config));
            usingDatabase = true;
        } else if (storageType.equalsIgnoreCase("sqlite")) {
            config.setJdbcUrl("jdbc:sqlite:" + HidePlayer.getPlugin().getDataFolder().getAbsolutePath() + "/hideplayer.db");
            HidePlayer.getPlugin().setHikari(new HikariDataSource(config));
            usingDatabase = true;
        } else {
            FileManager dataYml = new FileManager("data");
            HidePlayer.getPlugin().setDataYml(dataYml);
            usingDatabase = false;
        }
    }

}
