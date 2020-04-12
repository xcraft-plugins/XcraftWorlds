package me.umbreon.xcraftworlds.Commands;

import me.umbreon.xcraftworlds.Helper.CommandHelperWorld;
import me.umbreon.xcraftworlds.XcraftWorlds;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandWorldWarpto extends CommandHelperWorld {
    
    public CommandWorldWarpto(XcraftWorlds plugin) {
        super(plugin);
    }
    
    @Override
    public void replyUsage(CommandSender sender) {
        reply(sender, "Usage: /gworld warpto <worldname>");
    }
    
    @Override
    public void execute(CommandSender sender, String worldName, List<String> args) {
        if (worldName == null) {
            error(sender, "No world given.");
            replyUsage(sender);
        } else if (!hasWorld(worldName)) {
            reply(sender, "World not found: " + worldName);
        } else {
            if (!getWorld(worldName).isLoaded()) {
                getWorld(worldName).load();
            }
            
            Location loc = getWorld(worldName).getWorld().getSpawnLocation();
            if (loc != null) {
                ((Player) sender).teleport(loc);
            } else {
                error(sender, "Couldn't find a safe spot at your destination");
            }
        }
    }
}
