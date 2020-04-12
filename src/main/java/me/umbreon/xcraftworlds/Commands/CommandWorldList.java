package me.umbreon.xcraftworlds.Commands;

import me.umbreon.xcraftworlds.Helper.CommandHelperWorld;
import me.umbreon.xcraftworlds.utils.DataWorld;
import me.umbreon.xcraftworlds.XcraftWorlds;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class CommandWorldList extends CommandHelperWorld {

    public CommandWorldList(XcraftWorlds plugin) {
        super(plugin);
    }

    @Override
    public void replyUsage(CommandSender sender) {
    }

    @Override
    public void execute(CommandSender sender, String worldName, List<String> args) {
        String worlds = "";
        for (DataWorld thisWorld : plugin.getWorlds()) {
            worlds += ", " + thisWorld.getName();
            if (thisWorld.isLoaded()) {
                worlds += "(*)";
            }
        }
        reply(sender, "Worlds: " + ChatColor.WHITE + worlds.substring(2));
        reply(sender, "World marked with (*) are currently active on the server.");
    }

}
