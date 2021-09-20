package me.luucka.hideplayer.utility;

import me.luucka.hideplayer.HidePlayer;
import me.luucka.lcore.utils.ColorTranslate;

public class Chat {

    public static String message(String s) {
        return ColorTranslate.translate(HidePlayer.yamlManager.cfg("messages").getString("prefix") + s);
    }

}
