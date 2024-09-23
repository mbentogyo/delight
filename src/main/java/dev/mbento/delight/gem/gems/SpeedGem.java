package dev.mbento.delight.gem.gems;

import dev.mbento.delight.DelightMain;
import dev.mbento.delight.file.PlayerData;
import dev.mbento.delight.gem.CooldownManager;
import dev.mbento.delight.gem.Gem;
import dev.mbento.delight.utility.TrustedManager;
import dev.mbento.delight.utility.Utilities;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SpeedGem extends Gem {
    private static final Set<PotionEffect> offhandEffects = Set.of(new PotionEffect(PotionEffectType.SPEED, DelightMain.PASSIVE_EFFECT_LENGTH, 0, false, false, true), new PotionEffect(PotionEffectType.FAST_DIGGING, DelightMain.PASSIVE_EFFECT_LENGTH, 1, false, false, true));
    private static final Set<PotionEffect> badEffects = Set.of(new PotionEffect(PotionEffectType.SLOW, 600, 1, false, true, true), new PotionEffect(PotionEffectType.SLOW_DIGGING, 600, 2, false, true, true));
    private static final Set<PotionEffect> worseEffects = Set.of(new PotionEffect(PotionEffectType.SLOW, 600, 3, false, true, true), new PotionEffect(PotionEffectType.SLOW_DIGGING, 600, 2, false, true, true));
    private static final Set<PotionEffect> speedTrustedEffects = Set.of(new PotionEffect(PotionEffectType.SPEED, 400, 1, false, true, true));
    private static final Set<PotionEffect> speedPlayerEffects = Set.of(new PotionEffect(PotionEffectType.SPEED, 600, 1, false, true, true));
    private static final Set<Player> leftPowerPlayers = new HashSet<>();

    public SpeedGem() {
        super(
                ChatColor.YELLOW + "Speed Gem",
                "speed_gem",
                "Sloth's Sedative",
                "Gives the next enemy you hit an 80% chance to get Slowness II, Mining Fatigue III or 20% chance to get Slowness IV, Mining Fatigue I. Skill expires after 30s.",
                "Speed Storm",
                "Grants +0.7 attack speed and Speed II for 30s. Trusted players within a 20 block distance gets +0.4 attack speed and Speed II for 20s.",
                "Speedster",
                "Grants Speed I and Haste II."
        );
    }

    /**
     * Attack speed reset
     */
    @SuppressWarnings("ConstantConditions")
    public static void resetAttackSpeed(Player player){
        if (!player.isOnline()) return;

        if (player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).getBaseValue() != 4L) {
            player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4);
            player.sendMessage(ChatColor.RED + "Your attack speed boost has expired!");
        }
    }

    /**
     * Left click activation
     */
    public static void onPlayerAttack(Player user, Player attacked) {
        if (TrustedManager.getTrustedPlayers(user).contains(attacked)) return;
        if (!leftPowerPlayers.contains(user)) return;

        boolean isWorse = Utilities.randomChance(20);

        user.sendMessage(ChatColor.GREEN + "You have applied Slowness " + (isWorse ? "IV" : "II") + " and Mining Fatigue III to " + attacked.getName() + "!");
        attacked.sendMessage(ChatColor.RED + "You have been given Slowness " + (isWorse ? "IV" : "II") + " and Mining Fatigue III due to the Speed Gem of " + user.getName() + "!");

        attacked.addPotionEffects(isWorse ? worseEffects : badEffects);
        leftPowerPlayers.remove(user);
    }

    /**
     * Left click expiring
     */
    public static void leftClickExpired(Player player){
        if (!leftPowerPlayers.contains(player)) return;
        if (player.isOnline()) player.sendMessage(ChatColor.RED + "You have not hit a player for 30s, your left click skill has expired.");
        leftPowerPlayers.remove(player);
    }

    @Override
    public void onLeftClick(@NotNull Player player) {
        player.sendMessage(ChatColor.GREEN + "Sloth's Sedative activated! The next enemy player you hit will get a debuff!");
        leftPowerPlayers.add(player);
        CooldownManager.runTaskLater(() -> leftClickExpired(player), 30);
        CooldownManager.addCooldown(player, CooldownManager.CooldownType.LEFT_CLICK, Duration.ofMinutes(2));
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void onRightClick(@NotNull Player player) {
        player.sendMessage(ChatColor.GREEN + "Speed Storm activated! You and your teammates have received buffs!");
        World world = player.getWorld();

        //Player
        world.strikeLightningEffect(player.getLocation());
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4.7);
        player.addPotionEffects(speedPlayerEffects);
        CooldownManager.runTaskLater(() -> resetAttackSpeed(player), 30);

        //Trusted players
        List<Player> trustedPlayers = Utilities.getPlayersFromList(player.getNearbyEntities(10, 10, 10));
        trustedPlayers.retainAll(TrustedManager.getTrustedPlayers(player));

        for (Player trustedPlayer : trustedPlayers) {
            world.strikeLightningEffect(trustedPlayer.getLocation());
            trustedPlayer.addPotionEffects(speedTrustedEffects);

            trustedPlayer.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4.4);
            CooldownManager.runTaskLater(() -> resetAttackSpeed(trustedPlayer), 30);
            trustedPlayer.sendMessage(ChatColor.GREEN + "You have received +0.3 attack speed and Speed II for 20s from the Speed Gem ability of " + player.getDisplayName() + "!");
        }

        CooldownManager.addCooldown(player, CooldownManager.CooldownType.RIGHT_CLICK, Duration.ofMinutes(2));
    }

    @Override
    public void tick() {
        for (Player player : getOffhandList()) player.addPotionEffects(offhandEffects);
    }
}
