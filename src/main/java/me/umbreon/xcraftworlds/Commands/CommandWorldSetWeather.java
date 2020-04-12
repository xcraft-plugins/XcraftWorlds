package me.umbreon.xcraftworlds.Commands;

import me.umbreon.xcraftworlds.Helper.CommandHelperWorld;
import me.umbreon.xcraftworlds.utils.DataWorld;
import me.umbreon.xcraftworlds.XcraftWorlds;
import org.bukkit.command.CommandSender;

import java.util.List;

public class CommandWorldSetWeather extends CommandHelperWorld {

    public CommandWorldSetWeather(XcraftWorlds plugin) {
        super(plugin);
    }

    @Override
    public void replyUsage(CommandSender sender) {
        reply(sender, "Usage: /gworld setweather <worldname> <sun|storm>");
    }

    @Override
    public void execute(CommandSender sender, String worldName, List<String> args) {
        if (worldName == null) {
            error(sender, "No gate given.");
            replyUsage(sender);
        } else if (args.isEmpty()) {
            error(sender, "No weather given.");
            replyUsage(sender);
        } else if (!hasWorld(worldName)) {
            reply(sender, "World not found: " + worldName);
        } else {
            for (DataWorld.Weather thisWeather : DataWorld.Weather.values()) {
                if (thisWeather.toString().equalsIgnoreCase(args.get(0))) {
                    getWorld(worldName).setWeather(thisWeather);
                    reply(sender, "Weather of world " + worldName + " changed to " + args.get(0) + ".");
                    return;
                }
            }

            reply(sender, "Unknown weather type: " + args.get(0) + ". Use \"sun\" or \"storm\"");
        }
    }

}
