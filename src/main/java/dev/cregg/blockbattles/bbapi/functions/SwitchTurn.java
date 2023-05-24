package dev.cregg.blockbattles.bbapi.functions;



import dev.cregg.blockbattles.DuelManager;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;


public class SwitchTurn extends OneArgFunction {




	@Override
	public LuaValue call(LuaValue game) {
		DuelManager.switchTurn(DuelManager.gameIds.get(game.toString()));
		return LuaValue.NIL;
	}




}

