package me.luucka.hideplayer.items;

public class ItemManager {

//    public static ItemStack showItem(Player player) {
//        return createItem("show", player);
//    }
//
//    public static ItemStack hideItem(Player player) {
//        return createItem("hide", player);
//    }
//
//    public static ItemStack createItem(String type, Player player) {
//
//        String sMaterial = HidePlayer.getPlugin().getConfig().getString("item." + type + ".material");
//        String headValue = HidePlayer.getPlugin().getConfig().getString("item." + type + ".custom-head-value");
//
//        ItemBuilder itemBuilder;
//
//        if (sMaterial.equalsIgnoreCase("PLAYER_HEAD")) {
//            if (headValue.isEmpty()) {
//                itemBuilder = new ItemBuilder(Material.PLAYER_HEAD);
//                itemBuilder.setOwningPlayer(player);
//            } else {
//                itemBuilder = new ItemBuilder(getCustomHead(headValue));
//            }
//        } else {
//            itemBuilder = new ItemBuilder(Material.matchMaterial(sMaterial));
//        }
//
//        itemBuilder
//                .setDisplayName(ColorTranslate.translate(HidePlayer.getPlugin().getConfig().getString("item." + type + ".name")))
//                .setLore((ColorTranslate.translate(HidePlayer.getPlugin().getConfig().getString("item." + type + ".lore"))))
//                .setPersistentDataContainer(HidePlayer.getPlugin(), "status", type.toUpperCase())
//                .setUnbreakable(true);
//
//        return itemBuilder.toItemStack();
//    }
//
//    public static ItemStack getCustomHead(String url) {
//        ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
//        if (url == null || url.isEmpty())
//            return skull;
//
//        url = "http://textures.minecraft.net/texture/" + url;
//
//        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
//        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
//        // old code for custom head: byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
//        byte[] encodedData = BaseEncoding.base64().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes()).getBytes();
//        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
//        Field profileField = null;
//        try {
//            profileField = skullMeta.getClass().getDeclaredField("profile");
//        } catch (NoSuchFieldException | SecurityException e) {
//            e.printStackTrace();
//        }
//        profileField.setAccessible(true);
//        try {
//            profileField.set(skullMeta, profile);
//        } catch (IllegalArgumentException | IllegalAccessException e) {
//            e.printStackTrace();
//        }
//        skull.setItemMeta(skullMeta);
//        return skull;
//    }

}
