package dev.cregg.blockbattles;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class BlockBattlesShopCommand implements CommandExecutor {



	// This method is called, when somebody uses our command
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player player = (Player) sender;
		System.out.println("why does nothing ever work");
		if(!DuelCommand.isInGame(player.getUniqueId().toString())) {
			Blockbattles.shopGUI.openInventory(player);
		}
		return true;
	}






}