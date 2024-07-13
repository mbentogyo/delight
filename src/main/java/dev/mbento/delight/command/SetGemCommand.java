package dev.mbento.delight.command;

import dev.mbento.delight.file.PlayerData;
import dev.mbento.delight.gem.CooldownManager;
import dev.mbento.delight.gem.Gem;
import dev.mbento.delight.gem.GemsEnum;
import dev.mbento.delight.utility.Utilities;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SetGemCommand implements TabExecutor {
    @Override
    @SuppressWarnings("ConstantConditions")
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        //Check if there's 4 arguments
        if (strings.length != 4){
            commandSender.sendMessage(ChatColor.RED + "Missing arguments!");
            return false;
        }

        //Check if the player exists
        OfflinePlayer player = Utilities.getPlayer(strings[0]);
        if (player == null) {
            commandSender.sendMessage(ChatColor.RED + "Player " + strings[0] + " does not exist.");
            return false;
        }

        //Check if the gem exists
        Gem gem = GemsEnum.getById(strings[1]);
        if (gem == null) {
            commandSender.sendMessage(ChatColor.RED + "Gem " + strings[1] + " does not exist.");
            return false;
        }

        //Check if the number of lives given is indeed a number
        int lives;
        try { lives = Integer.parseInt(strings[2]); }
        catch (NumberFormatException e){
            commandSender.sendMessage(ChatColor.RED + strings[2] + " is not a number.");
            return false;
        }

        //Check if the lives are within the allowed bounds
        if (lives > 5 || lives < 1){
            commandSender.sendMessage(ChatColor.RED + String.valueOf(lives) + " is outside the possible range. Lives can only be set from 1-5");
            return false;
        }

        //Check if the grade is rough or pristine
        String grade = strings[3];
        boolean isPristine;
        if (grade.equals("rough") || grade.equals("pristine")) {
            isPristine = grade.equals("pristine");
        } else {
            commandSender.sendMessage(ChatColor.RED + "Grade " + grade + " is not valid. Pick between Rough and Pristine.");
            return false;
        }

        //Check if the number of lives allow pristine
        if (isPristine && lives < 4){
            commandSender.sendMessage(ChatColor.RED + "Pristine cannot be applied to lives lower than 4.");
            return false;
        }

        //Get the player's old gem before rewriting with the new one
        Gem oldGem = PlayerData.getGem(player);
        PlayerData.setPlayer(player, gem, lives, isPristine);

        //Code for when the player is online (Too much explanation, but code is long and I had a headache because of this)
        if (player.isOnline()) {
            //Player object only becomes non null if player is online
            Player onlinePlayer = player.getPlayer();

            //Removes the player from the offhand list of the old gem
            oldGem.getOffhandList().remove(onlinePlayer);

            //Remove cooldown if the user is given a rough gem
            if (!isPristine) CooldownManager.removePlayer(player);

            //Not directly putting it in to assure it did change
            gem.create(onlinePlayer, PlayerData.getLives(player), PlayerData.getGrade(player), null);

            //If item is in offhand, then add player to the new gem's offhand list
            if (Utilities.getGemSlot(onlinePlayer) == 40) gem.getOffhandList().add(player.getPlayer());

        } else PlayerData.removePlayerFromMap(player);

        //Success message
        commandSender.sendMessage(
                ChatColor.GREEN + "Player " + player.getName() + " has been set with the following gem:" +
                ChatColor.GREEN + "\n  Gem: " + gem.getName() + ChatColor.RESET +
                ChatColor.GREEN + "\n  Lives: " + Utilities.getColorOfLives(lives) + ChatColor.RESET +
                ChatColor.GREEN + "\n  Grade: " + Utilities.getColorOfGrade(isPristine)
        );

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
            return GemsEnum.getGemIdList();
        }

        //Lives
        else if (strings.length == 3){
            return Arrays.asList("1", "2", "3", "4", "5");
        }

        //Grade
        else if (strings.length == 4){
            return Arrays.asList("rough", "pristine");
        }
        return null;
    }
}
