package dev.mbento.delight.item.items;

import dev.mbento.delight.file.PlayerData;
import dev.mbento.delight.gem.CooldownManager;
import dev.mbento.delight.gem.Gem;
import dev.mbento.delight.item.Item;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;

public class EnergyItem extends Item {
    public EnergyItem() {
        super(
                ChatColor.AQUA + "Energy Bottle",
                "energy",
                "Gives you an additional life when used.",
                Material.POTION,
                Color.AQUA,
                Arrays.asList("ABA", "BCB", "ABA"),
                Map.of('A', Material.DIAMOND, 'B', Material.LAPIS_BLOCK, 'C', Material.GLASS_BOTTLE)
        );
    }

    @Override
    public void onUse(@NotNull Player player) {
        if (CooldownManager.hasCooldown(player, CooldownManager.CooldownType.ITEM)) {
            player.sendMessage(ChatColor.RED + "You can use this item again at " + CooldownManager.getRemainingTime(player, CooldownManager.CooldownType.ITEM) + " seconds.");
            return;
        }

        int lives = PlayerData.getLives(player);
        if (lives == 5) {
            player.sendMessage(ChatColor.RED + "You cannot have more than 5 lives!");
            return;
        }

        Gem gem = PlayerData.getGem(player);
        PlayerData.setPlayer(player, Map.of("lives", ++lives));
        gem.create(player, null);
        player.playNote(player.getLocation(), Instrument.PLING, Note.natural(1, Note.Tone.C)); //WIP

        CooldownManager.addCooldown(player, CooldownManager.CooldownType.ITEM, Duration.ofSeconds(3));
        player.sendMessage(ChatColor.GREEN + "Your have gained an additional life! You now have " + lives + " lives");
        consumeItem(player);
    }
}
