package dev.cregg.blockbattles.bbapi;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;
import org.luaj.vm2.LuaTable;

public class EntityBuilder {



	private String type;
	private String world;
	private String paintingType = null;


	public EntityBuilder(Entity entity) {
		this.type = entity.getType().name().toString();
		this.world = entity.getWorld().getName();
		if(entity instanceof Painting) {
			Painting painting = (Painting) entity;
			paintingType = painting.getArt().toString();
		}
	}

	public LuaTable build() {
		LuaTable table = new LuaTable();

		table.set("type", this.type);
		table.set("world", this.world);
		table.set("painting", paintingType);
		return table;
	}
}
