package dev.cregg.blockbattles;

import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class PlayerWins {
	public static Objective playerWins;

	public PlayerWins(ScoreboardManager manager) {
		Scoreboard board = manager.getNewScoreboard();
		Objective objective = board.registerNewObjective("blockbattles", "dummy");
		playerWins = objective;
	}
}
