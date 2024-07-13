package dev.mbento.delight.file;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dev.mbento.delight.DelightMain;
import dev.mbento.delight.gem.Gem;
import dev.mbento.delight.gem.GemsEnum;
import dev.mbento.delight.utility.DelightConsole;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class PlayerData {
    private static final DelightMain instance = DelightMain.getInstance();
    private static final HashMap<OfflinePlayer, JsonObject> playerMap = new HashMap<>();

    /**
     * Checks if the player has played before by checking if their player file exists
     * @param player the player
     * @return true if the file exists, false if not
     */
    public static boolean hasPlayedBefore(@NotNull OfflinePlayer player){
        return new File(instance.getDataFolder(), "playerdata/" + player.getUniqueId() + ".json").exists();
    }

    /**
     * Sets the player's gem attributes
     * @param player the player
     * @param gem the gem type
     * @param lives how many lives (must be from 1-5)
     * @param isPristine if the grade will be pristine
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void setPlayer(@NotNull OfflinePlayer player, @NotNull Gem gem, int lives, boolean isPristine) {
        File playerFile = new File(instance.getDataFolder(), "playerdata/" + player.getUniqueId() + ".json");
        if (!playerFile.exists()) {
            try {
                playerFile.createNewFile();
            } catch (IOException e) {
                DelightConsole.sendError("File of " + player.getName() + " failed to create.");
                return;
            }
        }

        JsonObject json = new JsonObject();
        json.addProperty("gem", gem.getId());
        json.addProperty("lives", lives);
        json.addProperty("is_pristine", isPristine);

        try (FileWriter fileWriter = new FileWriter(playerFile)){
            Gson gson = new Gson();
            gson.toJson(json, fileWriter);
            if (player.isOnline()) playerMap.put(player, json);
        } catch (IOException e){
            DelightConsole.sendError("Failed to save player data for " + player.getName());
        }
    }

    /**
     * Sets a new player up with the default parameters
     * @param player the player
     */
    public static void setNewPlayer(@NotNull OfflinePlayer player) {
        setPlayer(player, GemsEnum.pickRandomGem(), 5, false);
    }

    /**
     * Gets the Json object of the player and puts it in a HashMap
     * Did this so it doesn't have to access the file for getting each attribute
     * @param player the player
     * @return the Json object
     */
    public static JsonObject getJson(@NotNull OfflinePlayer player) {
        File playerFile = new File(instance.getDataFolder(), "playerdata/" + player.getUniqueId() + ".json");
        if (!playerFile.exists()) setNewPlayer(player); //theoretically shouldn't happen but whatever

        if (!playerMap.containsKey(player)) {
            try (FileReader fileReader = new FileReader(playerFile)) {
                JsonObject jsonObject = new Gson().fromJson(fileReader, JsonObject.class);
                playerMap.put(player, jsonObject);
                fileReader.close();
                return jsonObject;
            } catch (IOException e) {
                DelightConsole.sendError("Failed to load player data for " + player.getName());
                return null;
            }
        } else {
            return playerMap.get(player);
        }
    }

    /**
     * Gets the player's gem type
     * If this goes null, you have bigger problems to deal with
     * @param player the player
     * @return the gem type
     */
    @NotNull
    @SuppressWarnings("ConstantConditions")
    public static Gem getGem(@NotNull OfflinePlayer player) {
        return GemsEnum.getById(getJson(player).get("gem").getAsString());
    }

    /**
     * Gets the amount of lives the player has
     * @param player the player
     * @return number of lives
     */
    @SuppressWarnings("ConstantConditions")
    public static Integer getLives(@NotNull OfflinePlayer player) {
        return getJson(player).get("lives").getAsInt();
    }

    /**
     * Gets the grade of the player
     * @param player the player
     * @return true if the grade is pristine, false if not pristine
     */
    @SuppressWarnings("ConstantConditions")
    public static Boolean getGrade(@NotNull OfflinePlayer player) {
        return getJson(player).get("is_pristine").getAsBoolean();
    }

    /**
     * Removes a player from the HashMap
     * @param player the player
     */
    public static void removePlayerFromMap(@NotNull OfflinePlayer player){
        playerMap.remove(player);
    }
}
