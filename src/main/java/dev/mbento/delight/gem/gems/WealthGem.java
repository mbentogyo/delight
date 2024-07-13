package dev.mbento.delight.gem.gems;

import dev.mbento.delight.gem.CooldownManager;
import dev.mbento.delight.gem.Gem;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public class WealthGem extends Gem {
    /**
     * -PRISTINE
     * Even cheaper villager trades, Luck Auto enchants looting 3, fortune 3,
     * mending Get two more ore for every 3 ores mined 2x the netherite scrap w
     * hen taken out of furnaceChip more of your enemies durability (2X)
     *
     * -UNFORTUNATE
     * 1/3 Of actions canceled 40s, R21/2 Of actions canceled 40s
     *
     * -RICH RUSHDouble the ores, and double the mob drops for 5 minutes
     */
    public WealthGem() {
        super(
                ChatColor.GREEN + "Wealth Gem",
                "wealth_gem",
                "Unfortunate",
                "Make your enemies unlucky!",
                "Rich Rush",
                "Double mob drops and ores for 5 minutes",
                "Fortuna's Blessing",
                "Get lucky trades idk this is a test"
        );
    }

    @Override
    public void onLeftClick(@NotNull Player player) {
        player.sendMessage(getName() + " LEFT CLICK");
        CooldownManager.addCooldown(player, CooldownManager.CooldownType.LEFT_CLICK, Duration.ofSeconds(10));
    }

    @Override
    public void onRightClick(@NotNull Player player) {
        player.sendMessage(getName() + " RIGHT CLICK");
        CooldownManager.addCooldown(player, CooldownManager.CooldownType.RIGHT_CLICK, Duration.ofSeconds(10));
    }

    @Override
    public void tick() {

    }
}
