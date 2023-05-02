package dev.cregg.blockbattles;

import dev.cregg.blockbattles.bbapi.BBAPIBuilder;
import dev.cregg.blockbattles.bbapi.PlaceEventBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;

import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.luaj.vm2.lib.jse.JsePlatform;
import org.luaj.vm2.LuaValue;

import java.util.logging.Level;
import java.util.logging.Logger;

public class BlockListener implements Listener {
    Logger logger = Bukkit.getServer().getLogger();

    public static LuaValue globals = JsePlatform.standardGlobals();

    public static void reloadLua() {
        globals = JsePlatform.standardGlobals();
        globals.get("dofile").call( LuaValue.valueOf( datapath));
        globals.set("BBAPI", new BBAPIBuilder().build());
    }

    public static String datapath = "";

    public BlockListener(String path) {
        super();
        datapath = path + "\\script.lua";
        globals.get("dofile").call( LuaValue.valueOf( path + "\\script.lua"));
        globals.set("BBAPI", new BBAPIBuilder().build());

    }



    @EventHandler
    public void onBlockPlace(BlockPlaceEvent blockPlaceEvent) {

        //call the function MyAdd with two parameters 5, and 5

        if(DuelCommand.isInGame(blockPlaceEvent.getPlayer().getUniqueId().toString())) {
            LuaValue on_place = globals.get("on_place");
            if(on_place != LuaValue.NIL) {
                on_place.call(new PlaceEventBuilder(blockPlaceEvent).build());
            }
        }

    }


}
