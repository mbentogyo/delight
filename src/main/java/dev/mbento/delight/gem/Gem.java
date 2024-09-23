package dev.mbento.delight.gem;

import dev.mbento.delight.DelightMain;
import dev.mbento.delight.file.PlayerData;
import dev.mbento.delight.utility.Utilities;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Getter
public abstract class Gem {
    private final String name;
    private final String id;
    private final String leftName;
    private final String leftLore;
    private final String rightName;
    private final String rightLore;
    private final String passiveName;
    private final String passiveLore;
    private final Set<Player> offhandList = Collections.synchronizedSet(new HashSet<>());

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
     * @param slot what slot to put the gem to, null if you want it to replace an existing slot
     */
    @SuppressWarnings("ConstantConditions")
    public void create(@NotNull Player player, @Nullable Integer slot){
        int lives = PlayerData.getLives(player);
        boolean grade = PlayerData.getGrade(player);

        ItemStack item = new ItemStack(Material.EMERALD, 1);
        item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(this.name);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
        meta.setUnbreakable(true);

        //Starting Lore
        List<String> lore = new ArrayList<>(Arrays.asList(ChatColor.GRAY + "Fragility (Lives): " + Utilities.getColorOfLives(lives), ChatColor.GRAY + "Grade: " + Utilities.getColorOfGrade(grade), "", ChatColor.AQUA + "Passive Power: " + this.passiveName, ChatColor.YELLOW + "" + ChatColor.BOLD + "OFFHAND"));
        lore.addAll(Utilities.splitStringByCharacters(this.passiveLore, DelightMain.LORE_DIVIDE_LENGTH));

        if (grade){
            lore.addAll(Arrays.asList("", ChatColor.AQUA + "First Power: " + this.leftName, ChatColor.YELLOW + "" +ChatColor.BOLD + "LEFT CLICK"));
            lore.addAll(Utilities.splitStringByCharacters(this.leftLore, DelightMain.LORE_DIVIDE_LENGTH));
            lore.addAll(Arrays.asList("", ChatColor.AQUA + "Second Power: " + this.rightName, ChatColor.YELLOW + "" +ChatColor.BOLD + "RIGHT CLICK"));
            lore.addAll(Utilities.splitStringByCharacters(this.rightLore, DelightMain.LORE_DIVIDE_LENGTH));
        }

        meta.setLore(lore);
        item.setItemMeta(meta);

        Utilities.storeStringInItem(item, "id", this.id);
        Utilities.storeStringInItem(item, "is_upgraded", String.valueOf(grade));
        Utilities.storeStringInItem(item, "preventstack", UUID.randomUUID().toString());
        //Method call null check
        player.getInventory().setItem(Objects.requireNonNullElseGet(slot, () -> Utilities.getGemSlot(player)), item);
    }

    /**
     * Action done when gem is left clicked
     */
    public abstract void onLeftClick(@NotNull Player player);

    /**
     * Action done when gem is right clicked
     */
    public abstract void onRightClick(@NotNull Player player);

    /**
     * Action done when gem is in offhand
     * Done every 1/2 a second
     */
    public abstract void tick();

}
