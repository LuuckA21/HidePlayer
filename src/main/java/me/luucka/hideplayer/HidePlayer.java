package me.luucka.hideplayer;

import lombok.Getter;
import me.luucka.hideplayer.commands.CmdHide;
import me.luucka.hideplayer.commands.CmdKeepvisible;
import me.luucka.hideplayer.commands.CmdReload;
import me.luucka.hideplayer.commands.CmdShow;
import me.luucka.hideplayer.files.FileManager;
import me.luucka.hideplayer.listeners.PlayerListeners;
import me.luucka.hideplayer.utility.ChatUtils;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class HidePlayer extends JavaPlugin {

    @Getter
    private static HidePlayer plugin;

    @Getter private final PlayerManager playerManager = new PlayerManager();

    @Getter
    private FileManager messagesYml;
    @Getter
    private FileManager dataYml;

    @Getter
    private final Map<UUID, Long> cooldown = new HashMap<>();

    @Override
    public void onEnable() {
        plugin = this;
        saveDefaultConfig();
        messagesYml = new FileManager("messages");
        dataYml = new FileManager("data");
        registerCommands();
        registerListeners();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
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
        messagesYml.reloadConfig();
        dataYml.reloadConfig();
    }

    public boolean cooldownManager(Player player) {
        if (player.hasPermission("hideplayer.cooldown")) {
            return true;
        }
        if (getCooldown().containsKey(player.getUniqueId())) {
            if (getCooldown().get(player.getUniqueId()) > System.currentTimeMillis()) {
                player.sendMessage(ChatUtils.message(getMessagesYml().getConfig().getString("cooldown")
                        .replace("%time%", getConfig().getString("cooldown"))));
                return false;
            }
            getCooldown().remove(player.getUniqueId());
        }
        getCooldown().put(player.getUniqueId(), System.currentTimeMillis() + (getConfig().getInt("cooldown") * 1000));
        return true;
    }
}
