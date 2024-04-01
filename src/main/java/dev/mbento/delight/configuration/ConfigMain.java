package dev.mbento.delight.configuration;

import dev.mbento.delight.DelightMain;
import dev.mbento.delight.utilities.DelightConsole;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class ConfigMain {
    private static final DelightMain instance = DelightMain.getInstance();
    @Setter private static LinkedHashMap<String, Object> defaults;
    @Setter private static LinkedHashMap<String, List<String>> comments;

    /**
     * Initializes the config
     * Use DelightMain.getInstance().getConfig() to get the config.
     */
    public static void initializeConfig() {
        instance.saveDefaultConfig();

        File file = new File(instance.getDataFolder(), "config.yml");
        FileConfiguration config = instance.getConfig();
        setDefaults();
        setComments();

        // Updating config.yml
        checkForTabs(file);

        List<String> unincludedKeys = new ArrayList<>(defaults.keySet());

        if (unincludedKeys.removeAll(fixKeys(config.getKeys(true))) && !unincludedKeys.isEmpty()) {
            DelightConsole.sendWithPrefix(ChatColor.YELLOW + "config.yml is outdated. Fixing...");
            for (String key : unincludedKeys) {
                DelightConsole.sendWithPrefix(ChatColor.YELLOW + "added option \"" + key + "\"");
            }
        } else DelightConsole.sendWithPrefix(ChatColor.GREEN + "config.yml is up to date.");

        //Work around config.addDefaults, since addDefaults dont allow comments for some reason.
        for (Map.Entry<String, Object> map : defaults.entrySet()){
            if (unincludedKeys.contains(map.getKey())) config.set(map.getKey(), map.getValue());
        }

        for (Map.Entry<String, List<String>> comment : comments.entrySet()){
            if (comment.getKey().equals("header")) config.options().setHeader(comment.getValue());
            else config.setComments(comment.getKey(), comment.getValue());
        }

        // Saving
        instance.saveConfig();
    }

    /**
     * Sets the default values of the config.yml
     */
    private static void setDefaults(){
        LinkedHashMap<String, Object> defaults = new LinkedHashMap<>();

        defaults.put("dead-players", Collections.emptyList());
        defaults.put("astra.left.cooldown", 60);
        defaults.put("astra.right.cooldown", 60);

        defaults.put("fire.left.cooldown", 60);
        defaults.put("fire.right.cooldown", 60);

        defaults.put("life.left.cooldown", 60);
        defaults.put("life.right.cooldown", 60);

        defaults.put("puff.left.cooldown", 60);
        defaults.put("puff.right.cooldown", 60);

        defaults.put("speed.left.cooldown", 60);
        defaults.put("speed.right.cooldown", 60);

        defaults.put("strength.left.cooldown", 60);
        defaults.put("strength.right.cooldown", 60);

        defaults.put("wealth.left.cooldown", 60);
        defaults.put("wealth.right.cooldown", 60);

        setDefaults(defaults);
    }

    /**
     * Sets the comments of the config.yml
     */
    private static void setComments(){
        LinkedHashMap<String, List<String>> comments = new LinkedHashMap<>();

        comments.put("header", Arrays.asList("Delight Configuration file", "Pro tip: Use dual spaces instead of tab, tab will cause an error (its deliberate)"));
        comments.put("dead-players", Arrays.asList("These players are ones that have zero-ed out on their lives.", "If you want to bring back a player, just use the command /bringback <player>"));

        comments.put("astra", Collections.singletonList("Astra Gem"));
        comments.put("fire", Collections.singletonList("Fire Gem"));
        comments.put("life", Collections.singletonList("Life Gem"));
        comments.put("puff", Collections.singletonList("Puff Gem"));
        comments.put("speed", Collections.singletonList("Speed Gem"));
        comments.put("strength", Collections.singletonList("Strength Gem"));
        comments.put("wealth", Collections.singletonList("Wealth Gem"));

        comments.put("config-version", Collections.singletonList("Configuration version, it doesn't do anything other than to tell you what version your plugin is using."));

        setComments(comments);
    }

    /**
     * File checking for containing tabs
     * Spigot apparently suggests checking for tabs as it can cause errors
     * @param file file in question
     */
    private static void checkForTabs(File file){
        Scanner scanner = null;

        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(file.getAbsolutePath() + " has not been made. Scanner failed to read file."); //Shouldn't happen btw
        }

        //TODO: change the tabs automatically than making the user do it
        int row = 0;
        while (scanner.hasNextLine()){
            row++;
            String line = scanner.nextLine();

            if (line.startsWith("#")) continue;
            if (line.contains("\t")){
                throw new IllegalArgumentException(file.getAbsolutePath() + " has not been made. Tab found in file, line #" + row + ". Please replace this with 2 spaces.");
            }
        }
    }

    /**
     * Removes keys that are prefixes and retains the lowest children (idk)
     * @param set the key set
     * @return the fixed key set (in List)
     */
    private static List<String> fixKeys(Set<String> set){
        Set<String> toRemove = new HashSet<>();
        for (String value : set) {
            String[] parts = value.split("\\.");
            StringBuilder prefixBuilder = new StringBuilder();
            for (int i = 0; i < parts.length - 1; i++) {
                if (i > 0) {
                    prefixBuilder.append(".");
                }
                prefixBuilder.append(parts[i]);
                String prefix = prefixBuilder.toString();
                if (set.contains(prefix)) {
                    toRemove.add(prefix);
                }
            }
        }

        List<String> newSet = new ArrayList<>();

        for (String value : set) {
            if (!toRemove.contains(value)) {
                newSet.add(value);
            }
        }

        return newSet;
    }

}
