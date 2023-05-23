package dev.cregg.blockbattles;

import dev.cregg.blockbattles.bbapi.functions.SetScene;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.SpawnCategory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.structure.Structure;
import org.bukkit.structure.StructureManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;


public class DuelCommand implements CommandExecutor {
	public static List<UUID[]> playersInGame;

	private HashMap<UUID, UUID> outgoingChallengeList;
	public static HashMap<UUID, Location> previousLocationList;
	public static HashMap<UUID, ItemStack[]> previousInventoryList;
	public static HashMap<UUID, Double> previousHealthList;
	public static HashMap<UUID, Integer> previousHungerList;
	public static HashMap<UUID, Integer> gameIds;
	public static HashMap<Integer, UUID> gameTurns;
	private static int gameIndex = 0;


	public DuelCommand() {
		super();
		this.outgoingChallengeList = new HashMap<>();
		previousLocationList = new HashMap<>();
		previousInventoryList = new HashMap<>();
		previousHealthList = new HashMap<>();
		previousHungerList = new HashMap<>();
		gameIds = new HashMap<>();
		playersInGame = new ArrayList<>();
		gameTurns = new HashMap<>();

	}

	public static void endGame(String player) {
		System.out.println("Tried to end game");
		for (int i = 0; i < playersInGame.size(); i++) {
			if(Arrays.asList(playersInGame.get(i)).contains(player)) {
				playersInGame.remove(i);
				return;
			}
		}
	}

	// This method is called, when somebody uses our command
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player && args.length == 1) {

			Player player = (Player) sender;
			Player other = Bukkit.getPlayer(args[0]);

			if(isInGame(player.getUniqueId())) {
				player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "You are already in game!");
			}
			if(outgoingChallengeList.containsKey(player.getUniqueId())) {
				this.outgoingChallengeList.remove(player.getUniqueId());
				other.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "You accepted " + other.getDisplayName() + "'s duel request");
				World world = Bukkit.getWorld(other.getUniqueId().toString());
				World altWorld;
				if(world == null) {
					altWorld = Bukkit.getWorld(player.getUniqueId().toString());
					if(altWorld == null) {
						WorldCreator wc = new WorldCreator(other.getUniqueId().toString());

						wc.environment(World.Environment.NORMAL);
						wc.type(WorldType.FLAT);
						wc.generatorSettings("{\"layers\": []}");
						wc.generateStructures(false);

						wc.createWorld();
						world = Bukkit.getWorld(other.getUniqueId().toString());

					} else {
						world = altWorld;
					}
				}

				previousLocationList.put(player.getUniqueId(), player.getLocation());
				previousInventoryList.put(player.getUniqueId(), player.getInventory().getContents());
				previousHealthList.put(player.getUniqueId(), player.getHealth());
				previousHungerList.put(player.getUniqueId(), player.getFoodLevel());
				gameIds.put(player.getUniqueId(), gameIndex);
				gameTurns.put(gameIndex, player.getUniqueId());
				if(!player.getUniqueId().equals(other.getUniqueId())) {
					previousLocationList.put(other.getUniqueId(), other.getLocation());
					previousInventoryList.put(other.getUniqueId(), other.getInventory().getContents());
					previousHealthList.put(other.getUniqueId(), other.getHealth());
					previousHungerList.put(other.getUniqueId(), other.getFoodLevel());
					gameIds.put(other.getUniqueId(), gameIndex);
				}
				gameIndex++;

				for(int x = -5; x < 10; x++){
					for(int y = -5; y < 10; y++){
						for(int z = -5; z < 10; z++){
							Block block = world.getBlockAt(x, y, z);
							block.setType(Material.AIR);
						}
					}
				}

				Blockbattles.killAll(world);
				world.setStorm(false);
				world.setTime(0);
				world.setSpawnLimit(SpawnCategory.MONSTER, 0);
				player.teleport(new Location(world, 5, 2, 5));
				player.setHealth(20);
				player.setFoodLevel(20);
				other.teleport(new Location(world, 5, 2, 5));
				other.setHealth(20);
				other.setFoodLevel(20);
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
				SetScene.placeStructure(world, "defaultfield", 0, 0, 0);
				SetScene.placeStructure(world, "arena", -15, -1, -16);
				playersInGame.add(new UUID[] {player.getUniqueId(), other.getUniqueId()});



			} else {
				other.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + player.getDisplayName() + " challenged you to a duel");
				outgoingChallengeList.put(other.getUniqueId(), player.getUniqueId());
			}
		} else {
			return false;
		}

		return true;
	}

	public static boolean isTurn(UUID uuid) {
		if(isInGame(uuid)) {
			UUID turn = gameTurns.get(gameIds.get(uuid));
			if(turn != null) {
				return turn.equals(uuid);
			}
			System.out.println("uh uh");
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

	public static Player getOpps(UUID uuid) {

		Player opp = null;
		for (UUID[] game : playersInGame) {
			for (int i = 0; i < game.length; i++) {


				if(game[i].equals(uuid)) {
					int idx = 0;

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
		System.out.println("opp of:" + Bukkit.getPlayer(uuid).getName() + " is " + opp.getName());
		return opp;
	}


}