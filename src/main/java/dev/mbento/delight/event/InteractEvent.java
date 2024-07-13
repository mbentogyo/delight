package dev.mbento.delight.event;

import dev.mbento.delight.file.PlayerData;
import dev.mbento.delight.gem.CooldownManager;
import dev.mbento.delight.gem.GemsEnum;
import dev.mbento.delight.utility.Utilities;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class InteractEvent implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerInteractEvent(PlayerInteractEvent e){
        ItemStack item = e.getItem();
        Player player = e.getPlayer();
        Action action = e.getAction();

        if (item == null) return;

        //For gems
        if (GemsEnum.getByItem(item) != null) {
            if (Utilities.getGemSlot(player) == 40) return;
            if (PlayerData.getGrade(player)) {
                //Left Click
                if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
                    if (CooldownManager.hasCooldown(player, CooldownManager.CooldownType.LEFT_CLICK)) {
                        player.sendMessage(ChatColor.RED + "You are on cooldown. You may use your gem after " + CooldownManager.getRemainingTime(player, CooldownManager.CooldownType.LEFT_CLICK) + " seconds.");
                    } else PlayerData.getGem(player).onLeftClick(player);
                }

                //Right Click
                else if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
                    if (CooldownManager.hasCooldown(player, CooldownManager.CooldownType.RIGHT_CLICK)) {
                        player.sendMessage(ChatColor.RED + "You are on cooldown. You may use your gem after " + CooldownManager.getRemainingTime(player, CooldownManager.CooldownType.RIGHT_CLICK) + " seconds.");

                    } else PlayerData.getGem(player).onRightClick(player);
                }
            }
        }

        //TODO for items
    }
}
