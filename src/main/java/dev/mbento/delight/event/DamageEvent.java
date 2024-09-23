package dev.mbento.delight.event;

import dev.mbento.delight.file.PlayerData;
import dev.mbento.delight.gem.Gem;
import dev.mbento.delight.gem.gems.SpeedGem;
import dev.mbento.delight.gem.gems.StrengthGem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageEvent implements Listener {
    @EventHandler
    public void entityDamageByEntityEvent(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player damagedPlayer)) return;
        if (!(e.getDamager() instanceof Player attackerPlayer)) return;

        Gem gem = PlayerData.getGem(attackerPlayer);

        if (gem.getId().equals("speed_gem")) SpeedGem.onPlayerAttack(attackerPlayer, damagedPlayer);
        else if (gem.getId().equals("strength_gem")) StrengthGem.onPlayerAttack(attackerPlayer, damagedPlayer, e);
    }
}
