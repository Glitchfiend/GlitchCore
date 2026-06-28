/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.event.client;

import glitchcore.event.Event;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;

public abstract class RenderHudEvent extends Event
{
    private final Type type;
    private final Hud hud;
    private final GuiGraphicsExtractor guiGraphics;
    private final DeltaTracker deltaTracker;
    private final int screenWidth;
    private final int screenHeight;

    private int rowTop;

    public RenderHudEvent(Type type, Hud hud, GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker, int screenWidth, int screenHeight, int rowTop)
    {
        this.type = type;
        this.hud = hud;
        this.guiGraphics = guiGraphics;
        this.deltaTracker = deltaTracker;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.rowTop = rowTop;
    }

    public Type getType()
    {
        return this.type;
    }

    public Hud getHud()
    {
        return this.hud;
    }

    public GuiGraphicsExtractor getGuiGraphics()
    {
        return this.guiGraphics;
    }

    public DeltaTracker getDeltaTracker()
    {
        return this.deltaTracker;
    }

    public int getScreenWidth()
    {
        return this.screenWidth;
    }

    public int getScreenHeight()
    {
        return this.screenHeight;
    }

    public int getRowTop()
    {
        if (this.rowTop == -1)
            throw new UnsupportedOperationException("Row top is not implemented");

        return this.rowTop;
    }

    public void setRowTop(int value)
    {
        if (this.rowTop == -1)
            throw new UnsupportedOperationException("Row top is not implemented");

        this.rowTop = value;
    }

    public static class Pre extends RenderHudEvent
    {
        public Pre(Type type, Hud hud, GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker, int screenWidth, int screenHeight, int rowTop)
        {
            super(type, hud, guiGraphics, deltaTracker, screenWidth, screenHeight, rowTop);
        }

        public Pre(Type type, Hud hud, GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker, int screenWidth, int screenHeight)
        {
            this(type, hud, guiGraphics, deltaTracker, screenWidth, screenHeight, -1);
        }
    }

    public enum Type
    {
        AIR,
        FOOD,
        FROSTBITE;
    }
}
