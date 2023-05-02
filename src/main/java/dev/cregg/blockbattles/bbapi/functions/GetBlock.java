package dev.cregg.blockbattles.bbapi.functions;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.luaj.vm2.Lua;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.CoerceLuaToJava;

public class GetBlock extends TwoArgFunction {
    @Override
    public LuaValue call(LuaValue player,LuaValue xyz) {
        World world = Bukkit.getPlayer(player.get("name").toString()).getWorld();

        return LuaValue.valueOf(world.getBlockAt(xyz.get(0).toint(), xyz.get(1).toint(), xyz.get(1).toint()).getBlockData().getMaterial().toString());
    }
}

