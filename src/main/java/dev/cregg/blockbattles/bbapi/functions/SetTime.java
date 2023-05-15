package dev.cregg.blockbattles.bbapi.functions;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

public class SetTime extends TwoArgFunction {
	@Override
	public LuaValue call(LuaValue worldName, LuaValue time) {
		World world = Bukkit.getWorld(worldName.toString());
		world.setTime(time.tolong());
		return LuaValue.NIL;
	}
}

