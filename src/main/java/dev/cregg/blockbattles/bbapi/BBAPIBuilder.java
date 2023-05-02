package dev.cregg.blockbattles.bbapi;

import dev.cregg.blockbattles.bbapi.functions.*;
import org.luaj.vm2.LuaTable;

public class BBAPIBuilder {
    public BBAPIBuilder() {

    }

    public LuaTable build() {
        LuaTable table = new LuaTable();
        table.set("log", new Log());
        table.set("execute", new Execute());
        table.set("intime", new In());
        table.set("random", new RandomGen());
        table.set("getblock", new GetBlock());
        table.set("getposition", new GetPosition());
        return table;
    }
}
