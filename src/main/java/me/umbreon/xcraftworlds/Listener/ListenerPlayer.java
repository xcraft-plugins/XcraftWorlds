package me.umbreon.xcraftworlds.Listener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import me.umbreon.xcraftworlds.utils.DataWorld;
import me.umbreon.xcraftworlds.Manager.InventoryManager;
import me.umbreon.xcraftworlds.XcraftWorlds;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.yaml.snakeyaml.Yaml;

public class ListenerPlayer implements Listener {

    private Location location;
    private XcraftWorlds plugin = null;
    private final Map<UUID, String> playerDiedInWorld = new HashMap<>();
    private Map<String, String> playerLeftInWorld = new HashMap<>();

    public ListenerPlayer(XcraftWorlds instance) {
        plugin = instance;
    }

    @SuppressWarnings("unchecked")
    public void loadPlayers() {
        File configFile = plugin.getConfigFile("playerWorlds.yml");
        try {
            Yaml yaml = new Yaml();
            if ((playerLeftInWorld = (Map<String, String>) yaml.load(new FileInputStream(configFile))) == null) {
                playerLeftInWorld = new HashMap<>();
            }
        } catch (IOException ex) {
            plugin.getLogger().log(Level.WARNING, "Loading players data failed", ex);
        }
    }

    public void savePlayers() {
        File configFile = plugin.getConfigFile("playerWorlds.yml");
        Yaml yaml = new Yaml();
        String dump = yaml.dump(playerLeftInWorld);
        try (FileOutputStream fh = new FileOutputStream(configFile)) {
            new PrintStream(fh).println(dump);
            fh.flush();
        } catch (IOException ex) {
            plugin.getLogger().log(Level.WARNING, "Saving players data failed", ex);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerLogin(PlayerLoginEvent event) {
        if (event.getResult() == PlayerLoginEvent.Result.ALLOWED) {
            Player player = event.getPlayer();
            String worldName = this.playerLeftInWorld.get(player.getUniqueId().toString());
            DataWorld world = this.plugin.getWorlds().get(worldName);
            plugin.getLogger().log(Level.INFO, "Player {0} ({1}) trying to join in world {2}", new Object[]{player.getName(), player.getUniqueId(), worldName});
            if (world != null && !world.isLoaded()) {
                world.load();
            }
            if (worldName == null) {
                this.playerLeftInWorld.put(event.getPlayer().getUniqueId().toString(), event.getPlayer().getWorld().getName());
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        DataWorld thisWorld = this.plugin.getWorlds().get(player.getWorld());
        plugin.getLogger().log(Level.INFO, "Player {0} joined world \"{1}\" in GM {2}", new Object[]{player.getName(), thisWorld.getName(), thisWorld.getGameMode()});
        if (!player.hasPermission("XcraftGate.world.nogamemodechange")) {
            player.setGameMode(thisWorld.getGameMode());
        }
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        DataWorld fromWorld = plugin.getWorlds().get(event.getFrom());
        DataWorld toWorld = plugin.getWorlds().get(event.getPlayer().getWorld());

        if (plugin.getConfig().getBoolean("invsep.enabled") && !fromWorld.getInventoryGroup().equalsIgnoreCase(toWorld.getInventoryGroup())) {
            InventoryManager.changeInventory(event.getPlayer(), fromWorld, toWorld);
        }

        if (event.getPlayer().hasPermission("XcraftGate.world.info")) {
            event.getPlayer().sendMessage(ChatColor.AQUA + "World changed from " + fromWorld.getName() + " to " + toWorld.getName());
        }

        if (!event.getPlayer().hasPermission("XcraftGate.world.nogamemodechange")) {
            event.getPlayer().setGameMode(toWorld.getGameMode());
        }

        playerLeftInWorld.put(event.getPlayer().getUniqueId().toString(), event.getPlayer().getWorld().getName());
    }

    @EventHandler
    public void onPlayerGameModeChange(PlayerGameModeChangeEvent event) {
        if (plugin.getConfig().getBoolean("invsep.enabled")) {
            InventoryManager.changeInventroy(event.getPlayer(), event.getPlayer().getGameMode(), event.getNewGameMode(), plugin.getWorlds().get(event.getPlayer().getWorld()));
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        location = event.getTo();

        Location portTo;
        Location portFrom;

        if (plugin.getWorlds().get(location.getWorld()) == null) {
            return;
        }

        int border = plugin.getWorlds().get(location.getWorld()).getBorder();
        if (border > 0) {
            double x = location.getX();
            double z = location.getZ();

            if (Math.abs(x) >= border || Math.abs(z) >= border) {
                x = Math.abs(x) >= border ? (x > 0 ? border - 1 : -border + 1) : x;
                z = Math.abs(z) >= border ? (z > 0 ? border - 1 : -border + 1) : z;

                Location back = new Location(location.getWorld(), x, location.getY(), z, location.getYaw(), location.getPitch());

                //event.setCancelled(true);
                event.setTo(back);
                event.getPlayer().sendMessage(ChatColor.RED + "You reached the border of this world.");
                return;
            }
        }

        portTo = plugin.justTeleported.get(event.getPlayer().getName());
        portFrom = plugin.justTeleportedFrom.get(event.getPlayer().getName());

        if (portTo != null && portFrom == null) {
            plugin.justTeleported.remove(event.getPlayer().getName());
        }

        if (portTo == null && portFrom != null) {
            plugin.justTeleportedFrom.remove(event.getPlayer().getName());
        }

        if (portTo != null && portFrom != null) {
            if ((Math.floor(portTo.getX()) != Math.floor(location.getX()) || Math.floor(portTo.getZ()) != Math.floor(location.getZ()))
                    && (Math.floor(portFrom.getX()) != Math.floor(location.getX()) || Math.floor(portFrom.getZ()) != Math.floor(location.getZ()))) {
                plugin.justTeleported.remove(event.getPlayer().getName());
                plugin.justTeleportedFrom.remove(event.getPlayer().getName());
            }
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (plugin.config.getBoolean("fixes.chunkRefreshOnTeleport")) {
            Location targetLoc = event.getTo();
            World targetWorld = targetLoc.getWorld();
            Chunk targetChunk = targetWorld.getChunkAt(targetLoc);
            targetWorld.refreshChunk(targetChunk.getX(), targetChunk.getZ());
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        playerDiedInWorld.put(event.getEntity().getUniqueId(), event.getEntity().getWorld().getName());

        DataWorld world = plugin.getWorlds().get(event.getEntity().getWorld());

        if (world == null) {
            return;
        }

        if (!world.getAnnouncePlayerDeath()) {
            ((PlayerDeathEvent) event).setDeathMessage("");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        DataWorld worldDied = plugin.getWorlds().get(playerDiedInWorld.get(event.getPlayer().getUniqueId()));

        if (worldDied == null) {
            plugin.getLogger().log(Level.INFO, "Player {0} died, but i don\'t know where?! ({1})", new Object[]{event.getPlayer().getName(), event.getPlayer().getWorld().getName()});
            return;
        }

        switch (worldDied.getRespawnLocation()) {
            case WORLDSPAWN:
                event.setRespawnLocation(worldDied.getWorld().getSpawnLocation());
                break;
            case BEDSPAWN:
                if (event.getPlayer().getBedSpawnLocation() != null) {
                    event.setRespawnLocation(event.getPlayer().getBedSpawnLocation());
                } else {
                    event.setRespawnLocation(worldDied.getWorld().getSpawnLocation());
                }
                break;
            case WORLD:
                String respawnWorldName = worldDied.getRespawnWorldName();
                DataWorld respawnWorld;

                if (respawnWorldName != null && plugin.getWorlds().get(respawnWorldName) != null) {
                    respawnWorld = plugin.getWorlds().get(respawnWorldName);
                } else {
                    respawnWorld = worldDied;
                }

                event.setRespawnLocation(respawnWorld.getWorld().getSpawnLocation());
                break;
        }
    }

}
