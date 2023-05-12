package dev.cregg.blockbattles;

import dev.cregg.blockbattles.bbapi.functions.SetScene;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.MerchantRecipe;

import java.util.*;


public class SpawnShopVillager implements CommandExecutor {



    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;

            Villager villager = (Villager)player.getWorld().spawnEntity(player.getLocation(), EntityType.VILLAGER);
            villager.setProfession(Villager.Profession.NITWIT);
            villager.setVillagerLevel(5);
            villager.setAI(false);
            villager.setInvulnerable(true);
            List<MerchantRecipe> trades = new ArrayList<>();

            for (Material key:Blockbattles.trades.keySet()
                 ) {

            }
            villager.setRecipes(trades);

        }

        return true;
    }


}