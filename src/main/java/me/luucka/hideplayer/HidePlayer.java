package me.luucka.hideplayer;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.Setter;
import me.luucka.hideplayer.commands.CmdHide;
import me.luucka.hideplayer.commands.CmdKeepvisible;
import me.luucka.hideplayer.commands.CmdReload;
import me.luucka.hideplayer.commands.CmdShow;
import me.luucka.hideplayer.listeners.PlayerListeners;
import me.luucka.hideplayer.storage.SQLManager;
import me.luucka.hideplayer.storage.StorageManager;
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

    @Getter @Setter private HikariDataSource hikari;

    @Getter
    private final Map<UUID, Long> cooldown = new HashMap<>();

    @Override
    public void onEnable() {
        plugin = this;
        saveDefaultConfig();
        YamlFileManager.addFile(new YamlFileManager(this, "messages"));
        registerCommands();
        registerListeners();
        StorageManager.setStorageType();
        if (hikari != null) {
            getLogger().log(Level.INFO,
                    ChatColor.translateAlternateColorCodes('&', "&aDatabase connected successfully! ("
                            + StorageManager.getStorageType() + ")"));
            SQLManager.init();
        }
    }

    @Override
    public void onDisable() {
        if (hikari != null) {
            hikari.close();
        }
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
