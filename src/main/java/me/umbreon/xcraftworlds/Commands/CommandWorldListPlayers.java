package me.umbreon.xcraftworlds.Commands;

import me.umbreon.xcraftworlds.Helper.CommandHelperWorld;
import me.umbreon.xcraftworlds.XcraftWorlds;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandWorldListPlayers extends CommandHelperWorld {

    public CommandWorldListPlayers(XcraftWorlds plugin) {
        super(plugin);
    }

    @Override
    public void replyUsage(CommandSender sender) {
        reply(sender, "Usage: /gworld listplayers <worldname>");
    }

    @Override
    public void execute(CommandSender sender, String worldName, List<String> args) {
        if (worldName == null) {
            error(sender, "No world given.");
            replyUsage(sender);
        } else if (!hasWorld(worldName)) {
            reply(sender, "World not found: " + worldName);
        } else {
            String players = "";
            for (Player player : plugin.getServer().getWorld(worldName).getPlayers()) {
                players += ", " + player.getName();
            }

            if (players.length() > 0) {
                reply(sender, "Players in world " + worldName + ": " + players.substring(2));
            } else {
                reply(sender, "No players in world " + worldName + ".");
            }
        }
    }

}
