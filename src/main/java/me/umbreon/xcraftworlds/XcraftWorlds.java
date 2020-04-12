package me.umbreon.xcraftworlds;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.stream.Collectors;


import me.umbreon.xcraftworlds.Handler.CommandHandlerWorld;
import me.umbreon.xcraftworlds.Listener.*;
import me.umbreon.xcraftworlds.Generator.Generator;
import me.umbreon.xcraftworlds.Manager.InventoryManager;
import me.umbreon.xcraftworlds.Manager.PluginManager;
import me.umbreon.xcraftworlds.utils.DataWorld;
import me.umbreon.xcraftworlds.utils.SetWorld;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

public class XcraftWorlds extends JavaPlugin {

    private final ListenerServer pluginListener = new ListenerServer(this);
    private final ListenerPlayer playerListener = new ListenerPlayer(this);
    private final ListenerCreature creatureListener = new ListenerCreature(this);
    private final ListenerEntity entityListener = new ListenerEntity(this);
    private final ListenerWeather weatherListener = new ListenerWeather(this);
    private final ListenerWorld worldListener = new ListenerWorld(this);
    private final InventoryManager inventoryManager = new InventoryManager(this);

    private PluginManager pm = null;

    private final SetWorld worlds = new SetWorld(this);

    public Map<String, Location> justTeleported = new HashMap<>();
    public Map<String, Location> justTeleportedFrom = new HashMap<>();

    public YamlConfiguration config = null;

    public final Properties serverconfig = new Properties();

    private String aboutPluginString = null;

    private void taskCreatureLimit() {
        for (DataWorld thisWorld : worlds) {
            thisWorld.checkCreatureLimit();
        }
    }

    private void taskTimeFrozen() {
        for (DataWorld thisWorld : worlds) {
            if (thisWorld.isTimeFrozen()) {
                thisWorld.resetFrozenTime();
            }
        }
    }

    private void taskCheckWorldInactive() {
        for (World thisWorld : getServer().getWorlds()) {
            if (worlds.get(thisWorld).checkInactive() && !thisWorld.getName().equalsIgnoreCase(serverconfig.getProperty("level-name"))) {
                getLogger().log(Level.INFO, "World \"{0}\" inactive. Unloading.", thisWorld.getName());

                worlds.get(thisWorld).unload();
            }
        }
    }

    private void taskLoadAllWorlds() {
        for (World thisWorld : getServer().getWorlds()) {
            worlds.onWorldLoad(thisWorld);
        }

        for (DataWorld thisWorld : worlds) {
            if (!thisWorld.isLoaded() && (!config.getBoolean("dynworld.enabled", false) || thisWorld.isSticky())) {
                thisWorld.load();
            }
        }
    }

    private void saveAll() {
        playerListener.savePlayers();
        inventoryManager.save();
        worlds.save();
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
        saveAll();
    }

    @Override
    public void onEnable() {
        playerListener.loadPlayers();
        inventoryManager.load();

        pm = new PluginManager(this);

        pm.registerEvents(creatureListener);
        pm.registerEvents(entityListener);
        pm.registerEvents(playerListener);
        pm.registerEvents(pluginListener);
        pm.registerEvents(weatherListener);
        pm.registerEvents(worldListener);

        File serverconfigFile = new File("server.properties");
        if (!serverconfigFile.exists()) {
            getLogger().log(Level.SEVERE, "Missing server.properties");
        } else {
            try {
                serverconfig.load(new FileInputStream(serverconfigFile));
            } catch (IllegalArgumentException | IOException ex) {
                getLogger().log(Level.SEVERE, "Error loading server config file: {0}", serverconfigFile);
            }
        }

        config = getConfig(getConfigFile("config.yml"));
        try {
            setConfigDefaults();
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Saving config file failed", e);
        }
        worlds.load();

        getServer().getScheduler().scheduleSyncRepeatingTask(this, this::taskCreatureLimit, 600, 600);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, this::taskTimeFrozen, 200, 200);
        getServer().getScheduler().runTaskTimerAsynchronously(this, this::saveAll, 12000, 12000);

        if (config.getBoolean("dynworld.enabled", false)) {
            getServer().getScheduler().scheduleSyncRepeatingTask(this, this::taskCheckWorldInactive, config.getInt("dynworld.checkInterval", 60) * 20, config.getInt("dynworld.checkInterval", 60) * 20);
        }

        getServer().getScheduler().scheduleSyncDelayedTask(this, this::taskLoadAllWorlds);
        getServer().getScheduler().scheduleSyncDelayedTask(this, pm::checkPluginVault);

        getCommand("gworld").setExecutor(new CommandHandlerWorld(this));
    }

    @Override
    public YamlConfiguration getConfig() {
        return config;
    }

    public YamlConfiguration getConfig(String fileName) {
        return getConfig(getConfigFile(fileName));
    }

    public YamlConfiguration getConfig(File file) {
        YamlConfiguration ret = new YamlConfiguration();
        try {
            ret.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            getLogger().log(Level.SEVERE, "Loading config file failed", e);
        }

        return ret;
    }

    public File getConfigFile(String fileName) {
        File configFile = new File(getDataFolder(), fileName);

        if (!configFile.exists()) {
            try {
                getDataFolder().mkdir();
                getDataFolder().setWritable(true);

                configFile.createNewFile();
            } catch (IOException ex) {
                getLogger().log(Level.SEVERE, "Creating config file failed", ex);
            }
        }

        return configFile;
    }

    private void setConfigDefaults() throws IOException {
        config.addDefault("dynworld.enabled", true);
        config.addDefault("dynworld.checkInterval", 60);
        config.addDefault("dynworld.maxInactiveTime", 300);

        config.addDefault("invsep.enabled", true);
        config.addDefault("invsep.exp", true);
        config.addDefault("invsep.health", true);
        config.addDefault("invsep.food", true);

        config.addDefault("fixes.chunkRefreshOnTeleport", false);

        config.addDefault("biomes.desert.chanceCactus", 1);
        config.addDefault("biomes.desert.chanceDeadShrub", 2);
        config.addDefault("biomes.forest.chanceLakeWater", 1);
        config.addDefault("biomes.forest.chanceTreeNormal", 32);
        config.addDefault("biomes.forest.chanceTreeBig", 2);
        config.addDefault("biomes.forest.chanceTreeBirch", 32);
        config.addDefault("biomes.forest.chanceTreeRedwood", 16);
        config.addDefault("biomes.forest.chanceTreeTallRedwood", 2);
        config.addDefault("biomes.forest.chanceFlowerYellow", 4);
        config.addDefault("biomes.forest.chanceFlowerRedRose", 4);
        config.addDefault("biomes.forest.chanceGrassTall", 50);
        config.addDefault("biomes.plains.chanceTreeNormal", 1);
        config.addDefault("biomes.plains.chanceFlowerYellow", 10);
        config.addDefault("biomes.plains.chanceFlowerRedRose", 10);
        config.addDefault("biomes.plains.chanceGrassTall", 150);
        config.addDefault("biomes.rainforest.chanceLakeWater", 3);
        config.addDefault("biomes.rainforest.chanceTreeNormal", 28);
        config.addDefault("biomes.rainforest.chanceTreeBig", 2);
        config.addDefault("biomes.rainforest.chanceTreeBirch", 28);
        config.addDefault("biomes.rainforest.chanceTreeRedwood", 32);
        config.addDefault("biomes.rainforest.chanceTreeTallRedwood", 2);
        config.addDefault("biomes.rainforest.chanceFlowerYellow", 5);
        config.addDefault("biomes.rainforest.chanceFlowerRedRose", 5);
        config.addDefault("biomes.rainforest.chanceGrassFern", 30);
        config.addDefault("biomes.rainforest.chanceGrassTall", 70);
        config.addDefault("biomes.savanna.chanceTreeNormal", 1);
        config.addDefault("biomes.seasonalforest.chanceLakeWater", 2);
        config.addDefault("biomes.seasonalforest.chanceTreeNormal", 32);
        config.addDefault("biomes.seasonalforest.chanceTreeBig", 2);
        config.addDefault("biomes.seasonalforest.chanceTreeBirch", 32);
        config.addDefault("biomes.seasonalforest.chanceTreeRedwood", 28);
        config.addDefault("biomes.seasonalforest.chanceTreeTallRedwood", 2);
        config.addDefault("biomes.seasonalforest.chanceFlowerYellow", 4);
        config.addDefault("biomes.seasonalforest.chanceFlowerRedRose", 4);
        config.addDefault("biomes.seasonalforest.chanceGrassTall", 70);
        config.addDefault("biomes.shrubland.chanceLakeLava", 1);
        config.addDefault("biomes.shrubland.chanceTreeNormal", 3);
        config.addDefault("biomes.shrubland.chanceGrassShrub", 5);
        config.addDefault("biomes.swampland.chanceSugarCane", 75);
        config.addDefault("biomes.swampland.chanceLakeWater", 10);
        config.addDefault("biomes.taiga.chanceTreeRedwood", 4);
        config.addDefault("biomes.taiga.chanceGrassTall", 2);
        config.addDefault("biomes.tundra.chanceLakeWater", 1);

        getLogger().info("Saving default config.");
        config.options().copyDefaults();
        config.save(getConfigFile("config.yml"));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("gworld")) {
            getCommand("gworld").setExecutor(new CommandHandlerWorld(this));
            getCommand("gworld").execute(sender, commandLabel, args);
            return true;
        } else {
            return false;
        }
    }

    public String getPluginAbout() {
        if (aboutPluginString == null) {
            aboutPluginString = String.valueOf(ChatColor.LIGHT_PURPLE) +
                    '[' +
                    getDescription().getFullName() +
                    ']' +
                    " by " +
                    String.join(" & ", getDescription().getAuthors());
        }
        return aboutPluginString;
    }

    public SetWorld getWorlds() {
        return worlds;
    }

    public PluginManager getPluginManager() {
        return pm;
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        for (Generator thisGen : Generator.values()) {
            if (thisGen.toString().equalsIgnoreCase(id)) {
                return thisGen.getChunkGenerator(this);
            }
        }

        return null;
    }
}
