package dev.cregg.blockbattles.bbapi.functions;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ThreeArgFunction;

import java.util.UUID;

public class Kill extends OneArgFunction {
	@Override
	public LuaValue call(LuaValue player) {


		Player playerInstance = Bukkit.getPlayer(UUID.fromString(player.get("uuid").toString()));
		playerInstance.damage(1000);
		Bukkit.getPluginManager().callEvent(new EntityDamageEvent(playerInstance, EntityDamageEvent.DamageCause.MAGIC, 1000));
		return LuaValue.NIL;
	}
}

