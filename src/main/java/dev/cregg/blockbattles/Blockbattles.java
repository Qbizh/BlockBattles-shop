package dev.cregg.blockbattles;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.ScoreboardManager;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Blockbattles extends JavaPlugin {
	Logger logger = Bukkit.getServer().getLogger();

	public static boolean DEBUG_MODE = true;

	public static Plugin PLUGIN;
	public static Configuration config;
	public static ShopGUI shopGUI;
	public static String datapath;
	public static Map<Material, Material> trades;
	public static PlayerWins playerWins;

	public Blockbattles() {
		super();
		datapath = this.getDataFolder().getAbsolutePath();
		PLUGIN = this;
		shopGUI = new ShopGUI();
		config = this.getConfig();

		DEBUG_MODE = config.getBoolean("debug");


		ScoreboardManager manager = Bukkit.getScoreboardManager();
		PlayerWins.init(manager);
		DeckManager.init();

	}

	public static void defaultConfig() {
		config = config.getDefaults();
	}

	@Override
	public void onEnable() {

		// Plugin startup logic
		if(DEBUG_MODE) logger.log(Level.INFO, "Block battles loaded");
		if(DEBUG_MODE) logger.log(Level.INFO, this.getDataFolder().getAbsolutePath());
		this.getCommand("duel").setExecutor(new DuelManager());
		this.getCommand("reloadlua").setExecutor(new ReloadLuaCommand());
		this.getCommand("blockdeck").setExecutor(new BlockBattlesShopCommand());
		//this.getCommand("rules").setExecutor(new RulesCommand());
		this.getCommand("version").setExecutor(new VersionCommand());

		getServer().getPluginManager().registerEvents(new BlockListener(), this);
		getServer().getPluginManager().registerEvents(shopGUI, this);

	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
		File gamesFile = new File(Blockbattles.datapath, "games.yml");

		try {
			PlayerWins.serializeData().save(gamesFile);
			if(DEBUG_MODE) System.out.println("saved games data at: " + gamesFile.getAbsolutePath());
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
		if(DEBUG_MODE) logger.log(Level.INFO, "Block battles unloaded");
	}

	public static void killAll(World world) {
		for (Entity entity : world.getEntities()
		) {
			entity.remove();
		}
	}
}
