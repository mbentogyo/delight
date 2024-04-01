package dev.mbento.delight.utilities;

import dev.mbento.delight.DelightMain;
import lombok.SneakyThrows;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class Repeater {
    public List<BukkitTask> tasks;

    public Repeater(){
        this.tasks = new ArrayList<>();

        this.tasks.add(new BukkitRunnable() {
            @SneakyThrows
            @Override
            public void run() {

            }
        }.runTaskTimer(DelightMain.getInstance(), 0, 5));
    }

    public void stop(){
        this.tasks.forEach(BukkitTask::cancel);
    }
}
