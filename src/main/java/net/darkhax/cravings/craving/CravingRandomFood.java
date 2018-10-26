package net.darkhax.cravings.craving;

import java.lang.reflect.Field;

import net.darkhax.bookshelf.lib.Constants;
import net.darkhax.bookshelf.util.StackUtils;
import net.darkhax.cravings.Cravings;
import net.darkhax.cravings.handler.ConfigurationHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

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

                final ItemFood food = (ItemFood) item;
                final PotionEffect effect = getPotion(food);
                
                if (!ConfigurationHandler.allowBadFood && effect != null && effect.getPotion().isBadEffect()) {
                    
                    continue;
                }
                
                for (ItemStack subItem : StackUtils.getAllItems(item)) {
                    
                    if (!ConfigurationHandler.allowFoodsWithNoValue && food.getHealAmount(subItem) <= 0) {
                        
                        continue;
                    }
                    
                    if (!ConfigurationHandler.allowFoodsWithNoSaturation && food.getSaturationModifier(subItem) <= 0) {
                        
                        continue;
                    }
                    
                    list.addAll(StackUtils.findVariations(item));
                }
            }
        }

        Cravings.LOG.info("Food list built. {} items found. Took {}ms", list.size(), System.currentTimeMillis() - time);
        return list;
    }
    
    private static Field foodEffectField = ReflectionHelper.findField(ItemFood.class, "potionId", "field_77851_ca");
    
    private static PotionEffect getPotion(ItemFood food) {
        
        try {
            
            return (PotionEffect) foodEffectField.get(food);
        }
        
        catch (Exception e) {
            
            return null;
        }
    }
}