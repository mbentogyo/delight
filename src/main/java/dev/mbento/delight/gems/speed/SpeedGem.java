package dev.mbento.delight.gems.speed;

import dev.mbento.delight.gems.Gem;
import org.bukkit.ChatColor;

public class SpeedGem extends Gem {
    /**
     * Passive:
     *  Speed 3, Dophin's Grace,Immune To Soul Sand, Auto Enchants Tools To EfficIency 5
     *
     * Left Click: Sloth's Sedative
     *  Level 1: Slowness 2, Mining Fatigue 3, Clears Speed Effects, 0,8x Slower Crits, 30s R4
     *  Level 2: Slowness 4, Mining Fatigue 3, Clears Speed Effects, 0,5x Slower Crits, 40s R4
     *
     * Right Click: Speed Storm
     *  Spawns A Thunder Storm With Lighting Strikes (just purely for effects)
     *  The Caster Gets constant 1.5x faster crits while the storm is active and speed 3 while in the storm
     */

    public SpeedGem() {
        super(
                ChatColor.YELLOW + "Speed Gem",
                "speed_gem",
                "Sloth's Sedative",
                "Gives the nearest enemy multiple debuffs",
                "Speed Storm",
                "Get zapped by lightning that gives you 1.5x more damage",
                "Speedster",
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
