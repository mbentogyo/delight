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
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SwapEvents implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void inventoryClickEvent(InventoryClickEvent e) {
        ClickType clickType = e.getClick();
        Player player = (Player) e.getWhoClicked();
        Gem gem = PlayerData.getGem(player);
        int gemSlot = Utilities.getGemSlot(player);
        int clickedSlot = e.getSlot();

        ItemStack cursor = e.getCursor();
        if (cursor == null) return;

        ItemStack currentItem = e.getCurrentItem();
        if (currentItem == null) return;

        Inventory inventory = e.getClickedInventory();
        if (inventory == null) return;

        //OFFHAND
        if (clickType == ClickType.SWAP_OFFHAND) {
            if ((clickedSlot > 8 || inventory.getType() != InventoryType.PLAYER) && gemSlot == 40) {
                e.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You can only switch to your hotbar slots");
                return;
            }

            //Adding and removing to offhandList
            if (gemSlot == clickedSlot){
                gem.getOffhandList().add(player);
            } else if (clickedSlot <= 8 && gemSlot == 40){
                gem.getOffhandList().remove(player);
            }
        }

        //HOTBAR NUMBER KEY
        else if (clickType == ClickType.NUMBER_KEY){
            int hotbarSlot = e.getHotbarButton();
            if (((clickedSlot > 8 && clickedSlot != 40) || inventory.getType() != InventoryType.PLAYER) && gemSlot == hotbarSlot) {
                e.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You can only switch to other hotbar slots or offhand");
                return;
            }

            //Adding and removing to offhandList
            if (clickedSlot == 40 && gemSlot == hotbarSlot){
                gem.getOffhandList().add(player);
            } else if (clickedSlot == 40 && gemSlot == 40){
                gem.getOffhandList().remove(player);
            }
        } else {
            if (GemsEnum.getByItem(cursor) != null || GemsEnum.getByItem(currentItem) != null) {
                e.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You can only switch slots by swapping to offhand or to your hotbar");
                return;
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerSwapHandItemsEvent(PlayerSwapHandItemsEvent e){
        Player player = e.getPlayer();
        Gem gem = PlayerData.getGem(player);

        //Since this event is called before the actual swap, if the gem isn't at the gem slot, then it will be once the event is done.
        if (Utilities.getGemSlot(player) != 40) gem.getOffhandList().add(player);
        else gem.getOffhandList().remove(player);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerDropItemEvent(PlayerDropItemEvent e){
        if (GemsEnum.getByItem(e.getItemDrop().getItemStack()) != null) e.setCancelled(true);
    }
}
