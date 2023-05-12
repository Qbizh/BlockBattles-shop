package dev.cregg.blockbattles.bbapi;

import org.bukkit.Location;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.luaj.vm2.LuaTable;

public class StructureGrowEventBuilder {

    private String world;
    private String species;

    public int x;
    public int y;
    public int z;

    public StructureGrowEventBuilder(StructureGrowEvent event) {
        this.world = event.getWorld().getName();
        species = event.getSpecies().toString();
        Location location = event.getLocation();
        this.x = location.getBlockX();
        this.y = location.getBlockX();
        this.z = location.getBlockZ();
    }

    public LuaTable build() {
        LuaTable table = new LuaTable();
        table.set("world", world);
        table.set("species", species);
        table.set("x", x);
        table.set("y", y);
        table.set("z", z);
        return table;
    }
}
