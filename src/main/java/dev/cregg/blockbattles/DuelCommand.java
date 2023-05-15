package dev.cregg.blockbattles;

import dev.cregg.blockbattles.bbapi.functions.SetScene;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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
	public static List<String[]> playersInGame = new ArrayList<String[]>();

	private HashMap<String, String> outgoingChallengeList;
	public static HashMap<String, Location> previousLocationList;
	public static HashMap<String, ItemStack[]> previousInventoryList;
	public static HashMap<String, Double> previousHealthList;
	public static HashMap<String, Integer> previousHungerList;
	public static HashMap<String, Integer> gameIds;
	public static HashMap<Integer, String> gameTurns;
	private static int gameIndex = 0;


	public DuelCommand() {
		super();
		this.outgoingChallengeList = new HashMap<>();
		previousLocationList = new HashMap<>();
		previousInventoryList = new HashMap<>();
		previousHealthList = new HashMap<>();
		previousHungerList = new HashMap<>();
		gameIds = new HashMap<>();
		gameTurns = new HashMap<>();

	}

	public static void endGame(String player) {
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

			if(isInGame(player.getUniqueId().toString())) {
				player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "You are already in game!");
			}
			if(outgoingChallengeList.containsKey(player.getDisplayName())) {
				this.outgoingChallengeList.remove(player.getDisplayName());
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

				previousLocationList.put(player.getUniqueId().toString(), player.getLocation());
				previousInventoryList.put(player.getUniqueId().toString(), player.getInventory().getContents());
				previousHealthList.put(player.getUniqueId().toString(), player.getHealth());
				previousHungerList.put(player.getUniqueId().toString(), player.getFoodLevel());
				gameIds.put(player.getUniqueId().toString(), gameIndex);
				if(!player.getUniqueId().equals(other.getUniqueId())) {
					previousLocationList.put(other.getUniqueId().toString(), other.getLocation());
					previousInventoryList.put(other.getUniqueId().toString(), other.getInventory().getContents());
					previousHealthList.put(other.getUniqueId().toString(), other.getHealth());
					previousHungerList.put(other.getUniqueId().toString(), other.getFoodLevel());
					gameIds.put(other.getUniqueId().toString(), gameIndex);
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
				player.teleport(new Location(world, 5, 2, 5));
				player.setHealth(20);
				player.setFoodLevel(20);
				other.teleport(new Location(world, 5, 2, 5));
				other.setHealth(20);
				other.setFoodLevel(20);
				ItemStack[] playerContents = Blockbattles.decks.get(player.getUniqueId().toString());
				if(playerContents == null) {
					playerContents = new ItemStack[] {};
				}
				player.getInventory().setContents(playerContents);
				ItemStack[] otherContents = Blockbattles.decks.get(other.getUniqueId().toString());
				if(otherContents == null) {
					otherContents = new ItemStack[] {};
				}
				other.getInventory().setContents(otherContents);
				SetScene.placeStructure(world, "defaultfield", 0, 0, 0);
				playersInGame.add(new String[] {player.getUniqueId().toString(), other.getUniqueId().toString()});



			} else {
				other.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + player.getDisplayName() + " challenged you to a duel");
				outgoingChallengeList.put(other.getDisplayName(), player.getDisplayName());
			}
		} else {
			return false;
		}

		return true;
	}

	public static boolean isTurn(String uuid) {
		return isInGame(uuid) && gameTurns.get(gameIds.get(uuid)).equals(uuid);
	}

	public static void switchTurn(int gameid) {
		String currentTurn = gameTurns.get(gameid);
		String nextTurn = getOpps(currentTurn).getUniqueId().toString();
		gameTurns.put(gameid, nextTurn);
	}

	public static boolean isInGame(String uuid) {

		for (String[] game : playersInGame) {
			for (String player : game) {
				if(player.equals(uuid)) {
					return true;
				}
			}
		}
		return false;
	}

	public static Player getOpps(String uuid) {

		Player opp = null;
		for (String[] game : playersInGame) {
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

					opp = Bukkit.getPlayer(UUID.fromString(game[idx]));

				}


			}
		}
		System.out.println("opp of:" + Bukkit.getPlayer(UUID.fromString(uuid)).getName() + " is " + opp.getName());
		return opp;
	}


}