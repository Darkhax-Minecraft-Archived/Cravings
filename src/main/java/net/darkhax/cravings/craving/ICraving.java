package net.darkhax.cravings.craving;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * This interface is used to define a craving. It can be used to create a wide range of craving
 * effects.
 */
public interface ICraving {

    /**
     * Gets the food ItemStack being craved.
     *
     * @return The food being craved.
     */
    ItemStack getCravedItem ();

    /**
     * Called when the player manages to satisfy their craving.
     *
     * @param player The player who satisfied the craving.
     */
    void onCravingSatisfied (EntityPlayer player);

    /**
     * Called when the player fails to satisfy their craving.
     *
     * @param player The player who failed to satisfy the craving.
     */
    void onCravingUnsatisifed (EntityPlayer player);
}