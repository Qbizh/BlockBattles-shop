package dev.cregg.blockbattles.bbapi.functions;

import org.bukkit.Bukkit;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ThreeArgFunction;

public class In extends ThreeArgFunction {
    @Override
    public LuaValue call(LuaValue time, LuaValue callback, LuaValue passargs) {
        System.out.println(Bukkit.getPluginManager().getPlugin("Blockbattles") != null);

        Bukkit.getScheduler().runTaskLater(Bukkit.getPluginManager().getPlugin("Blockbattles"), () -> {

            ((LuaFunction)callback).call(passargs);
        }, time.tolong() * 20);
        return LuaValue.NIL;
    }
}

