/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.event.client;

import glitchcore.event.Event;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.block.BlockTintSource;

import java.util.List;
import java.util.function.BiConsumer;

public abstract class RegisterColorsEvent<ObjColor, Obj> extends Event
{
    private final BiConsumer<List<ObjColor>, Obj> register;

    public RegisterColorsEvent(BiConsumer<List<ObjColor>, Obj> register)
    {
        this.register = register;
    }

    public void register(List<ObjColor> color, Obj... objs)
    {
        for (var obj : objs)
        {
            this.register.accept(color, obj);
        }
    }

    public static class Block extends RegisterColorsEvent<BlockTintSource, net.minecraft.world.level.block.Block>
    {
        public Block(BiConsumer<List<BlockTintSource>, net.minecraft.world.level.block.Block> register)
        {
            super(register);
        }
    }
}
