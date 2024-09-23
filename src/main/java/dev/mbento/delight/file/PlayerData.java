package dev.mbento.delight.file;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dev.mbento.delight.DelightMain;
import dev.mbento.delight.gem.Gem;
import dev.mbento.delight.gem.GemsEnum;
import dev.mbento.delight.utility.DelightConsole;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
     * Get the JsonObject from a file
     * Null-check this for errors
     * @param file the file
     * @return the JsonObject, is nullable
     */
    @Nullable
    private static JsonObject getJsonFromFile(File file){
        try (FileReader fileReader = new FileReader(file)) {
            return new Gson().fromJson(fileReader, JsonObject.class);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Sets a player
     * @param player the player
     * @param data the map data, only put String, Character, Number, or Boolean
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void setPlayer(@NotNull OfflinePlayer player, @NotNull Map<String, Object> data) {
        File playerFile = new File(instance.getDataFolder(), "playerdata/" + player.getUniqueId() + ".json");
        JsonObject json = new JsonObject();
        if (playerFile.exists()) {
            json = getJsonFromFile(playerFile);

            if (json == null) {
                DelightConsole.sendError("File of " + player.getName() + " failed to read.");
                return;
            }
        } else {
            try {
                playerFile.createNewFile();
            } catch (IOException e) {
                DelightConsole.sendError("File of " + player.getName() + " failed to create.");
                return;
            }
        }

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof String) json.addProperty(key, (String) value);
            else if (value instanceof Character) json.addProperty(key, (Character) value);
            else if (value instanceof Number) json.addProperty(key, (Number) value);
            else if (value instanceof Boolean) json.addProperty(key, (Boolean) value);
            else {
                DelightConsole.sendError("Data of " + player.getName() + " contains invalid value.");
                throw new RuntimeException("Data of " + player.getName() + " contains invalid value.");
            }
        }

        try (FileWriter fileWriter = new FileWriter(playerFile)){
            Gson gson = new Gson();
            gson.toJson(json, fileWriter);
            playerMap.put(player, json);
        } catch (IOException e){
            DelightConsole.sendError("Failed to save player data for " + player.getName());
        }
    }

    /**
     * Sets a new player up with the default parameters
     * @param player the player
     */
    public static void setNewPlayer(@NotNull OfflinePlayer player){
        Map<String, Object> data = new HashMap<>();
        data.put("gem", GemsEnum.pickRandomGem().getId());
        data.put("lives", 5);
        data.put("is_pristine", false);
        setPlayer(player, data);
    }

    /**
     * Get a players data
     * @param player the player
     * @return the jsonObject data
     */
    @NotNull
    private static JsonObject getData(@NotNull OfflinePlayer player){
        File playerFile = new File(instance.getDataFolder(), "playerdata/" + player.getUniqueId() + ".json");

        if (!playerMap.containsKey(player)) {
            JsonObject json = getJsonFromFile(playerFile);
            if (json == null) {
                DelightConsole.sendError("File of " + player.getName() + " failed to read.");
                throw new RuntimeException("File of " + player.getName() + " failed to read.");
            }

            if (!json.has("gem") || !json.has("lives") || !json.has("is_pristine")) {
                setNewPlayer(player);
                return playerMap.get(player);
            } else {
                playerMap.put(player, json);
                return json;
            }
        }

        else return playerMap.get(player);
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
        return GemsEnum.getById(getData(player).get("gem").getAsString());
    }

    /**
     * Gets the amount of lives the player has
     * @param player the player
     * @return number of lives
     */
    public static Integer getLives(@NotNull OfflinePlayer player) {
        return getData(player).get("lives").getAsInt();
    }

    /**
     * Gets the grade of the player
     * @param player the player
     * @return true if the grade is pristine, false if not pristine
     */
    public static Boolean getGrade(@NotNull OfflinePlayer player) {
        return getData(player).get("is_pristine").getAsBoolean();
    }

    /**
     * Removes a player from the HashMap
     * @param player the player
     */
    public static void removePlayerFromMap(@NotNull OfflinePlayer player){
        playerMap.remove(player);
    }
}
