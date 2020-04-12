package me.umbreon.xcraftworlds.Commands;

import me.umbreon.xcraftworlds.Helper.CommandHelperWorld;
import me.umbreon.xcraftworlds.XcraftWorlds;
import org.bukkit.command.CommandSender;

import java.util.List;

public class CommandWorldSetGameRule extends CommandHelperWorld {
    
    public CommandWorldSetGameRule(XcraftWorlds plugin) {
        super(plugin);
    }
    
    @Override
    public void replyUsage(CommandSender sender) {
        reply(sender, "Usage: /gworld setgamerule <worldname> <rulename> <value>");
    }

    @Override
    public void execute(CommandSender sender, String worldName, List<String> args) {
        if (worldName == null) {
            error(sender, "No world given.");
            replyUsage(sender);
        } else if (!hasWorld(worldName)) {
            reply(sender, "World not found: " + worldName);
        } else if (args.size() != 2) {
            error(sender, "Wrong argument count.");
            replyUsage(sender);
        } else {
            String rule = args.get(0);
            String value = args.get(1);
            
            if (getWorld(worldName).getWorld().getGameRuleValue(rule) == null) {
                reply(sender, "Unknown gamerule '" + rule + "'");
            } else {
                getWorld(worldName).getWorld().setGameRuleValue(rule, value);
                reply(sender, "GameRule '" + rule + "' for world " + worldName + " set to " + value);
            }
        }
    }
    
}
