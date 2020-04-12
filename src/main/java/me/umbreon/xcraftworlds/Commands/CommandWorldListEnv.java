package me.umbreon.xcraftworlds.Commands;

import me.umbreon.xcraftworlds.Helper.CommandHelperWorld;
import me.umbreon.xcraftworlds.Generator.Generator;
import me.umbreon.xcraftworlds.XcraftWorlds;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.command.CommandSender;

import java.util.List;

public class CommandWorldListEnv extends CommandHelperWorld {

    public CommandWorldListEnv(XcraftWorlds plugin) {
        super(plugin);
    }

    @Override
    public void replyUsage(CommandSender sender) {
    }

    @Override
    public void execute(CommandSender sender, String worldName, List<String> args) {
        reply(sender, "Environments provided by Bukkit:");
        for (Environment thisEnv : World.Environment.values()) {
            sender.sendMessage(thisEnv.toString());
        }

        reply(sender, "Environments provided by XcraftGate:");
        for (Generator thisEnv : Generator.values()) {
            if (thisEnv.getId() != 0) {
                sender.sendMessage(thisEnv.toString());
            }
        }
    }
}
