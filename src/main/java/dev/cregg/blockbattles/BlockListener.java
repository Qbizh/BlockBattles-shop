package dev.cregg.blockbattles;

import dev.cregg.blockbattles.bbapi.*;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.luaj.vm2.lib.jse.JsePlatform;
import org.luaj.vm2.LuaValue;

import java.nio.file.Paths;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.List;

public class BlockListener implements Listener {

	Logger logger = Bukkit.getServer().getLogger();

	public static LuaValue globals = JsePlatform.standardGlobals();

	public static void reloadLua() {
		globals = JsePlatform.standardGlobals();
		globals.get("dofile").call( LuaValue.valueOf( Paths.get(Blockbattles.datapath, "script.lua").toString()));
		globals.set("BBAPI", BBAPI.getAPI());
	}



	public BlockListener() {
		super();
		reloadLua();

	}





	@EventHandler
	public void onBlockPlace(BlockPlaceEvent blockPlaceEvent) {

		//call the function MyAdd with two parameters 5, and 5
		if(DuelManager.isInGame(blockPlaceEvent.getPlayer().getUniqueId())) {
			if (inRange(blockPlaceEvent.getBlock().getState().getLocation()) &&
					blockPlaceEvent.getItemInHand().getItemMeta().getLore() != null &&
					blockPlaceEvent.getItemInHand().getItemMeta().getLore().get(0).equals("To be used in block battles...") &&
					DuelManager.isTurn(blockPlaceEvent.getPlayer().getUniqueId())) {

				LuaValue on_place = globals.get("on_place");
				if (on_place != LuaValue.NIL) {
					on_place.call(BBAPI.placeEvent(blockPlaceEvent));
				}

			} else {
				blockPlaceEvent.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent blockBreakEvent) {
		if(DuelManager.isInGame(blockBreakEvent.getPlayer().getUniqueId())) {
			if(!inRange(blockBreakEvent.getBlock().getState().getLocation())) {
				blockBreakEvent.setCancelled(true);
			}
		}
	}

	public static boolean inRange(Location location) {

		return location.getX() >= 1 && location.getX() <= 8 &&
				location.getZ() >= 1 && location.getZ() <= 8 &&
				location.getY() >= 1;
	}

	private static boolean gameWorld(World world) {
		List<Player> players = world.getPlayers();
		boolean allInGame = true;
		for (Player player:players
		) {
			if(!DuelManager.isInGame(player.getUniqueId())) {
				allInGame = false;
			}
		}
		return allInGame && players.size() > 0;
	}

	@EventHandler
	public void onStructureGrow(StructureGrowEvent event) {
		World world = event.getLocation().getWorld();

		if(gameWorld(world)) {
			LuaValue on_grow = globals.get("on_grow");
			if (on_grow != LuaValue.NIL) {
				on_grow.call(BBAPI.structureGrowEvent(event));
			}
		}

	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		player.setScoreboard(createScoreboard(player));
		ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
		BookMeta meta = (BookMeta) book.getItemMeta();
		System.out.println(meta);
		meta.setAuthor("blank");
		meta.setTitle("blank");
		meta.setPages("Welcome to BLOCKBATTLES development BETA!\nBugs are to be expected, let us know of anything you find!\n\nEmbark on an exciting journey through the vast world of Blockbattles, a thrilling multiplayer game that takes strategic gameplay to","new heights. Prepare for intense duels where your creativity knows no limits and the power of blocks reigns supreme. Engage in adrenaline-fueled combat, strategically placing and activating blocks to outsmart and overpower your opponents. Discover a variety of block", "cards, each with unique attributes, to construct fortresses, launch devastating attacks, or set up clever traps. Join the Blockbattles community today and immerse yourself in a world where Minecraft's potential is unlocked, and your strategic prowess becomes the key to victory. Are"," you ready to showcase your skills, rise to the challenge, and make your mark in the chronicles of Blockbattles history as the ultimate champion?");
		book.setItemMeta(meta);
		player.openBook(book);
		if(!player.hasPlayedBefore()) {
			RulesCommand.giveRules(player);


		}
	}

	private Scoreboard createScoreboard(Player player) {
		Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective objective = scoreboard.registerNewObjective("scoreboard", "dummy", ChatColor.BOLD + rainbow("Block Battles"));


		objective.setDisplaySlot(DisplaySlot.SIDEBAR);

		Score first = objective.getScore("Wins");
		Score second = objective.getScore("Losses");

		UUID uuid = player.getUniqueId();
		PlayerData playergame = PlayerWins.gameData.get(uuid);
		if(playergame == null) {
			PlayerWins.gameData.put(player.getUniqueId(), new PlayerData(0, 0));
			playergame = PlayerWins.gameData.get(player.getUniqueId());
		}

		first.setScore(playergame.wins);

		second.setScore(playergame.losses);


		return scoreboard;
	}

	public static String rainbow(String string) {
		StringBuilder temp = new StringBuilder();
		int i = 0;
		for (Character chr:string.toCharArray()
		) {
			ChatColor color;

			switch(i) {
				case 0:
					color = ChatColor.RED;
					break;
				case 1:
					color = ChatColor.GOLD;
					break;
				case 2:
					color = ChatColor.YELLOW;
					break;
				case 3:
					color = ChatColor.GREEN;
					break;
				case 4:
					color = ChatColor.BLUE;
					break;
				case 5:
					color = ChatColor.LIGHT_PURPLE;
					break;
				case 6:
					color = ChatColor.DARK_PURPLE;
					break;
				default:
					color = ChatColor.BLACK;
					break;
			}

			temp.append(color).append(chr);
			i++;
			i = i % 6;
		}
		return temp.toString();
	}

	@EventHandler
	public void onSpawn(PlayerRespawnEvent event) {
		//Whoever respawned first must be the loser
		Player loser = event.getPlayer();
		if(DuelManager.isInGame(loser.getUniqueId())) {
				//Whoever their opponent is then must be the winner
				Player winner = DuelManager.getOpps(loser.getUniqueId());

				//return the loser to spawn
				loser.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());

				//restore player to how they were before the match
				DuelManager.previousStatus.get(loser.getUniqueId()).restoreWithoutTeleport(loser);

				// remove their data
				DuelManager.previousStatus.remove(loser.getUniqueId());
				DuelManager.gameTurns.remove(DuelManager.gameIds.get(loser.getUniqueId()));
				DuelManager.gameIds.remove(loser.getUniqueId());

				//Add a loss to the loser's score
				PlayerData playergame = PlayerWins.gameData.get(loser.getUniqueId());
				if(playergame == null) {
					PlayerWins.gameData.put(loser.getUniqueId(), new PlayerData(0, 0));
					playergame = PlayerWins.gameData.get(loser.getUniqueId());
				}
				playergame.addLoss();

				//refresh gui scoreboard
				loser.setScoreboard(createScoreboard(loser));

				//Check if the winner and loser are the same player
				if(!loser.getUniqueId().equals(winner.getUniqueId())) {

					//Return winner to their original position before the match and return their player data back
					DuelManager.previousStatus.get(winner.getUniqueId()).restore(winner);
					DuelManager.previousStatus.remove(winner.getUniqueId());
					DuelManager.gameIds.remove(winner.getUniqueId());

					//Add a win to the winner's score
					PlayerData othergame = PlayerWins.gameData.get(winner.getUniqueId());
					if (othergame == null) {
						PlayerWins.gameData.put(winner.getUniqueId(), new PlayerData(0, 0));
						othergame = PlayerWins.gameData.get(loser.getUniqueId());
					}
					othergame.addWin();

					//refresh gui scoreboard
					winner.setScoreboard(createScoreboard(winner));

					//Give the winning player the losing player's deck
					ItemStack[] deck = DeckManager.decks.get(loser.getUniqueId());
					if(deck != null) {
						for (ItemStack item : deck
						) {
							if(item != null) {
								ItemStack modifyItem = item.clone();

								modifyItem.getItemMeta().getLore().add("Item received from besting: " + loser.getName());

								winner.getInventory().addItem(modifyItem);
							}
						}
						DeckManager.decks.put(loser.getUniqueId(), new ItemStack[0]);
					}
				}
				//Cleanup all remaining game data
				DuelManager.endGame(loser.getUniqueId());


			

		}

	}



	@EventHandler
	public void _onEntitySpawn(EntitySpawnEvent event) {

//		World world = event.getLocation().getWorld();
//		List<Player> players = world.getPlayers();
//		boolean allInGame = true;
//		for (Player player:players
//		) {
//			if(!DuelCommand.isInGame(player.getUniqueId().toString())) {
//				allInGame = false;
//			}
//		}
//		if(allInGame && players.size() > 0) {
//			LuaValue on_spawn = globals.get("on_spawn");
//			if (on_spawn != LuaValue.NIL) {
//				on_spawn.call(new EntitySpawnEventBuilder(event).build());
//			}
//		}

	}
	@EventHandler
	public void onEntitySpawn(CreatureSpawnEvent event) {

		World world = event.getLocation().getWorld();

		if(gameWorld(world)) {

			LuaValue on_spawn = globals.get("on_spawn");
			if (on_spawn != LuaValue.NIL) {
				on_spawn.call(BBAPI.entitySpawnEvent(event));
			}
		}

	}

	@EventHandler
	public void onAttack(EntityDamageByEntityEvent event) {
		if(event.getDamager() instanceof Player) {
			Player attacker = (Player) event.getDamager();
			if(event.getEntity() instanceof Player) {

					Player attacked = (Player) event.getEntity();
					if((!DuelManager.isInGame(attacker.getUniqueId()) && !DuelManager.isInGame(attacked.getUniqueId()))) {
					Bukkit.getScheduler().runTaskLater(Bukkit.getPluginManager().getPlugin("Blockbattles"), () -> {
						if((!DuelManager.isInGame(attacker.getUniqueId()) && !DuelManager.isInGame(attacked.getUniqueId()))) {
							attacker.performCommand("duel " + attacked.getName());
							attacked.performCommand("duel " + attacker.getName());
						}
					}, 20);
				} else {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onProjectile(ProjectileHitEvent event) {
		World world = (event.getHitBlock() == null ? event.getHitEntity().getWorld():event.getHitBlock().getWorld());
		if(gameWorld(world)) {
			LuaValue on_projectile = globals.get("on_projectile_hit");
			if (on_projectile != LuaValue.NIL) {
				on_projectile.call(BBAPI.projectileHitEvent(event));
			}
		}
	}
//	@EventHandler
//	public void onMove(PlayerMoveEvent event) {
//		if(DuelCommand.isInGame(event.getPlayer().getUniqueId().toString())) {
//		Bukkit.getScheduler().runTaskLater(Bukkit.getPluginManager().getPlugin("Blockbattles"), () -> {
//
//				if(!inRange(event.getTo()) && DuelCommand.isInGame(event.getPlayer().getUniqueId().toString())) {
//					System.out.println("Should kill");
//					event.getPlayer().setHealth(0);
//				}
//
//		}, 10);
//		}
//
//	}



}
