package dev.mbento.delight.item.items;

import dev.mbento.delight.file.PlayerData;
import dev.mbento.delight.gem.CooldownManager;
import dev.mbento.delight.gem.Gem;
import dev.mbento.delight.gem.GemsEnum;
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

public class RouletteItem extends Item {
    public RouletteItem() {
        super(
                ChatColor.LIGHT_PURPLE + "Gem Roulette",
                "roulette",
                "Using this item will change your gem type. Will not change into the same gem type.",
                Material.ENCHANTING_TABLE,
                null,
                Arrays.asList("ABA", "BCB", "ABA"),
                Map.of('A', Material.EMERALD_BLOCK, 'B', Material.AMETHYST_BLOCK, 'C', Material.DIAMOND_BLOCK)
        );
    }

    @Override
    public void onUse(@NotNull Player player) {
        if (CooldownManager.hasCooldown(player, CooldownManager.CooldownType.ITEM)) {
            player.sendMessage(ChatColor.RED + "You can use this item again at " + CooldownManager.getRemainingTime(player, CooldownManager.CooldownType.ITEM) + " seconds.");
            return;
        }

        Gem gem = GemsEnum.pickRandomGem(PlayerData.getGem(player));
        PlayerData.setPlayer(player, Map.of("gem", gem.getId()));
        gem.create(player, null);
        player.playNote(player.getLocation(), Instrument.PLING, Note.natural(1, Note.Tone.C)); //WIP

        CooldownManager.addCooldown(player, CooldownManager.CooldownType.ITEM, Duration.ofSeconds(3));
        player.sendMessage(ChatColor.GREEN + "Your gem has turned into a " + gem.getName() + ChatColor.GREEN + "!");
        consumeItem(player);

    }
}
