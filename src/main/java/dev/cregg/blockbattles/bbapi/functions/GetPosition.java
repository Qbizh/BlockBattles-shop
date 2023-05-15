package dev.cregg.blockbattles.bbapi.functions;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

public class GetPosition extends OneArgFunction {
	@Override
	public LuaValue call(LuaValue playertable) {
		Player player = Bukkit.getPlayer(playertable.get("name").toString());
		double[] pos = {player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ()};
		LuaTable table = new LuaTable();
		table.set(1, LuaValue.valueOf(pos[0]));
		table.set(2, LuaValue.valueOf(pos[1]));
		table.set(3, LuaValue.valueOf(pos[2]));
		return (LuaValue) table;
	}
}

