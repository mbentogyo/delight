package dev.mbento.delight.gem;

import dev.mbento.delight.file.PlayerData;
import dev.mbento.delight.utility.ActionBarMain;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CooldownManager {
    @Getter public enum CooldownType {
        LEFT_CLICK(), RIGHT_CLICK(), ITEM();
        private final Map<OfflinePlayer, Instant> map;
        CooldownType(){ this.map = new ConcurrentHashMap<>(); }
    }

    /**
     * Adds a cooldown to a player
     * @param player the player
     * @param type the CooldownType
     * @param duration how long
     */
    public static void addCooldown(@NotNull OfflinePlayer player, @NotNull CooldownType type, @NotNull Duration duration){
        type.getMap().put(player, Instant.now().plus(duration));
    }

    /**
     * Removes the cooldowns of the player
     * @param player the player
     */
    public static void removePlayer(@NotNull OfflinePlayer player){
        CooldownType.LEFT_CLICK.getMap().remove(player);
        CooldownType.RIGHT_CLICK.getMap().remove(player);
    }

    /**
     * Checks if the player has a cooldown
     * @param player the player
     * @param type the CooldownType
     * @return true if there is
     */
    public static Boolean hasCooldown(@NotNull OfflinePlayer player, @NotNull CooldownType type){
        return getRemainingTime(player, type) > 0;
    }

    /**
     * Gets the remaining time before the cooldown of a player ends
     * @param player the player
     * @param type the CooldownType
     * @return the remaining time (in seconds)
     */
    public static long getRemainingTime(@NotNull OfflinePlayer player, @NotNull CooldownType type){
        Map<OfflinePlayer, Instant> map = type.getMap();
        if (map.containsKey(player)){
            long time = Duration.between(Instant.now(), map.get(player)).toSeconds();
            if (time > 0) return time;
        }

        return 0;
    }

    public static void tick(){
        for (Player player : Bukkit.getOnlinePlayers()){
            if (PlayerData.getGrade(player)) {
                ActionBarMain.sendToPlayer(player,
                        !hasCooldown(player, CooldownType.LEFT_CLICK),
                        PlayerData.getGem(player).getOffhandList().contains(player),
                        !hasCooldown(player, CooldownType.RIGHT_CLICK)
                );
            } else {
                ActionBarMain.sendToPlayer(player, PlayerData.getGem(player).getOffhandList().contains(player));
            }
        }
    }
}
