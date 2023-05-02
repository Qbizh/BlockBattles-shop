package dev.cregg.blockbattles.bbapi.functions;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;


import static dev.cregg.blockbattles.BlockListener.globals;

public class Log extends OneArgFunction {
    @Override
    public LuaValue call(LuaValue text) {
        System.out.println("[Block Battles Script] " + text.toString());


        return LuaValue.NIL;
    }
}

