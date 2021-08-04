package me.luucka.hideplayer.utility;

import me.luucka.lcore.file.YamlFileManager;
import me.luucka.lcore.utility.ColorTranslate;

public class Chat {

    public static String message(String s) {
        return ColorTranslate.translate(YamlFileManager.file("messages").getString("prefix") + s);
    }

}
