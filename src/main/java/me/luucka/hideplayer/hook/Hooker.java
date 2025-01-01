package me.luucka.hideplayer.hook;

import com.alessiodp.parties.api.Parties;
import com.alessiodp.parties.api.interfaces.PartiesAPI;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Hooker {

	private static PartiesHook partiesHook;

	public static void loadDependencies() {
		if (Common.doesPluginExist("Parties")) {
			partiesHook = new PartiesHook();
		}
	}

	public static boolean isPartiesLoaded() {
		return partiesHook != null;
	}

	public static Set<UUID> getPartyPlayers(Player player) {
		if (isPartiesLoaded()) {
			return partiesHook.getPartyPlayers(player);
		}
		return new HashSet<>();
	}

}

class PartiesHook {

	private final PartiesAPI partiesApi;

	PartiesHook() {
		partiesApi = Parties.getApi();
	}

	Set<UUID> getPartyPlayers(Player player) {
		final Party party = partiesApi.getPartyOfPlayer(player.getUniqueId());
		if (party != null) {
			return party.getOnlineMembers().stream()
					.map(PartyPlayer::getPlayerUUID)
					.collect(Collectors.toSet());
		}
		return Collections.emptySet();
	}

}