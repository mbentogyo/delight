package dev.mbento.delight.command;

import dev.mbento.delight.item.Item;
import dev.mbento.delight.item.ItemsEnum;
import dev.mbento.delight.utility.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class GiveItemCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length < 2 || strings.length > 3){
            commandSender.sendMessage(ChatColor.RED + "Missing arguments!");
            return false;
        }

        Player player = Bukkit.getPlayer(strings[0]);
        if (player == null) {
            commandSender.sendMessage(ChatColor.RED + "Player " + strings[0] + " is not online.");
            return false;
        }

        Item item = ItemsEnum.getById(strings[1]);
        if (item == null){
            commandSender.sendMessage(ChatColor.RED + "Item " + strings[1] + " does not exist.");
            return false;
        }

        int amount;
        if (strings.length == 3){
            try { amount = Integer.parseInt(strings[2]); }
            catch (NumberFormatException e){
                commandSender.sendMessage(ChatColor.RED + strings[2] + " is not a number.");
                return false;
            }
        } else amount = 1;

        ItemStack itemStack = item.getItem().clone();
        itemStack.setAmount(amount);
        player.getInventory().addItem(itemStack);

        commandSender.sendMessage(ChatColor.WHITE + "Gave " + amount + " [" + ChatColor.stripColor(item.getName()) + "] to " + player.getName());
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        //Players
        if (strings.length == 1){
            List<String> playerList = Utilities.playerPartialSearch(strings[0]);
            if (playerList.isEmpty()) return Collections.singletonList("<player>");
            else return playerList;
        }

        //Gem List
        else if (strings.length == 2){
            return ItemsEnum.getItemIdList();
        }

        return List.of();
    }
}
