package dev.mbento.delight.gems.life;

import dev.mbento.delight.gems.Gem;
import org.bukkit.ChatColor;

public class LifeGem extends Gem {
    /**
     * Passive
     * Feed Animals And Grow Plants
     * By Crouch Clicking With Bare Hand, 3x DamageTo Undead Mobs, 2x Saturation.Immune to withering heal half a heart every 5 seconds 2 more hearts from Golden Apples.
     *
     *
     * -HEART DRAINER
     * Removes 4 Hearts 20s
     * Removes 2 Hearts R4, 1m
    *
     * -CIRCLE OF LIFE
     *
     * Summons a 8 block wide zone that follows you
     * Gives Trusted People +4 Hearts And Untrusted-4 Hearts
     * Slowly Mends Your Armor And Tools If Trusted Caster Only Can Steal Health While Zone is active
     * Gives Trusted Players +5 Hearts For 1m And 30 Seconds. (Radius 4)
     */
    public LifeGem() {
        super(
                ChatColor.LIGHT_PURPLE + "Life Gem",
                "life_gem",
                "Heart Drainer",
                "Removes 4 hearts from your nearest enemy for 20 seconds",
                "Circle of Life",
                "Summons a zone that heals trusted players",
                "Environmentalist",
                "Feed animals and grow plants by pressing right click with bare hands"
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
