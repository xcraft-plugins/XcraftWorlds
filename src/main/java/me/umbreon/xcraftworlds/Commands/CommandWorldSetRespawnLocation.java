package me.umbreon.xcraftworlds.Commands;

import me.umbreon.xcraftworlds.Helper.CommandHelperWorld;
import me.umbreon.xcraftworlds.utils.DataWorld;
import me.umbreon.xcraftworlds.XcraftWorlds;
import org.bukkit.command.CommandSender;

import java.util.List;

public class CommandWorldSetRespawnLocation extends CommandHelperWorld {

    public CommandWorldSetRespawnLocation(XcraftWorlds plugin) {
        super(plugin);
    }

    @Override
    public void replyUsage(CommandSender sender) {
        reply(sender, "Usage: /gworld setrespawnlocation <worldname> <worldspawn|bedspawn|world <worldname>>");
    }

    @Override
    public void execute(CommandSender sender, String worldName, List<String> args) {
        if (worldName == null) {
            error(sender, "No world given.");
            replyUsage(sender);
        } else if (!hasWorld(worldName)) {
            reply(sender, "Unknown world: " + worldName);
        } else if (args.size() < 1) {
            error(sender, "No location given.");
            replyUsage(sender);
        } else {
            String rsLoc = args.get(0);
            DataWorld.RespawnLocation newRSLoc = null;

            for (DataWorld.RespawnLocation thisRLoc : DataWorld.RespawnLocation.values()) {
                if (thisRLoc.toString().equalsIgnoreCase(rsLoc)) {
                    newRSLoc = thisRLoc;
                }
            }

            if (newRSLoc == null) {
                reply(sender, "Unknown respawn location: " + rsLoc);
                reply(sender, "Usage: /gworld setrespawnlocation <worldname> <worldspawn|bedspawn|world <worldname>>");
            }

            if (newRSLoc == DataWorld.RespawnLocation.WORLD) {
                if (args.size() < 2) {
                    error(sender, "No respawn world given.");
                    reply(sender, "Usage: /gworld setrespawnlocation <worldname> <worldspawn|bedspawn|world <worldname>>");
                    return;
                } else if (!hasWorld(args.get(1))) {
                    reply(sender, "Unknown respawn world: " + args.get(1));
                    return;
                } else {
                    getWorld(worldName).setRespawnWorldName(args.get(1));
                }
            }

            getWorld(worldName).setRespawnLocation(newRSLoc);
            reply(sender, "RespawnLocation for world " + worldName + " set to " + newRSLoc.toString() + (newRSLoc == DataWorld.RespawnLocation.WORLD ? ": " + args.get(1) : ""));
        }
    }

}
