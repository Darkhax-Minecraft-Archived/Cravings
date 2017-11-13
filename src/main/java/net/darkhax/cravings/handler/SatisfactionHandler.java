package net.darkhax.cravings.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.darkhax.bookshelf.util.PlayerUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SatisfactionHandler {

    public static void onPlayerEatItem (EntityPlayer player, ItemStack stack) {

    }

    // LivingEntityUseItemEvent.Finish doesn't keep reference to the original item, so we need
    // to track a reference to it ourselves.
    private static final Map<UUID, ItemStack> FOOD_IN_USE = new HashMap<>();

    @SubscribeEvent
    public void onEntityStartUsing (LivingEntityUseItemEvent.Start event) {

        // Player has started eating. Track them in map.
        if (PlayerUtils.isPlayerReal(event.getEntityLiving()) && event.getItem().getItemUseAction() == EnumAction.EAT) {

            FOOD_IN_USE.put(event.getEntityLiving().getPersistentID(), event.getItem().copy());
        }
    }

    @SubscribeEvent
    public void onEntityStopUsing (LivingEntityUseItemEvent.Stop event) {

        // Player stopped eating without finishing. Stop tracking them.
        if (PlayerUtils.isPlayerReal(event.getEntityLiving()) && event.getItem().getItemUseAction() == EnumAction.EAT) {

            FOOD_IN_USE.remove(event.getEntityLiving().getPersistentID());
        }
    }

    @SubscribeEvent
    public void onItemUsed (LivingEntityUseItemEvent.Finish event) {

        // Player finished eating the item, and they are still being tracked.
        if (PlayerUtils.isPlayerReal(event.getEntityLiving())) {

            final ItemStack usedStack = FOOD_IN_USE.getOrDefault(event.getEntityLiving().getPersistentID(), ItemStack.EMPTY);

            if (!usedStack.isEmpty() && usedStack.getItemUseAction() == EnumAction.EAT) {

                onPlayerEatItem((EntityPlayer) event.getEntityLiving(), usedStack);
                FOOD_IN_USE.remove(event.getEntityLiving().getPersistentID());
            }
        }
    }
}