package dev.mbento.delight;

import dev.mbento.delight.commands.SetGemCommand;
import dev.mbento.delight.configuration.ConfigMain;
import dev.mbento.delight.events.PlayerEvent;
import dev.mbento.delight.utilities.DelightConsole;
import dev.mbento.delight.utilities.Repeater;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.atomic.AtomicInteger;

public final class DelightMain extends JavaPlugin {
    @Getter private static DelightMain instance;
    private PluginManager pluginManager;
    public static Repeater repeater;
    @Getter private static final AtomicInteger time = new AtomicInteger(0); // measured in ticks when the server starts


    /**
     * For events
     * > pluginManager.registerEvents(new ClassThatExtendsListener(), this);
     * <p>
     * For commands
     * > getCommand("commandhere").setExecutor(new ClassThatExtendsTabExecutor());
     */
    @SuppressWarnings("ConstantConditions")
    private void registerThings(){
        pluginManager.registerEvents(new PlayerEvent(), this);

        getCommand("setgem").setExecutor(new SetGemCommand());
        getCommand("bringback").setExecutor(new SetGemCommand());
    }

    @Override
    public void onEnable() {
        instance = this;
        pluginManager = getServer().getPluginManager();

        ConfigMain.initializeConfig();

        registerThings();

        repeater = new Repeater();
        repeater.tasks.add(new BukkitRunnable(){
            @Override
            public void run() {
                time.getAndIncrement();
            }
        }.runTaskTimerAsynchronously(instance, 0, 0));

        DelightConsole.sendWithPrefix(ChatColor.GREEN + "Delight v" + instance.getDescription().getVersion() + " has loaded successfully.");
    }

    @Override
    public void onDisable() {
        repeater.stop();
    }
}
