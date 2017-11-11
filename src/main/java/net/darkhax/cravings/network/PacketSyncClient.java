package net.darkhax.cravings.network;

import net.darkhax.bookshelf.network.SerializableMessage;
import net.darkhax.bookshelf.util.PlayerUtils;
import net.darkhax.cravings.handler.CravingDataHandler;
import net.darkhax.cravings.handler.CravingDataHandler.ICustomData;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This packet is used to sync all of the stages from the server to the client. It must first
 * be requested by having the client send a request packet.
 */
public class PacketSyncClient extends SerializableMessage {

    private ItemStack cravedItem = ItemStack.EMPTY;

    private int timeToSatisfy = 0;

    private int timeToNextAttempt = 0;

    public PacketSyncClient () {

    }

    public PacketSyncClient (ICustomData data) {

        this.cravedItem = data.getCravedItem();
        this.timeToSatisfy = data.getTimeToSatisfy();
        this.timeToNextAttempt = data.getTimeToNextAttempt();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IMessage handleMessage (MessageContext context) {

        Minecraft.getMinecraft().addScheduledTask( () -> {

            final EntityPlayer player = PlayerUtils.getClientPlayer();
            final ICustomData info = CravingDataHandler.getStageData(player);

            info.setCravedItem(this.cravedItem);
            info.setTimeToSatisfy(this.timeToSatisfy);
            info.setTimeToNextAttempt(this.timeToNextAttempt);
        });

        return null;
    }
}
