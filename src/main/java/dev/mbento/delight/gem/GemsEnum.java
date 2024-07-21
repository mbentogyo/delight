package dev.mbento.delight.gem;

import dev.mbento.delight.gem.gems.AstraGem;
import dev.mbento.delight.gem.gems.FireGem;
import dev.mbento.delight.gem.gems.LifeGem;
import dev.mbento.delight.gem.gems.PuffGem;
import dev.mbento.delight.gem.gems.SpeedGem;
import dev.mbento.delight.gem.gems.StrengthGem;
import dev.mbento.delight.gem.gems.WealthGem;
import dev.mbento.delight.utility.Utilities;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
public enum GemsEnum {

    ASTRA_GEM(new AstraGem()),
    FIRE_GEM(new FireGem()),
    LIFE_GEM(new LifeGem()),
    PUFF_GEM(new PuffGem()),
    SPEED_GEM(new SpeedGem()),
    STRENGTH_GEM(new StrengthGem()),
    WEALTH_GEM(new WealthGem())

    ;

    @NotNull private final Gem gem;

    GemsEnum(@NotNull Gem gem){
        this.gem = gem;
    }

    /**
     * Returns the GemsEnum entry of the gem you enter
     * @param id the id
     * @return GemsEnum entry, null if none found
     */
    @Nullable
    public static Gem getById(@NotNull String id){
        for (GemsEnum gemsEnum : GemsEnum.values()){
            if (gemsEnum.getGem().getId().equalsIgnoreCase(id)) {
                return gemsEnum.getGem();
            }
        }

        return null;
    }

    /**
     * Returns the GemsEnum entry of the gem you enter
     * @param item the item/gem
     * @return GemsEnum entry, null if none found
     */
    @Nullable
    public static Gem getByItem(@Nullable ItemStack item){
        String id = Utilities.getStringFromItem(item, "id");
        if (id == null) return null;
        return getById(id);
    }

    /**
     * Gets the number of gems that exists
     * @return the number of gems
     */
    public static int getNumberOfGems(){
        return GemsEnum.values().length;
    }

    /**
     * Picks a random gem from existing list
     * @return a randomly picked gem
     */
    public static Gem pickRandomGem(){
        Random random = new Random();
        GemsEnum[] gems = GemsEnum.values();
        return gems[random.nextInt(getNumberOfGems())].getGem();
    }

    /**
     * Picks a random gem from existing list
     * Wont return the gem you've excluded
     * @param excluded excluded gem
     * @return a randomly picked gem
     */
    public static Gem pickRandomGem(@NotNull Gem excluded){
        Random random = new Random();
        GemsEnum[] gems = GemsEnum.values();
        while (true) {
            Gem gem = gems[random.nextInt(getNumberOfGems())].getGem();
            if (gem != excluded) return gem;
        }
    }

    public static List<String> getGemIdList(){
        List<String> list = new ArrayList<>();

        for (GemsEnum gemsEnum : GemsEnum.values()){
            list.add(gemsEnum.getGem().getId());
        }

        return list;
    }
}
