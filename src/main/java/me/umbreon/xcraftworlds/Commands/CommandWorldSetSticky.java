package me.umbreon.xcraftworlds.Commands;

import me.umbreon.xcraftworlds.Helper.CommandHelperWorld;
import me.umbreon.xcraftworlds.XcraftWorlds;
import org.bukkit.command.CommandSender;

import java.util.List;

public class CommandWorldSetSticky extends CommandHelperWorld {
    
    public CommandWorldSetSticky(XcraftWorlds plugin) {
        super(plugin);
    }
    
    @Override
    public void replyUsage(CommandSender sender) {
        reply(sender, "Usage: /gworld setsticky <worldname> <true|false>");
    }

    @Override
    public void execute(CommandSender sender, String worldName, List<String> args) {
        if (worldName == null) {
            error(sender, "No world given.");
            replyUsage(sender);
        } else if (!hasWorld(worldName)) {
            reply(sender, "Unknown world: " + worldName);
        } else {
            Boolean sticky;
            
            sticky = (args.isEmpty() || !args.get(0).equalsIgnoreCase("false"));
            
            getWorld(worldName).setSticky(sticky);
            reply(sender, (sticky ? "Sticked" : "Unsticked") + " world " + worldName + ".");
        }
    }
    
}
