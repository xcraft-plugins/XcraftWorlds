package me.umbreon.xcraftworlds.Listener;

import me.umbreon.xcraftworlds.XcraftWorlds;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;

public class ListenerServer implements Listener {

    private XcraftWorlds plugin = null;

    public ListenerServer(XcraftWorlds instance) {
        plugin = instance;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPluginDisable(PluginDisableEvent event) {
        plugin.getPluginManager().checkDisabledPlugin(event.getPlugin());
    }

}
