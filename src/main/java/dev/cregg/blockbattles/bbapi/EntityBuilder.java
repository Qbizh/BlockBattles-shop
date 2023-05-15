package dev.cregg.blockbattles.bbapi;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.luaj.vm2.LuaTable;

public class EntityBuilder {



	private String type;
	private String world;


	public EntityBuilder(Entity entity) {
		this.type = entity.getType().name().toString();
		this.world = entity.getWorld().getName();

	}

	public LuaTable build() {
		LuaTable table = new LuaTable();

		table.set("type", this.type);
		table.set("world", this.world);
		return table;
	}
}
