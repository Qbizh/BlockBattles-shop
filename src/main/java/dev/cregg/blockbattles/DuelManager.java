package dev.cregg.blockbattles;

import dev.cregg.blockbattles.bbapi.functions.SetScene;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.SpawnCategory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static dev.cregg.blockbattles.Blockbattles.DEBUG_MODE;


public class DuelManager implements CommandExecutor {
	public static List<UUID[]> playersInGame = new ArrayList<>();

	private HashMap<UUID, UUID> outgoingChallenges = new HashMap<>();
	public static HashMap<UUID, PlayerStatus> previousStatus = new HashMap<>();
	public static HashMap<UUID, Integer> gameIds = new HashMap<>();
	public static HashMap<Integer, UUID> gameTurns = new HashMap<>();

	private static int gameIndex = 0;

	public static void endGame(UUID player) {
		if(DEBUG_MODE) System.out.println("Tried to end game");
		for (int i = 0; i < playersInGame.size(); i++) {
			if(Arrays.asList(playersInGame.get(i)).contains(player)) {
				playersInGame.remove(i);
				return;
			}
		}
	}

	// This method is called, when /duel is used
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		//check if this is a player and if they have selected a player to duel
		if(sender instanceof Player && args.length == 1) {

			Player player = (Player) sender;
			Player other = Bukkit.getPlayer(args[0]);

			//check if the player is already in game
			if(isInGame(player.getUniqueId())) {
				player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "You are already in game!");
			}

			//Check if the player already has been challenged
			if(outgoingChallenges.containsKey(player.getUniqueId())) {
				this.outgoingChallenges.remove(player.getUniqueId());
				other.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "You accepted " + other.getDisplayName() + "'s duel request");

				//Creating a world is costly so this section of code tries to optimize it
				World world = Bukkit.getWorld(other.getUniqueId().toString());
				World altWorld;
				//If your opponent doesn't have a world
				if(world == null) {
					altWorld = Bukkit.getWorld(player.getUniqueId().toString());
					//If you don't have a world too then you actually generate a world
					if(altWorld == null) {
						WorldCreator wc = new WorldCreator(other.getUniqueId().toString());

						wc.environment(World.Environment.NORMAL);
						wc.type(WorldType.FLAT);
						wc.generatorSettings("{\"layers\": []}");
						wc.generateStructures(false);

						wc.createWorld();
						world = Bukkit.getWorld(other.getUniqueId().toString());

					} else {
						//If you do have a world use that one
						world = altWorld;
					}
				}

				//save the status of the player and initialize the turn of a player
				previousStatus.put(player.getUniqueId(), new PlayerStatus(player));
				gameIds.put(player.getUniqueId(), gameIndex);
				gameTurns.put(gameIndex, player.getUniqueId());
				if(!player.getUniqueId().equals(other.getUniqueId())) {
					previousStatus.put(other.getUniqueId(), new PlayerStatus(other));
					gameIds.put(other.getUniqueId(), gameIndex);
				}
				//Gameindex sequentially identifies each game
				gameIndex++;

				//Empty the world
				for(int x = -5; x < 10; x++) {
					for (int y = -5; y < 10; y++) {
						for (int z = -5; z < 10; z++) {
							Block block = world.getBlockAt(x, y, z);
							block.setType(Material.AIR);
						}
					}
				}
				Blockbattles.killAll(world);
				world.setStorm(false);
				world.setTime(0);
				world.setSpawnLimit(SpawnCategory.MONSTER, 0);

				//Teleport the player to the world and heal and feed them
				player.teleport(new Location(world, 5, 2, 5));
				player.setHealth(20);
				player.setFoodLevel(20);
				other.teleport(new Location(world, 5, 2, 5));
				other.setHealth(20);
				other.setFoodLevel(20);

				//Replace contents of player inventories with their decks
				ItemStack[] playerContents = DeckManager.decks.get(player.getUniqueId());
				if(playerContents == null) {
					playerContents = new ItemStack[] {};
				}
				player.getInventory().setContents(playerContents);
				ItemStack[] otherContents = DeckManager.decks.get(other.getUniqueId().toString());
				if(otherContents == null) {
					otherContents = new ItemStack[] {};
				}
				other.getInventory().setContents(otherContents);

				//Put down the field and arena
				SetScene.placeStructure(world, "defaultfield", 0, 0, 0);
				SetScene.placeStructure(world, "arena", -15, -1, -16);

				//Keep track of who is in game and against who
				playersInGame.add(new UUID[] {player.getUniqueId(), other.getUniqueId()});



			} else {
				//Notify the other player that they have been challenged to a duel
				other.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + player.getDisplayName() + " challenged you to a duel");
				outgoingChallenges.put(other.getUniqueId(), player.getUniqueId());
			}
		} else {
			return false;
		}

		return true;
	}

	//Check if it is a player's turn
	public static boolean isTurn(UUID uuid) {
		//If they aren't in game it isn't their turn
		if(isInGame(uuid)) {
			UUID turn = gameTurns.get(gameIds.get(uuid));
			if(turn != null) {
				return turn.equals(uuid);
			}
			System.out.println(gameTurns);
		}
		return false;
	}

	public static void switchTurn(int gameid) {
		UUID currentTurn = gameTurns.get(gameid);
		UUID nextTurn = getOpps(currentTurn).getUniqueId();
		gameTurns.put(gameid, nextTurn);
	}

	public static boolean isInGame(UUID uuid) {

		for (UUID[] game : playersInGame) {
			for (UUID player : game) {
				if(player.equals(uuid)) {
					return true;
				}
			}
		}
		return false;
	}

	//Get the opponent of a player
	public static Player getOpps(UUID uuid) {

		Player opp = null;
		for (UUID[] game : playersInGame) {
			for (int i = 0; i < game.length; i++) {


				if(game[i].equals(uuid)) {
					int idx = 0;
					//get the other player in the game
					switch(i) {
						case 0:
							idx = 1;
							break;
						case 1:
							break;
						default:
							continue;
					}

					opp = Bukkit.getPlayer(game[idx]);

				}


			}
		}

		return opp;
	}


}