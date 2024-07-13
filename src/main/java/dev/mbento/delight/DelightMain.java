package dev.mbento.delight;

import dev.mbento.delight.command.SetGemCommand;
import dev.mbento.delight.command.UnbanPlayerCommand;
import dev.mbento.delight.event.InteractEvent;
import dev.mbento.delight.event.PlayerItemEvents;
import dev.mbento.delight.event.SwapEvents;
import dev.mbento.delight.file.ConfigMain;
import dev.mbento.delight.utility.DelightConsole;
import dev.mbento.delight.utility.Repeater;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.atomic.AtomicInteger;

public final class DelightMain extends JavaPlugin {
    @Getter private static DelightMain instance;
    private PluginManager pluginManager;
    public static Repeater repeater;
    @Getter private static final AtomicInteger time = new AtomicInteger(0); // measured in ticks when the server starts
    @Getter private static Plugin betterTeams;
    @Getter private static Plugin openInv;

    public static final ItemStack AIR = new ItemStack(Material.AIR, 1);


    /**
     * For events
     * > pluginManager.registerEvents(new ClassThatExtendsListener(), this);
     * <p>
     * For commands
     * > getCommand("commandhere").setExecutor(new ClassThatExtendsTabExecutor());
     */
    @SuppressWarnings("ConstantConditions")
    private void registerThings(){
        pluginManager.registerEvents(new PlayerItemEvents(), this);
        pluginManager.registerEvents(new InteractEvent(), this);
        pluginManager.registerEvents(new SwapEvents(), this);

        getCommand("setgem").setExecutor(new SetGemCommand());
        getCommand("bringback").setExecutor(new UnbanPlayerCommand());
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

        betterTeams = pluginManager.getPlugin("BetterTeams");
        openInv = pluginManager.getPlugin("OpenInv");

        if (betterTeams != null) DelightConsole.sendWithPrefix(ChatColor.GREEN + "BetterTeams plugin detected. Adding...");
        if (openInv != null) DelightConsole.sendWithPrefix(ChatColor.GREEN + "OpenInv plugin detected. Adding...");

        DelightConsole.sendWithPrefix(ChatColor.GREEN + "Delight v" + instance.getDescription().getVersion() + " has loaded successfully.");
    }

    @Override
    public void onDisable() {
        repeater.stop();
    }
}
