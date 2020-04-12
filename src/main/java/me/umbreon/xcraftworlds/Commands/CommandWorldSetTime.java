package me.umbreon.xcraftworlds.Commands;

import me.umbreon.xcraftworlds.Helper.CommandHelperWorld;
import me.umbreon.xcraftworlds.utils.DataWorld;
import me.umbreon.xcraftworlds.XcraftWorlds;
import org.bukkit.command.CommandSender;

import java.util.List;

public class CommandWorldSetTime extends CommandHelperWorld {

    public CommandWorldSetTime(XcraftWorlds plugin) {
        super(plugin);
    }

    @Override
    public void replyUsage(CommandSender sender) {
        reply(sender, "Usage: /gworld settime <worldname> <sunrise|noon|sunset|midnight>");
    }

    @Override
    public void execute(CommandSender sender, String worldName, List<String> args) {
        if (worldName == null) {
            error(sender, "No world given.");
            replyUsage(sender);
        } else if (args.isEmpty()) {
            error(sender, "No time given.");
            replyUsage(sender);
        } else if (!hasWorld(worldName)) {
            reply(sender, "World not found: " + worldName);
        } else {
            for (DataWorld.DayTime thisTime : DataWorld.DayTime.values()) {
                if (thisTime.toString().equalsIgnoreCase(args.get(0))) {
                    getWorld(worldName).setDayTime(thisTime);
                    reply(sender, "Time of world " + worldName + " changed to " + args.get(0) + ".");
                    return;
                }
            }

            reply(sender, "Unknown time: " + args.get(0) + ". Use \"sunrise\", \"noon\", \"sunset\" or \"midnight\"");
        }
    }

}
