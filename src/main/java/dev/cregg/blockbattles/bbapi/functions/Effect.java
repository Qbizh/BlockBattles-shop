package dev.cregg.blockbattles.bbapi.functions;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

import java.util.UUID;

public class Effect extends TwoArgFunction {
	@Override
	public LuaValue call(LuaValue player, LuaValue effect) {


		Player playerInstance = Bukkit.getPlayer(UUID.fromString(player.get("uuid").toString()));
		playerInstance.addPotionEffect(new PotionEffect(PotionEffectType.getByName(effect.get("type").toString()),
				effect.get("duration").toint(),
				effect.get("amplifier").toint(),
				effect.get("ambient").toboolean()));
		return LuaValue.NIL;
	}
}

