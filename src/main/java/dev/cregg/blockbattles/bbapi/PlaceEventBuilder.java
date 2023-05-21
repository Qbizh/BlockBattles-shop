package dev.cregg.blockbattles.bbapi;

import dev.cregg.blockbattles.DuelCommand;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Painting;
import org.bukkit.event.block.BlockPlaceEvent;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

public class PlaceEventBuilder {

	public String material;

	public LuaTable placer;
	public LuaTable opps;

	public int x;
	public int y;
	public int z;

	public String meta;

	public PlaceEventBuilder(BlockPlaceEvent event) {
		this.material = event.getBlock().getBlockData().getMaterial().toString();

		x = event.getBlock().getState().getX();
		y = event.getBlock().getState().getY();
		z = event.getBlock().getState().getZ();

		meta = event.getItemInHand().getItemMeta().toString();

		this.placer = new PlayerBuilder(event.getPlayer()).build();
		this.opps = new PlayerBuilder(DuelCommand.getOpps(event.getPlayer().getUniqueId().toString())).build();
	}



	public LuaTable build() {
		LuaTable table = new LuaTable();
		table.set("material", this.material);
		table.set("placer", this.placer);
		table.set("opps", this.opps);
		table.set("x", x);
		table.set("y", y);
		table.set("z", z);
		table.set("meta", LuaValue.valueOf(meta));
		return table;
	}
}
