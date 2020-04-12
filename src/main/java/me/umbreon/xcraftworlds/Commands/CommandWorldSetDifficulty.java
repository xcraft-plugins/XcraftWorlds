package me.umbreon.xcraftworlds.Commands;

import me.umbreon.xcraftworlds.Helper.CommandHelperWorld;
import me.umbreon.xcraftworlds.utils.Util;
import me.umbreon.xcraftworlds.XcraftWorlds;
import org.bukkit.Difficulty;
import org.bukkit.command.CommandSender;

import java.util.List;

public class CommandWorldSetDifficulty extends CommandHelperWorld {

    public CommandWorldSetDifficulty(XcraftWorlds plugin) {
        super(plugin);
    }

    @Override
    public void replyUsage(CommandSender sender) {
        reply(sender, "Usage: /gworld setdifficulty <worldname> <peaceful|easy|normal|hard>");
    }

    @Override
    public void execute(CommandSender sender, String worldName, List<String> args) {
        if (worldName == null) {
            error(sender, "No world given.");
            replyUsage(sender);
        } else if (!hasWorld(worldName)) {
            reply(sender, "World not found: " + worldName);
        } else {
            Difficulty newDif = Util.castDifficulty(args, null);

            if (newDif == null) {
                error(sender, "Unknown difficulty.");
                return;
            }

            getWorld(worldName).setDifficulty(newDif);
            reply(sender, "Difficulty on world " + worldName + " set to " + newDif.toString());
        }
    }

}
