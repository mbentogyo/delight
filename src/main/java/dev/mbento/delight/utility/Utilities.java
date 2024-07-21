package dev.mbento.delight.utility;

import dev.mbento.delight.DelightMain;
import dev.mbento.delight.file.PlayerData;
import dev.mbento.delight.gem.GemsEnum;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Utilities {

    /**
     * Store a string in your item
     * @param item the item
     * @param key the key for your string
     * @param value the string
     */
    @SuppressWarnings("ConstantConditions")
    public static void storeStringInItem(@NotNull ItemStack item, @NotNull String key, @NotNull String value){
        NamespacedKey k = new NamespacedKey(DelightMain.getInstance(), key);

        if (!item.hasItemMeta()) return;

        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(k, PersistentDataType.STRING, value);
        item.setItemMeta(meta);
    }

    /**
     * Get a string from an item
     * @param item the item
     * @param key the key for your string
     * @return the string if it exists, null if not
     */
    @Nullable
    @SuppressWarnings("ConstantConditions")
    public static String getStringFromItem(ItemStack item, @NotNull String key){
        NamespacedKey k = new NamespacedKey(DelightMain.getInstance(), key);

        if (item == null) return null;
        if (!item.hasItemMeta()) return null;

        else {
            PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
            return container.has(k, PersistentDataType.STRING) ? container.get(k, PersistentDataType.STRING) : null;
        }
    }

    /**
     * Split up a string into a list, with a minimum number of characters per line
     * @param text The string
     * @param minChar The minimum amount of characters per line
     * @return the list of string
     */
    @NotNull
    public static List<String> splitStringByCharacters(@NotNull String text, int minChar) {
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
     * Checks if the gem of a player is in their offhand
     * @param player the player
     * @return slot number of the gem
     */
    @SuppressWarnings("ConstantConditions")
    @NotNull
    public static Integer getGemSlot(@NotNull Player player){
        PlayerInventory playerInventory = player.getInventory();

        int currentGemLocation = -1;
        for (int i = 0; i < playerInventory.getSize(); i++){
            if (GemsEnum.getByItem(playerInventory.getItem(i)) != null ) {
                if (i < 9 || i == 40){
                    if (currentGemLocation == -1) currentGemLocation = i;
                    else {
                        DelightConsole.sendError("Player " + player.getName() + " has more than 1 gems in their inventory. Removing...");
                        playerInventory.setItem(currentGemLocation, DelightMain.AIR);
                    }
                } else {
                    DelightConsole.sendError("Player " + player.getName() + " has their gem outside bounds. Removing...");
                    playerInventory.setItem(currentGemLocation, DelightMain.AIR);
                }
            }
        }

        if (currentGemLocation != -1) return currentGemLocation;
        else { //If gem isn't found, make a new one
            //Check hotbar for empty spaces
            for (int i = 0; i <= 8; i++){
                if (playerInventory.getItem(i).getType() == Material.AIR) {
                    PlayerData.getGem(player).create(player, PlayerData.getLives(player), PlayerData.getGrade(player), i);
                    return i;
                }
            }

            //Check offhand if empty
            if (playerInventory.getItem(40).getType() == Material.AIR) {
                PlayerData.getGem(player).create(player, PlayerData.getLives(player), PlayerData.getGrade(player), 40);
                return 40;
            }

            //Just replace 8th slot with the gem, its the player's fault lol
            PlayerData.getGem(player).create(player, PlayerData.getLives(player), PlayerData.getGrade(player), 8);
            return 8;
        }
    }

    /**
     * Get the string (with color) on the lives of the player
     * @param lives the number of lives of a player
     * @return the string, returns null if its over 5 or below 1
     */
    @Nullable
    public static String getColorOfLives(int lives){
        return switch (lives) {
            case 1 -> ChatColor.DARK_RED + "Brittle (1)";
            case 2 -> ChatColor.RED + "Weak (2)";
            case 3 -> ChatColor.GOLD + "Firm (3)";
            case 4 -> ChatColor.YELLOW + "Tough (4)";
            case 5 -> ChatColor.GREEN + "Strong (5)";
            default -> null;
        };
    }

    /**
     * Gets the color based on the grading of the gem
     * @param isPristine if the gem is pristine
     * @return the string (with color)
     */
    public static String getColorOfGrade(boolean isPristine){
        return isPristine? ChatColor.GREEN + "Pristine" : ChatColor.RED + "Rough";
    }

    /**
     * Searches all players and returns those that has the prefix
     * @param prefix the prefix
     * @return list of players with
     */
    @NotNull
    @SuppressWarnings("ConstantConditions")
    public static List<String> playerPartialSearch(@NotNull String prefix){
        List<String> list = new ArrayList<>();

        for (OfflinePlayer player : Bukkit.getOfflinePlayers()){
            String name = player.getName();
            if (name.startsWith(prefix)) list.add(name);
        }

        return list;
    }

    /**
     * Gets a player from a string
     * Work around with Bukkit.getOfflinePlayer(Player) being deprecated
     * @param name the player name
     * @return the player if the search was successful, null if not
     */
    @Nullable
    @SuppressWarnings("ConstantConditions")
    public static OfflinePlayer getPlayer(@NotNull String name){
        for (OfflinePlayer player : Bukkit.getOfflinePlayers()){
            if (player.getName().equals(name)) {
                return player;
            }
        }

        return null;
    }
}
