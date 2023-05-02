package dev.cregg.blockbattles.bbapi;

import org.bukkit.event.block.BlockPlaceEvent;
import org.luaj.vm2.LuaTable;

public class PlaceEventBuilder {

    public String material;

    public LuaTable placer;

    public int x;
    public int y;
    public int z;

    public PlaceEventBuilder(BlockPlaceEvent event) {
        this.material = event.getBlock().getBlockData().getMaterial().toString();

        x = event.getBlock().getState().getX();
        y = event.getBlock().getState().getY();
        z = event.getBlock().getState().getZ();

        this.placer = new PlayerBuilder(event.getPlayer()).build();
    }

    public LuaTable build() {
        LuaTable table = new LuaTable();
        table.set("material", this.material);
        table.set("placer", this.placer);
        table.set("x", x);
        table.set("y", y);
        table.set("z", z);
        return table;
    }
}
