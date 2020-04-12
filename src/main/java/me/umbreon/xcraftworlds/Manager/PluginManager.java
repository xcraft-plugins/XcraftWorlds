package me.umbreon.xcraftworlds.Manager;

import java.util.logging.Level;

import me.umbreon.xcraftworlds.XcraftWorlds;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

public class PluginManager {

    private final XcraftWorlds core;
    private final org.bukkit.plugin.PluginManager pm;

    private Plugin vault = null;

    private Permission permission = null;
    private Economy economy = null;

    public PluginManager(XcraftWorlds core) {
        this.core = core;
        this.pm = core.getServer().getPluginManager();
    }

    public void registerEvents(Listener listener) {
        pm.registerEvents(listener, core);
    }

    public Permission getPermissions() {
        return permission;
    }

    public Economy getEconomy() {
        return economy;
    }

    public void checkPluginVault() {
        if (vault != null) {
            return;
        }

        Plugin vaultCheck = pm.getPlugin("Vault");
        if (vaultCheck != null && vaultCheck.isEnabled()) {
            vault = vaultCheck;
            core.getLogger().info("Found Vault plugin");

            RegisteredServiceProvider<Permission> permissionProvider = core.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
            if (permissionProvider != null) {
                permission = permissionProvider.getProvider();
                core.getLogger().log(Level.INFO, "Reported permission provider: {0}", new Object[]{permission.getName()});
            }

            RegisteredServiceProvider<Economy> economyProvider = core.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
            if (economyProvider != null) {
                economy = economyProvider.getProvider();
                core.getLogger().log(Level.INFO, "Reported economy provider: {0}", new Object[]{economy.getName()});
            }

        }
    }

    public void checkDisabledPlugin(Plugin plugin) {
        if (plugin.getDescription().getName().equals("Vault")) {
            permission = null;
            economy = null;
            vault = null;
            core.getLogger().info("Lost Vault plugin");
        }

    }
}
