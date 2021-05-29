package me.luucka.hideplayer.listeners;

import me.luucka.hideplayer.HidePlayer;
import me.luucka.hideplayer.HidePlayerUser;
import me.luucka.hideplayer.PlayerVisibilityManager;
import me.luucka.hideplayer.items.ItemManager;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class PlayerListeners implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        HidePlayerUser user = new HidePlayerUser(player);
        user.createUser();

        // Add item
        if (HidePlayer.getPlugin().getConfig().getBoolean("default-state-show")) {
            player.getInventory().setItem(6, ItemManager.giveShowItem());
        } else {
            if (user.getVisible()) {
                player.getInventory().setItem(6, ItemManager.giveShowItem());
            } else {
                PlayerVisibilityManager.hidePlayers(player);
                player.getInventory().setItem(6, ItemManager.giveHideItem());
            }
        }
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && !event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            return;
        }

        Player player = event.getPlayer();
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
