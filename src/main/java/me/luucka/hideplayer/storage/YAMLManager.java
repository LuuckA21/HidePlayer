package me.luucka.hideplayer.storage;

import me.luucka.hideplayer.HidePlayer;

import java.util.List;
import java.util.UUID;

public class YAMLManager {

    // Table USER
    //------------------------------------------------------------------------------------------------------------------

    public static boolean userExists(UUID uuid) {
        return HidePlayer.yamlManager.cfg("data").contains(uuid.toString());
    }

    public static void createUser(UUID uuid) {
        if (userExists(uuid)) {
            return;
        }

        HidePlayer.yamlManager.cfg("data").set(uuid + ".visible", true);
        HidePlayer.yamlManager.save("data");
    }

    public static boolean getVisible(UUID uuid) {
        return HidePlayer.yamlManager.cfg("data").getBoolean(uuid + ".visible");
    }

    public static void setVisible(UUID uuid, boolean visible) {
        HidePlayer.yamlManager.cfg("data").set(uuid + ".visible", visible);
        HidePlayer.yamlManager.save("data");
    }

    // Table KEEPVISIBLE
    //------------------------------------------------------------------------------------------------------------------

    public static List<String> getKeepvisibleList(UUID myUUID) {
        return HidePlayer.yamlManager.cfg("data").getStringList(myUUID + ".keepvisible");
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
        HidePlayer.yamlManager.cfg("data").set(myUUID + ".keepvisible", myList);
        HidePlayer.yamlManager.save("data");
    }

    public static void removeKeepvisiblePlayer(UUID myUUID, UUID otherUUID) {
        List<String> myList = getKeepvisibleList(myUUID);
        myList.remove(otherUUID.toString());
        HidePlayer.yamlManager.cfg("data").set(myUUID + ".keepvisible", myList);
        HidePlayer.yamlManager.save("data");
    }

    public static void resetKeepvisiblePlayer(UUID myUUID) {
        HidePlayer.yamlManager.cfg("data").set(myUUID + ".keepvisible", null);
        HidePlayer.yamlManager.save("data");
    }

}
