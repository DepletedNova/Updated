package com.depletednova.updated.foundation.registry.blocks;

import com.depletednova.updated.foundation.data.GeneratorHub;
import com.depletednova.updated.foundation.registry.Registrate;
import com.depletednova.updated.foundation.registry.RegistryType;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

public class BlockRegistry extends RegistryType {
	@Override public void register(byte registryType) {
        if (registryType == (byte) 10) {
            // Block registry
            Registry.register(Registry.BLOCK, getIdentifier(), block);
            // Flammability registry
            if (!isFlammable) return;
            FlammableBlockRegistry.getDefaultInstance().add(block, burn, spread);
        } else if (registryType == (byte) 15 && isStrippable) {
            // Stripping registry
            StrippableBlockRegistry.register(block, Registrate.getRegistry(BlockRegistry.class, (byte)10, stripsTo).block);
        } else if (registryType == (byte) 40 && generateItem) {
            // BlockItem registry
            Registry.register(Registry.ITEM, getIdentifier(), new BlockItem(block, new Item.Settings().group(itemGroup)));
        } else if (registryType == (byte) 45 && isFuel) {
            FuelRegistry.INSTANCE.add(block, fuelTime);
        }
    }

    @Override public void registerClient(byte registryType) {
        if (registryType != (byte) 10 || !usesRenderLayer) return;
        BlockRenderLayerMap.INSTANCE.putBlock(block, renderLayer);
    }

    public BlockRegistry(String tag, Block toRegister) {
        super(tag, (byte) 10); // Block
        Registrate.addRegistry(this, (byte) 15); // Strippables
        Registrate.addRegistry(this, (byte) 40); // Item
        Registrate.addRegistry(this, (byte) 45); // Fuel
        
        GeneratorHub.addRegistry("block_tag", this);
        
        block = toRegister;
    }

    public Block block;

    // Block burning
    protected boolean isFlammable = false;
    protected int burn;
    protected int spread;
    public BlockRegistry setFlammable(int burn, int spread, boolean useFlammability) {
        if (!useFlammability) return this;
        isFlammable = true;
        this.burn = burn;
        this.spread = spread;
        return this;
    }
    public BlockRegistry setFlammable(int burn, int spread) { return this.setFlammable(burn, spread, true); }

    // Block/item as fuel
    protected boolean isFuel = false;
    protected int fuelTime;
    public BlockRegistry setFuel(int time) {
        this.isFuel = true;
        this.fuelTime = time;
        return this;
    }

    // Generate item
    protected boolean generateItem = false;
    protected ItemGroup itemGroup;
    public BlockRegistry setItem(ItemGroup itemGroup) {
        generateItem = true;
        this.itemGroup = itemGroup;
        return this;
    }

    // Renderlayer
    protected boolean usesRenderLayer = false;
    protected RenderLayer renderLayer;
    public BlockRegistry setRenderLayer(RenderLayer layer) {
        usesRenderLayer = true;
        renderLayer = layer;
        return this;
    }

    // Stripping
    protected boolean isStrippable = false;
    protected String stripsTo;
    public BlockRegistry setStrippable(String tag) {
        isStrippable = true;
        stripsTo = tag;
        return this;
    }

    // Tags
    public List<TagKey<Block>> tags = new ArrayList<>();
    public BlockRegistry addTag(TagKey<Block> tagToAdd) {
        this.tags.add(tagToAdd);
        return this;
    }
    public BlockRegistry addTags(TagKey<Block>... tagsToAdd) {
        tags.addAll(Arrays.asList(tagsToAdd));
        return this;
    }
    
    public BiFunction<BlockStateModelGenerator, Block, Void> modelCallback;
    public BlockRegistry setModelCallback(BiFunction<BlockStateModelGenerator, Block, Void> callback) {
        this.modelCallback = callback;
        return this;
    }
    
    // Generic static
    public static boolean never(BlockState state, BlockView world, BlockPos pos) {
        return false;
    }
    public static Boolean canSpawnOnLeaves(BlockState state, BlockView world, BlockPos pos, EntityType<?> type) {
        return type == EntityType.OCELOT || type == EntityType.PARROT;
    }
}
