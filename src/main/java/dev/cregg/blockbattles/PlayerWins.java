package dev.cregg.blockbattles;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.io.File;
import java.util.*;

public class PlayerWins {
	public static Objective playerWins;
	public static HashMap<UUID, PlayerData> gameData;

	public static void init(ScoreboardManager manager) {
		Scoreboard board = manager.getNewScoreboard();
		Objective objective = board.registerNewObjective("blockbattles", "dummy");
		playerWins = objective;
		gameData = loadGameData();
	}

	private static HashMap<UUID, PlayerData> loadGameData() {
		File dataFile = new File(Blockbattles.datapath, "games.yml");

		FileConfiguration data = YamlConfiguration.loadConfiguration(dataFile);

		HashMap<UUID, PlayerData> output = new HashMap();
		Set<String> keys = data.getKeys(false);
		for (String key:keys
		) {
			System.out.println("Game data: " + data.get(key));
			List<Integer> playerdata = data.getIntegerList(key);
			output.put(UUID.fromString(key), new PlayerData(playerdata.get(0), playerdata.get(1)));
		}

		return output;
	}

	public static FileConfiguration serializeData() {
		FileConfiguration fileConfig = new YamlConfiguration();
		for (UUID key:gameData.keySet()
			 ) {
			fileConfig.set(key.toString(), serializePlayerData(gameData.get(key)));
		}
		return fileConfig;
	}

	private static List<Integer> serializePlayerData(PlayerData data) {
		List<Integer> list = new ArrayList<>();
		list.set(0, data.wins);
		list.set(1, data.losses);
		return list;
	}
}
