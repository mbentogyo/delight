package dev.mbento.delight.event;

import dev.mbento.delight.file.PlayerData;
import dev.mbento.delight.gem.Gem;
import dev.mbento.delight.gem.GemsEnum;
import dev.mbento.delight.utility.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Map;

public class PlayerEvents implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerDeathEvent(PlayerDeathEvent e) {
        Player player = e.getEntity();
        PlayerInventory inventory = player.getInventory();

        //Removes gem from dropping
        for (ItemStack item : inventory) {
            if (GemsEnum.getByItem(item) != null) e.getDrops().remove(item);
        }

        Gem gem = PlayerData.getGem(player);
        int lives = PlayerData.getLives(player);
        boolean isPristine = PlayerData.getGrade(player);

        lives--;

        //TODO banning or whatever
        if (lives <= 0){
            player.sendMessage(ChatColor.GREEN + "lol testing done");
            lives = 5;
        }

        //Gem upgrade breaking
        else if (lives <= 3){
            if (isPristine) player.sendMessage(ChatColor.RED + "You broke your gem upgrade!");
            isPristine = false;
        }

        PlayerData.setPlayer(player, Map.of("lives", lives, "is_pristine", isPristine));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerRespawnEvent(PlayerRespawnEvent e){
        Player player = e.getPlayer();
        Gem gem = PlayerData.getGem(player);
        boolean hasNoKeepInventory = player.getInventory().isEmpty();

        gem.create(player, hasNoKeepInventory? 8 : null);
        if (hasNoKeepInventory) gem.getOffhandList().remove(player);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerJoinEvent(PlayerJoinEvent e){
        Player player = e.getPlayer();

        if (PlayerData.hasPlayedBefore(player)) {
            PlayerData.getGem(player).create(player, null);
            if (Utilities.getGemSlot(player) == 40) PlayerData.getGem(player).getOffhandList().add(player);
        }

        else {
            PlayerData.setNewPlayer(player);
            Gem gem = PlayerData.getGem(player);

            gem.create(player, 8);

            player.sendMessage(ChatColor.GREEN + "You have been given the " + ChatColor.RESET + gem.getName());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerQuitEvent(PlayerQuitEvent e){
        Player player = e.getPlayer();
        PlayerData.getGem(player).getOffhandList().remove(player);
        PlayerData.removePlayerFromMap(player);
    }
}
