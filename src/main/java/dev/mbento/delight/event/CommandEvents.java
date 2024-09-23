package dev.mbento.delight.event;

import dev.mbento.delight.file.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

public class CommandEvents implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerCommandPreprocessEvent(PlayerCommandPreprocessEvent e){
        String message = e.getMessage().trim();
        if (!message.startsWith("/clear")) return;

        Player player;
        message = message.substring(6).trim();
        if (message.isBlank()) player = e.getPlayer();
        else player = Bukkit.getPlayer(message);

        if (player == null) return;

        PlayerData.getGem(player).getOffhandList().remove(player);
        player.sendMessage(ChatColor.RED + "Your inventory has been cleared, and therefor your gem as well. Leave and rejoin to get your gem back.");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void serverCommandEvent(ServerCommandEvent e){
        String message = e.getCommand().trim();
        if (!message.startsWith("clear")) return;

        e.getSender().sendMessage(message);
        Player player = Bukkit.getPlayer(message.substring(5).trim());

        if (player == null) return;

        player.sendMessage(ChatColor.RED + "Your inventory has been cleared. Leave and rejoin to get your gem back.");
        PlayerData.getGem(player).getOffhandList().remove(player);
    }
}
