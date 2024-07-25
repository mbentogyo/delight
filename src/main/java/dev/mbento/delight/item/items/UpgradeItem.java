package dev.mbento.delight.item.items;

import dev.mbento.delight.file.PlayerData;
import dev.mbento.delight.gem.CooldownManager;
import dev.mbento.delight.gem.Gem;
import dev.mbento.delight.item.Item;
import org.bukkit.ChatColor;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;

public class UpgradeItem extends Item {
    public UpgradeItem() {
        super(
                ChatColor.GREEN + "Gem Upgrader",
                "upgrader",
                "Using this update will turn your " + ChatColor.RED + "Rough " + ChatColor.GRAY + "gem into a " + ChatColor.GREEN + "Pristine " + ChatColor.GRAY + "gem. Only works if lives is greater than 3.",
                Material.ANVIL,
                null,
                Arrays.asList("ABA", "CDC", "ABA"),
                Map.of('A', Material.GOLD_BLOCK, 'B', Material.DIAMOND_BLOCK, 'C', Material.NETHERITE_INGOT, 'D', Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE)
        );
    }

    @Override
    public void onUse(@NotNull Player player) {
        if (CooldownManager.hasCooldown(player, CooldownManager.CooldownType.ITEM)) {
            player.sendMessage(ChatColor.RED + "You can use this item again at " + CooldownManager.getRemainingTime(player, CooldownManager.CooldownType.ITEM) + " seconds.");
            return;
        }

        if (PlayerData.getGrade(player)){
            player.sendMessage(ChatColor.RED + "Your gem is already " + ChatColor.GREEN + "Pristine" + ChatColor.RED + "!");
            return;
        }

        int lives = PlayerData.getLives(player);
        if (lives < 4) {
            player.sendMessage(ChatColor.RED + "Lives must be above 3. You need " + (4 - lives) + " more lives to use this item.");
            return;
        }

        Gem gem = PlayerData.getGem(player);
        PlayerData.setPlayer(player, Map.of("is_pristine", true));
        gem.create(player, null);
        player.playNote(player.getLocation(), Instrument.PLING, Note.natural(1, Note.Tone.C)); //WIP

        CooldownManager.addCooldown(player, CooldownManager.CooldownType.ITEM, Duration.ofSeconds(3));
        player.sendMessage(ChatColor.GREEN + "Your gem grade is now Pristine!");
        consumeItem(player);
    }
}
