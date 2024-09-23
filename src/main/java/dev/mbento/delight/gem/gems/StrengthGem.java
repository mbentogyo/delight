package dev.mbento.delight.gem.gems;

import dev.mbento.delight.DelightMain;
import dev.mbento.delight.gem.CooldownManager;
import dev.mbento.delight.gem.Gem;
import dev.mbento.delight.utility.TrustedManager;
import dev.mbento.delight.utility.Utilities;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StrengthGem extends Gem {
    private static final Set<PotionEffect> offhandEffects = Set.of(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, DelightMain.PASSIVE_EFFECT_LENGTH, 0, false, false, true));
    private static final Set<PotionEffect> badEffects = Set.of(new PotionEffect(PotionEffectType.WEAKNESS, 400, 0, false, true, true), new PotionEffect(PotionEffectType.WITHER, 200, 0, false, true, true));
    private static final Set<Player> leftPowerPlayers = new HashSet<>();
    private static final Map<Player, Integer> rightPowerPlayers = new HashMap<>();

    public StrengthGem() {
        super(
                ChatColor.DARK_RED + "Strength Gem",
                "strength_gem",
                "Frailer",
                "Gives the next enemy you hit Weakness I for 20s and Withering I for 10s. Skill expires after 30s.",
                "Chad Strength",
                "Tracks the critical hits you deal to your enemies. Get 1.5x damage at 4 crits. Get 2x damage at 9 crits. Skill expires after 9 crits or 30s.",
                "Stronk",
                "Grants Strength I"
        );
    }

    /**
     * Left click activation
     */
    public static void onPlayerAttack(Player user, Player attacked, EntityDamageByEntityEvent e) {
        if (TrustedManager.getTrustedPlayers(user).contains(attacked)) return;

        if (leftPowerPlayers.contains(user)) {
            user.sendMessage(ChatColor.GREEN + "You have applied Weakness I, Withering I to " + attacked.getName() + "!");
            attacked.sendMessage(ChatColor.RED + "You have been given Weakness I, Withering I due to the Strength Gem of " + user.getName() + "!");
            attacked.addPotionEffects(badEffects);
            leftPowerPlayers.remove(user);
        }

        if (rightPowerPlayers.containsKey(user)) {
            if (!Utilities.isCritical(user)) return;
            int count = rightPowerPlayers.get(user);
            count++;

            if (count == 9){
                user.sendMessage(ChatColor.GREEN + "Chad Strength Counter at 9. Damage is boosted by 2x. Skill has expired.");
                rightPowerPlayers.remove(user);
                e.setDamage(e.getDamage() * 2);
                return;
            } else if (count == 4){
                user.sendMessage(ChatColor.GREEN + "Chad Strength Counter at " + count + ". Damage is boosted by 1.5x.");
                e.setDamage(e.getDamage() * 1.5);
            } else {
                user.sendMessage(ChatColor.YELLOW + "Chad Strength Counter at " + count + ".");
            }

            rightPowerPlayers.put(user, count);
        }
    }

    /**
     * Left click expiring
     */
    public static void leftClickExpired(Player player){
        if (!leftPowerPlayers.contains(player)) return;
        if (player.isOnline()) player.sendMessage(ChatColor.RED + "You have not hit a player for 30s, your left click skill has expired.");
        leftPowerPlayers.remove(player);
    }

    /**
     * Right click expiring
     */
    public static void rightClickExpired(Player player){
        if (!rightPowerPlayers.containsKey(player)) return;
        if (player.isOnline()) player.sendMessage(ChatColor.RED + "Your right click skill has expired.");
        rightPowerPlayers.remove(player);
    }

    @Override
    public void onLeftClick(@NotNull Player player) {
        player.sendMessage(ChatColor.GREEN + "Frailer activated! The next enemy player you hit will get a debuff!");
        leftPowerPlayers.add(player);
        CooldownManager.runTaskLater(() -> leftClickExpired(player), 30);
        CooldownManager.addCooldown(player, CooldownManager.CooldownType.LEFT_CLICK, Duration.ofMinutes(2));
    }

    @Override
    public void onRightClick(@NotNull Player player) {
        player.sendMessage(ChatColor.GREEN + "Chad Strength activated! Every critical hit will now be tracked!");
        rightPowerPlayers.put(player, 0);
        CooldownManager.runTaskLater(() -> rightClickExpired(player), 30);
        CooldownManager.addCooldown(player, CooldownManager.CooldownType.RIGHT_CLICK, Duration.ofMinutes(2));
    }

    @Override
    public void tick() {
        for (Player player : getOffhandList()) player.addPotionEffects(offhandEffects);
    }
}
