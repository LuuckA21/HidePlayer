package me.luucka.hideplayer;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import me.luucka.hideplayer.commands.CmdHide;
import me.luucka.hideplayer.commands.CmdKeepvisible;
import me.luucka.hideplayer.commands.CmdReload;
import me.luucka.hideplayer.commands.CmdShow;
import me.luucka.hideplayer.hook.PlaceholderHidePlayer;
import me.luucka.hideplayer.listeners.PlayerListeners;
import me.luucka.hideplayer.storage.SQLManager;
import me.luucka.hideplayer.storage.StorageTypeManager;
import me.luucka.hideplayer.utility.Chat;
import me.luucka.lcore.file.YamlFile;
import me.luucka.lcore.manager.YamlManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public final class HidePlayer extends JavaPlugin {

    @Getter
    private static HidePlayer plugin;

    public static final YamlManager yamlManager = new YamlManager();

    @Getter private HikariDataSource hikari;

    private final Map<UUID, Long> cooldownMap = new HashMap<>();

    @Override
    public void onEnable() {
        plugin = this;
        saveDefaultConfig();
        registerCommands();
        registerListeners();
        registerFiles();
        loadSQLConnection();
        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PlaceholderHidePlayer().register();
        }
    }

    @Override
    public void onDisable() {
        if (hikari != null) {
            hikari.close();
        }
    }

    private void loadSQLConnection() {
        String storageType = getConfig().getString("storage.type");
        if (storageType == null || storageType.isEmpty()) storageType = "YAML";
        if (storageType.equalsIgnoreCase("yaml")) {
            StorageTypeManager.setYamlFile();
            return;
        }
        hikari = StorageTypeManager.getDataSource(storageType);
        SQLManager.init();
        getLogger().log(Level.INFO,
                ChatColor.translateAlternateColorCodes('&',
                        "&aDatabase connected successfully! ("
                                + storageType.toUpperCase())
                        + ")");
    }

    // Register Commands / Listener
    //------------------------------------------------------------------------------------------------------------------

    private void registerCommands() {
        getCommand("hideplayer").setExecutor(new CmdReload());
        getCommand("hideall").setExecutor(new CmdHide());
        getCommand("showall").setExecutor(new CmdShow());
        getCommand("keepvisible").setExecutor(new CmdKeepvisible());
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerListeners(), this);
    }

    private void registerFiles() {
        yamlManager.addFile(new YamlFile(this, "messages"));
    }

    public void reloadAllConfig() {
        reloadConfig();
        yamlManager.reload();
    }

    public boolean cooldownManager(Player player) {
        if (player.hasPermission("hideplayer.cooldown")) {
            return true;
        }
        if (cooldownMap.containsKey(player.getUniqueId())) {
            if (cooldownMap.get(player.getUniqueId()) > System.currentTimeMillis()) {
                player.sendMessage(Chat.message(yamlManager.cfg("messages").getString("cooldown")
                        .replace("%time%", getConfig().getString("cooldown"))));
                return false;
            }
            cooldownMap.remove(player.getUniqueId());
        }
        cooldownMap.put(player.getUniqueId(), System.currentTimeMillis() + (getConfig().getInt("cooldown") * 1000L));
        return true;
    }
}
