package me.luucka.hideplayer.command;

import org.mineacademy.fo.annotation.AutoRegister;
import org.mineacademy.fo.command.DebugCommand;
import org.mineacademy.fo.command.ReloadCommand;
import org.mineacademy.fo.command.SimpleCommandGroup;

@AutoRegister
public final class HidePlayerCommandGroup extends SimpleCommandGroup {

	public HidePlayerCommandGroup() {
		super("hideplayer");
	}

	@Override
	protected void registerSubcommands() {
		registerSubcommand(new ReloadCommand());
		registerSubcommand(new DebugCommand());
	}

	@Override
	protected String getCredits() {
		return "&7Visit &fgithub.com/LuuckA21/HidePlayer &7for more information.";
	}
}
