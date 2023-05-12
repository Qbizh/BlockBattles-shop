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
    public LuaValue call(LuaValue worldName,LuaValue xyz) {
        World world = Bukkit.getWorld(worldName.toString());
    //This next line might be wonky with the indices

        return LuaValue.valueOf(world.getBlockAt(Math.round(xyz.get(1).tofloat()), Math.round(xyz.get(2).tofloat()), Math.round(xyz.get(3).tofloat())).getBlockData().getMaterial().toString());
    }
}

