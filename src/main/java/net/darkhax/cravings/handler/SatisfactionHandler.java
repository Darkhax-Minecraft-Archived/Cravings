package net.darkhax.cravings.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.darkhax.bookshelf.util.MathsUtils;
import net.darkhax.bookshelf.util.PlayerUtils;
import net.darkhax.bookshelf.util.StackUtils;
import net.darkhax.cravings.Cravings;
import net.darkhax.cravings.craving.ICraving;
import net.darkhax.cravings.handler.CravingDataHandler.ICustomData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class SatisfactionHandler {

    public static void onPlayerEatItem (EntityPlayer player, ItemStack stack) {

        final ICustomData data = CravingDataHandler.getStageData(player);

        // Player has satisfied their craving!
        if (data.getCraving() != null && StackUtils.areStacksSimilar(data.getCravedItem(), stack)) {

            data.getCraving().onCravingSatisfied(player);
            player.sendMessage(new TextComponentTranslation("cravings.info.success", data.getCravedItem().getDisplayName()));
            data.resetCravings();
        }
    }

    @SubscribeEvent
    public void onLivingTick (TickEvent.PlayerTickEvent event) {

        if (PlayerUtils.isPlayerReal(event.player)) {

            final ICustomData data = CravingDataHandler.getStageData(event.player);

            // Player has a craving. Tick it down.
            if (data.getCraving() != null && !data.getCravedItem().isEmpty()) {

                // Tick down
                data.setTimeToSatisfy(data.getTimeToSatisfy() - 1);

                // Player has not satisfied the craving.
                if (data.getTimeToSatisfy() <= 0) {

                    data.getCraving().onCravingUnsatisifed(event.player);
                    event.player.sendMessage(new TextComponentTranslation("cravings.info.failed", data.getCravedItem().getDisplayName()));
                    data.resetCravings();
                }
            }

            // Player doesn't have a craving. Tick the attempt tracker.
            else {

                data.setTimeToNextAttempt(data.getTimeToNextAttempt() - 1);

                // Player has ticked down enough to get a craving
                if (data.getTimeToNextAttempt() <= 0) {

                    data.resetCravings();

                    // Player has a chance to get a craving.
                    if (MathsUtils.tryPercentage(ConfigurationHandler.cravingChance)) {

                        final ICraving craving = Cravings.CRAVING_REGISTRY.getRandomEntry().getEntry();
                        data.setCraving(craving);
                        data.setCravedItem(craving.getCravedItem());
                        data.setTimeToSatisfy(ConfigurationHandler.timeToSatisfy);
                    }
                }
            }
        }
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