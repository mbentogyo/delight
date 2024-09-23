package dev.mbento.delight.gem;

import dev.mbento.delight.DelightMain;
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
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CooldownManager {
    @Getter public enum CooldownType {
        LEFT_CLICK(), RIGHT_CLICK(), ITEM(),
        ;private final Map<UUID, Instant> map;
        CooldownType(){ this.map = new ConcurrentHashMap<>(); }
    }

    /**
     * Adds a runnable task to be ran later
     * Its just BukkitScheduler.runTaskLater but easier
     * @param runnable the code
     * @param duration duration length in seconds
     */
    public static void runTaskLater(Runnable runnable, long duration){
        Bukkit.getScheduler().runTaskLater(DelightMain.getInstance(), runnable, duration *20);
    }

    /**
     * Adds a cooldown to a player
     * @param player the player
     * @param type the CooldownType
     * @param duration how long
     */
    public static void addCooldown(@NotNull OfflinePlayer player, @NotNull CooldownType type, @NotNull Duration duration){
        type.getMap().put(player.getUniqueId(), Instant.now().plus(duration));
    }

    /**
     * Removes the gem cooldowns of the player (LEFT_CLICK & RIGHT_CLICK)
     * @param player the player
     */
    public static void removePlayer(@NotNull OfflinePlayer player){
        CooldownType.LEFT_CLICK.getMap().remove(player.getUniqueId());
        CooldownType.RIGHT_CLICK.getMap().remove(player.getUniqueId());
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
        Map<UUID, Instant> map = type.getMap();
        if (map.containsKey(player.getUniqueId())){
            long time = Duration.between(Instant.now(), map.get(player.getUniqueId())).toSeconds();
            if (time > 0) return time;
        }

        return 0;
    }

    public static void tick(){
        for (Player player : Bukkit.getOnlinePlayers()){
            if (PlayerData.getGrade(player)) {
                ActionBarMain.sendToPlayer(player,
                        getRemainingTime(player, CooldownType.LEFT_CLICK),
                        PlayerData.getGem(player).getOffhandList().contains(player),
                        getRemainingTime(player, CooldownType.RIGHT_CLICK)
                );
            } else {
                ActionBarMain.sendToPlayer(player, PlayerData.getGem(player).getOffhandList().contains(player));
            }
        }
    }
}
