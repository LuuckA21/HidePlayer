package me.luucka.hideplayer.items;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.luucka.hideplayer.HidePlayer;
import me.luucka.lcore.item.ItemBuilder;
import me.luucka.lcore.utils.ColorTranslate;
import org.bukkit.Material;
import org.bukkit.craftbukkit.libs.org.apache.commons.codec.binary.Base64;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.UUID;

public class ItemManager {

    public static ItemStack showItem(Player player) {
        return createItem("show", player);
    }

    public static ItemStack hideItem(Player player) {
        return createItem("hide", player);
    }

    public static ItemStack createItem(String type, Player player) {

        String sMaterial = HidePlayer.getPlugin().getConfig().getString("item." + type + ".material");

        ItemBuilder itemBuilder;

        if (sMaterial.equalsIgnoreCase("CUSTOM_HEAD")) {
            String headValue = HidePlayer.getPlugin().getConfig().getString("item." + type + ".custom-head-value");
            if (headValue.isEmpty()) {
                itemBuilder = new ItemBuilder(Material.PLAYER_HEAD);
                itemBuilder.setOwningPlayer(player);
            } else {
                itemBuilder = new ItemBuilder(getCustomHead(headValue));
            }
        } else {
            itemBuilder = new ItemBuilder(Material.matchMaterial(sMaterial));
        }

        itemBuilder.setDisplayName(ColorTranslate.translate(HidePlayer.getPlugin().getConfig().getString("item." + type + ".name")));
        itemBuilder.setLore((ColorTranslate.translate(HidePlayer.getPlugin().getConfig().getString("item." + type + ".lore"))));
        itemBuilder.setPersistentDataContainer(HidePlayer.getPlugin(), "status", type.toUpperCase());

        return itemBuilder.toItemStack();
    }

    public static ItemStack getCustomHead(String url) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
        if (url == null || url.isEmpty())
            return skull;

        url = "http://textures.minecraft.net/texture/" + url;

        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField = null;
        try {
            profileField = skullMeta.getClass().getDeclaredField("profile");
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        profileField.setAccessible(true);
        try {
            profileField.set(skullMeta, profile);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        skull.setItemMeta(skullMeta);
        return skull;
    }

}
