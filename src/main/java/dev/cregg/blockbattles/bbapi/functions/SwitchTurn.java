package dev.cregg.blockbattles.bbapi.functions;



import dev.cregg.blockbattles.DuelCommand;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ThreeArgFunction;


public class SwitchTurn extends OneArgFunction {




	@Override
	public LuaValue call(LuaValue game) {
		DuelCommand.switchTurn(DuelCommand.gameIds.get(game.toString()));
		return LuaValue.NIL;
	}




}

