package me.luucka.hideplayer.listeners;

import me.luucka.hideplayer.HidePlayer;
import me.luucka.hideplayer.items.ItemManager;
import me.luucka.hideplayer.utility.ChatUtils;
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

        // Data.yml
        if (!HidePlayer.getPlugin().getDataYml().getConfig().contains(player.getUniqueId().toString())) {
            System.out.println("CREAZIONEEE");
            HidePlayer.getPlugin().getDataYml().getConfig().set(player.getUniqueId() + ".visible", true);
            HidePlayer.getPlugin().getDataYml().saveConfig();
        }

        // Add item
        if (HidePlayer.getPlugin().getConfig().getBoolean("default-state-show")) {
            player.getInventory().setItem(6, ItemManager.giveShowItem());
        } else {
            if (HidePlayer.getPlugin().getDataYml().getConfig().getBoolean(player.getUniqueId() + ".visible")) {
                player.getInventory().setItem(6, ItemManager.giveShowItem());
            } else {
                HidePlayer.getPlugin().hidePlayers(player);
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

        NamespacedKey key = new NamespacedKey(HidePlayer.getPlugin(), "status");
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (container.has(key, PersistentDataType.STRING)) {
            String sKey = container.get(key, PersistentDataType.STRING);
            if (sKey.equals("SHOW")) {
                if (HidePlayer.getPlugin().cooldownManager(player)) {
                    player.performCommand("hideall");
                }
            } else if (sKey.equals("HIDE")) {
                if (HidePlayer.getPlugin().cooldownManager(player)) {
                    player.performCommand("showall");
                }
            }
        }
    }

}
