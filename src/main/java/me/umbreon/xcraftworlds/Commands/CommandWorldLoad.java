package me.umbreon.xcraftworlds.Commands;

import me.umbreon.xcraftworlds.Helper.CommandHelperWorld;
import me.umbreon.xcraftworlds.XcraftWorlds;
import org.bukkit.command.CommandSender;

import java.util.List;

public class CommandWorldLoad extends CommandHelperWorld {

    public CommandWorldLoad(XcraftWorlds plugin) {
        super(plugin);
    }

    @Override
    public void replyUsage(CommandSender sender) {
        reply(sender, "Usage: /gworld load <worldname>");
    }

    @Override
    public void execute(CommandSender sender, String worldName, List<String> args) {
        if (worldName == null) {
            error(sender, "No world given.");
            replyUsage(sender);
        } else if (!hasWorld(worldName)) {
            reply(sender, "Unknown world: " + worldName);
        } else if (getWorld(worldName).isLoaded()) {
            reply(sender, "World " + worldName + " already loaded.");
        } else {
            getWorld(worldName).load();
            reply(sender, "Loaded world " + worldName);
        }
    }

}
