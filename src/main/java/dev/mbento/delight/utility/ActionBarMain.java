package dev.mbento.delight.utility;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ActionBarMain {

    /**
     * Sends the action bar to a player
     * @param player the player
     * @param leftClick how many seconds are remaining (0 means ready)
     * @param rightClick how many seconds are remaining (0 means ready)
     * @param passiveActive if passive is currently activepassiveActive
     */
    public static void sendToPlayer(@NotNull Player player, long leftClick, boolean passiveActive, long rightClick) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(
                (leftClick == 0? ChatColor.GREEN + "READY" : ChatColor.RED + String.valueOf(leftClick))
                + ChatColor.WHITE + " | " +
                (passiveActive? ChatColor.GREEN + "ACTIVE" : ChatColor.RED + "INACTIVE")
                + ChatColor.WHITE + " | " +
                (rightClick == 0? ChatColor.GREEN + "READY" : ChatColor.RED + String.valueOf(rightClick))
        ));
    }

    /**
     * Sends the action bar to a player
     * @param player the player
     * @param passiveActive if passive is currently active
     */
    public static void sendToPlayer(@NotNull Player player, boolean passiveActive) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(
                passiveActive? ChatColor.GREEN + "ACTIVE" : ChatColor.RED + "INACTIVE"
        ));
    }
}
