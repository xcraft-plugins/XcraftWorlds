package me.umbreon.xcraftworlds.Commands;

import me.umbreon.xcraftworlds.Helper.CommandHelperWorld;
import me.umbreon.xcraftworlds.XcraftWorlds;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandWorldSetSpawn extends CommandHelperWorld {

    public CommandWorldSetSpawn(XcraftWorlds plugin) {
        super(plugin);
    }

    @Override
    public void replyUsage(CommandSender sender) {
    }
    @Override
    public void execute(CommandSender sender, String worldName, List<String> args) {
        Location loc = ((Player) sender).getLocation();
        loc.getWorld().setSpawnLocation((int) Math.floor(loc.getX()), (int) Math.floor(loc.getY()), (int) Math.floor(loc.getZ()));
        reply(sender, "Set spawn location of " + loc.getWorld().getName() + " to your current position.");
    }

}
