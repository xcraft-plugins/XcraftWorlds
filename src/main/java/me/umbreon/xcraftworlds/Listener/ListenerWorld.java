package me.umbreon.xcraftworlds.Listener;

import java.util.logging.Level;

import me.umbreon.xcraftworlds.XcraftWorlds;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.*;

public class ListenerWorld implements Listener {

    private final XcraftWorlds plugin;

    public ListenerWorld(XcraftWorlds instance) {
        plugin = instance;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWorldLoad(WorldLoadEvent event) {
        World world = event.getWorld();

        plugin.getWorlds().onWorldLoad(world);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWorldUnload(WorldUnloadEvent event) {
        World world = event.getWorld();
        plugin.getLogger().log(Level.INFO, "Trying to unload world {0}", new Object[]{world.getName()});
        // this is never called?!
    }
}
