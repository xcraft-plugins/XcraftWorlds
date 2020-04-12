package me.umbreon.xcraftworlds.Helper;

import java.util.List;

import me.umbreon.xcraftworlds.utils.DataWorld;
import me.umbreon.xcraftworlds.XcraftWorlds;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

public abstract class CommandHelperWorld extends CommandHelper {

    public CommandHelperWorld(XcraftWorlds plugin) {
        super(plugin);
    }

    public abstract void execute(CommandSender sender, String worldName, List<String> args);

    public boolean hasWorld(World world) {
        return hasWorld(world.getName());
    }

    public boolean hasWorld(String world) {
        return (plugin.getWorlds().get(world) != null);
    }

    public DataWorld getWorld(World world) {
        return getWorld(world.getName());
    }

    public DataWorld getWorld(String name) {
        return plugin.getWorlds().get(name);
    }

}
