package me.luucka.hideplayer;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import me.luucka.hideplayer.hook.PlaceholderHidePlayer;
import org.bukkit.plugin.java.JavaPlugin;

public final class HidePlayer extends JavaPlugin {

    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this)
                .shouldHookPaperReload(true)
        );
    }

    @Override
    public void onEnable() {
        CommandAPI.onEnable();

        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PlaceholderHidePlayer().register();
        }
    }

    @Override
    public void onDisable() {
        CommandAPI.onDisable();
    }
}
