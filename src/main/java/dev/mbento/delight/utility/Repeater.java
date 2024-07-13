package dev.mbento.delight.utility;

import dev.mbento.delight.DelightMain;
import dev.mbento.delight.gem.CooldownManager;
import dev.mbento.delight.gem.GemsEnum;
import lombok.SneakyThrows;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class Repeater {
    public final List<BukkitTask> tasks;

    public Repeater(){
        this.tasks = new ArrayList<>();

        this.tasks.add(new BukkitRunnable() {
            @SneakyThrows
            @Override
            public void run() {
                for (GemsEnum gem : GemsEnum.values()) {
                    gem.getGem().tick();
                }

                CooldownManager.tick();

            }
        }.runTaskTimer(DelightMain.getInstance(), 0, 10));
    }

    /**
     * Stops all the tasks in Repeater
     */
    public void stop(){
        this.tasks.forEach(BukkitTask::cancel);
    }
}
