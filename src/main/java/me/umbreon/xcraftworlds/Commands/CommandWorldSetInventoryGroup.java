package me.umbreon.xcraftworlds.Commands;

import me.umbreon.xcraftworlds.Helper.CommandHelperWorld;
import me.umbreon.xcraftworlds.XcraftWorlds;
import org.bukkit.command.CommandSender;

import java.util.List;

public class CommandWorldSetInventoryGroup extends CommandHelperWorld {

    public CommandWorldSetInventoryGroup(XcraftWorlds plugin) {
        super(plugin);
    }

    @Override
    public void replyUsage(CommandSender sender) {
        reply(sender, "Usage: /gworld setinventorygroup <worldname> <groupname>");
    }

    @Override
    public void execute(CommandSender sender, String worldName, List<String> args) {
        if (worldName == null) {
            error(sender, "No world given.");
            replyUsage(sender);
        } else if (!hasWorld(worldName)) {
            reply(sender, "World not found: " + worldName);
        } else if (args.size() != 1) {
            error(sender, "Wrong argument count.");
            replyUsage(sender);
        } else {
            getWorld(worldName).setInventoryGroup(args.get(0));
            reply(sender, "Inventory group for world " + worldName + " set to " + args.get(0));
        }
    }

}
