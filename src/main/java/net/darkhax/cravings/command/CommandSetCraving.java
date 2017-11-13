package net.darkhax.cravings.command;

import org.apache.commons.lang3.math.NumberUtils;

import net.darkhax.bookshelf.command.Command;
import net.darkhax.cravings.Cravings;
import net.darkhax.cravings.craving.ICraving;
import net.darkhax.cravings.handler.ConfigurationHandler;
import net.darkhax.cravings.handler.CravingDataHandler;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandSetCraving extends Command {

    @Override
    public String getName () {

        return "set";
    }

    @Override
    public int getRequiredPermissionLevel () {

        return 2;
    }

    @Override
    public String getUsage (ICommandSender sender) {

        return "commands.cravings.set.usage";
    }

    @Override
    public void execute (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        if (args.length >= 2) {

            final EntityPlayer player = getPlayer(server, sender, args[0]);

            final ICraving craving = Cravings.CRAVING_REGISTRY.getValue(new ResourceLocation(args[1]));
            final int timeToComplete = args.length == 3 && NumberUtils.isCreatable(args[2]) ? NumberUtils.createInteger(args[2]) : ConfigurationHandler.timeToSatisfy;

            if (craving == null) {

                throw new WrongUsageException("commands.cravings.nocraving", args[1]);
            }

            CravingDataHandler.setCravingData(player, craving, timeToComplete);

            if (player != sender) {

                sender.sendMessage(new TextComponentTranslation("commands.cravings.set.sender", craving.getRegistryName().toString(), player.getDisplayNameString()));
            }
        }

        else {

            throw new WrongUsageException("commands.cravings.set.usage", new Object[0]);
        }
    }

    @Override
    public boolean isUsernameIndex (String[] args, int index) {

        return index == 0;
    }
}