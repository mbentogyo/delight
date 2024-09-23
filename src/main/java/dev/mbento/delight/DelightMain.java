package dev.mbento.delight;

import dev.mbento.delight.command.GiveItemCommand;
import dev.mbento.delight.command.SetGemCommand;
import dev.mbento.delight.command.UnbanPlayerCommand;
import dev.mbento.delight.event.BlockEvents;
import dev.mbento.delight.event.CommandEvents;
import dev.mbento.delight.event.DamageEvent;
import dev.mbento.delight.event.ItemEvents;
import dev.mbento.delight.event.PlayerEvents;
import dev.mbento.delight.event.SwapEvents;
import dev.mbento.delight.file.ConfigMain;
import dev.mbento.delight.item.ItemsEnum;
import dev.mbento.delight.utility.DelightConsole;
import dev.mbento.delight.utility.Repeater;
import dev.mbento.delight.utility.TrustedManager;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.atomic.AtomicInteger;

public final class DelightMain extends JavaPlugin {
    @Getter private static DelightMain instance;
    public static Repeater repeater;
    @Getter private static final AtomicInteger time = new AtomicInteger(0); // measured in ticks when the server starts
    public static final int LORE_DIVIDE_LENGTH = 30;
    public static final int PASSIVE_EFFECT_LENGTH = 35;

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
        PluginManager pluginManager = getServer().getPluginManager();

        pluginManager.registerEvents(new PlayerEvents(), this);
        pluginManager.registerEvents(new ItemEvents(), this);
        pluginManager.registerEvents(new SwapEvents(), this);
        pluginManager.registerEvents(new CommandEvents(), this);
        pluginManager.registerEvents(new DamageEvent(), this);
        pluginManager.registerEvents(new BlockEvents(), this);

        getCommand("setgem").setExecutor(new SetGemCommand());
        getCommand("bringback").setExecutor(new UnbanPlayerCommand());
        getCommand("giveitem").setExecutor(new GiveItemCommand());
    }

    @Override
    public void onEnable() {
        instance = this;

        //Initialization
        ConfigMain.initializeConfig();
        registerThings();
        ItemsEnum.loadItems();
        TrustedManager.initialize();
        repeater = new Repeater();

        DelightConsole.sendWithPrefix(ChatColor.GREEN + "Delight v" + instance.getDescription().getVersion() + " has loaded successfully.");
    }

    @Override
    public void onDisable() {
        repeater.stop();
    }
}
