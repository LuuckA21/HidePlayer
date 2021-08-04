package me.luucka.hideplayer;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import me.luucka.hideplayer.commands.CmdHide;
import me.luucka.hideplayer.commands.CmdKeepvisible;
import me.luucka.hideplayer.commands.CmdReload;
import me.luucka.hideplayer.commands.CmdShow;
import me.luucka.hideplayer.listeners.PlayerListeners;
import me.luucka.hideplayer.storage.SQLManager;
import me.luucka.hideplayer.storage.StorageTypeManager;
import me.luucka.hideplayer.utility.Chat;
import me.luucka.lcore.file.YamlFileManager;
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

    @Getter private HikariDataSource hikari;

    @Getter
    private final Map<UUID, Long> cooldown = new HashMap<>();

    @Override
    public void onEnable() {
        plugin = this;
        saveDefaultConfig();
        YamlFileManager.addFile(new YamlFileManager(this, "messages"));
        registerCommands();
        registerListeners();
        loadSQLConnection();
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

    public void reloadAllConfig() {
        reloadConfig();
        YamlFileManager.reload();
    }

    public boolean cooldownManager(Player player) {
        if (player.hasPermission("hideplayer.cooldown")) {
            return true;
        }
        if (getCooldown().containsKey(player.getUniqueId())) {
            if (getCooldown().get(player.getUniqueId()) > System.currentTimeMillis()) {
                player.sendMessage(Chat.message(YamlFileManager.file("messages").getString("cooldown")
                        .replace("%time%", getConfig().getString("cooldown"))));
                return false;
            }
            getCooldown().remove(player.getUniqueId());
        }
        getCooldown().put(player.getUniqueId(), System.currentTimeMillis() + (getConfig().getInt("cooldown") * 1000));
        return true;
    }
}
