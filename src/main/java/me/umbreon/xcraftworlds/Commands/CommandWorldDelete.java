package me.umbreon.xcraftworlds.Commands;

import me.umbreon.xcraftworlds.Helper.CommandHelperWorld;
import me.umbreon.xcraftworlds.XcraftWorlds;
import org.bukkit.command.CommandSender;

import java.util.List;

public class CommandWorldDelete extends CommandHelperWorld {

    public CommandWorldDelete(XcraftWorlds plugin) {
        super(plugin);
    }

    @Override
    public void replyUsage(CommandSender sender) {
        reply(sender, "Usage: /gworld delete <worldname>");
    }

    @Override
    public void execute(CommandSender sender, String worldName, List<String> args) {
        if (worldName == null) {
            error(sender, "No world given.");
            replyUsage(sender);
        } else if (!hasWorld(worldName)) {
            reply(sender, "World not found: " + worldName);
        } else {
            if (getWorld(worldName).isLoaded()) {
                if (plugin.getServer().getWorld(worldName).getPlayers().size() > 0) {
                    error(sender, "Unable to unload world with active players.");
                    return;
                } else {
                    getWorld(worldName).unload();
                }
            }

            plugin.getWorlds().remove(worldName);
            reply(sender, "World " + worldName + " removed.");
        }
    }
}
