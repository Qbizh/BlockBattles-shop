package dev.cregg.blockbattles.bbapi.functions;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

public class SetBlock extends ThreeArgFunction {
	@Override
	public LuaValue call(LuaValue worldName,LuaValue xyz, LuaValue block) {
		World world = Bukkit.getWorld(worldName.toString());
		//This next line might be wonky with the indices
		world.setBlockData(new Location(world, xyz.get(1).toint(), xyz.get(2).toint(), xyz.get(3).toint()), Material.getMaterial(block.toString()).createBlockData());
		return LuaValue.NIL;
	}
}

