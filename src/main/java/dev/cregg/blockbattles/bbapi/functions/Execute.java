package dev.cregg.blockbattles.bbapi.functions;

import org.bukkit.Bukkit;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

public class Execute extends OneArgFunction {
	@Override
	public LuaValue call(LuaValue command) {
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.toString());
		return LuaValue.NIL;
	}
}

