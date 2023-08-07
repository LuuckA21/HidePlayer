package me.luucka.hideplayer.listener;

import org.bukkit.event.Listener;

public class PlayerListener implements Listener {

//    private final HidePlayer PLUGIN;
//
//    public PlayerListener(HidePlayer PLUGIN) {
//        this.PLUGIN = PLUGIN;
//    }
//
//    @EventHandler
//    public void onJoin(PlayerJoinEvent event) {
//        Player player = event.getPlayer();
//        User user = new User(player);
//        user.createUser();
//
//        if (PLUGIN.getConfig().getBoolean("use-visible-status-on-join")) {
//            if (user.getVisible()) {
//                if (PLUGIN.getConfig().getBoolean("item.enable")) {
//                    user.setShowItem();
//                }
//            } else {
//                PlayerVisibilityManager.hidePlayers(player);
//                if (PLUGIN.getConfig().getBoolean("item.enable")) {
//                    user.setHideItem();
//                }
//            }
//        } else {
//            user.setVisible(true);
//            if (PLUGIN.getConfig().getBoolean("item.enable")) {
//                user.setShowItem();
//            }
//        }
//
//        PLUGIN.getServer().getOnlinePlayers().forEach(onlinePlayer -> {
//            User onlineUser = new User(onlinePlayer);
//            if (!onlineUser.getVisible()) {
//                if (!onlineUser.isPlayerInKeepvisibleList(player.getUniqueId())) {
//                    onlinePlayer.hidePlayer(PLUGIN, player);
//                }
//            }
//        });
//    }
//
//    @EventHandler
//    public void onInteract(PlayerInteractEvent event) {
//        if (event.getHand() == EquipmentSlot.HAND && event.getAction() == Action.RIGHT_CLICK_AIR)
//            interact(event.getPlayer());
//        if (event.getHand() == EquipmentSlot.HAND && event.getAction() == Action.RIGHT_CLICK_BLOCK)
//            interact(event.getPlayer());
//    }
//
//    private void interact(Player player) {
//        ItemStack item = player.getInventory().getItemInMainHand();
//        ItemMeta meta = item.getItemMeta();
//        if (meta == null) return;
//
//        NamespacedKey key = new NamespacedKey(PLUGIN, "status");
//        PersistentDataContainer container = meta.getPersistentDataContainer();
//        if (container.has(key, PersistentDataType.STRING)) {
//            String sKey = container.get(key, PersistentDataType.STRING);
//            if (sKey == null) return;
//
//            User user = new User(player);
//
//            if (sKey.equals("SHOW")) {
//                if (!PLUGIN.cooldownManager(player)) {
//                    return;
//                }
//
//                if (user.getVisible()) {
//                    PlayerVisibilityManager.hidePlayers(user.getPlayer());
//                    user.getPlayer().sendMessage(Chat.message(HidePlayer.yamlManager.cfg("messages").getString("hideall")));
//                    player.getInventory().getItemInMainHand().setType(ItemManager.hideItem(player).getType());
//                    player.getInventory().getItemInMainHand().setItemMeta(ItemManager.hideItem(player).getItemMeta());
//                }
//            } else if (sKey.equals("HIDE")) {
//                if (!PLUGIN.cooldownManager(player)) {
//                    return;
//                }
//
//                if (!user.getVisible()) {
//                    PlayerVisibilityManager.showPlayers(user.getPlayer());
//                    user.getPlayer().sendMessage(Chat.message(HidePlayer.yamlManager.cfg("messages").getString("showall")));
//                    player.getInventory().getItemInMainHand().setType(ItemManager.showItem(player).getType());
//                    player.getInventory().getItemInMainHand().setItemMeta(ItemManager.showItem(player).getItemMeta());
//                }
//            }
//        }
//    }

}
