package dev.mbento.delight.gems.wealth;

import dev.mbento.delight.gems.Gem;
import org.bukkit.ChatColor;

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
    public void onLeftClick() {

    }

    @Override
    public void onRightClick() {

    }

    @Override
    public void onDrop() {

    }

    @Override
    public void passivePower() {

    }
}
