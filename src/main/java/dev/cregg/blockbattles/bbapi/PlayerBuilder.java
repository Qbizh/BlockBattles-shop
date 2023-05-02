package dev.cregg.blockbattles.bbapi;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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


    public PlayerBuilder(Player player) {
        this.name = player.getName();
        this.health = player.getHealth();
        this.uuid = player.getUniqueId().toString();

    }

    public LuaTable build() {
        LuaTable table = new LuaTable();

        table.set("health", this.health);
        table.set("name", name);
        table.set("uuid", uuid);
        return table;
    }
}
