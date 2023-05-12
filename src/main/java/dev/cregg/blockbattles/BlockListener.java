package dev.cregg.blockbattles;

import dev.cregg.blockbattles.bbapi.BBAPIBuilder;
import dev.cregg.blockbattles.bbapi.EntitySpawnEventBuilder;
import dev.cregg.blockbattles.bbapi.PlaceEventBuilder;
import dev.cregg.blockbattles.bbapi.StructureGrowEventBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.luaj.vm2.lib.jse.JsePlatform;
import org.luaj.vm2.LuaValue;

import java.nio.file.Paths;
import java.util.Map;
import java.util.logging.Logger;
import java.util.List;

public class BlockListener implements Listener {
    Logger logger = Bukkit.getServer().getLogger();

    public static LuaValue globals = JsePlatform.standardGlobals();

    public static void reloadLua() {
        globals = JsePlatform.standardGlobals();
        globals.get("dofile").call( LuaValue.valueOf( Paths.get(datapath, "script.lua").toString()));
        globals.set("BBAPI", new BBAPIBuilder().build());
    }

    private static String datapath = "";

    public BlockListener(String path) {
        super();
        datapath = Blockbattles.datapath;
        globals.get("dofile").call( LuaValue.valueOf(  Paths.get(datapath, "script.lua").toString()));
        globals.set("BBAPI", new BBAPIBuilder().build());

    }



    @EventHandler
    public void onBlockPlace(BlockPlaceEvent blockPlaceEvent) {

        //call the function MyAdd with two parameters 5, and 5
        if(DuelCommand.isInGame(blockPlaceEvent.getPlayer().getUniqueId().toString())) {
                if (inRange(blockPlaceEvent.getBlock().getState().getLocation()) &&blockPlaceEvent.getItemInHand().getItemMeta().getLore() != null && blockPlaceEvent.getItemInHand().getItemMeta().getLore().get(0).equals("To be used in block battles...") && DuelCommand.isTurn(blockPlaceEvent.getPlayer().getUniqueId().toString())) {

                    LuaValue on_place = globals.get("on_place");
                    if (on_place != LuaValue.NIL) {
                        on_place.call(new PlaceEventBuilder(blockPlaceEvent).build());
                    }
                    DuelCommand.switchTurn(DuelCommand.gameIds.get(blockPlaceEvent.getPlayer().getUniqueId().toString()));
                } else {
                blockPlaceEvent.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent blockBreakEvent) {
        if(DuelCommand.isInGame(blockBreakEvent.getPlayer().getUniqueId().toString())) {
            if(!inRange(blockBreakEvent.getBlock().getState().getLocation()) && !DuelCommand.isTurn(blockBreakEvent.getPlayer().getUniqueId().toString())) {
                blockBreakEvent.setCancelled(true);
            }
        }
    }

    public static boolean inRange(Location location) {

        return location.getX() >= 1 && location.getX() <= 8 && location.getZ() >= 1 && location.getZ() <= 8 && location.getY() >= 1;
    }

    @EventHandler
    public void onStructureGrow(StructureGrowEvent event) {
        World world = event.getLocation().getWorld();
        List<Player> players = world.getPlayers();
        boolean allInGame = true;
        for (Player player:players
        ) {
            if(!DuelCommand.isInGame(player.getUniqueId().toString())) {
                allInGame = false;
            }
        }
        if(allInGame && players.size() > 0) {
            LuaValue on_grow = globals.get("on_grow");
            if (on_grow != LuaValue.NIL) {
                on_grow.call(new StructureGrowEventBuilder(event).build());
            }
        }

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.setScoreboard(createScoreboard(player));
    }

    private Scoreboard createScoreboard(Player player) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("scoreboard", "dummy", ChatColor.BOLD + rainbow("Block Battles"));


        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        Score first = objective.getScore("Wins");
        Score second = objective.getScore("Losses");

        String uuid = player.getUniqueId().toString();
        PlayerData playergame = Blockbattles.gameData.get(uuid);
        if(playergame == null) {
            Blockbattles.gameData.put(player.getUniqueId().toString(), new PlayerData(0, 0));
            playergame = Blockbattles.gameData.get(player.getUniqueId().toString());
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
        Player player = event.getPlayer();
        if(DuelCommand.isInGame(player.getUniqueId().toString())) {
            Bukkit.getScheduler().runTaskLater(Bukkit.getPluginManager().getPlugin("Blockbattles"), () -> {
                Player other = DuelCommand.getOpps(player.getUniqueId().toString());
                System.out.println("Map here: " +  DuelCommand.previousLocationList.toString());

                player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());


                player.setHealth(DuelCommand.previousHealthList.get(player.getUniqueId().toString()));
                player.setFoodLevel(DuelCommand.previousHungerList.get(player.getUniqueId().toString()));
                player.getInventory().setContents(DuelCommand.previousInventoryList.get(player.getUniqueId().toString()));

                DuelCommand.previousLocationList.remove(player.getUniqueId().toString());
                DuelCommand.previousInventoryList.remove(player.getUniqueId().toString());
                DuelCommand.previousHungerList.remove(player.getUniqueId().toString());
                DuelCommand.previousHealthList.remove(player.getUniqueId().toString());
                DuelCommand.gameIds.remove(player.getUniqueId().toString());

                player.sendMessage("yippee");
                Location old = DuelCommand.previousLocationList.get(other.getUniqueId().toString());
                System.out.println("location here " + old);
                System.out.println("other here " + other);

                PlayerData playergame = Blockbattles.gameData.get(player.getUniqueId().toString());
                if(playergame == null) {
                    Blockbattles.gameData.put(player.getUniqueId().toString(), new PlayerData(0, 0));
                    playergame = Blockbattles.gameData.get(player.getUniqueId().toString());
                }
                playergame.addLoss();


                player.setScoreboard(createScoreboard(player));
                if(!player.getUniqueId().equals(other.getUniqueId())) {
                    other.teleport(old);
                    other.getInventory().setContents(DuelCommand.previousInventoryList.get(other.getUniqueId().toString()));
                    other.setHealth(DuelCommand.previousHealthList.get(other.getUniqueId().toString()));
                    other.setFoodLevel(DuelCommand.previousHungerList.get(other.getUniqueId().toString()));
                    DuelCommand.previousLocationList.remove(other.getUniqueId().toString());
                    DuelCommand.previousInventoryList.remove(other.getUniqueId().toString());
                    DuelCommand.previousHungerList.remove(other.getUniqueId().toString());
                    DuelCommand.previousHealthList.remove(other.getUniqueId().toString());
                    DuelCommand.gameIds.remove(other.getUniqueId().toString());

                    PlayerData othergame = Blockbattles.gameData.get(other.getUniqueId().toString());
                    if (othergame == null) {
                        Blockbattles.gameData.put(other.getUniqueId().toString(), new PlayerData(0, 0));
                        othergame = Blockbattles.gameData.get(player.getUniqueId().toString());
                    }
                    othergame.addWin();

                    other.setScoreboard(createScoreboard(other));

                    ItemStack[] deck = Blockbattles.decks.get(player.getUniqueId().toString());
                    if(deck != null) {
                    for (ItemStack item : deck
                         ) {
                        ItemStack modifyItem = item.clone();
                        modifyItem.getItemMeta().getLore().add("Item recieved from besting: " + player.getName());
                        other.getInventory().addItem(item);
                    }
                    Blockbattles.decks.put(player.getUniqueId().toString(), new ItemStack[0]);
                    }
                }
                DuelCommand.endGame(player.getUniqueId().toString());


            }, 10);

        }

    }


    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {

            World world = event.getLocation().getWorld();
            List<Player> players = world.getPlayers();
            boolean allInGame = true;
            for (Player player:players
                 ) {
                if(!DuelCommand.isInGame(player.getUniqueId().toString())) {
                    allInGame = false;
                }
            }
            if(allInGame && players.size() > 0) {
                LuaValue on_spawn = globals.get("on_spawn");
                if (on_spawn != LuaValue.NIL) {
                    on_spawn.call(new EntitySpawnEventBuilder(event).build());
                }
            }

    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Player) {
            Player attacker = (Player) event.getDamager();
            if(event.getEntity() instanceof Player) {
                Player attacked = (Player) event.getEntity();
                attacker.performCommand("duel " + attacked.getName());
                attacked.performCommand("duel " + attacker.getName());
            }
        }
    }

}
