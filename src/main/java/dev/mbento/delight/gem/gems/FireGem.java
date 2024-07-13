package dev.mbento.delight.gem.gems;

import dev.mbento.delight.gem.CooldownManager;
import dev.mbento.delight.gem.Gem;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public class FireGem extends Gem {
    /**
     * Passives
     * -PRISTINE
     * Auto Smelting Longer Fire
     * Thorns Auto Enchant Flame & Fire Aspect 2
     *
     *
     * -FIREBALL
     * Spawns a fireball that charges up passively.when on fire blocks charge x2 faster.Deals more damage the more you charge it!
     *
     * -COZY CAMPFIRE
     * Spawns a campfire that heals 2 hearts and hunger a second in a 4 block radius
     */
    public FireGem() {
        super(
                ChatColor.RED + "Fire Gem",
                "fire_gem",
                "Fireball",
                "Spawns a fireball to the direction you're facing",
                "Cozy Campfire",
                "Spawn a campfire that heals trusted players",
                "Blazy",
                "idk I still haven't decided lololol"
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
