/*******************************************************************************
 * Copyright 2024, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.data;

import com.google.gson.JsonElement;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.blockstates.BlockStateGenerator;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.renderer.item.ClientItem;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class ModelProviderBase implements DataProvider
{
    private final PackOutput.PathProvider blockStatePathProvider;
    private final PackOutput.PathProvider itemInfoPathProvider;
    private final PackOutput.PathProvider modelPathProvider;
    private final String modId;
    private final boolean autogenBlockItemInfos;

    public ModelProviderBase(PackOutput output, String modId, boolean autogenBlockItemInfos) {
        this.blockStatePathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "blockstates");
        this.itemInfoPathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "items");
        this.modelPathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models");
        this.modId = modId;
        this.autogenBlockItemInfos = autogenBlockItemInfos;
    }

    public ModelProviderBase(PackOutput output, String modId) {
        this(output, modId, true);
    }

    abstract protected BlockModelGenerators createBlockModelGenerators(Consumer<BlockStateGenerator> blockStateOutput, ItemModelOutput itemModelOutput, BiConsumer<ResourceLocation, ModelInstance> modelOutput);
    abstract protected ItemModelGenerators createItemModelGenerators(ItemModelOutput itemModelOutput, BiConsumer<ResourceLocation, ModelInstance> modelOutput);

    @Override
    public CompletableFuture<?> run(CachedOutput output)
    {
        ItemInfoCollector itemInfoCollector = new ItemInfoCollector();
        BlockStateGeneratorCollector blockStateGeneratorCollector = new BlockStateGeneratorCollector();
        SimpleModelCollector simpleModelCollector = new SimpleModelCollector();

        var blockModelGenerators = createBlockModelGenerators(blockStateGeneratorCollector, itemInfoCollector, simpleModelCollector);
        var itemModelGenerators = createItemModelGenerators(itemInfoCollector, simpleModelCollector);

        if (blockModelGenerators != null) blockModelGenerators.run();
        if (itemModelGenerators != null) itemModelGenerators.run();

        itemInfoCollector.finalizeAndValidate();
        return CompletableFuture.allOf(
                blockStateGeneratorCollector.save(output, this.blockStatePathProvider),
                simpleModelCollector.save(output, this.modelPathProvider),
                itemInfoCollector.save(output, this.itemInfoPathProvider)
        );
    }

    static <T> CompletableFuture<?> saveAll(CachedOutput output, Function<T, Path> p_386455_, Map<T, ? extends Supplier<JsonElement>> generators) {
        return DataProvider.saveAll(output, Supplier::get, p_386455_, generators);
    }

    @Override
    public final String getName() {
        return "Model Definitions";
    }

    static class BlockStateGeneratorCollector implements Consumer<BlockStateGenerator>
    {
        private final Map<Block, BlockStateGenerator> generators = new HashMap<>();

        public void accept(BlockStateGenerator p_388748_) {
            Block block = p_388748_.getBlock();
            BlockStateGenerator blockstategenerator = this.generators.put(block, p_388748_);
            if (blockstategenerator != null) {
                throw new IllegalStateException("Duplicate blockstate definition for " + block);
            }
        }

        public CompletableFuture<?> save(CachedOutput p_388014_, PackOutput.PathProvider p_388192_) {
            return saveAll(p_388014_, p_387598_ -> p_388192_.json(p_387598_.builtInRegistryHolder().key().location()), this.generators);
        }
    }

    class ItemInfoCollector implements ItemModelOutput
    {
        private final Map<Item, ClientItem> itemInfos = new HashMap<>();
        private final Map<Item, Item> copies = new HashMap<>();

        @Override
        public void accept(Item item, ItemModel.Unbaked model) {
            this.register(item, new ClientItem(model, ClientItem.Properties.DEFAULT));
        }

        private void register(Item p_388205_, ClientItem p_388233_) {
            ClientItem clientitem = this.itemInfos.put(p_388205_, p_388233_);
            if (clientitem != null) {
                throw new IllegalStateException("Duplicate item model definition for " + p_388205_);
            }
        }

        @Override
        public void copy(Item p_386920_, Item p_386789_) {
            this.copies.put(p_386789_, p_386920_);
        }

        public void finalizeAndValidate()
        {
            if (ModelProviderBase.this.autogenBlockItemInfos)
            {
                BuiltInRegistries.ITEM.listElements().filter(ModelProviderBase.this::isModded).forEach(p_388426_ -> {
                    if (!this.copies.containsKey(p_388426_)) {
                        if (p_388426_.value() instanceof BlockItem blockitem && !this.itemInfos.containsKey(blockitem)) {
                            ResourceLocation resourcelocation = ModelLocationUtils.getModelLocation(blockitem.getBlock());
                            this.accept(blockitem, ItemModelUtils.plainModel(resourcelocation));
                        }
                    }
                });
            }
            this.copies.forEach((p_386494_, p_386575_) -> {
                ClientItem clientitem = this.itemInfos.get(p_386575_);
                if (clientitem == null) {
                    throw new IllegalStateException("Missing donor: " + p_386575_ + " -> " + p_386494_);
                } else {
                    this.register(p_386494_, clientitem);
                }
            });
        }

        public CompletableFuture<?> save(CachedOutput output, PackOutput.PathProvider pathProvider)
        {
            return DataProvider.saveAll(
                    output, ClientItem.CODEC, p_388594_ -> pathProvider.json(p_388594_.builtInRegistryHolder().key().location()), this.itemInfos
            );
        }
    }

    static class SimpleModelCollector implements BiConsumer<ResourceLocation, ModelInstance>
    {
        private final Map<ResourceLocation, ModelInstance> models = new HashMap<>();

        public void accept(ResourceLocation location, ModelInstance instance)
        {
            Supplier<JsonElement> supplier = this.models.put(location, instance);
            if (supplier != null) {
                throw new IllegalStateException("Duplicate model definition for " + location);
            }
        }

        public CompletableFuture<?> save(CachedOutput output, PackOutput.PathProvider pathProvider)
        {
            return saveAll(output, pathProvider::json, this.models);
        }
    }

    private boolean isModded(Holder.Reference<?> reference)
    {
        return reference.key().location().getNamespace().equals(this.modId);
    }
}
