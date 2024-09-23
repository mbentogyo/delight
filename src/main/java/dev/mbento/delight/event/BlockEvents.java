package dev.mbento.delight.event;

import dev.mbento.delight.DelightMain;
import dev.mbento.delight.gem.GemsEnum;
import dev.mbento.delight.item.ItemsEnum;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class BlockEvents implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void brewEvent(BrewEvent e) {
        for (ItemStack item : e.getContents().getContents()){
            if (ItemsEnum.getByItem(item) == null) continue;
            e.setCancelled(true);
            return;
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerInteractAtEntityEvent(PlayerInteractAtEntityEvent e){
        Player player = e.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (GemsEnum.getByItem(item) == null) return;

        ItemEvents.playerInteractEvent(new PlayerInteractEvent(player, Action.RIGHT_CLICK_AIR, item, null, null));
        e.setCancelled(true);
        //player.sendMessage(ChatColor.RED + "You cannot do that!");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerInteractEntityEvent(PlayerInteractEntityEvent e){
        Player player = e.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (GemsEnum.getByItem(item) == null) return;

        ItemEvents.playerInteractEvent(new PlayerInteractEvent(player, Action.RIGHT_CLICK_AIR, item, null, null));
        e.setCancelled(true);
        //player.sendMessage(ChatColor.RED + "You cannot do that!");
    }
}
