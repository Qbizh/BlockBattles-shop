package dev.cregg.blockbattles.bbapi;

import dev.cregg.blockbattles.DuelCommand;
import dev.cregg.blockbattles.bbapi.functions.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import java.util.ArrayList;
import java.util.List;

//Collection of functions for generating lua data
public class BBAPI {
	public static LuaTable getAPI() {
		//This function generates the object "BBAPI" for usage in the lua code
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
		table.set("chat", new Chat());
		table.set("switchturn", new SwitchTurn());
		table.set("kill", new Kill());
		table.set("effect", new Effect());
		return table;
	}

	//this function generates a lua table based on on entity instance
	public static LuaTable entity(Entity entity) {

		String paintingType = null;
		//If the entity is a painting then it includes the type of the painting
		if(entity instanceof Painting) {
			Painting painting = (Painting) entity;
			paintingType = painting.getArt().toString();
		}
		LuaTable table = new LuaTable();

		table.set("type", entity.getType().name().toString());
		table.set("world", entity.getWorld().getName());
		table.set("painting", paintingType);
		return table;
	}

	public static LuaTable entitySpawnEvent(EntitySpawnEvent event) {
		LuaTable table = new LuaTable();
		table.set("entity", entity(event.getEntity()));
		Location location = event.getLocation();
		table.set("x", location.getX());
		table.set("y", location.getY());
		table.set("z", location.getZ());
		return table;
	}

	public static LuaTable item(ItemStack item) {
		LuaTable table = new LuaTable();

		table.set("material", item.getType().toString());
		table.set("amount", item.getAmount());
		return table;
	}

	public static LuaTable placeEvent(BlockPlaceEvent event) {
		LuaTable table = new LuaTable();
		table.set("material", event.getBlock().getBlockData().getMaterial().toString());
		table.set("placer", player(event.getPlayer()));
		table.set("opps", player(DuelCommand.getOpps(event.getPlayer().getUniqueId().toString())));
		Location location = event.getBlock().getLocation();
		table.set("x", location.getX());
		table.set("y", location.getY());
		table.set("z", location.getZ());

		return table;
	}

	public static LuaTable player(Player player) {
		LuaTable table = new LuaTable();

		table.set("health", player.getHealth());
		table.set("name", player.getName());
		table.set("uuid", player.getName());
		table.set("luck", getLuck(player));
		table.set("world", player.getLocation().getWorld().getName());
		table.set("inventory", LuaValue.listOf(serializeInventory(player.getInventory())));
		table.set("echest", LuaValue.listOf(serializeInventory(player.getEnderChest())));

		return table;
	}

	public static int getLuck(Player player) {
		int level = 0;
		for (PotionEffect effect : player.getActivePotionEffects()
		) {
			if(effect.getType().equals(PotionEffectType.LUCK)) {
				level = effect.getAmplifier();
			}
		}
		return level;
	}

	public static LuaTable[] serializeInventory(Inventory inventory) {
		List<LuaTable> inventoryItems = new ArrayList<>();
		for (ItemStack item:inventory.getContents()
		) {
			if(item != null) {
				inventoryItems.add(item(item));
			} else {
				inventoryItems.add((item(new ItemStack(Material.AIR, 1))));
			}
		}
		LuaTable[] itemArr = new LuaTable[inventoryItems.size()];
		inventoryItems.toArray(itemArr);
		return itemArr;
	}

	public static LuaTable projectileHitEvent(ProjectileHitEvent event) {
		Player launcher = null;
		Player opps = null;
		if(event.getEntity().getShooter() instanceof Player) {
			launcher = (Player)event.getEntity().getShooter();
			opps = DuelCommand.getOpps(launcher.getUniqueId().toString());
		}

		String hitblock = null;
		Entity hitentity = null;
		if(event.getHitBlock() != null) {
			hitblock = event.getHitBlock().getBlockData().getMaterial().toString();
		} else {
			hitentity = event.getHitEntity();
		}

		LuaTable table = new LuaTable();
		table.set("hitblock", hitblock);
		table.set("hitentity", entity(hitentity));
		table.set("launcher", player(launcher));
		table.set("opps", player(opps));
		table.set("launched", entity(event.getEntity()));
		Location location = event.getEntity().getLocation();
		table.set("x", location.getX());
		table.set("y", location.getY());
		table.set("z", location.getZ());
		return table;
	}

	public static LuaTable structureGrowEvent(StructureGrowEvent event) {
		LuaTable table = new LuaTable();
		table.set("world", event.getWorld().getName());
		table.set("species", event.getSpecies().toString());
		Location location = event.getLocation();
		table.set("x", location.getX());
		table.set("y", location.getY());
		table.set("z", location.getZ());
		return table;
	}

}
