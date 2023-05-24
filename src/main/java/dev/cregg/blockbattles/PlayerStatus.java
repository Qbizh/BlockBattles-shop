package dev.cregg.blockbattles;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerStatus {


	private final Location location;
	private final ItemStack[] inventory;
	private final double health;
	private final int hunger;

	public PlayerStatus(Location location, ItemStack[] inventory, double health, int hunger) {
		this.location = location;
		this.inventory = inventory;
		this.health = health;
		this.hunger = hunger;
	}

	public PlayerStatus(Player player) {
		this.location = player.getLocation();
		this.inventory = player.getInventory().getContents();
		this.health = player.getHealth();
		this.hunger = player.getFoodLevel();
	}

	public void restore(Player player) {
		player.teleport(location);
		player.getInventory().setContents(inventory);
		player.setHealth(health);
		player.setFoodLevel(hunger);
	}

	public void restoreWithoutTeleport(Player player) {
		player.getInventory().setContents(inventory);
		player.setHealth(health);
		player.setFoodLevel(hunger);
	}
}
