package dev.cregg.blockbattles;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;

public class DeckManager {
    public static HashMap<UUID, ItemStack[]> decks;
    public DeckManager() {
        decks = loadDecksFromFile();
    }

    private static HashMap<UUID, ItemStack[]> loadDecksFromFile() {
        HashMap<UUID, ItemStack[]> output = new HashMap<>();

        File dataFile = new File(Blockbattles.datapath, "decks.yml");

        FileConfiguration data = YamlConfiguration.loadConfiguration(dataFile);


        Set<String> keys = data.getKeys(false);
        for (String key:keys
        ) {
            List<ItemStack> itemsList = new ArrayList<>();
            for (Map<?, ?> item : data.getMapList(key)) { //Iterate across the List of Maps in the config
                itemsList.add(ItemStack.deserialize((Map<String, Object>) item)); //Add the deserialized ItemStack to your List
            }
            ItemStack[] array = new ItemStack[itemsList.size()];
            output.put(UUID.fromString(key), itemsList.toArray(array));
        }

        return output;
    }

}
