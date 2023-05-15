package dev.cregg.blockbattles.bbapi;

import dev.cregg.blockbattles.DuelCommand;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.luaj.vm2.LuaTable;

public class EntitySpawnEventBuilder {

	public LuaTable entity;

	public int x;
	public int y;
	public int z;

	public EntitySpawnEventBuilder(EntitySpawnEvent event) {
		this.entity = (new EntityBuilder(event.getEntity())).build();
		Location location = event.getLocation();
		this.x = location.getBlockX();
		this.y = location.getBlockX();
		this.z = location.getBlockZ();
	}

	public LuaTable build() {
		LuaTable table = new LuaTable();
		table.set("entity", this.entity);
		table.set("x", x);
		table.set("y", y);
		table.set("z", z);
		return table;
	}
}
