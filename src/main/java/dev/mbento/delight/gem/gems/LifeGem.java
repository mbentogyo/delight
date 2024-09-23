package dev.mbento.delight.gem.gems;

import dev.mbento.delight.gem.CooldownManager;
import dev.mbento.delight.gem.Gem;
import dev.mbento.delight.utility.DelightConsole;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

public class LifeGem extends Gem {
    private static Set<Vector> vectors = new HashSet<>();
    private static Set<Player> rightPowerPlayers = new HashSet<>();

    public LifeGem() {
        super(
                ChatColor.AQUA + "Life Gem",
                "life_gem",
                "Heart Drainer",
                "Removes 4 hearts from your nearest enemy for 20 seconds",
                "Circle of Life",
                "Summons a zone that heals trusted players",
                "Environmentalist",
                "Grants Saturation II, immunity to Withering, "
        );

        for (double i = 7.5; i <= 360; i+=7.5){
            double angleRadians = Math.toRadians(i);

            double newX = Math.cos(angleRadians);
            double newZ = Math.sin(angleRadians);

            Vector vector = new Vector(newX, 0, newZ);
            vector.normalize().multiply(4);
            vectors.add(vector);
        }
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

    public static void particles() {
        //for (Player player : getOffhandList()){
        //    World world = player.getWorld();
        //    Vector playerVector = player.getLocation().add(0, 0.2, 0).toVector();

        //    for (Vector vector : vectors){
        //        world.spawnParticle(Particle.VILLAGER_HAPPY, playerVector.clone().add(vector).toLocation(world), 1);
        //    }
        //}
    }
}
