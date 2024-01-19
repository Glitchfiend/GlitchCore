/*******************************************************************************
 * Copyright 2024, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.event.server;

import com.mojang.brigadier.CommandDispatcher;
import glitchcore.event.Event;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class RegisterCommandsEvent extends Event
{
    private final CommandDispatcher<CommandSourceStack> dispatcher;
    private final Commands.CommandSelection selection;
    private final CommandBuildContext context;

    public RegisterCommandsEvent(CommandDispatcher<CommandSourceStack> dispatcher, Commands.CommandSelection selection, CommandBuildContext context)
    {
        this.dispatcher = dispatcher;
        this.selection = selection;
        this.context = context;
    }

    public CommandDispatcher<CommandSourceStack> getDispatcher()
    {
        return dispatcher;
    }

    public Commands.CommandSelection getCommandSelection()
    {
        return selection;
    }

    public CommandBuildContext getBuildContext()
    {
        return context;
    }
}

