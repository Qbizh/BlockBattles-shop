package dev.cregg.blockbattles.bbapi;

import dev.cregg.blockbattles.DuelCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.projectiles.ProjectileSource;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

public class ProjectileHitEventBuilder {

	private LuaValue hitentity;
	public LuaTable launched;
	public LuaValue hitblock;
	public LuaValue launcher;
	public LuaValue opps;

	public int x;
	public int y;
	public int z;

	public ProjectileHitEventBuilder(ProjectileHitEvent event) {
		if(event.getHitBlock() != null) {
			this.hitblock = LuaValue.valueOf(event.getHitBlock().getBlockData().getMaterial().toString());
			this.hitentity = LuaValue.NIL;
		} else {
			this.hitblock = LuaValue.NIL;
			this.hitentity = new EntityBuilder(event.getHitEntity()).build();
		}
		launched = new EntityBuilder(event.getEntity()).build();
		x = event.getHitBlock().getX();
		y = event.getHitBlock().getY();
		z = event.getHitBlock().getZ();


		ProjectileSource shooter = event.getEntity().getShooter();
		if(shooter instanceof Player) {
			this.launcher = new PlayerBuilder((Player)shooter).build();
			this.opps = new PlayerBuilder(DuelCommand.getOpps(((Player)shooter).getUniqueId().toString())).build();
		} else {
			this.launcher = LuaValue.NIL;
			this.opps = LuaValue.NIL;
		}


	}

	public LuaTable build() {
		LuaTable table = new LuaTable();
		table.set("hitblock", this.hitblock);
		table.set("hitentity", this.hitentity);
		table.set("launcher", this.launcher);
		table.set("opps", this.opps);
		table.set("launched", this.launched);
		table.set("x", x);
		table.set("y", y);
		table.set("z", z);
		return table;
	}
}
