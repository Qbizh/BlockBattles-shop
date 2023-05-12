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
        table.set("setblock", new SetBlock());
        table.set("getposition", new GetPosition());
        table.set("checkarea", new CheckArea());
        table.set("setscene", new SetScene());
        table.set("settime", new SetTime());
        table.set("setweather", new SetWeather());
        table.set("setslot", new SetSlot());
        return table;
    }
}
