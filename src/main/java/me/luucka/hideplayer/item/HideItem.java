package me.luucka.hideplayer.item;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.luucka.hideplayer.manager.VisibilityManager;
import me.luucka.hideplayer.settings.HideSettings;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.menu.tool.Tool;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.Remain;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HideItem extends Tool {

	@Getter
	private static final HideItem instance = new HideItem();

	@Override
	public ItemStack getItem() {
		if (HideSettings.Item.Hide.CUSTOM_HEAD_VALUE.isEmpty()) {
			return ItemCreator.of(HideSettings.Item.Hide.MATERIAL)
					.name(HideSettings.Item.Hide.NAME)
					.lore(HideSettings.Item.Hide.LORE)
					.make();
		} else {
			return ItemCreator.of(CompMaterial.PLAYER_HEAD)
					.name(HideSettings.Item.Hide.NAME)
					.lore(HideSettings.Item.Hide.LORE)
					.skullUrl(HideSettings.Item.URL_BASE + HideSettings.Item.Hide.CUSTOM_HEAD_VALUE)
					.make();
		}
	}

	@Override
	protected void onBlockClick(PlayerInteractEvent event) {
		if (!Remain.isInteractEventPrimaryHand(event)) return;
		if (event.getAction() != Action.RIGHT_CLICK_AIR) return;

		Player player = event.getPlayer();
		VisibilityManager.showPlayers(player);
	}

	@Override
	protected boolean ignoreCancelled() {
		return false;
	}
}
