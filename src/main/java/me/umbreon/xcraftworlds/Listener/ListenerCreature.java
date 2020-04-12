package me.umbreon.xcraftworlds.Listener;

import me.umbreon.xcraftworlds.XcraftWorlds;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class ListenerCreature implements Listener {

    private final XcraftWorlds plugin;

    public ListenerCreature(XcraftWorlds instance) {
        plugin = instance;
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (plugin.getWorlds().get(event.getLocation().getWorld()) != null) {
            plugin.getWorlds().get(event.getLocation().getWorld()).checkCreatureLimit();
        }
    }
}
