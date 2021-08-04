package me.luucka.hideplayer.items;

import me.luucka.hideplayer.HidePlayer;
import me.luucka.lcore.utility.ColorTranslate;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {

    public static ItemStack showItem() {
        return createItem("show");
    }

    public static ItemStack hideItem() {
        return createItem("hide");
    }

    public static ItemStack createItem(String type) {
        ItemStack item = new ItemStack(Material.matchMaterial(HidePlayer.getPlugin().getConfig().getString("item." + type + ".material")));
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ColorTranslate.translate(HidePlayer.getPlugin().getConfig().getString("item." + type + ".name")));
        List<String> lore = new ArrayList<>();
        lore.add(ColorTranslate.translate(HidePlayer.getPlugin().getConfig().getString("item." + type + ".lore")));
        meta.setLore(lore);

        NamespacedKey key = new NamespacedKey(HidePlayer.getPlugin(), "status");
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, type.toUpperCase());

        item.setItemMeta(meta);

        return item;
    }

}
