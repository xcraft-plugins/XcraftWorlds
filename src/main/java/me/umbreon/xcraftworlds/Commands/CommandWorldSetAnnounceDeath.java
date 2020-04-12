package me.umbreon.xcraftworlds.Commands;

import me.umbreon.xcraftworlds.Helper.CommandHelperWorld;
import me.umbreon.xcraftworlds.XcraftWorlds;
import org.bukkit.command.CommandSender;

import java.util.List;

public class CommandWorldSetAnnounceDeath extends CommandHelperWorld {

    public CommandWorldSetAnnounceDeath(XcraftWorlds plugin) {
        super(plugin);
    }

    @Override
    public void replyUsage(CommandSender sender) {
        reply(sender, "Usage: /gworld setannouncedeath <worldname> <true|false>");
    }

    @Override
    public void execute(CommandSender sender, String worldName, List<String> args) {
        if (worldName == null) {
            error(sender, "No world given.");
            replyUsage(sender);
        } else if (!hasWorld(worldName)) {
            reply(sender, "World not found: " + worldName);
        } else {
            Boolean announce;

            announce = (args.isEmpty() || !args.get(0).equalsIgnoreCase("false"));

            getWorld(worldName).setAnnouncePlayerDeath(announce);
            reply(sender, "Death announcements on " + worldName + (announce ? " enabled." : " disabled."));
        }
    }

}
