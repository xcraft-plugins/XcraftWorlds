package me.umbreon.xcraftworlds.Commands;

import me.umbreon.xcraftworlds.Helper.CommandHelperWorld;
import me.umbreon.xcraftworlds.XcraftWorlds;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.stream.Collectors;

public class CommandWorldSetLoginMessage extends CommandHelperWorld {

    public CommandWorldSetLoginMessage(XcraftWorlds plugin) {
        super(plugin);
    }

    @Override
    public void replyUsage(CommandSender sender) {
        reply(sender, "Usage: /gworld setloginmessage <worldname> [message]");
    }

    @Override
    public void execute(CommandSender sender, String worldName, List<String> args) {
        if (worldName == null) {
            error(sender, "No world given.");
            replyUsage(sender);
        } else if (!hasWorld(worldName)) {
            reply(sender, "World not found: " + worldName);
        } else if (args == null) {
            getWorld(worldName).setLoginMessage("none");
            reply(sender, "Reset login message on world " + worldName + ".");
        } else {
            String newMessage = null;
            newMessage = args.stream().collect(Collectors.joining()); //use multiple arguments, hope it works

            getWorld(worldName).setLoginMessage(newMessage);
            reply(sender, "Login message on world " + worldName + " set to " + newMessage);
        }
    }

}
