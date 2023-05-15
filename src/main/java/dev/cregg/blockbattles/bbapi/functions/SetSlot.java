package dev.cregg.blockbattles.bbapi.functions;



import dev.cregg.blockbattles.Blockbattles;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.structure.Structure;
import org.bukkit.structure.StructureManager;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class SetSlot extends ThreeArgFunction {




	@Override
	public LuaValue call(LuaValue playerObject, LuaValue inventoryAndSlot, LuaValue itemObject) {
		Player player = Bukkit.getPlayer(playerObject.get("name").toString());
		ItemStack itemstack = deserializeItem(itemObject);
		int slot = inventoryAndSlot.get(2).toint();
		String inventoryType = inventoryAndSlot.get(1).toString();
		switch(inventoryType.toString()) {
			case "inventory":
				player.getInventory().setItem(slot, itemstack);
				break;
			case "echest":

				player.getEnderChest().setItem(slot, itemstack);
				break;
			default:
				throw new LuaError("'" + inventoryType + "'" + " is not a valid inventory type");
		}


		return LuaValue.NIL;
	}

	private ItemStack deserializeItem(LuaValue itemObject) {
		int amount = itemObject.get("amount").toint();
		Material material = Material.getMaterial(itemObject.get("material").toString());
		return new ItemStack(material, amount);

	}

	private void setSlot(Inventory inventory) {


	}


}

