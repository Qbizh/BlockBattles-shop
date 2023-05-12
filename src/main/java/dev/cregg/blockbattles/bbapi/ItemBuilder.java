package dev.cregg.blockbattles.bbapi;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.luaj.vm2.LuaTable;

public class ItemBuilder {



    private String material;
    private int amount;


    public ItemBuilder(ItemStack item) {
        this.material = item.getType().toString();
        this.amount = item.getAmount();

    }

    public LuaTable build() {
        LuaTable table = new LuaTable();

        table.set("material", this.material);
        table.set("amount", this.amount);
        return table;
    }
}
