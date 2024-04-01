package dev.mbento.delight.events;

import dev.mbento.delight.gems.GemsEnum;
import dev.mbento.delight.utilities.DelightConsole;
import dev.mbento.delight.utilities.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerEvent implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        if (!player.hasPlayedBefore()) GemsEnum.pickRandomGem().create(player);
        ItemStack gemItem = ItemUtils.getGem(player);
        if (gemItem == null) {
            GemsEnum.pickRandomGem().create(player);
            DelightConsole.sendError(player + " has joined the server before and didn't have a gem.");
        }

        GemsEnum.pickRandomGem(GemsEnum.getByItem(gemItem)).createExisting(player, 0, false);
    }

}
