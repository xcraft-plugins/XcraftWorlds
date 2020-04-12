package me.umbreon.xcraftworlds.Commands;

import me.umbreon.xcraftworlds.Helper.CommandHelperWorld;
import me.umbreon.xcraftworlds.XcraftWorlds;
import org.bukkit.command.CommandSender;

import java.util.List;

public class CommandWorldUnload extends CommandHelperWorld {

    public CommandWorldUnload(XcraftWorlds instance) {
        super(instance);
    }

    @Override
    public void replyUsage(CommandSender sender) {
        reply(sender, "Usage: /gworld unload <worldname>");
    }

    @Override
    public void execute(CommandSender sender, String worldName,
            List<String> args) {
        if (worldName == null) {
            error(sender, "No world given.");
            replyUsage(sender);
        } else if (!hasWorld(worldName)) {
            reply(sender, "Unknown world: " + worldName);
        } else if (!getWorld(worldName).isLoaded()) {
            reply(sender, "World " + worldName + " is not loaded.");
        } else if (plugin.getServer().getWorld(worldName).getPlayers().size() > 0) {
            error(sender, "Unable to unload world with active players.");
        } else {
            getWorld(worldName).unload();
            reply(sender, "Unloaded world " + worldName);
        }
    }

}
