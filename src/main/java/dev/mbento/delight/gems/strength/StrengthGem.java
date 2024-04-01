package dev.mbento.delight.gems.strength;

import dev.mbento.delight.gems.Gem;
import org.bukkit.ChatColor;

public class StrengthGem extends Gem {
    /**
     * Strength gem
     *
     * Passives
     * - PRISTINE
     * Strength 2
     *
     * Auto enchants to Sharpness V //change
     *
     * Powers
     * - FRAILER
     * Clears the enemy's potion effects
     * Gives weakness one for 20 seconds
     * Gives withering for 40 seconds
     *
     * - CHAD STRENGTH
     * Every 3 Crits charges a hit that deals 2x the DMG
     * every 8 crit charges a hit that deals 2x the DMG
     */
    public StrengthGem() {
        super(
                ChatColor.DARK_RED + "Strength Gem",
                "strength_gem",
                "Frailer",
                "Removes the nearest enemy's potion effects and give them {insert potion}",
                "Chad Strength",
                "In every 3 crits, deal some damage idk",
                "Stronk",
                "Gives {insert potion}"
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
