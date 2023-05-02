package dev.cregg.blockbattles.bbapi.functions;

import org.bukkit.Bukkit;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

import java.util.Random;
import java.util.random.RandomGenerator;

public class RandomGen extends TwoArgFunction {
    @Override
    public LuaValue call(LuaValue min, LuaValue max) {
        Random rand = new Random();


        return LuaValue.valueOf(rand.nextFloat(max.tofloat()) + min.tofloat());
    }
}

