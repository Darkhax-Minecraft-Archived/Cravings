package net.darkhax.cravings.craving;

import net.darkhax.cravings.handler.ConfigurationHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 * This default implementation of ICraving provides default logic for handling potion effect
 * craving rewards and punishments. These rewards and punishments are potion based, and pulled
 * from random entries on a config file.
 */
public abstract class CravingDefault extends IForgeRegistryEntry.Impl<ICraving> implements ICraving {

    @Override
    public void onCravingSatisfied (EntityPlayer player) {

        final PotionEffect effect = ConfigurationHandler.satisfiedEffects.get(player.getRNG().nextInt(ConfigurationHandler.satisfiedEffects.size()));
        player.addPotionEffect(new PotionEffect(effect));
    }

    @Override
    public void onCravingUnsatisifed (EntityPlayer player) {

        final PotionEffect effect = ConfigurationHandler.unsatisfiedEffects.get(player.getRNG().nextInt(ConfigurationHandler.unsatisfiedEffects.size()));
        player.addPotionEffect(new PotionEffect(effect));
    }
}