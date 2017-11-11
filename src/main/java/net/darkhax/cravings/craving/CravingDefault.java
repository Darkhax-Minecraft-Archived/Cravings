package net.darkhax.cravings.craving;

import net.minecraft.entity.player.EntityPlayer;

/**
 * This default implementation of ICraving provides default logic for handling potion effect
 * craving rewards and punishments. These rewards and punishments are potion based, and pulled
 * from random entries on a config file.
 */
public abstract class CravingDefault implements ICraving {

    @Override
    public void onCravingSatisfied (EntityPlayer player) {

    }

    @Override
    public void onCravingUnsatisifed (EntityPlayer player) {

    }
}