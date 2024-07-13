package dev.mbento.delight.utility;

import dev.mbento.delight.DelightMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DelightConsole {
    private static final ConsoleCommandSender console = Bukkit.getConsoleSender();
    private static final String prefix = ChatColor.AQUA + "[Delight]" + ChatColor.RESET + " ";

    /**
     * Send a message with [Delight] prefix
     * @param message the message
     */
    public static void sendWithPrefix(@NotNull String message){
        console.sendMessage(prefix + message);
    }

    /**
     * Send a message with [Delight] prefix
     * Sends a message per String in List
     * @param messages List of messages to send
     */
    public static void sendWithPrefix(@NotNull List<String> messages) {
        for (String message : messages) {
            console.sendMessage(prefix + message);
        }
    }

    /**
     * Sends an error on the console
     * @param message the message
     */
    public static void sendError(@NotNull String message){
        console.sendMessage(prefix + ChatColor.RED + "An error has occured! " + message);
        console.sendMessage(prefix + ChatColor.RED + "Please open a bug report here: " + ChatColor.WHITE + ChatColor.UNDERLINE + DelightMain.getInstance().getDescription().getWebsite());
    }
}
