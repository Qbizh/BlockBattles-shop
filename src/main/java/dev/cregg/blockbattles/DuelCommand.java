package dev.cregg.blockbattles;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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

    public DuelCommand() {
        super();
        this.outgoingChallengeList = new HashMap<>();
    }

    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {

            Player player = (Player) sender;
            Player other = Bukkit.getPlayer(args[0]);
            if(outgoingChallengeList.containsKey(player.getDisplayName())) {
                this.outgoingChallengeList.remove(player.getDisplayName());
                other.sendMessage(ChatColor.GREEN + "You accepted " + other.getDisplayName() + "'s duel request");
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
                player.teleport(new Location(world, 0, 0, 0));
                playersInGame.add(new String[] {player.getUniqueId().toString(), other.getUniqueId().toString()});



            } else {
                other.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + player.getDisplayName() + " challenged you to a duel");
                outgoingChallengeList.put(other.getDisplayName(), player.getDisplayName());
            }
        }

        return true;
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
            for (String player : game) {
                int idx = 0;
                int pidx = player.indexOf(player);
                if(pidx == -1) {
                    continue;
                } else if (pidx == 0) {
                    idx = 1;
                }
               opp = Bukkit.getPlayer(UUID.fromString(game[idx]));
            }
        }

        return opp;
    }


}