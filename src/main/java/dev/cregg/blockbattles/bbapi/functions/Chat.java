package dev.cregg.blockbattles.bbapi.functions;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

import java.util.UUID;

public class Chat extends TwoArgFunction {
	@Override
	public LuaValue call(LuaValue player, LuaValue message) {


		Player playerInstance = Bukkit.getPlayer(UUID.fromString(player.get("uuid").toString()));
		playerInstance.sendMessage(message.toString());
		return LuaValue.NIL;
	}
}

