package dev.mbento.delight.gems;

import dev.mbento.delight.DelightMain;
import dev.mbento.delight.utilities.DelightConsole;
import dev.mbento.delight.utilities.ItemUtils;
import lombok.Getter;
import lombok.Value;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Getter
public abstract class Gem {
    private static final int LORE_DIVIDE_LENGTH = 20;

    private final String name;
    private final String id;
    private final String leftName;
    private final String leftLore;
    private final String rightName;
    private final String rightLore;
    private final String passiveName;
    private final String passiveLore;

    /**
     * Gem Constructor
     * @param name Name of the gem
     * @param id ID of the gem
     * @param leftName The name of the left power
     * @param leftLore The description for the left power
     * @param rightName The name of the right power
     * @param rightLore The description for the right power
     * @param passiveName The name of the passive power
     * @param passiveLore The description of the passive power
     */
    public Gem (@NotNull String name, @NotNull String id, @NotNull String leftName, @NotNull String leftLore, @NotNull String rightName, @NotNull String rightLore, @NotNull String passiveName, @NotNull String passiveLore){
        this.name = name;
        this.id = id;
        this.leftName = leftName;
        this.leftLore = leftLore;
        this.rightName = rightName;
        this.rightLore = rightLore;
        this.passiveName = passiveName;
        this.passiveLore = passiveLore;
    }

    /**
     * Creates the gem for a player
     * @param player the player
     */
    @SuppressWarnings("ConstantConditions")
    public void create(@NotNull Player player){
        ItemStack item = new ItemStack(Material.EMERALD, 1);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(this.name);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
        meta.setUnbreakable(true);

        //lore
        List<String> lore = new ArrayList<>(Arrays.asList(ChatColor.GRAY + "Lives: " + ItemUtils.getColorOfLives(5), ChatColor.GRAY + "Grade: " + ItemUtils.getColorOfGrade(false), "", ChatColor.AQUA + "Passive Power: " + this.passiveName, ChatColor.YELLOW + "" + ChatColor.BOLD + "OFFHAND"));
        for (String line : ItemUtils.splitStringByCharacters(this.passiveLore, LORE_DIVIDE_LENGTH)) lore.add(ChatColor.GRAY + line);
        lore.addAll(Arrays.asList("", ChatColor.AQUA + "First Power: " + this.leftName, ChatColor.YELLOW + "" +ChatColor.BOLD + "LEFT CLICK"));
        for (String line : ItemUtils.splitStringByCharacters(this.leftLore, LORE_DIVIDE_LENGTH)) lore.add(ChatColor.GRAY + line);
        lore.addAll(Arrays.asList("", ChatColor.AQUA + "Second Power: " + this.rightName, ChatColor.YELLOW + "" +ChatColor.BOLD + "RIGHT CLICK"));
        for (String line : ItemUtils.splitStringByCharacters(this.rightLore, LORE_DIVIDE_LENGTH)) lore.add(ChatColor.GRAY + line);

        meta.setLore(lore);
        item.setItemMeta(meta);

        ItemUtils.storeIntInItem(item, "lives", 5);
        ItemUtils.storeStringInItem(item, "is_upgraded", "false");
        ItemUtils.storeStringInItem(item, "id", this.id);
        ItemUtils.storeStringInItem(item, "preventstack", UUID.randomUUID().toString());

        player.getInventory().setItem(8, item);
    }

    /**
     * Creates a gem for an already existing player
     * Check if their lives are at 0, ban them.
     * Also check if they're already upgraded, don't double upgrade.
     * @param player the player
     * @param livesChanged how many lives you want to add/deduct (Do not make value cross 5 or go lower than 0)
     * @param doUpgrade if player will upgrade to pristine (Do not upgrade if player is already upgraded)
     */
    @SuppressWarnings("ConstantConditions")
    public void createExisting(@NotNull Player player, int livesChanged, boolean doUpgrade){
        ItemStack olditem = ItemUtils.getGem(player);

        boolean oldGrade = Boolean.getBoolean(ItemUtils.getStringFromItem(olditem, "is_upgraded"));
        int currentlives = ItemUtils.getIntFromItem(olditem, "lives") + livesChanged;

        // Theoretical errors, IT SHOULDN'T HAPPEN. DON'T MAKE IT HAPPEN.
        if (currentlives > 5 || currentlives < 0) {
            DelightConsole.sendError("Player " + player.getName() + " had more than 5 or less than 0 lives.");
            currentlives = 5;
        }
        if (doUpgrade && oldGrade) DelightConsole.sendError("Player " + player.getName() + " already has a Pristine gem and it cannot be upgraded once more.");


        ItemStack item = new ItemStack(Material.EMERALD, 1);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(this.name);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
        meta.setUnbreakable(true);

        //Lore
        List<String> lore = new ArrayList<>(Arrays.asList(ChatColor.GRAY + "Lives: " + ItemUtils.getColorOfLives(currentlives), ChatColor.GRAY + "Grade: " + ItemUtils.getColorOfGrade(doUpgrade || oldGrade), "", ChatColor.AQUA + "Passive Power: " + this.passiveName, ChatColor.YELLOW + "" + ChatColor.BOLD + "OFFHAND"));
        for (String line : ItemUtils.splitStringByCharacters(this.passiveLore, LORE_DIVIDE_LENGTH)) lore.add(ChatColor.GRAY + line);
        lore.addAll(Arrays.asList("", ChatColor.AQUA + "First Power: " + this.leftName, ChatColor.YELLOW + "" +ChatColor.BOLD + "LEFT CLICK"));
        for (String line : ItemUtils.splitStringByCharacters(this.leftLore, LORE_DIVIDE_LENGTH)) lore.add(ChatColor.GRAY + line);
        lore.addAll(Arrays.asList("", ChatColor.AQUA + "Second Power: " + this.rightName, ChatColor.YELLOW + "" +ChatColor.BOLD + "RIGHT CLICK"));
        for (String line : ItemUtils.splitStringByCharacters(this.rightLore, LORE_DIVIDE_LENGTH)) lore.add(ChatColor.GRAY + line);

        meta.setLore(lore);
        item.setItemMeta(meta);

        ItemUtils.storeIntInItem(item, "lives", currentlives);
        ItemUtils.storeStringInItem(item, "is_upgraded", String.valueOf(doUpgrade || oldGrade));
        ItemUtils.storeStringInItem(item, "id", this.id);
        ItemUtils.storeStringInItem(item, "preventstack", UUID.randomUUID().toString());

        if (ItemUtils.isGemInOffHand(player)) player.getInventory().setItemInOffHand(item);
        else player.getInventory().setItem(8, item);
    }

    public abstract void onLeftClick();
    public abstract void onRightClick();
    public abstract void onDrop();
    public abstract void passivePower();

}
