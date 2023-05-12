package dev.cregg.blockbattles.bbapi.functions;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;


public class CheckArea extends ThreeArgFunction {
    @Override
    public LuaValue call(LuaValue worldName, LuaValue material,LuaValue positions) {
        World world = Bukkit.getWorld(worldName.toString());
    //This next line might be wonky with the indices
        boolean isfull = true;

        LuaTable startposition = (LuaTable)positions.get(1);
        LuaTable endposition = (LuaTable)positions.get(2);

        for (int x = startposition.get(1).toint(); x <= endposition.get(1).toint(); x++) {
            for (int y = startposition.get(2).toint(); y <= endposition.get(2).toint(); y++) {
                for (int z = startposition.get(3).toint(); z <= endposition.get(3).toint(); z++) {
                    if(!world.getBlockAt(x, y, z).getBlockData().getMaterial().toString().equals(material.toString())) {

                        isfull = false;
                    }
                }
            }
        }

        return LuaValue.valueOf(isfull);
    }
}

