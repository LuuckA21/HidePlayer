package me.luucka.hideplayer;

import me.luucka.hideplayer.storage.SQLManager;
import me.luucka.hideplayer.storage.StorageManager;
import me.luucka.hideplayer.storage.YAMLManager;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class HidePlayerUser {

    private final Player player;

    public HidePlayerUser(Player player) {
        this.player = player;
    }

    public void createUser() {
        if (StorageManager.isUsingDatabase()) {
            SQLManager.createUser(player);
        } else {
            YAMLManager.createUser(player.getUniqueId());
        }
    }

    public boolean getVisible() {
        if (StorageManager.isUsingDatabase()) {
            return SQLManager.getVisible(player.getUniqueId());
        } else {
            return YAMLManager.getVisible(player.getUniqueId());
        }
    }

    public void setVisible(boolean visible) {
        if (StorageManager.isUsingDatabase()) {
            SQLManager.setVisible(player.getUniqueId(), visible);
        } else {
            YAMLManager.setVisible(player.getUniqueId(), visible);
        }
    }

    public List<String> getKeepvisibleList() {
        if (StorageManager.isUsingDatabase()) {
            return SQLManager.getKeepvisibleList(player.getUniqueId());
        } else {
            return YAMLManager.getKeepvisibleList(player.getUniqueId());
        }
    }

    public boolean isPlayerInKeepvisibleList(UUID otherUUID) {
        if (StorageManager.isUsingDatabase()) {
            return SQLManager.isPlayerInKeepvisibleList(player.getUniqueId(), otherUUID);
        } else {
            return YAMLManager.isPlayerInKeepvisibleList(player.getUniqueId(), otherUUID);
        }
    }

    public void addKeepvisiblePlayer(UUID otherUUID) {
        if (StorageManager.isUsingDatabase()) {
            SQLManager.addKeepvisiblePlayer(player.getUniqueId(), otherUUID);
        } else {
            YAMLManager.addKeepvisiblePlayer(player.getUniqueId(), otherUUID);
        }
    }

    public void removeKeepvisiblePlayer(UUID otherUUID) {
        if (StorageManager.isUsingDatabase()) {
            SQLManager.removeKeepvisiblePlayer(player.getUniqueId(), otherUUID);
        } else {
            YAMLManager.removeKeepvisiblePlayer(player.getUniqueId(), otherUUID);
        }
    }

    public void resetKeepvisiblePlayer() {
        if (StorageManager.isUsingDatabase()) {
            SQLManager.resetKeepvisiblePlayer(player.getUniqueId());
        } else {
            YAMLManager.resetKeepvisiblePlayer(player.getUniqueId());
        }
    }
}
