package dev.cregg.blockbattles.bbapi.functions;



import dev.cregg.blockbattles.BlockListener;
import dev.cregg.blockbattles.Blockbattles;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.structure.Structure;
import org.bukkit.structure.StructureManager;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.logging.Level;



public class SetScene extends TwoArgFunction {




    @Override
    public LuaValue call(LuaValue worldName, LuaValue scenename) {
        World world = Bukkit.getWorld(worldName.toString());


        placeStructure(world, scenename.toString(), 0, 0, 0);
        return LuaValue.NIL;
    }

    public static void placeStructure(World world, String structurename, int x, int y, int z) {

        StructureManager manager = Bukkit.getStructureManager();
        File file = Paths.get(Blockbattles.datapath, "scenes",structurename + ".nbt").toFile();
        Structure structure;
        try {
            structure = manager.loadStructure(file);


        } catch (IOException e) {
            e.printStackTrace();
            return;
        }


        structure.place(new Location(world, x, y, z), false, StructureRotation.NONE, Mirror.NONE, 0, 1, new Random());
        System.out.println("got here");
    }
}

