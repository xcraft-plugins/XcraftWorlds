package me.umbreon.xcraftworlds.Helper;

import me.umbreon.xcraftworlds.XcraftWorlds;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class CommandHelper {

    protected final XcraftWorlds plugin;
    private final String messagePrefix;

    public CommandHelper(XcraftWorlds instance) {
        plugin = instance;
        messagePrefix = ChatColor.LIGHT_PURPLE + "[" + plugin.getDescription().getFullName() + "] " + ChatColor.DARK_AQUA;
    }

    public void reply(CommandSender sender, String message) {
        sender.sendMessage(messagePrefix + message);
    }

    public void error(CommandSender sender, String message) {
        sender.sendMessage(ChatColor.RED + "Error: " + message);
    }
    
    public abstract void replyUsage(CommandSender sender);

    public boolean isPermitted(Player player, String command, String subcommand) {
        if (player == null) {
            return true;
        }

        if (plugin.getPluginManager().getPermissions() != null) {
            if (subcommand != null) {
                return plugin.getPluginManager().getPermissions().has(player, "XcraftGate." + command
                        + "." + subcommand);
            } else {
                return plugin.getPluginManager().getPermissions().has(player, "XcraftGate." + command);
            }
        } else if (subcommand != null) {
            return player.hasPermission("XcraftGate." + command + "." + subcommand);
        } else {
            return player.hasPermission("XcraftGate." + command);
        }
    }
}
