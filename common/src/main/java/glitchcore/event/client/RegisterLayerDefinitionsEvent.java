/*******************************************************************************
 * Copyright 2024, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.event.client;

import glitchcore.event.Event;
import glitchcore.util.RenderHelper;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;

import java.util.function.Supplier;

public class RegisterLayerDefinitionsEvent extends Event
{
    public static void registerLayerDefinition(ModelLayerLocation layerLocation, Supplier<LayerDefinition> supplier)
    {
        RenderHelper.registerLayerDefinition(layerLocation, supplier);
    }
}
