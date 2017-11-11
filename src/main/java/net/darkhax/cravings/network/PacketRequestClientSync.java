package net.darkhax.cravings.network;

import net.darkhax.bookshelf.network.SerializableMessage;
import net.darkhax.cravings.handler.CravingDataHandler;
import net.darkhax.cravings.handler.CravingDataHandler.ICustomData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * A packet to request stages be synced to the client.
 */
public class PacketRequestClientSync extends SerializableMessage {

    public PacketRequestClientSync () {

    }

    @Override
    public IMessage handleMessage (MessageContext context) {

        final EntityPlayer player = context.getServerHandler().player;
        final ICustomData info = CravingDataHandler.getStageData(player);
        return new PacketSyncClient(info);
    }
}
