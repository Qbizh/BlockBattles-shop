package dev.cregg.blockbattles;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Map;

public class ShopGUI implements Listener {
	private Inventory inv;

	public ShopGUI() {
		// Create a new inventory, with no owner (as this isn't a real inventory), a size of nine, called example
		inv = Bukkit.createInventory(null, 9 * 3, "Block Deck");

		// Put the items into the inventory
		initializeItems();
	}

	// You can call this whenever you want to put the items in
	public void initializeItems() {
		if(Blockbattles.config == null) {
			return;
		}
		List<String> shopitems = Blockbattles.config.getStringList("shopitems");
		if(shopitems == null) {
			Blockbattles.defaultConfig();
			return;
		}

	}


	// You can open the inventory with this
	public void openInventory(final HumanEntity ent) {
		inv = Bukkit.createInventory(null, 9 * 1, "Block Deck");
		ItemStack[] items = DeckManager.decks.get(ent.getUniqueId());
		if(items != null) {
			inv.setContents(items);
		}

		ent.openInventory(inv);
	}

	// Check for clicks on items
	@EventHandler
	public void onInventoryClick(final InventoryClickEvent e) {
		if (e.getView().getTitle().equals("Block Deck")) {
			ItemStack itemStack = e.getCurrentItem();
			if(itemStack == null) {
				return;
			}
			if(itemStack.getItemMeta().getLore() == null|| !itemStack.getItemMeta().getLore().get(0).equals("To be used in block battles...")) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		if (e.getView().getTitle().equals("Block Deck")) {
			DeckManager.decks.put(e.getPlayer().getUniqueId(), e.getInventory().getContents());
			File dataFile = new File(Blockbattles.datapath, "decks.yml");
			FileConfiguration data = YamlConfiguration.loadConfiguration(dataFile);
			data.set(e.getPlayer().getUniqueId().toString(), serializeInventory(e.getInventory().getContents()));
			try {
				data.save(dataFile);
				System.out.println("saved it at: " + dataFile.getAbsolutePath());
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}

	public static List<Map<String, Object>> serializeInventory(ItemStack[] contents) {
		List<Map<String, Object>> output = new ArrayList<>();
		for (ItemStack item:contents
		) {
			if(item != null) {
				output.add(item.serialize());
			}
		}
		return output;
	}
}