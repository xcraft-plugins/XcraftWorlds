package me.umbreon.xcraftworlds.Handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.umbreon.xcraftworlds.Commands.*;
import me.umbreon.xcraftworlds.Helper.CommandHelper;
import me.umbreon.xcraftworlds.Helper.CommandHelperWorld;
import me.umbreon.xcraftworlds.XcraftWorlds;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandlerWorld extends CommandHelper implements CommandExecutor {

    private static final Map<String, CommandHelperWorld> subcommands = new HashMap<>();
    private static final Map<String, String> permNodes = new HashMap<>();

    public CommandHandlerWorld(XcraftWorlds instance) {
        super(instance);

        permNodes.put("create", "create");
        permNodes.put("delete", "delete");
        permNodes.put("warpto", "warp");
        permNodes.put("setborder", "setborder");
        permNodes.put("setcreaturelimit", "setcreaturelimit");
        permNodes.put("allowanimals", "setcreaturelimit");
        permNodes.put("allowmonsters", "setcreaturelimit");
        permNodes.put("suppresshealthregain", "setcreaturelimit");
        permNodes.put("suppresshunger", "setcreaturelimit");
        permNodes.put("allowpvp", "pvp");
        permNodes.put("allowweatherchange", "weather");
        permNodes.put("setweather", "weather");
        permNodes.put("timefrozen", "time");
        permNodes.put("settime", "time");
        permNodes.put("info", "info");
        permNodes.put("list", "info");
        permNodes.put("listenv", "info");
        permNodes.put("listplayers", "info");
        permNodes.put("load", "load");
        permNodes.put("unload", "load");
        permNodes.put("setsticky", "load");
        permNodes.put("keepspawninmemory", "load");
        permNodes.put("setdifficulty", "difficulty");
        permNodes.put("setannouncedeath", "difficulty");
        permNodes.put("setgamemode", "gamemode");
        permNodes.put("setgamerule", "gamemode");
        permNodes.put("setloginmessage", "gamemode");
        permNodes.put("setspawn", "spawn");
        permNodes.put("setrespawnlocation", "spawn");
        permNodes.put("setinventorygroup", "inventory");

        subcommands.put("create", new CommandWorldCreate(plugin));
        subcommands.put("info", new CommandWorldInfo(plugin));
        subcommands.put("listenv", new CommandWorldListEnv(plugin));
        subcommands.put("list", new CommandWorldList(plugin));
        subcommands.put("delete", new CommandWorldDelete(plugin));
        subcommands.put("warpto", new CommandWorldWarpto(plugin));
        subcommands.put("setborder", new CommandWorldSetBorder(plugin));
        subcommands.put("setcreaturelimit", new CommandWorldSetCreatureLimit(plugin));
        subcommands.put("allowanimals", new CommandWorldAllowAnimals(plugin));
        subcommands.put("allowmonsters", new CommandWorldAllowMonsters(plugin));
        subcommands.put("allowpvp", new CommandWorldAllowPvP(plugin));
        subcommands.put("allowweatherchange", new CommandWorldAllowWeatherchange(plugin));
        subcommands.put("setweather", new CommandWorldSetWeather(plugin));
        subcommands.put("timefrozen", new CommandWorldTimeFrozen(plugin));
        subcommands.put("settime", new CommandWorldSetTime(plugin));
        subcommands.put("suppresshealthregain", new CommandWorldSuppressHealthregain(plugin));
        subcommands.put("suppresshunger", new CommandWorldSuppressHunger(plugin));
        subcommands.put("listplayers", new CommandWorldListPlayers(plugin));
        subcommands.put("load", new CommandWorldLoad(plugin));
        subcommands.put("unload", new CommandWorldUnload(plugin));
        subcommands.put("setsticky", new CommandWorldSetSticky(plugin));
        subcommands.put("keepspawninmemory", new CommandWorldKeepSpawnInMemory(plugin));
        subcommands.put("setdifficulty", new CommandWorldSetDifficulty(plugin));
        subcommands.put("setgamemode", new CommandWorldSetGameMode(plugin));
        subcommands.put("setgamerule", new CommandWorldSetGameRule(plugin));
        subcommands.put("setloginmessage", new CommandWorldSetLoginMessage(plugin));
        subcommands.put("setannouncedeath", new CommandWorldSetAnnounceDeath(plugin));
        subcommands.put("setspawn", new CommandWorldSetSpawn(plugin));
        subcommands.put("setrespawnlocation", new CommandWorldSetRespawnLocation(plugin));
        subcommands.put("setinventorygroup", new CommandWorldSetInventoryGroup(plugin));
    }

    @Override
    public void replyUsage(CommandSender sender) {
        sender.sendMessage(plugin.getPluginAbout());
        sender.sendMessage(ChatColor.LIGHT_PURPLE + "-> " + ChatColor.GREEN + "/gworld list");
        sender.sendMessage(ChatColor.LIGHT_PURPLE + "-> " + ChatColor.GREEN + "/gworld info <world>");
        sender.sendMessage(ChatColor.LIGHT_PURPLE + "-> " + ChatColor.GREEN + "/gworld listenv");
        sender.sendMessage(ChatColor.LIGHT_PURPLE + "-> " + ChatColor.GREEN + "/gworld listplayers <world>");
        sender.sendMessage(ChatColor.LIGHT_PURPLE + "-> " + ChatColor.GREEN + "/gworld create <name> [<environment> [seed]]");
        sender.sendMessage(ChatColor.LIGHT_PURPLE + "-> " + ChatColor.GREEN + "/gworld delete <name>");
        sender.sendMessage(ChatColor.LIGHT_PURPLE + "-> " + ChatColor.GREEN + "/gworld warpto <name>");
        sender.sendMessage(ChatColor.LIGHT_PURPLE + "-> " + ChatColor.GREEN + "/gworld setborder <world> <#>");
        sender.sendMessage(ChatColor.LIGHT_PURPLE + "-> " + ChatColor.GREEN + "/gworld setcreaturelimit <world> <#>");
        sender.sendMessage(ChatColor.LIGHT_PURPLE + "-> " + ChatColor.GREEN + "/gworld allowanimals <world> <true|false>");
        sender.sendMessage(ChatColor.LIGHT_PURPLE + "-> " + ChatColor.GREEN + "/gworld allowmonsters <world> <true|false>");
        sender.sendMessage(ChatColor.LIGHT_PURPLE + "-> " + ChatColor.GREEN + "/gworld allowpvp <world> <true|false>");
        sender.sendMessage(ChatColor.LIGHT_PURPLE + "-> " + ChatColor.GREEN + "/gworld allowweatherchange <world> <true|false>");
        sender.sendMessage(ChatColor.LIGHT_PURPLE + "-> " + ChatColor.GREEN + "/gworld setweather <world> <sun|storm>");
        sender.sendMessage(ChatColor.LIGHT_PURPLE + "-> " + ChatColor.GREEN + "/gworld timefrozen <world> <true|false>");
        sender.sendMessage(ChatColor.LIGHT_PURPLE + "-> " + ChatColor.GREEN + "/gworld settime <world> <sunrise|noon|sunset|midnight>");
        sender.sendMessage(ChatColor.LIGHT_PURPLE + "-> " + ChatColor.GREEN + "/gworld setdifficulty <world> <peaceful|easy|normal|hard>");
        sender.sendMessage(ChatColor.LIGHT_PURPLE + "-> " + ChatColor.GREEN + "/gworld setannouncedeath <world> <true|false>");
        sender.sendMessage(ChatColor.LIGHT_PURPLE + "-> " + ChatColor.GREEN + "/gworld setgamemode <world> <survival|creative|adventure>");
        sender.sendMessage(ChatColor.LIGHT_PURPLE + "-> " + ChatColor.GREEN + "/gworld setgamerule <world> <rulename> <value>");
        sender.sendMessage(ChatColor.LIGHT_PURPLE + "-> " + ChatColor.GREEN + "/gworld setloginmessage <world> [message]");
        sender.sendMessage(ChatColor.LIGHT_PURPLE + "-> " + ChatColor.GREEN + "/gworld setspawn <world>");
        sender.sendMessage(ChatColor.LIGHT_PURPLE + "-> " + ChatColor.GREEN + "/gworld setrespawnlocation <world> <worldspawn|bedspawn|world <worldname>>");
        sender.sendMessage(ChatColor.LIGHT_PURPLE + "-> " + ChatColor.GREEN + "/gworld suppresshealthregain <world> <true|false>");
        sender.sendMessage(ChatColor.LIGHT_PURPLE + "-> " + ChatColor.GREEN + "/gworld suppresshunger <world> <true|false>");
        sender.sendMessage(ChatColor.LIGHT_PURPLE + "-> " + ChatColor.GREEN + "/gworld setinventorygroup <world> <groupname>");
        sender.sendMessage(ChatColor.LIGHT_PURPLE + "-> " + ChatColor.GREEN + "/gworld setsticky <world> <true|false>");
        sender.sendMessage(ChatColor.LIGHT_PURPLE + "-> " + ChatColor.GREEN + "/gworld keepspawninmemory <world> <true|false>");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        Player player = (sender instanceof Player) ? (Player) sender : null;

        if (!isPermitted(player, "world", (args.length > 0 ? permNodes.get(args[0]) : null))) {
            error(sender, "You don't have permission to use this command");
            return true;
        }

        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            replyUsage(sender);
            return true;
        }

        if (player == null && (args[0].equalsIgnoreCase("warpto") || args[0].equalsIgnoreCase("setspawn"))) {
            error(sender, "/gworld" + args[0].toLowerCase() + "cannot be used from the console");
            return true;
        }

        if (subcommands.get(args[0].toLowerCase()) == null) {
            replyUsage(sender);
            error(sender, "Unkown gworld command: " + args[0].toLowerCase());
        } else {
            List<String> largs = Arrays.asList(args);
            largs = largs.subList(1, largs.size());

            subcommands.get(args[0].toLowerCase()).execute(sender,
                    (largs.size() > 0 ? largs.get(0) : null),
                    (largs.size() > 1 ? largs.subList(1, largs.size()) : new ArrayList<>())
            );
        }

        return true;
    }
}
