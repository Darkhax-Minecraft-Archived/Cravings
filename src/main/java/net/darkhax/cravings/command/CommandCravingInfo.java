package net.darkhax.cravings.command;

import net.darkhax.bookshelf.command.Command;
import net.darkhax.bookshelf.util.MathsUtils;
import net.darkhax.cravings.handler.CravingDataHandler;
import net.darkhax.cravings.handler.CravingDataHandler.ICustomData;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandCravingInfo extends Command {

    @Override
    public String getName () {

        return "info";
    }

    @Override
    public int getRequiredPermissionLevel () {

        return 0;
    }

    @Override
    public String getUsage (ICommandSender sender) {

        return "commands.cravings.info.usage";
    }

    @Override
    public void execute (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        if (sender instanceof EntityPlayer) {

            final ICustomData data = CravingDataHandler.getStageData((EntityPlayer) sender);

            if (data.getCravedItem().isEmpty()) {

                sender.sendMessage(new TextComponentTranslation("commands.cravings.info.empty"));
                sender.sendMessage(new TextComponentString("Next craving attempt in: " + MathsUtils.ticksToTime(data.getTimeToNextAttempt())));
                return;
            }

            else {

                sender.sendMessage(new TextComponentString("You have a craving for " + data.getCravedItem().getDisplayName()));
                sender.sendMessage(new TextComponentString("Time to satisfy: " + MathsUtils.ticksToTime(data.getTimeToSatisfy())));
            }
        }
    }
}
