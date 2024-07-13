package dev.mbento.delight.gem.gems;

import dev.mbento.delight.gem.CooldownManager;
import dev.mbento.delight.gem.Gem;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public class AstraGem extends Gem {
    /**
     * -PRISTINE
     * 15% Of attacks will phase
     * Through you. Absorbing souls will heal you 2.5 hearts if mob,
     * 5 if player.Capture 2 mobs inside your gem, releasing
     * them in the opposite order they were captured.
     *
     * DAGGERS
     * Shoot 5 daggers, if a player is hit with onethey take
     * 3 hearts of damage and their gem is disabled for 10 seconds
     * for every dagger they're hit withenter the body of that
     * player, exit by crouching or when that player recieves
     * damage. Leaving the body will haunt them disabling their gem for 10 seconds.
     *
     * ASTRAL PROJECTIONBecome a projection and explore in a 150
     * block Radius Spook players by left clicking with your gem,
     * radius or tagging them with right click.You exit the Astral
     * state if your npc is hit,or you click the air with an empty hand.
     */
    public AstraGem() {
        super(
                ChatColor.DARK_PURPLE + "Astra Gem",
                "astra_gem",
                "Daggers",
                "Summons 5 daggers that you can shoot with bare hands",
                "Astral Projection",
                "Idk I still haven't decided on what exactly to do soo",
                "Ghostly",
                "shits too long, I cba test test 1234"
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
