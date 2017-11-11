package net.darkhax.cravings.craving;

import net.darkhax.bookshelf.lib.Constants;
import net.darkhax.bookshelf.util.StackUtils;
import net.darkhax.cravings.Cravings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

/**
 * This craving is used as the default implementation.
 */
public class CravingRandomFood extends CravingDefault {

    /**
     * A list of all the food items that were found. This serves as a random pool of food items
     * to choose a food craving from.
     */
    public static final NonNullList<ItemStack> FOODS = getSomeFoods();

    public CravingRandomFood () {

    }

    @Override
    public ItemStack getCravedItem () {

        return FOODS.get(Constants.RANDOM.nextInt(FOODS.size()));
    }

    /**
     * Searches through the item registry for food items. Items are obtained by looking for
     * items that extend ItemFood, and variations are obtained by looking at
     * {@link Item#getSubItems(net.minecraft.creativetab.CreativeTabs, NonNullList)}.
     *
     * @return A list of all the found items.
     */
    private static NonNullList<ItemStack> getSomeFoods () {

        Cravings.LOG.info("Building list of food items.");

        final long time = System.currentTimeMillis();

        final NonNullList<ItemStack> list = NonNullList.create();

        for (final Item item : ForgeRegistries.ITEMS.getValues()) {

            if (item instanceof ItemFood) {

                list.addAll(StackUtils.findVariations(item));
            }
        }

        Cravings.LOG.info("Food list built. {} items found. Took {}ms", list.size(), System.currentTimeMillis() - time);
        return list;
    }
}