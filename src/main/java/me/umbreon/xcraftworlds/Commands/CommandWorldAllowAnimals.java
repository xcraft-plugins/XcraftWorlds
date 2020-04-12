package me.umbreon.xcraftworlds.Commands;

import java.util.List;

import me.umbreon.xcraftworlds.Helper.CommandHelperWorld;
import me.umbreon.xcraftworlds.XcraftWorlds;
import org.bukkit.command.CommandSender;

public class CommandWorldAllowAnimals extends CommandHelperWorld {

    public CommandWorldAllowAnimals(XcraftWorlds plugin) {
        super(plugin);
    }

    @Override
    public void replyUsage(CommandSender sender) {
        reply(sender, "Usage: /gworld allowanimals <worldname> <true|false>");
    }

    @Override
    public void execute(CommandSender sender, String worldName, List<String> args) {
        if (worldName == null) {
            error(sender, "No world given.");
            replyUsage(sender);
        } else if (!hasWorld(worldName)) {
            reply(sender, "World not found: " + worldName);
        } else {
            Boolean allowed;

            allowed = (args.isEmpty() || !args.get(0).equalsIgnoreCase("false"));

            getWorld(worldName).setAllowAnimals(allowed);
            reply(sender, "Animal spawn on " + worldName + (allowed ? " enabled." : " disabled."));
        }
    }

}
