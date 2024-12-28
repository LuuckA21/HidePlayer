package me.luucka.hideplayer.settings;

import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.settings.YamlStaticConfig;

import java.util.Arrays;
import java.util.List;

public class HideSettings extends YamlStaticConfig {

	@Override
	protected void onLoad() throws Exception {
		loadConfiguration("settings.yml");
	}

	public static final class Storage {
		public static String TYPE = "sqlite";

		private static void init() {
			setPathPrefix("Storage");

			if (isSetDefault("Type"))
				TYPE = getString("Type");
		}

		public static final class Settings {
			public static String HOST = "localhost";
			public static String PORT = "3306";
			public static String DATABASE = "hideplayer";
			public static String USER = "root";
			public static String PASSWORD = "";
			public static String SQLITE_FILE = "hideplayer.sqlite";

			private static void init() {
				setPathPrefix("Storage.Settings");

				if (isSetDefault("Host"))
					HOST = getString("Host");

				if (isSetDefault("Port"))
					PORT = getString("Port");

				if (isSetDefault("Database"))
					DATABASE = getString("Database");

				if (isSetDefault("User"))
					USER = getString("User");

				if (isSetDefault("Password"))
					PASSWORD = getString("Password");

				if (isSetDefault("Sqlite_File"))
					SQLITE_FILE = getString("Sqlite_File") + ".sqlite";
			}
		}
	}

	public static final class Item {
		public static Integer SLOT = 6;

		public static final String URL_BASE = "https://textures.minecraft.net/texture/";

		private static void init() {
			setPathPrefix("Item");

			if (isSetDefault("Slot"))
				SLOT = getInteger("Slot");
		}

		public static final class Show {
			public static CompMaterial MATERIAL = CompMaterial.LIME_DYE;

			public static String NAME = "&7Players: &aVisible &7(Right-Click)";

			public static List<String> LORE = Arrays.asList(
					"",
					"&7Right-Click to toggle",
					"player visibility!"
			);

			public static String CUSTOM_HEAD_VALUE = "";

			private static void init() {
				setPathPrefix("Item.Show");

				if (isSetDefault("Material"))
					MATERIAL = getMaterial("Material");

				if (isSetDefault("Name"))
					NAME = getString("Name");

				if (isSetDefault("Lore"))
					LORE = getStringList("Lore");

				if (isSetDefault("Custom_Head_Value"))
					CUSTOM_HEAD_VALUE = getString("Custom_Head_Value");
			}
		}

		public static final class Hide {
			public static CompMaterial MATERIAL = CompMaterial.GRAY_DYE;

			public static String NAME = "&7Players: &cHidden &7(Right-Click)";

			public static List<String> LORE = Arrays.asList(
					"",
					"&7Right-Click to toggle",
					"player visibility!"
			);

			public static String CUSTOM_HEAD_VALUE = "";

			private static void init() {
				setPathPrefix("Item.Hide");

				if (isSetDefault("Material"))
					MATERIAL = getMaterial("Material");

				if (isSetDefault("Name"))
					NAME = getString("Name");

				if (isSetDefault("Lore"))
					LORE = getStringList("Lore");

				if (isSetDefault("Custom_Head_Value"))
					CUSTOM_HEAD_VALUE = getString("Custom_Head_Value");
			}
		}
	}

	public static final class Cooldown {
		public static Integer COOLDOWN = 3;

		private static void init() {
			setPathPrefix(null);

			if (isSetDefault("Cooldown"))
				COOLDOWN = getInteger("Cooldown");
		}
	}
}
