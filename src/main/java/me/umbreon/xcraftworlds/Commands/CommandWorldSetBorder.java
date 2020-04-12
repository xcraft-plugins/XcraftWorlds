package me.umbreon.xcraftworlds.Commands;

import me.umbreon.xcraftworlds.Helper.CommandHelperWorld;
import me.umbreon.xcraftworlds.XcraftWorlds;
import org.bukkit.command.CommandSender;

import java.util.List;

public class CommandWorldSetBorder extends CommandHelperWorld {

    public CommandWorldSetBorder(XcraftWorlds plugin) {
        super(plugin);
    }

    @Override
    public void replyUsage(CommandSender sender) {
        reply(sender, "Usage: /gworld setborder <worldname> <#border>");
    }

    @Override
    public void execute(CommandSender sender, String worldName, List<String> args) {
        if (worldName == null) {
            error(sender, "No world given.");
            replyUsage(sender);
        } else if (!hasWorld(worldName)) {
            reply(sender, "World not found: " + worldName);
        } else {
            int border;

            try {
                border = Integer.parseInt(args.get(0));
            } catch (IndexOutOfBoundsException | NumberFormatException ex) {
                reply(sender, "Invalid number: " + (args.size() > 0 ? args.get(0) : "<null>"));
                reply(sender, "Usage: /gworld setborder <worldname> <#border>");
                return;
            }

            if (border <= 0) {
                getWorld(worldName).setBorder(0);
                reply(sender, "Border of world " + worldName + " removed.");
            } else {
                getWorld(worldName).setBorder(border);
                reply(sender, "Border of world " + worldName + " set to " + border + ".");
            }
        }
    }

}
