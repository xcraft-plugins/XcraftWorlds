package me.umbreon.xcraftworlds.Commands;

import me.umbreon.xcraftworlds.Helper.CommandHelperWorld;
import me.umbreon.xcraftworlds.XcraftWorlds;
import org.bukkit.command.CommandSender;

import java.util.List;

public class CommandWorldKeepSpawnInMemory extends CommandHelperWorld {

    public CommandWorldKeepSpawnInMemory(XcraftWorlds plugin) {
        super(plugin);
    }

    @Override
    public void replyUsage(CommandSender sender) {
        reply(sender, "Usage: /gworld keepspawninmemory <worldname> <true|false>");
    }

    @Override
    public void execute(CommandSender sender, String worldName,
            List<String> args) {
        if (worldName == null) {
            error(sender, "No world given.");
            replyUsage(sender);
        } else if (!hasWorld(worldName)) {
            reply(sender, "World not found: " + worldName);
        } else {
            Boolean keep;

            keep = (args.isEmpty() || !args.get(0).equalsIgnoreCase("false"));

            getWorld(worldName).setKeepSpawnInMemory(keep);
            reply(sender, "Spawnarea on " + worldName + (keep ? " stays in memory." : " will get unloaded normally."));
        }
    }

}
