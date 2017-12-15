package net.darkhax.cravings.command;

import net.darkhax.bookshelf.command.Command;
import net.darkhax.cravings.Cravings;
import net.darkhax.cravings.craving.CravingRandomFood;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandFoods extends Command {

    @Override
    public String getName () {

        return "foods";
    }

    @Override
    public int getRequiredPermissionLevel () {

        return 0;
    }

    @Override
    public String getUsage (ICommandSender sender) {

        return "commands.cravings.foods.usage";
    }

    @Override
    public void execute (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        sender.sendMessage(new TextComponentTranslation("commands.cravings.foods"));
        Cravings.LOG.info("Printing out {} detected foods.", CravingRandomFood.FOODS.size());

        for (final ItemStack foodStack : CravingRandomFood.FOODS) {

            Cravings.LOG.info(foodStack.getDisplayName());
        }
    }
}
