package me.umbreon.xcraftworlds.Commands;

import me.umbreon.xcraftworlds.Helper.CommandHelperWorld;
import me.umbreon.xcraftworlds.XcraftWorlds;
import org.bukkit.command.CommandSender;

import java.util.List;

public class CommandWorldInfo extends CommandHelperWorld {
    
    public CommandWorldInfo(XcraftWorlds plugin) {
        super(plugin);
    }
    
    @Override
    public void replyUsage(CommandSender sender) {
        reply(sender, "Usage: /gworld info <worldname>");
    }

    @Override
    public void execute(CommandSender sender, String worldName, List<String> args) {
        if (worldName == null) {
            error(sender, "No world given.");
            replyUsage(sender);
        } else if (!hasWorld(worldName)) {
            reply(sender, "World not found: " + worldName);
        } else {
            reply(sender, "Infos for world " + worldName + ":");
            getWorld(worldName).sendInfo(sender);
        }
    }
    
}
