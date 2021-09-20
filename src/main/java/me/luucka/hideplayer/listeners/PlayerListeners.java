package me.luucka.hideplayer.listeners;

import me.luucka.hideplayer.HidePlayer;
import me.luucka.hideplayer.PlayerVisibilityManager;
import me.luucka.hideplayer.User;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class PlayerListeners implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        User user = new User(player);
        user.createUser();

        if (HidePlayer.getPlugin().getConfig().getBoolean("use-visible-status-on-join")) {
            if (user.getVisible()) {
                if (HidePlayer.getPlugin().getConfig().getBoolean("item.enable")) {
                    user.setShowItem();
                }
            } else {
                PlayerVisibilityManager.hidePlayers(player);
                if (HidePlayer.getPlugin().getConfig().getBoolean("item.enable")) {
                    user.setHideItem();
                }
            }
        } else {
            if (HidePlayer.getPlugin().getConfig().getBoolean("item.enable")) {
                user.setShowItem();
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

        if (event.getHand() == EquipmentSlot.HAND && event.getAction() == Action.RIGHT_CLICK_AIR) interact(event.getPlayer());
        if (event.getHand() == EquipmentSlot.HAND && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
            ItemMeta meta = item.getItemMeta();
            if (meta == null) {
                return;
            }
            NamespacedKey key = new NamespacedKey(HidePlayer.getPlugin(), "status");
            PersistentDataContainer container = meta.getPersistentDataContainer();
            if (container.has(key, PersistentDataType.STRING)) {
                String sKey = container.get(key, PersistentDataType.STRING);
                if (sKey.equals("SHOW")) {
                    //event.getPlayer().performCommand("hideall");
                    event.getPlayer().sendMessage("hideall");
                } else if (sKey.equals("HIDE")) {
                    //event.getPlayer().performCommand("showall");
                    event.getPlayer().sendMessage("showall");
                }
            }
            //event.getPlayer().sendMessage("HAND - RIGHT - BLOCK");
        }

    }

    private void interact(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }
        NamespacedKey key = new NamespacedKey(HidePlayer.getPlugin(), "status");
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (container.has(key, PersistentDataType.STRING)) {
            String sKey = container.get(key, PersistentDataType.STRING);
            if (sKey.equals("SHOW")) {
                player.performCommand("hideall");
            } else if (sKey.equals("HIDE")) {
                player.performCommand("showall");
            }
        }
    }

}
