package dev.cregg.blockbattles.bbapi;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import java.util.List;
import java.util.ArrayList;

public class PlayerBuilder {

    private final double health;
    private String name;
    private String uuid;
    private int luck;
    private String world;
    private LuaTable[] inventory;
    private LuaTable[] echest;


    public PlayerBuilder(Player player) {
        this.name = player.getName();
        this.health = player.getHealth();
        this.uuid = player.getUniqueId().toString();
        int level = 0;
        for (PotionEffect effect : player.getActivePotionEffects()
             ) {
            if(effect.getType().equals(PotionEffectType.LUCK)) {
                level = effect.getAmplifier();
            }
        }
        this.world = player.getWorld().getName();

        this.luck = level;

        this.inventory = serializeInventory(player.getInventory());
        this.echest = serializeInventory(player.getEnderChest());


    }

    private static LuaTable[] serializeInventory(Inventory inventory) {
        List<LuaTable> inventoryItems = new ArrayList<>();
        for (ItemStack item:inventory.getContents()
        ) {
            if(item != null) {
                inventoryItems.add((new ItemBuilder(item)).build());
            } else {
                inventoryItems.add((new ItemBuilder(new ItemStack(Material.AIR, 1))).build());
            }
        }
        LuaTable[] itemArr = new LuaTable[inventoryItems.size()];
        inventoryItems.toArray(itemArr);
        return itemArr;
    }

    public LuaTable build() {
        LuaTable table = new LuaTable();

        table.set("health", this.health);
        table.set("name", name);
        table.set("uuid", uuid);
        table.set("luck", this.luck);
        table.set("world", this.world);
        table.set("inventory", LuaValue.listOf(inventory));
        table.set("echest", LuaValue.listOf(echest));

        return table;
    }
}
