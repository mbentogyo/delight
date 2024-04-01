package dev.mbento.delight.gems.puff;

import dev.mbento.delight.gems.Gem;
import org.bukkit.ChatColor;

public class PuffGem extends Gem {
    /**
     *-PRISTINE
     * No fall damage, double jump Auto Enchant Power 5 And punch 2and feather falling 4.
     *
     * -BREEZY BASH
     * Lifts player 35 blocks and smashesthem down with a high Velocity, Pushes Enemies Away From You
     *
     * -DASH
     * Dashes you in the direction you are looking
     * deals 2 hearts of damage regardless the protection to layers and mobs upon contact
     */
    public PuffGem() {
        super(
                ChatColor.WHITE + "Puff Gem",
                "puff_gem",
                "Breezy Bash",
                "Lift closest enemy 35 blocks up",
                "Dash",
                "Dashes you into the direction you're facing. Deals 2 hearts.",
                "Light-weight",
                "No fall damage, others idk");
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
