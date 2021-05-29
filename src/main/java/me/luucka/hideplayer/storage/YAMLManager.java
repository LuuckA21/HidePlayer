package me.luucka.hideplayer.storage;

import me.luucka.hideplayer.HidePlayer;

import java.util.List;
import java.util.UUID;

public class YAMLManager {

    // Table USER
    //------------------------------------------------------------------------------------------------------------------

    public static boolean userExists(UUID uuid) {
        return HidePlayer.getPlugin().getDataYml().getConfig().contains(uuid.toString());
    }

    public static void createUser(UUID uuid) {
        if (userExists(uuid)) {
            return;
        }

        HidePlayer.getPlugin().getDataYml().getConfig().set(uuid + ".visible", true);
        HidePlayer.getPlugin().getDataYml().saveConfig();
    }

    public static boolean getVisible(UUID uuid) {
        return HidePlayer.getPlugin().getDataYml().getConfig().getBoolean(uuid + ".visible");
    }

    public static void setVisible(UUID uuid, boolean visible) {
        HidePlayer.getPlugin().getDataYml().getConfig().set(uuid + ".visible", visible);
        HidePlayer.getPlugin().getDataYml().saveConfig();
    }

    // Table KEEPVISIBLE
    //------------------------------------------------------------------------------------------------------------------

    public static List<String> getKeepvisibleList(UUID myUUID) {
        return HidePlayer.getPlugin().getDataYml().getConfig().getStringList(myUUID + ".keepvisible");
    }

    public static boolean isPlayerInKeepvisibleList(UUID myUUID, UUID otherUUID) {
        return getKeepvisibleList(myUUID).contains(otherUUID.toString());
    }

    public static void addKeepvisiblePlayer(UUID myUUID, UUID otherUUID) {
        if (isPlayerInKeepvisibleList(myUUID, otherUUID)) {
            return;
        }

        List<String> myList = getKeepvisibleList(myUUID);
        myList.add(otherUUID.toString());
        HidePlayer.getPlugin().getDataYml().getConfig().set(myUUID + ".keepvisible", myList);
        HidePlayer.getPlugin().getDataYml().saveConfig();
    }

    public static void removeKeepvisiblePlayer(UUID myUUID, UUID otherUUID) {
        List<String> myList = getKeepvisibleList(myUUID);
        myList.remove(otherUUID.toString());
        HidePlayer.getPlugin().getDataYml().getConfig().set(myUUID + ".keepvisible", myList);
        HidePlayer.getPlugin().getDataYml().saveConfig();
    }

    public static void resetKeepvisiblePlayer(UUID myUUID) {
        HidePlayer.getPlugin().getDataYml().getConfig().set(myUUID + ".keepvisible", null);
        HidePlayer.getPlugin().getDataYml().saveConfig();
    }

}
