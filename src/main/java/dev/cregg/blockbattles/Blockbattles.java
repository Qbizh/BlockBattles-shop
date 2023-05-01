package dev.cregg.blockbattles;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class Blockbattles extends JavaPlugin {
    Logger logger = Bukkit.getServer().getLogger();
    @Override
    public void onEnable() {
        // Plugin startup logic
        logger.log(Level.INFO, "Block battles loaded");
        this.getCommand("duel").setExecutor(new DuelCommand());
        getServer().getPluginManager().registerEvents(new BlockListener(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        logger.log(Level.INFO, "Block battles unloaded");
    }
}
