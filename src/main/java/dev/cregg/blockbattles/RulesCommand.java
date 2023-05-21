package dev.cregg.blockbattles;

import dev.cregg.blockbattles.bbapi.functions.SetScene;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.*;


public class RulesCommand implements CommandExecutor {



	private static final List<String> lore = new ArrayList<String>() {{
		add("This thing was found in my ass");
	}};


	// This method is called, when somebody uses our command
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			giveRules((Player) sender);
			//((Player)sender).getInventory().addItem(RulesCommand.getRules());
			return true;
		}
		return false;
	}

	private static ItemStack getRules() {

		ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
		BookMeta meta = (BookMeta)book.getItemMeta();
		meta.setLore(lore);
		meta.setAuthor("Gabriel");
		meta.setTitle("Gabriel's Big Book of Rules");
		meta.setPages("cool");
		meta.setCustomModelData(6969);
		book.setItemMeta(meta);
		return book;
	}

	public static void giveRules(Player player) {
		player.performCommand("give @s written_book{CustomModelData:6969,pages:['[\"\",{\"text\":\"\\\\n\\\\n\\\\n\\\\n\\\\n\"},{\"text\":\" \\\\u0020 Block Battle Rules \\\\u0020 \\\\u0020 \\\\u0020 \\\\u0020 \\\\u0020\",\"bold\":true,\"color\":\"black\"}]','[\"\",{\"text\":\"Disqualifications:\",\"bold\":true,\"color\":\"dark_red\"},{\"text\":\"\\\\n\\\\n \\\\u0020 \\\\u0020Stepping Off The Board - Lose Instantly\\\\n \\\\u0020 \\\\u0020\\\\n \\\\u0020 \\\\u0020Taking To Long To Take Your Turn - Lose One Turn \\\\n \",\"color\":\"reset\"}]','[\"\",{\"text\":\"Warps:\",\"bold\":true,\"color\":\"dark_purple\"},{\"text\":\"\\\\n\\\\n \\\\u0020 \\\\u0020Warps Benefit Combos And Certain Blocks,\\\\n For Example Using The Snowball In The Snow Warp Will Make You Gain A Extra Turn Causing A Freeze On Your Opponent. \\\\n\\\\n \",\"color\":\"reset\"}]','[\"\",{\"text\":\"All Warps (Descending Proc Chance)\",\"bold\":true},{\"text\":\"\\\\n\\\\n\",\"color\":\"reset\"},{\"text\":\"         \\\\u21d3\\\\u21d3\\\\u21d3\",\"bold\":true,\"color\":\"dark_aqua\"},{\"text\":\"\\\\n\\\\n\",\"color\":\"reset\"},{\"text\":\"Sun Warp \",\"bold\":true},{\"text\":\"- 100%\\\\nTorch + Campfire \",\"color\":\"reset\"},{\"text\":\"(Only on Wednesdays) \",\"color\":\"dark_aqua\"},{\"text\":\"\\\\n\\\\n\",\"color\":\"reset\"},{\"text\":\"Storm \",\"bold\":true},{\"text\":\"- 100%\\\\nSponge + Any Water\\\\n\\\\n\",\"color\":\"reset\"},{\"text\":\"Firework Warp\",\"bold\":true},{\"text\":\" - 73% Brown Wool with Firework\",\"color\":\"reset\"}]','[\"\",{\"text\":\"Desert Warp\",\"bold\":true,\"color\":\"black\"},{\"text\":\" - 65% Sandstone circle with soul torches\\\\n\\\\n\",\"color\":\"reset\"},{\"text\":\"Fire Warp \",\"bold\":true},{\"text\":\"- 60%\\\\nEnd Rod +Lantern\\\\n\",\"color\":\"reset\"},{\"text\":\"(\",\"bold\":true,\"color\":\"dark_aqua\"},{\"text\":\"May reverse Ice or Snow Warp, brings back the sun)\",\"color\":\"dark_aqua\"},{\"text\":\"\\\\n\\\\n\",\"color\":\"reset\"},{\"text\":\"Ice Warp\",\"bold\":true},{\"text\":\" - 60% End Rod + Soul Lantern \",\"color\":\"reset\"},{\"text\":\"(10% Stun Chance)\",\"color\":\"dark_aqua\"},{\"text\":\"\\\\n\\\\n\",\"color\":\"reset\"},{\"text\":\"Space Warp\",\"bold\":true},{\"text\":\" - 20% Blackstone Wall Crying Obsidian\\\\n \",\"color\":\"reset\"}]','[\"\",{\"text\":\"Redstone Warp\",\"bold\":true},{\"text\":\" - 20% Redstone Block With Redstone Torch\\\\n\\\\n\",\"color\":\"reset\"},{\"text\":\"Mushroom Warp\",\"bold\":true},{\"text\":\" - 20% Red Mushroom Block circle with Shroomlight\\\\n\\\\n\",\"color\":\"reset\"},{\"text\":\"Ocean Warp\",\"bold\":true},{\"text\":\" - 10%\\\\nDried Kelp Block circle and turtle egg\\\\n\\\\n\",\"color\":\"reset\"},{\"text\":\"Book Warp\",\"bold\":true},{\"text\":\" - 10% Bookshelf + Lectern\\\\n \",\"color\":\"reset\"}]','[\"\",{\"text\":\"Void Warp\",\"bold\":true},{\"text\":\" - 5%\\\\nCoal Block circle and Black Banner\\\\n\\\\n\",\"color\":\"reset\"},{\"text\":\"Sculk Warp\",\"bold\":true},{\"text\":\" - 5%\\\\nSculk Block With Sculk Sensor\\\\n\\\\n\",\"color\":\"reset\"},{\"text\":\"Amethyst Warp\",\"bold\":true},{\"text\":\" - 5% Obsidian circle with Amethyst in the middle\\\\n\\\\n\",\"color\":\"reset\"},{\"text\":\"End Warp \",\"bold\":true},{\"text\":\"- 1% Endstone Brick cross in ground + Item Frame with Ender Eye\\\\n\\\\n\",\"color\":\"reset\"},{\"text\":\"Sky Warp\",\"bold\":true},{\"text\":\" - 1%\\\\nAmethyst circle and dragon egg \",\"color\":\"reset\"},{\"text\":\"(Can be reversed with Candles)\",\"color\":\"dark_aqua\"}]','[\"\",{\"text\":\"Attacks and counters\",\"bold\":true},{\"text\":\"\\\\n\\\\n\",\"color\":\"reset\"},{\"text\":\" \\\\u0020 \\\\u0020 \\\\u0020 \\\\u0020 \\\\u0020 \\\\u21d3\\\\u21d3\\\\u21d3\",\"bold\":true,\"color\":\"dark_aqua\"},{\"text\":\"\\\\n\\\\nAmethyst Block - Counters Honey Block\\\\n\\\\nBanner - Countered by Candle Circle or Snow Warp \",\"color\":\"reset\"},{\"text\":\"(Black Banner used for Void Warp)\",\"color\":\"dark_aqua\"},{\"text\":\"\\\\n\\\\nBee Nest - Attack countered by Honey Block\\\\n\\\\nBell - Counters Emerald Block and Dirt or the 3 hit combo\",\"color\":\"reset\"}]','[\"\",{\"text\":\"Black Carpet _ Counters Purple Wool \\\\n\"},{\"text\":\"(countered by Weathered Cut Copper Stairs)\",\"color\":\"dark_aqua\"},{\"text\":\"\\\\n\\\\nBlast Furnace - Counters Magma Block \",\"color\":\"reset\"},{\"text\":\"(countered by smelting Diamond Ore and Bowl)\",\"color\":\"dark_aqua\"},{\"text\":\"\\\\n\\\\nBlue Ice - Counters Magma Block\\\\n\\\\nBrain Coral - Counters Tube Coral \",\"color\":\"reset\"},{\"text\":\"(Attack countered by Iron Trapdoor or Horn Coral)\",\"color\":\"dark_aqua\"}]','[\"\",{\"text\":\"Bowl - Counters Furnace with meat, or Blast Furnace with Diamond Ore\\\\n\\\\nButton - Counters Fence Gate, Wooden Trapdoors, Metal Doors, Turtling \"},{\"text\":\"(countered by Piston if Button is on Fence Gate) \",\"color\":\"dark_aqua\"},{\"text\":\"\\\\n\\\\nCake - Gives 50 Luck\\\\n\\\\nCampfire - Attack countered by Barrel \",\"color\":\"reset\"},{\"text\":\"(Used with Torch to make Sun Warp)\",\"color\":\"dark_aqua\"},{\"text\":\"\\\\n\\\\nChest - Countered by Lime and Pink Candle\\\\n\\\\nCompost Bin - Attack countered by Trapdoor\",\"color\":\"reset\"}]','[\"\",{\"text\":\"Cornflower - Counters Flower Pot\\\\n\\\\nDripstone - Counters Fletching Table\\\\n\\\\nEmerald Block - Attracts players with Villager Egg \"},{\"text\":\"(countered by Bell; counters triple moss strat)\",\"color\":\"dark_aqua\"},{\"text\":\"\\\\n\\\\nFence Gate - Counters Verdant Froglight \",\"color\":\"reset\"},{\"text\":\"(countered by Button)\",\"color\":\"dark_aqua\"},{\"text\":\"\\\\n\\\\n \",\"color\":\"reset\"}]','{\"text\":\"Fire Coral - Attack countered by Pink Carpet\\\\n\\\\nFletching Table - Attack countered by Dripstone\\\\n\\\\nFlower Pot - Counters Pink Carpet or Purple Wool\\\\n\\\\nFurnace - Attack countered by smelting alot of combos\\\\n\\\\nGreen Carpet - Counters Orange Wool\"}','[\"\",{\"text\":\"Green Wool - Attack countered by Gold Block\\\\n\\\\nGold Block - Attack countered by Purple Block; counters Green Wool and Purple Block\\\\n\\\\nHoney Block - Counters Bee Nest; countered by Amethyst Block\\\\n\\\\nHorn Coral - Counters Brain CoralIron Trapdoor \"},{\"text\":\"(Counters Brain Coral, countered by Lever or button)\",\"color\":\"dark_aqua\"}]','{\"text\":\"Lever - Counters Iron Trapdoor, countered by Lightning Rod +\\\\n\\\\nTrident combo\\\\nLight Blue Carpet - Counters Orange Wool and White Wool\\\\n\\\\nLight Blue Wool - Attack countered by\\\\nLight Gray Carpet\\\\n\\\\nLight Gray Carpet - Counters Light Blue Wool\\\\n \"}','{\"text\":\"Lightning Rod - Can be used by placing one as a counter, or an attack on the player as a circle; Can only be used while raining; possible stunning\\\\n\\\\nMagma Block - Attack countered by Blast Furnace or Blue Ice\"}'],title:\"Block Battles Rule Book\",author:\"http://minecraft.tools/\",display:{Lore:[\"Rule set for the Block Battle minecraft minigame\"]}}");

	}




}