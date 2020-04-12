package me.umbreon.xcraftworlds.Manager;

import java.io.IOException;
import java.util.logging.Level;

import me.umbreon.xcraftworlds.utils.DataWorld;
import me.umbreon.xcraftworlds.XcraftWorlds;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryManager {

    private static XcraftWorlds plugin;
    private static YamlConfiguration playerInventorys;

    public InventoryManager(XcraftWorlds plugin) {
        InventoryManager.plugin = plugin;
    }

    public void save() {
        try {
            playerInventorys.save(plugin.getConfigFile("playerInventorys.yml"));
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "Saving inventories data failed", ex);
        }
    }

    public void load() {
        playerInventorys = plugin.getConfig(plugin.getConfigFile("playerInventorys.yml"));
    }

    public static void changeInventory(Player player, DataWorld from, DataWorld to) {
        saveInventory(player, from, player.getGameMode());
        clearInventory(player);
        loadInventory(player, to, player.getGameMode());
    }

    public static void changeInventroy(Player player, GameMode from, GameMode to, DataWorld world) {
        saveInventory(player, world, from);
        clearInventory(player);
        loadInventory(player, world, to);
    }

    private static void saveInventory(Player player, DataWorld world, GameMode mode) {
        ConfigurationSection playerInv = playerInventorys.getConfigurationSection(player.getUniqueId() + "." + world.getInventoryGroup() + "." + mode.toString());
        if (playerInv == null) {
            playerInv = playerInventorys.createSection(player.getUniqueId() + "." + world.getInventoryGroup() + "." + mode.toString());
        }

        ConfigurationSection armor = playerInv.getConfigurationSection("armor");
        if (armor == null) {
            armor = playerInv.createSection("armor");
        }

        ConfigurationSection inv = playerInv.getConfigurationSection("inventory");
        if (inv == null) {
            inv = playerInv.createSection("inventory");
        }

        playerInv.set("ign", player.getName());
        playerInv.set("health", player.getHealth());
        playerInv.set("food", player.getFoodLevel());
        playerInv.set("exp_total", player.getTotalExperience());
        playerInv.set("exp_level", player.getLevel());
        playerInv.set("exp_tolvl", player.getExp());

        ItemStack[] thisArmor = player.getInventory().getArmorContents();
        ItemStack[] thisInv = player.getInventory().getContents();

        for (int j = 0; j < 4; j++) {
            armor.set(Integer.toString(j), thisArmor[j]);
        }

        for (int i = 0; i < 36; i++) {
            inv.set(Integer.toString(i), thisInv[i]);
        }
    }

    private static void loadInventory(Player player, DataWorld world, GameMode mode) {
        plugin.getLogger().log(Level.INFO, "Loading Inventory {0} in world {1} (group {2}) for {3}", new Object[]{mode.toString(), world.getName(), world.getInventoryGroup(), player.getUniqueId()});

        boolean convertInv = false;

        ConfigurationSection playerInv = playerInventorys.getConfigurationSection(player.getUniqueId() + "." + world.getInventoryGroup() + "." + mode.toString());
        if (playerInv == null) {
            playerInv = playerInventorys.getConfigurationSection(player.getUniqueId() + "." + world.getName() + "." + mode.toString());
            convertInv = true;

            if (playerInv == null) {
                playerInv = playerInventorys.createSection(player.getUniqueId() + "." + world.getInventoryGroup() + "." + mode.toString());
                convertInv = false;
            }
        }

        ConfigurationSection armor = playerInv.getConfigurationSection("armor");
        if (armor == null) {
            armor = playerInv.createSection("armor");
        }

        ConfigurationSection inv = playerInv.getConfigurationSection("inventory");
        if (inv == null) {
            inv = playerInv.createSection("inventory");
        }

        ItemStack[] thisArmor = new ItemStack[4];
        ItemStack[] thisInv = new ItemStack[36];

        for (int j = 0; j < 4; j++) {
            thisArmor[j] = armor.getItemStack(Integer.toString(j), new ItemStack(Material.AIR));
        }
        player.getInventory().setArmorContents(thisArmor);

        for (int i = 0; i < 36; i++) {
            thisInv[i] = inv.getItemStack(Integer.toString(i), new ItemStack(Material.AIR));
        }
        player.getInventory().setContents(thisInv);

        if (plugin.getConfig().getBoolean("invsep.health")) {
            int health = playerInv.getInt("health", 20);
            if (health > 20 | health == 0) {
                health = 20;
            }
            player.setHealth((double) health);
        }

        if (plugin.getConfig().getBoolean("invsep.food")) {
            player.setFoodLevel(playerInv.getInt("food", 20));
        }

        if (plugin.getConfig().getBoolean("invsep.exp")) {
            player.setTotalExperience(playerInv.getInt("exp_total", 0));
            player.setLevel(playerInv.getInt("exp_level", 0));
            player.setExp((float) playerInv.getDouble("exp_tolvl", 0.0));
        }

        if (convertInv) {
            playerInventorys.set(player.getUniqueId() + "." + world.getName() + "." + mode.toString(), null);
        }

        player.updateInventory();
    }

    private static void clearInventory(Player player) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[4]);
    }
}
