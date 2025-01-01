package me.luucka.hideplayer.hook;

import com.alessiodp.parties.api.Parties;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import de.simonsator.partyandfriends.spigot.api.pafplayers.PAFPlayer;
import de.simonsator.partyandfriends.spigot.api.pafplayers.PAFPlayerManager;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.sk8ingduck.friendsystem.SpigotAPI;
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
	private static FriendSystemHook friendSystemHook;
	private static PartyAndFriendsHook partyAndFriendsHook;

	public static void loadDependencies() {
		if (Common.doesPluginExist("Parties")) {
			partiesHook = new PartiesHook();
			Common.log("Hook into Parties");
		}

		if (Common.doesPluginExist("FriendSystem-Spigot-API")) {
			friendSystemHook = new FriendSystemHook();
			Common.log("Hook into FriendSystem");
		}

		if (Common.doesPluginExist("PartyAndFriends")) {
			partyAndFriendsHook = new PartyAndFriendsHook();
			Common.log("Hook into PartyAndFriends");
		}
	}

	public static boolean isPartiesLoaded() {
		return partiesHook != null;
	}

	public static boolean isFriendSystemLoaded() {
		return friendSystemHook != null;
	}

	public static boolean isPartyAndFriendsLoaded() {
		return partyAndFriendsHook != null;
	}

	public static Set<UUID> getPartyMembers(Player player) {
		if (isPartiesLoaded()) {
			return partiesHook.getPartyMembers(player);
		} else if (isFriendSystemLoaded()) {
			return friendSystemHook.getPartyMembers(player);
		} else if (isPartyAndFriendsLoaded()) {
			return partyAndFriendsHook.getPartyMembers(player);
		}
		return Collections.emptySet();
	}

	public static Set<UUID> getFriends(Player player) {
		if (isFriendSystemLoaded()) {
			return friendSystemHook.getFriends(player);
		}
		return Collections.emptySet();
	}

}

class PartiesHook {

	private final com.alessiodp.parties.api.interfaces.PartiesAPI partiesApi;

	PartiesHook() {
		partiesApi = Parties.getApi();
	}

	Set<UUID> getPartyMembers(Player player) {
		final Party party = partiesApi.getPartyOfPlayer(player.getUniqueId());
		if (party != null) {
			return party.getOnlineMembers().stream()
					.map(PartyPlayer::getPlayerUUID)
					.collect(Collectors.toSet());
		}
		return Collections.emptySet();
	}

}

class FriendSystemHook {

	private final me.sk8ingduck.friendsystem.SpigotAPI friendSystemApi;
	private final me.sk8ingduck.friendsystem.manager.FriendManager friendManager;
	private final me.sk8ingduck.friendsystem.manager.PartyManager partyManager;

	FriendSystemHook() {
		friendSystemApi = SpigotAPI.getInstance();
		friendManager = friendSystemApi.getFriendManager();
		partyManager = friendSystemApi.getPartyManager();
	}

	Set<UUID> getPartyMembers(Player player) {
		Set<UUID> members = new HashSet<>();
		partyManager.getParty(player.getUniqueId(), party -> {
			if (party != null) {
				members.addAll(party.getAllMembers());
			}
		});
		return members;
	}

	Set<UUID> getFriends(Player player) {
		Set<UUID> friends = new HashSet<>();
		friendManager.getFriendPlayer(player.getUniqueId(), friendPlayer -> {
			if (friendPlayer != null) {
				friends.addAll(friendPlayer.getFriends().keySet().stream()
						.map(UUID::fromString)
						.collect(Collectors.toSet()));
			}
		});
		return friends;
	}

}

class PartyAndFriendsHook {

	private final de.simonsator.partyandfriends.spigot.api.pafplayers.PAFPlayerManager partyAndFriendsApi;

	PartyAndFriendsHook() {
		partyAndFriendsApi = PAFPlayerManager.getInstance();
	}

	Set<UUID> getPartyMembers(Player player) {
		return partyAndFriendsApi.getPlayer(player.getUniqueId()).getFriends().stream().map(PAFPlayer::getUniqueId).collect(Collectors.toSet());
	}
}