package me.luucka.hideplayer.items;

import me.luucka.hideplayer.HidePlayer;
import me.luucka.lcore.utils.ColorTranslate;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {

    public static ItemStack showItem(Player player) {
        return createItem("show", player);
    }

    public static ItemStack hideItem(Player player) {
        return createItem("hide", player);
    }

    public static ItemStack createItem(String type, Player player) {

        String sMaterial = HidePlayer.getPlugin().getConfig().getString("item." + type + ".material");

        ItemStack item = new ItemStack(Material.matchMaterial(sMaterial.equalsIgnoreCase("CUSTOM_HEAD") ? "PLAYER_HEAD" : sMaterial));

        if (sMaterial.equalsIgnoreCase("CUSTOM_HEAD")) {
            SkullMeta sMeta = (SkullMeta) item.getItemMeta();
            if (sMeta == null) return null;
            sMeta.setOwningPlayer(player);
            item.setItemMeta(sMeta);
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;

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
