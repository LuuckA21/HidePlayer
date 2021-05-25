package me.luucka.hideplayer.files;

import com.google.common.base.Charsets;
import me.luucka.hideplayer.HidePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.logging.Level;

public class FileManager {

    private FileConfiguration newConfig;
    private File configFile;

    private final String fileName;

    public FileManager(String fileName) {
        this.fileName = fileName + ".yml";
        saveDefaultConfig();
    }

    public FileConfiguration getConfig() {
        if (newConfig == null) {
            reloadConfig();
        }
        return newConfig;
    }

    public void reloadConfig() {
        newConfig = YamlConfiguration.loadConfiguration(configFile);

        final InputStream defConfigStream = HidePlayer.getPlugin().getResource(fileName);
        if (defConfigStream == null) {
            return;
        }

        newConfig.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8)));
    }

    public void saveConfig() {
        try {
            getConfig().save(configFile);
        } catch (IOException ex) {
            HidePlayer.getPlugin().getLogger().log(Level.SEVERE, "Could not save config to " + configFile, ex);
        }
    }

    public void saveDefaultConfig() {
        if (configFile == null) {
            configFile = new File(HidePlayer.getPlugin().getDataFolder(), fileName);
        }
        if (!configFile.exists()) {
            HidePlayer.getPlugin().saveResource(fileName, false);
        }
    }
}