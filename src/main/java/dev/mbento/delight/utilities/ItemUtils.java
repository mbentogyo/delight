package dev.mbento.delight.utilities;

import dev.mbento.delight.DelightMain;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ItemUtils {

    /**
     * Store a string in your item
     * @param item the item
     * @param key the key for your string
     * @param value the string
     */
    public static void storeStringInItem(ItemStack item, String key, String value){
        NamespacedKey k = new NamespacedKey(DelightMain.getInstance(), key);

        if (item != null){
            if (item.hasItemMeta()){
                ItemMeta meta = item.getItemMeta();
                meta.getPersistentDataContainer().set(k, PersistentDataType.STRING, value);
                item.setItemMeta(meta);
            }
        }
    }

    /**
     * Store a number on your item
     * @param item the item
     * @param key the key for your number
     * @param value the number
     */
    public static void storeIntInItem(ItemStack item, String key, int value){
        NamespacedKey k = new NamespacedKey(DelightMain.getInstance(), key);

        if (item != null){
            if (item.hasItemMeta()){
                ItemMeta meta = item.getItemMeta();
                meta.getPersistentDataContainer().set(k, PersistentDataType.INTEGER, value);
                item.setItemMeta(meta);
            }
        }
    }

    /**
     * Get a string from an item
     * @param item the item
     * @param key the key for your string
     * @return the string if it exists, null if not
     */
    @Nullable
    public static String getStringFromItem(ItemStack item, String key){
        NamespacedKey k = new NamespacedKey(DelightMain.getInstance(), key);

        if (item == null){
            return null;
        }
        else if (!item.hasItemMeta()){
            return null;
        }
        else {
            PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
            return container.has(k, PersistentDataType.STRING) ? container.get(k, PersistentDataType.STRING) : null;
        }
    }

    /**
     * Get a number from an item
     * @param item the item
     * @param key the key for your number
     * @return the number if it exists, null if not
     */
    @Nullable
    public static Integer getIntFromItem(ItemStack item, String key){
        NamespacedKey k = new NamespacedKey(DelightMain.getInstance(), key);

        if (item == null){
            return null;
        }
        else if (!item.hasItemMeta()){
            return null;
        }
        else {
            PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
            return container.has(k, PersistentDataType.INTEGER) ? container.get(k, PersistentDataType.INTEGER) : null;
        }
    }

    /**
     * Split up a string into a list, with a minimum number of characters per line
     * @param text The string
     * @param minChar The minimum amount of characters per line
     * @return the list of string
     */
    public static List<String> splitStringByCharacters (String text, int minChar) {
        StringBuilder lineBuilder = new StringBuilder();
        List<String> list = new ArrayList<>();
        String[] words = text.split(" ");
        int currentIteration = 0;


        for (String word : words) {
            currentIteration++;
            lineBuilder.append(word).append(" ");

            if (lineBuilder.length() > minChar - 1 || currentIteration == words.length){
                list.add(lineBuilder.toString().trim() + "\n");
                lineBuilder.setLength(0);
            }
        }

        return list;
    }

    /**
     * Get the gem of a player
     * @param player the player
     * @return the gem, null if it can't find it (error)
     */
    @Nullable
    public static ItemStack getGem(Player player){
        PlayerInventory inventory = player.getInventory();
        ItemStack offHandItem = inventory.getItemInOffHand();
        ItemStack slotItem = inventory.getItem(8);

        ItemStack gem = offHandItem.getType() == Material.AIR ? slotItem : offHandItem;
        if (getStringFromItem(gem, "id") == null || gem.getType() == Material.AIR) {
            return null;
        }

        return gem;
    }

    /**
     * Checks if the gem of a player is in their offhand
     * @param player the player
     * @return true if it is, false if it isnt
     */
    public static boolean isGemInOffHand(Player player){
        PlayerInventory inventory = player.getInventory();
        ItemStack offHandItem = inventory.getItemInOffHand();
        ItemStack slotItem = inventory.getItem(8);
        boolean bool = offHandItem.getType() == Material.AIR;

        ItemStack gem = bool ? slotItem : offHandItem;
        if (getStringFromItem(gem, "id") == null || gem.getType() == Material.AIR) {
            throw new ClassCastException("Player somehow didn't have a gem");
            //Fix this part ngl
        }

        return !bool;
    }

    /**
     * Get the string (with color) on the lives of the player
     * @param lives the number of lives of a player
     * @return the string, returns null if its over 5 or below 1
     */
    @Nullable
    public static String getColorOfLives(int lives){
        switch (lives){
            case 1:
                return ChatColor.DARK_RED + "1";
            case 2:
                return ChatColor.RED + "2";
            case 3:
                return ChatColor.GOLD + "3";
            case 4:
                return ChatColor.YELLOW + "4";
            case 5:
                return ChatColor.GREEN + "5";
            default:
                return null;
        }
    }

    /**
     * Gets the color based on the grading of the gem
     * @param isUpgraded if the gem is pristine
     * @return the string (with color)
     */
    public static String getColorOfGrade(boolean isUpgraded){
        return isUpgraded? ChatColor.GREEN + "Pristine" : ChatColor.RED + "Rough";
    }
}
