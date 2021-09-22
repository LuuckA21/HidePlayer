package me.luucka.hideplayer.listeners;

import me.luucka.hideplayer.HidePlayer;
import me.luucka.hideplayer.PlayerVisibilityManager;
import me.luucka.hideplayer.User;
import me.luucka.hideplayer.items.ItemManager;
import me.luucka.hideplayer.utility.Chat;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
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
        if (event.getHand() == EquipmentSlot.HAND && event.getAction() == Action.RIGHT_CLICK_BLOCK) interact(event.getPlayer());
    }

    private void interact(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        NamespacedKey key = new NamespacedKey(HidePlayer.getPlugin(), "status");
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (container.has(key, PersistentDataType.STRING)) {
            String sKey = container.get(key, PersistentDataType.STRING);
            if (sKey == null) return;

            User user = new User(player);

            if (sKey.equals("SHOW")) {
                if (!HidePlayer.getPlugin().cooldownManager(player)) {
                    return;
                }

                if (user.getVisible()) {
                    PlayerVisibilityManager.hidePlayers(user.getPlayer());
                    user.getPlayer().sendMessage(Chat.message(HidePlayer.yamlManager.cfg("messages").getString("hideall")));
                    player.getInventory().getItemInMainHand().setType(ItemManager.hideItem(player).getType());
                    player.getInventory().getItemInMainHand().setItemMeta(ItemManager.hideItem(player).getItemMeta());
                }
            } else if (sKey.equals("HIDE")) {
                if (!HidePlayer.getPlugin().cooldownManager(player)) {
                    return;
                }

                if (!user.getVisible()) {
                    PlayerVisibilityManager.showPlayers(user.getPlayer());
                    user.getPlayer().sendMessage(Chat.message(HidePlayer.yamlManager.cfg("messages").getString("showall")));
                    player.getInventory().getItemInMainHand().setType(ItemManager.showItem(player).getType());
                    player.getInventory().getItemInMainHand().setItemMeta(ItemManager.showItem(player).getItemMeta());
                }
            }
        }
    }

}
