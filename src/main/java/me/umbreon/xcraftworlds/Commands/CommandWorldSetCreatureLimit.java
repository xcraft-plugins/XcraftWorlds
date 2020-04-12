package me.umbreon.xcraftworlds.Commands;

import me.umbreon.xcraftworlds.Helper.CommandHelperWorld;
import me.umbreon.xcraftworlds.XcraftWorlds;
import org.bukkit.command.CommandSender;

import java.util.List;

public class CommandWorldSetCreatureLimit extends CommandHelperWorld {

    public CommandWorldSetCreatureLimit(XcraftWorlds plugin) {
        super(plugin);
    }

    @Override
    public void replyUsage(CommandSender sender) {
        reply(sender, "Usage: /gworld setborder <worldname> <#limit>");
    }

    @Override
    public void execute(CommandSender sender, String worldName, List<String> args) {
        if (worldName == null) {
            error(sender, "No world given.");
            replyUsage(sender);
        } else if (!hasWorld(worldName)) {
            reply(sender, "World not found: " + worldName);
        } else {
            int limit;

            try {
                limit = Integer.parseInt(args.get(0));
            } catch (IndexOutOfBoundsException | NumberFormatException ex) {
                reply(sender, "Invalid number: " + (args.size() > 0 ? args.get(0) : "<null>"));
                replyUsage(sender);
                return;
            }

            if (limit <= 0) {
                getWorld(worldName).setCreatureLimit(0);
                reply(sender, "Creature limit of world " + worldName + " removed.");
            } else {
                getWorld(worldName).setCreatureLimit(limit);
                reply(sender, "Creature limit of world " + worldName + " set to " + limit + ".");
            }
        }
    }

}
