package net.darkhax.cravings.command;

import net.darkhax.bookshelf.command.CommandTree;
import net.minecraft.command.ICommandSender;

public class CommandCravingTree extends CommandTree {

    public CommandCravingTree () {

        this.addSubcommand(new CommandSetCraving());
        this.addSubcommand(new CommandCravingInfo());
        this.addSubcommand(new CommandFoods());
    }

    @Override
    public int getRequiredPermissionLevel () {

        return 0;
    }

    @Override
    public String getName () {

        return "craving";
    }

    @Override
    public String getUsage (ICommandSender sender) {

        return "commands.cravings.usage";
    }
}