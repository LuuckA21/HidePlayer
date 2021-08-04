package me.luucka.hideplayer.storage;

import me.luucka.hideplayer.HidePlayer;
import me.luucka.lcore.file.YamlFileManager;

import java.util.List;
import java.util.UUID;

public class YAMLManager {

    // Table USER
    //------------------------------------------------------------------------------------------------------------------

    public static boolean userExists(UUID uuid) {
        return YamlFileManager.file("data").contains(uuid.toString());
    }

    public static void createUser(UUID uuid) {
        if (userExists(uuid)) {
            return;
        }

        YamlFileManager.file("data").set(uuid + ".visible", true);
        YamlFileManager.saveFile("data");
    }

    public static boolean getVisible(UUID uuid) {
        return YamlFileManager.file("data").getBoolean(uuid + ".visible");
    }

    public static void setVisible(UUID uuid, boolean visible) {
        YamlFileManager.file("data").set(uuid + ".visible", visible);
        YamlFileManager.saveFile("data");
    }

    // Table KEEPVISIBLE
    //------------------------------------------------------------------------------------------------------------------

    public static List<String> getKeepvisibleList(UUID myUUID) {
        return YamlFileManager.file("data").getStringList(myUUID + ".keepvisible");
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
        YamlFileManager.file("data").set(myUUID + ".keepvisible", myList);
        YamlFileManager.saveFile("data");
    }

    public static void removeKeepvisiblePlayer(UUID myUUID, UUID otherUUID) {
        List<String> myList = getKeepvisibleList(myUUID);
        myList.remove(otherUUID.toString());
        YamlFileManager.file("data").set(myUUID + ".keepvisible", myList);
        YamlFileManager.saveFile("data");
    }

    public static void resetKeepvisiblePlayer(UUID myUUID) {
        YamlFileManager.file("data").set(myUUID + ".keepvisible", null);
        YamlFileManager.saveFile("data");
    }

}
