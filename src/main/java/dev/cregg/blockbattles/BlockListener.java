package dev.cregg.blockbattles;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.logging.Level;
import java.util.logging.Logger;

public class BlockListener implements Listener {
    Logger logger = Bukkit.getServer().getLogger();
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent blockPlaceEvent) {
        logger.log(Level.INFO, "You just placed: " + blockPlaceEvent.getBlock().getBlockData().getMaterial().toString());
    }
}
