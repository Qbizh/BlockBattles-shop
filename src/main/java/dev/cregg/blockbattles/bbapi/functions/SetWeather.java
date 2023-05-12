package dev.cregg.blockbattles.bbapi.functions;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

public class SetWeather extends TwoArgFunction {
    @Override
    public LuaValue call(LuaValue worldName, LuaValue weather) {
        World world = Bukkit.getWorld(worldName.toString());

        switch (weather.toString()) {
            case "rain":
            case "thunder":
                world.setStorm(true);
                break;
            default:
                world.setStorm(false);
                break;
        }
        return LuaValue.NIL;
    }
}

