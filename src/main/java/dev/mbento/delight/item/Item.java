package dev.mbento.delight.item;

import dev.mbento.delight.DelightMain;
import dev.mbento.delight.utility.Utilities;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Getter
public abstract class Item {
    private final ItemStack item;
    private final String name;
    private final String id;

    @SuppressWarnings("ConstantConditions")
    public Item(@NotNull String name, @NotNull String id, @NotNull String lore, @NotNull Material material, @Nullable Color potionColor, @NotNull List<String> shape, @NotNull Map<Character, Material> ingredientMap) {
        ItemStack item = new ItemStack(material, 1);
        item.addUnsafeEnchantment(Enchantment.DEPTH_STRIDER, 1);

        //Meta
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.addItemFlags(ItemFlag.values());
        meta.setUnbreakable(true);

        List<String> itemLore = new ArrayList<>();
        itemLore.add(ChatColor.YELLOW + "" +ChatColor.BOLD + "RIGHT CLICK");
        itemLore.addAll(Utilities.splitStringByCharacters(lore, DelightMain.LORE_DIVIDE_LENGTH));
        meta.setLore(itemLore);

        if (material.toString().contains("POTION")){
            PotionMeta potionMeta = (PotionMeta) meta;
            potionMeta.setColor(potionColor);
            item.setItemMeta(potionMeta);
        } else item.setItemMeta(meta);

        Utilities.storeStringInItem(item, "id", id);

        //Recipe
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(DelightMain.getInstance(), id), item);
        recipe.shape(shape.get(0), shape.get(1), shape.get(2));
        for (Map.Entry<Character, Material> entry : ingredientMap.entrySet()) recipe.setIngredient(entry.getKey(), entry.getValue());
        Bukkit.addRecipe(recipe);

        this.item = item;
        this.name = name;
        this.id = id;
    }

    public void consumeItem(@NotNull Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!Objects.equals(Utilities.getStringFromItem(item, "id"), id)) item = player.getInventory().getItemInOffHand();

        item.setAmount(item.getAmount() - 1); //Not sure if this'll STILL cause a bug with timings and stuff, further testing required
    }

    public abstract void onUse(@NotNull Player player);
}
