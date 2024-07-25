package dev.mbento.delight.item;

import dev.mbento.delight.item.items.EnergyItem;
import dev.mbento.delight.item.items.RouletteItem;
import dev.mbento.delight.item.items.UpgradeItem;
import dev.mbento.delight.utility.DelightConsole;
import dev.mbento.delight.utility.Utilities;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public enum ItemsEnum {
    ROULETTE(new RouletteItem()),
    UPGRADE(new UpgradeItem()),
    ENERGY(new EnergyItem())

    ;

    @NotNull private final Item item;

    ItemsEnum(@NotNull final Item item) {
        this.item = item;
    }

    @Nullable
    public static Item getById(@NotNull String id){
        for (ItemsEnum item : ItemsEnum.values()) {
            if (item.getItem().getId().equalsIgnoreCase(id)){
                return item.getItem();
            }
        }

        return null;
    }

    @Nullable
    public static Item getByItem(@NotNull ItemStack item){
        String id = Utilities.getStringFromItem(item, "id");
        if (id == null) return null;
        return getById(id);
    }

    @NotNull
    public static List<String> getItemIdList(){
        List<String> list = new ArrayList<>();

        for (ItemsEnum item : ItemsEnum.values()) list.add(item.getItem().getId());

        Collections.sort(list);
        return list;
    }

    public static void loadItems(){
        for (ItemsEnum item : ItemsEnum.values()) {
            DelightConsole.sendWithPrefix("\"" + item.getItem().getId() + "\" item loaded.");
        }
    }
}
