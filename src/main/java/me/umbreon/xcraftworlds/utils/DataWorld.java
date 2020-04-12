package me.umbreon.xcraftworlds.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import me.umbreon.xcraftworlds.Generator.Generator;
import me.umbreon.xcraftworlds.XcraftWorlds;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.generator.ChunkGenerator;

public class DataWorld {

    private XcraftWorlds plugin;
    private Server server;

    private String name;
    private String message;
    private Environment environment;
    private boolean allowAnimals = true;
    private boolean allowMonsters = true;
    private boolean allowPvP = false;
    private boolean allowWeatherChange = true;
    private int creatureLimit = 0;
    private int border = 0;
    private Weather setWeather = Weather.SUN;
    private long setTime = 100;
    private boolean timeFrozen = false;
    private boolean suppressHealthRegain = false;
    private boolean suppressHunger = false;
    private Generator generator = Generator.DEFAULT;
    private boolean sticky = false;
    private int viewDistance = 10;
    private boolean keepSpawnInMemory = false;
    private GameMode gamemode = GameMode.ADVENTURE;
    private Difficulty difficulty = Difficulty.NORMAL;
    private boolean announcePlayerDeath = true;
    private RespawnLocation respawnLocation = RespawnLocation.WORLDSPAWN;
    private String respawnWorld = null;
    private String inventoryGroup = name;
    private String loginMessage = "none";

    private long lastAction = 0;
    private World world;

    public DataWorld(XcraftWorlds instance) {
        this(instance, null, World.Environment.NORMAL, null);
    }

    public DataWorld(XcraftWorlds instance, String worldName) {
        this(instance, worldName, World.Environment.NORMAL, null);
    }

    public DataWorld(XcraftWorlds instance, String worldName, Environment env) {
        this(instance, worldName, env, null);
    }

    public DataWorld(XcraftWorlds instance, String worldName, Environment env, Generator gen) {
        plugin = instance;
        server = plugin.getServer();
        this.allowPvP = Util.castBoolean(plugin.serverconfig.getProperty("pvp", "false"));
        this.gamemode = Util.castGameMode(plugin.serverconfig.getProperty("gamemode", "ADVENTURE"));
        this.difficulty = Util.castDifficulty(plugin.serverconfig.getProperty("difficulty", "NORMAL"));
        this.allowAnimals = Util.castBoolean(plugin.serverconfig.getProperty("spawn-animals", "true"));
        this.allowMonsters = Util.castBoolean(plugin.serverconfig.getProperty("spawn-monsters", "true"));

        this.world = server.getWorld(worldName);
        this.name = worldName;
        this.inventoryGroup = worldName;
        this.environment = env;
        this.generator = (gen != null) ? gen : Generator.DEFAULT;
        this.lastAction = System.currentTimeMillis();
    }

    public enum Weather {
        SUN(0),
        STORM(1);

        private final int id;
        private static final Map<Integer, Weather> lookup = new HashMap<>();

        private Weather(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static Weather getWeather(int id) {
            return lookup.get(id);
        }

        static {
            for (Weather env : values()) {
                lookup.put(env.getId(), env);
            }
        }
    }

    public enum DayTime {
        SUNRISE(100),
        NOON(6000),
        SUNSET(12100),
        MIDNIGHT(18000);

        private final int id;
        private static final Map<Integer, DayTime> lookup = new HashMap<>();

        private DayTime(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static DayTime getDayTime(int id) {
            return lookup.get(id);
        }

        static {
            for (DayTime env : values()) {
                lookup.put(env.getId(), env);
            }
        }
    }

    public enum RespawnLocation {
        WORLDSPAWN(0),
        BEDSPAWN(1),
        WORLD(2);

        private final int id;
        private static final Map<Integer, RespawnLocation> lookup = new HashMap<>();

        private RespawnLocation(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static RespawnLocation getRespawnLocation(int id) {
            return lookup.get(id);
        }

        static {
            for (RespawnLocation env : values()) {
                lookup.put(env.getId(), env);
            }
        }
    }

    public void load() {
        load(null);
    }

    public void load(Long seed) {
        if (world != null) {
            return;
        }

        ChunkGenerator thisGen = (generator != Generator.DEFAULT) ? generator.getChunkGenerator(plugin) : null;

        WorldCreator creator = new WorldCreator(name);
        creator.environment(environment);

        if (seed != null) {
            creator.seed(seed);
        }

        if (thisGen != null) {
            creator.generator(thisGen);
        }

        this.world = creator.createWorld();

        lastAction = System.currentTimeMillis();

        plugin.getLogger().log(Level.INFO, "Loaded world {0} (Environment: {1}, Seed: {2}, Generator: {3})", new Object[]{name, environment.toString(), world.getSeed(), generator.toString()});
    }

    public void unload() {
        server.unloadWorld(world, true);
        plugin.getLogger().log(Level.INFO, "Unloaded world {0}", new Object[]{world.getName()});
        this.world = null;
    }

    public boolean isLoaded() {
        return this.world != null;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;

        if (world != null) {
            this.name = world.getName();
            this.environment = world.getEnvironment();
        }
    }

    public String getName() {
        return this.name;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", name);
        values.put("type", environment.toString());
        values.put("generator", generator.toString());
        values.put("border", border);
        values.put("creatureLimit", creatureLimit);
        values.put("allowAnimals", allowAnimals);
        values.put("allowMonsters", allowMonsters);
        values.put("allowPvP", allowPvP);
        values.put("allowWeatherChange", allowWeatherChange);
        values.put("setWeather", setWeather.toString());
        values.put("setTime", setTime);
        values.put("timeFrozen", timeFrozen);
        values.put("suppressHealthRegain", suppressHealthRegain);
        values.put("suppressHunger", suppressHunger);
        values.put("sticky", sticky);
        values.put("gamemode", gamemode);
        values.put("difficulty", difficulty);
        values.put("announcePlayerDeath", announcePlayerDeath);
        values.put("respawnLocation", respawnLocation.toString());
        values.put("respawnWorld", respawnWorld);
        values.put("inventorygroup", inventoryGroup);
        values.put("loginmessage", loginMessage);
        return values;
    }

    private void resetSpawnFlags() {
        world.setSpawnFlags(allowMonsters, allowAnimals);
    }

    public void checkCreatureLimit() {
        if (world == null) {
            return;
        }
        if (creatureLimit <= 0) {
            return;
        }

        int alive = world.getLivingEntities().size() - world.getPlayers().size();

        if (alive >= creatureLimit) {
            world.setSpawnFlags(false, false);
        } else if (alive <= creatureLimit * 0.8) {
            resetSpawnFlags();
        }
    }

    public Boolean checkInactive() {
        if (world == null || sticky) {
            return false;
        }

        if (world.getPlayers().size() > 0) {
            lastAction = System.currentTimeMillis();
            return false;
        }

        return lastAction + plugin.config.getInt("dynworld.maxInactiveTime", 300) * 1000 < System.currentTimeMillis();
    }

    public void setWorldTime(long time) {
        long actTime = world.getTime();
        actTime -= actTime % 24000;
        actTime += time + 24000;
        world.setTime(actTime);
    }

    public void resetFrozenTime() {
        if (world == null) {
            return;
        }
        if (!timeFrozen) {
            return;
        }
        setWorldTime(setTime - 100);
    }

    private void killAllMonsters() {
        if (world == null) {
            return;
        }
        for (LivingEntity entity : world.getLivingEntities()) {
            if (entity instanceof Monster);
            entity.remove();
        }
    }

    private void killAllAnimals() {
        if (world == null) {
            return;
        }
        for (LivingEntity entity : world.getLivingEntities()) {
            if (entity instanceof Animals) {
                entity.remove();
            }
        }
    }

    public void setCreatureLimit(Integer limit) {
        this.creatureLimit = (limit != null ? limit : 0);
    }

    public boolean isSticky() {
        return this.sticky;
    }

    public void setSticky(Boolean sticky) {
        this.sticky = (sticky != null ? sticky : false);
    }

    public void setAllowAnimals(Boolean allow) {
        this.allowAnimals = (allow != null ? allow : true);
        setParameters();
        if (!allow) {
            killAllAnimals();
        }
    }

    public void setAllowMonsters(Boolean allow) {
        this.allowMonsters = (allow != null ? allow : true);
        setParameters();
        if (!allow) {
            killAllMonsters();
        }
    }

    public boolean isAllowWeatherChange() {
        return this.allowWeatherChange;
    }

    public void setAllowWeatherChange(Boolean allow) {
        this.allowWeatherChange = (allow != null ? allow : true);
    }

    public int getBorder() {
        return this.border;
    }

    public void setBorder(Integer border) {
        this.border = (border != null ? border : 0);
    }

    public void setAllowPvP(Boolean allow) {
        this.allowPvP = (allow != null ? allow : false);
        setParameters();
    }

    public void setWeather(Weather weather) {
        boolean backup = this.allowWeatherChange;
        this.allowWeatherChange = true;
        this.setWeather = weather;
        setParameters();
        this.allowWeatherChange = backup;
    }

    public void setDayTime(DayTime time) {
        this.setTime = time.id;
        setParameters(true);
    }

    public void setDayTime(long time) {
        this.setTime = time;
        setParameters(true);
    }

    public boolean isTimeFrozen() {
        return this.timeFrozen;
    }

    public void setTimeFrozen(Boolean frozen) {
        this.timeFrozen = (frozen != null ? frozen : false);
        if (world != null) {
            this.setTime = world.getTime() % 24000;
        }
    }

    public boolean isSuppressHealthRegain() {
        return this.suppressHealthRegain;
    }

    public void setSuppressHealthRegain(Boolean suppressed) {
        this.suppressHealthRegain = (suppressed != null ? suppressed : true);
    }

    public boolean isSuppressHunger() {
        return this.suppressHunger;
    }

    public void setSuppressHunger(Boolean suppressed) {
        this.suppressHunger = (suppressed != null ? suppressed : true);
    }

    public void setViewDistance(int distance) {
        this.viewDistance = distance;
        setParameters();
    }

    public int getViewDistance() {
        return viewDistance;
    }

    public void setKeepSpawnInMemory(boolean keep) {
        this.keepSpawnInMemory = keep;
        setParameters();
    }

    public boolean getKeepSpawnInMemory() {
        return this.keepSpawnInMemory;
    }

    public void setGameMode(GameMode gamemode) {
        this.gamemode = gamemode;
        setParameters();
    }

    public GameMode getGameMode() {
        return this.gamemode;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
        setParameters();
    }

    public Difficulty getDifficulty() {
        return this.difficulty;
    }

    public void setAnnouncePlayerDeath(boolean announce) {
        this.announcePlayerDeath = announce;
    }

    public boolean getAnnouncePlayerDeath() {
        return this.announcePlayerDeath;
    }

    public void setInventoryGroup(String groupName) {
        this.inventoryGroup = groupName != null ? groupName : name;
    }

    public String getInventoryGroup() {
        return this.inventoryGroup;
    }

    public void setRespawnLocation(RespawnLocation loc) {
        this.respawnLocation = (loc != null ? loc : RespawnLocation.WORLDSPAWN);
    }

    public String getRespawnWorldName() {
        return this.respawnWorld;
    }

    public void setRespawnWorldName(String name) {
        this.respawnWorld = name;
    }

    public RespawnLocation getRespawnLocation() {
        return this.respawnLocation;
    }

    public boolean checkBorder(Location location) {
        return (border > 0 && Math.abs(location.getX()) <= border && Math.abs(location.getZ()) <= border) || border == 0;
    }

    public String timeToString(long time) {
        if (time <= 3000) {
            return "SUNRISE";
        } else if (time <= 9000) {
            return "NOON";
        } else if (time <= 15000) {
            return "SUNSET";
        } else {
            return "MIDNIGHT";
        }
    }

    public String getLoginMessage() {
        return this.loginMessage;
    }

    public void setLoginMessage(String message) {
        this.loginMessage = message;
    }

    public void setParameters() {
        setParameters(false);
    }

    public void setParameters(Boolean changeTime) {
        if (world == null) {
            return;
        }

        world.setPVP(allowPvP);
        world.setSpawnFlags(allowMonsters, allowAnimals);
        world.setStorm(setWeather.getId() == Weather.STORM.getId());
        //world.setKeepSpawnInMemory(keepSpawnInMemory); //FIXME: This is causing the bad behaviour (lag, falling through the world, etc ...)
        world.setDifficulty(difficulty);
        if (changeTime) {
            setWorldTime(setTime);
        }
        setCreatureLimit(creatureLimit);
    }

    public void sendInfo(CommandSender sender) {
        sender.sendMessage("World: " + name + " (" + (generator == Generator.DEFAULT ? environment.toString() : generator.toString()) + ")" + (sticky ? " Sticky!" : ""));
        sender.sendMessage("Spawnlocation: " + (world == null ? "world not loaded!" : Util.getLocationString(Util.getSaneLocation(world.getSpawnLocation()))) + (keepSpawnInMemory ? " (Stays in memory!)" : "") + " (" + this.getRespawnLocation().toString() + (this.getRespawnLocation() == RespawnLocation.WORLD ? ": " + this.getRespawnWorldName() : "") + ")");
        sender.sendMessage("Seed: " + (world != null ? world.getSeed() : "world not loaded!"));
        sender.sendMessage("Player count: " + (world != null ? world.getPlayers().size() : "world not loaded!"));
        sender.sendMessage("Border: " + (border > 0 ? border : "none"));
        sender.sendMessage("PvP allowed: " + (allowPvP ? "yes" : "no"));
        sender.sendMessage("Animals/Monsters allowed: " + (allowAnimals ? "yes" : "no") + " / " + (allowMonsters ? "yes" : "no"));
        sender.sendMessage("Creature count/limit: " + (world != null
                ? (world.getLivingEntities().size() - world.getPlayers().size()) + "/"
                + (creatureLimit > 0 ? creatureLimit : "unlimited") : "world not loaded!"));
        sender.sendMessage("Health regaining suppressed: " + (suppressHealthRegain ? "yes" : "no"));
        sender.sendMessage("Food bar depletion suppressed: " + (suppressHunger ? "yes" : "no"));
        sender.sendMessage("Weather / changes allowed: " + setWeather.toString() + " / " + (allowWeatherChange ? "yes" : "no"));
        sender.sendMessage("Current Time / frozen: " + (world != null ? timeToString(world.getTime()) : "world not loaded!") + " / " + (timeFrozen ? "yes" : "no"));
        sender.sendMessage("Inventory Group: " + inventoryGroup);
        sender.sendMessage("GameMode / Difficulty: " + gamemode.toString() + " / " + difficulty.toString());
        sender.sendMessage("Announce player deaths: " + (announcePlayerDeath ? "Yes" : "No"));
        sender.sendMessage("Login Message: " + loginMessage);
        if (world != null) {
            sender.sendMessage("Gamerules:");
            for (String thisRule : world.getGameRules()) {
                sender.sendMessage("  " + thisRule + ": " + world.getGameRuleValue(thisRule));
            }
        }
    }
}
