package dev.mbento.delight.event;

import dev.mbento.delight.file.PlayerData;
import dev.mbento.delight.gem.Gem;
import dev.mbento.delight.gem.GemsEnum;
import dev.mbento.delight.utility.Utilities;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class PlayerItemEvents implements Listener {
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
            player.sendMessage(ChatColor.RED + "You broke your gem upgrade!");
            isPristine = false;
        }

        PlayerData.setPlayer(player, gem, lives, isPristine);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerRespawnEvent(PlayerRespawnEvent e){
        Player player = e.getPlayer();
        Gem gem = PlayerData.getGem(player);
        boolean hasNoKeepInventory = player.getInventory().isEmpty();

        gem.create(player, PlayerData.getLives(player), PlayerData.getGrade(player), hasNoKeepInventory? 8 : null);
        if (hasNoKeepInventory) gem.getOffhandList().remove(player);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerJoinEvent(PlayerJoinEvent e){
        Player player = e.getPlayer();

        if (PlayerData.hasPlayedBefore(player)) {
            PlayerData.getGem(player).create(player, PlayerData.getLives(player), PlayerData.getGrade(player), null);
            if (Utilities.getGemSlot(player) == 40) PlayerData.getGem(player).getOffhandList().add(player);
        }

        else {
            PlayerData.setNewPlayer(player);
            Gem gem = PlayerData.getGem(player);

            gem.create(player, PlayerData.getLives(player), PlayerData.getGrade(player), 8);

            player.sendMessage(ChatColor.GREEN + "You have been given the " + ChatColor.RESET + gem.getName());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void itemSpawnEvent(ItemSpawnEvent e){
        //Just a safeguard just in case someone does find a way to get gems improperly
        ItemStack item = e.getEntity().getItemStack();
        if (GemsEnum.getByItem(item) != null) e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerQuitEvent(PlayerQuitEvent e){
        Player player = e.getPlayer();
        PlayerData.getGem(player).getOffhandList().remove(player);
        PlayerData.removePlayerFromMap(player);
    }
}
