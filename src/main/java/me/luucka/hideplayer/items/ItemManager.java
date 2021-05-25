package me.luucka.hideplayer.items;

import me.luucka.hideplayer.HidePlayer;
import me.luucka.hideplayer.utility.ChatUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {

    public static ItemStack giveShowItem() {
        ItemStack item = new ItemStack(Material.matchMaterial(HidePlayer.getPlugin().getConfig().getString("show.material")));
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatUtils.hexColor(HidePlayer.getPlugin().getConfig().getString("show.name")));
        List<String> lore = new ArrayList<>();
        lore.add(ChatUtils.hexColor(HidePlayer.getPlugin().getConfig().getString("show.lore")));
        meta.setLore(lore);

        NamespacedKey key = new NamespacedKey(HidePlayer.getPlugin(), "status");
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "SHOW");

        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack giveHideItem() {
        ItemStack item = new ItemStack(Material.matchMaterial(HidePlayer.getPlugin().getConfig().getString("hide.material")));
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatUtils.hexColor(HidePlayer.getPlugin().getConfig().getString("hide.name")));
        List<String> lore = new ArrayList<>();
        lore.add(ChatUtils.hexColor(HidePlayer.getPlugin().getConfig().getString("hide.lore")));
        meta.setLore(lore);

        NamespacedKey key = new NamespacedKey(HidePlayer.getPlugin(), "status");
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "HIDE");

        item.setItemMeta(meta);

        return item;
    }

}
