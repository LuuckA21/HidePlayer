package me.luucka.hideplayer.commands;

import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class SubCommand {

    @Getter
    private final List<SubCommand> subCommands = new ArrayList<>();

    public abstract String getName();

    public abstract String getDescription();

    public abstract String getSyntax();

    public abstract String getPermission();

    public abstract boolean canOnlyPlayerUse();

    public abstract void perform(CommandSender sender, String[] args);

    public abstract List<String> getSubcommandArgs(Player player, String[] args);

}
