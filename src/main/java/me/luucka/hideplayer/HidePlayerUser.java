package me.luucka.hideplayer;

import me.luucka.hideplayer.items.ItemManager;
import me.luucka.hideplayer.storage.SQLManager;
import me.luucka.hideplayer.storage.StorageTypeManager;
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
        if (StorageTypeManager.isUsingDatabase()) {
            SQLManager.createUser(player);
        } else {
            YAMLManager.createUser(player.getUniqueId());
        }
    }

    public boolean getVisible() {
        if (StorageTypeManager.isUsingDatabase()) {
            return SQLManager.getVisible(player.getUniqueId());
        } else {
            return YAMLManager.getVisible(player.getUniqueId());
        }
    }

    public void setVisible(boolean visible) {
        if (StorageTypeManager.isUsingDatabase()) {
            SQLManager.setVisible(player.getUniqueId(), visible);
        } else {
            YAMLManager.setVisible(player.getUniqueId(), visible);
        }
    }

    public List<String> getKeepvisibleList() {
        if (StorageTypeManager.isUsingDatabase()) {
            return SQLManager.getKeepvisibleList(player.getUniqueId());
        } else {
            return YAMLManager.getKeepvisibleList(player.getUniqueId());
        }
    }

    public boolean isPlayerInKeepvisibleList(UUID otherUUID) {
        if (StorageTypeManager.isUsingDatabase()) {
            return SQLManager.isPlayerInKeepvisibleList(player.getUniqueId(), otherUUID);
        } else {
            return YAMLManager.isPlayerInKeepvisibleList(player.getUniqueId(), otherUUID);
        }
    }

    public void addKeepvisiblePlayer(UUID otherUUID) {
        if (StorageTypeManager.isUsingDatabase()) {
            SQLManager.addKeepvisiblePlayer(player.getUniqueId(), otherUUID);
        } else {
            YAMLManager.addKeepvisiblePlayer(player.getUniqueId(), otherUUID);
        }
    }

    public void removeKeepvisiblePlayer(UUID otherUUID) {
        if (StorageTypeManager.isUsingDatabase()) {
            SQLManager.removeKeepvisiblePlayer(player.getUniqueId(), otherUUID);
        } else {
            YAMLManager.removeKeepvisiblePlayer(player.getUniqueId(), otherUUID);
        }
    }

    public void resetKeepvisiblePlayer() {
        if (StorageTypeManager.isUsingDatabase()) {
            SQLManager.resetKeepvisiblePlayer(player.getUniqueId());
        } else {
            YAMLManager.resetKeepvisiblePlayer(player.getUniqueId());
        }
    }

    public void setShowItem() {
        player.getInventory().setItem(HidePlayer.getPlugin().getConfig().getInt("item.slot"), ItemManager.showItem());
    }

    public void setHideItem() {
        player.getInventory().setItem(HidePlayer.getPlugin().getConfig().getInt("item.slot"), ItemManager.hideItem());
    }
}
