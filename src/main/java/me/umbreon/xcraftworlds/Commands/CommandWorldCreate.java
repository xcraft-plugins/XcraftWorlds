package me.umbreon.xcraftworlds.Commands;

import me.umbreon.xcraftworlds.Helper.CommandHelperWorld;
import me.umbreon.xcraftworlds.utils.DataWorld;
import me.umbreon.xcraftworlds.Generator.Generator;
import me.umbreon.xcraftworlds.XcraftWorlds;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.command.CommandSender;

import java.util.List;

public class CommandWorldCreate extends CommandHelperWorld {

    public CommandWorldCreate(XcraftWorlds plugin) {
        super(plugin);
    }

    @Override
    public void replyUsage(CommandSender sender) {
        reply(sender, "Usage: /gworld create <worldname> [<environment> [seed]]");
    }

    @Override
    public void execute(CommandSender sender, String worldName, List<String> args) {
        if (worldName == null) {
            error(sender, "No world given.");
            replyUsage(sender);
        } else if (hasWorld(worldName)) {
            reply(sender, "World already exists: " + worldName);
        } else {
            String env = args.size() < 1 ? "normal" : args.get(0);

            Environment worldEnv = null;
            Generator worldGen = null;

            for (Environment thisEnv : World.Environment.values()) {
                if (thisEnv.toString().equalsIgnoreCase(env)) {
                    worldEnv = thisEnv;
                }
            }

            for (Generator thisGen : Generator.values()) {
                if (thisGen.toString().equalsIgnoreCase(env)) {
                    worldGen = thisGen;
                    worldEnv = World.Environment.NORMAL;
                }
            }

            if (worldEnv == null) {
                reply(sender, "Unknown environment: " + env);
                return;
            }

            DataWorld thisWorld = new DataWorld(plugin, worldName, worldEnv, worldGen);
            plugin.getWorlds().add(thisWorld);

            if (args.size() < 2) {
                thisWorld.load();
            } else {
                Long seed = 0L;
                try {
                    seed = Long.parseLong(args.get(1));
                } catch (IndexOutOfBoundsException | NumberFormatException ex) {
                    seed = (long) args.get(1).hashCode();
                }

                thisWorld.load(seed);
            }

            reply(sender, "World " + worldName + " created with environment " + env + ".");
        }
    }
}
