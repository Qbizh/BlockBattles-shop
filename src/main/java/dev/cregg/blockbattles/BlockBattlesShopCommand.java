package dev.cregg.blockbattles;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BlockBattlesShopCommand implements CommandExecutor {



	// This method is called, when somebody uses our command
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player player = (Player) sender;

		if(!DuelManager.isInGame(player.getUniqueId())) {
			Blockbattles.shopGUI.openInventory(player);
		}
		return true;
	}






}