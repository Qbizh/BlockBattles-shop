package dev.cregg.blockbattles;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Blockbattles extends JavaPlugin {
	Logger logger = Bukkit.getServer().getLogger();

	public static Plugin PLUGIN;

	public static Configuration config;

	public static ShopGUI shopGUI;

	public static HashMap<String, ItemStack[]> decks;
	public static String datapath;

	public static Map<Material, Material> trades;

	public static HashMap<String, PlayerData> gameData;

	public Blockbattles() {
		super();
		datapath = this.getDataFolder().getAbsolutePath();
		decks = loadDecksFromFile();
		PLUGIN = this;
		shopGUI = new ShopGUI();
		config = this.getConfig();
		gameData = loadGameData();

		ScoreboardManager manager = Bukkit.getScoreboardManager();


	}

	public static void defaultConfig() {
		config = config.getDefaults();
	}

	private static HashMap<String, PlayerData> loadGameData() {
		File dataFile = new File(Blockbattles.datapath, "games.yml");

		FileConfiguration data = YamlConfiguration.loadConfiguration(dataFile);

		HashMap<String, PlayerData> output = new HashMap();
		Set<String> keys = data.getKeys(false);
		for (String key:keys
		) {
			System.out.println("Game data: " + data.get(key));
			//output.put(key, data.get(key)).;
		}

		return output;
	}




	private static HashMap<String, ItemStack[]> loadDecksFromFile() {
		HashMap<String, ItemStack[]> output = new HashMap<>();

		File dataFile = new File(Blockbattles.datapath, "decks.yml");

		FileConfiguration data = YamlConfiguration.loadConfiguration(dataFile);


		Set<String> keys = data.getKeys(false);
		for (String key:keys
		) {
			List<ItemStack> itemsList = new ArrayList<>();
			for (Map<?, ?> item : data.getMapList(key)) { //Iterate across the List of Maps in the config
				itemsList.add(ItemStack.deserialize((Map<String, Object>) item)); //Add the deserialized ItemStack to your List
			}
			ItemStack[] array = new ItemStack[itemsList.size()];
			output.put(key, itemsList.toArray(array));
		}

		return output;
	}

	private static ItemStack[] listToItemArray(List<String> list) {
		List<ItemStack> items = new ArrayList<>();
		for (String item:list
		) {



			items.add(ShopGUI.createGuiItem(Material.valueOf(item), "To be used in block battles..."));




		}
		ItemStack[] array = new ItemStack[items.size()];

		return items.toArray(array);
	}



	@Override
	public void onEnable() {

		// Plugin startup logic
		logger.log(Level.INFO, "Block battles loaded");
		logger.log(Level.INFO, this.getDataFolder().getAbsolutePath());
		this.getCommand("duel").setExecutor(new DuelCommand());
		this.getCommand("reloadlua").setExecutor(new ReloadLuaCommand());
		this.getCommand("blockdeck").setExecutor(new BlockBattlesShopCommand());
		this.getCommand("rules").setExecutor(new RulesCommand());

		getServer().getPluginManager().registerEvents(new BlockListener(), this);
		getServer().getPluginManager().registerEvents(shopGUI, this);

	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic



		File dataFile = new File(Blockbattles.datapath, "games.yml");
		FileConfiguration data = YamlConfiguration.loadConfiguration(dataFile);
		for (String key: gameData.keySet()
		) {
			data.set(key, gameData.get(key));
		}

		try {
			data.save(dataFile);
			System.out.println("saved it at: " + dataFile.getAbsolutePath());
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}









		logger.log(Level.INFO, "Block battles unloaded");
	}



	private static void saveClans() {

	}

	public static void killAll(World world) {
		for (Entity entity : world.getEntities()
		) {
			entity.remove();
		}
	}
}
