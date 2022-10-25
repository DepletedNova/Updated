package com.depletednova.updated.foundation.registry.world;

import com.depletednova.updated.Updated;
import com.depletednova.updated.foundation.registry.Registrate;
import com.depletednova.updated.foundation.registry.RegistryType;
import com.depletednova.updated.foundation.registry.blocks.BlockRegistry;
import com.depletednova.updated.foundation.registry.blocks.SignRegistry;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.ItemGroup;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.FeatureSize;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import net.minecraft.world.gen.root.RootPlacer;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.trunk.TrunkPlacer;

import java.util.*;
import java.util.function.Predicate;

public class TreeRegistry extends RegistryType {
    @Override public void register(byte registryType) {
        if (registryType == (byte) -50) {
            tree = ConfiguredFeatures.register(Updated.IDs(getTag() + "_tree"), Feature.TREE, treeBuilder.build());
            treeChecked = PlacedFeatures.register(Updated.IDs(getTag() + "_checked"), tree, PlacedFeatures.wouldSurvive(saplingRegistry.block));
            for (TreeSpawnModifer modifier : treeSpawnModifiers) {
                treeSpawns.put(modifier.value, ConfiguredFeatures.register(Updated.IDs(getTag() + "_spawn_" + modifier.value), Feature.RANDOM_SELECTOR,
                        new RandomFeatureConfig(List.of(new RandomFeatureEntry(treeChecked, modifier.featureChance)), treeChecked)));
            }
        } else if (registryType == (byte) 100) {
            for (TreeSpawnModifer modifer : treeSpawnModifiers) {
                RegistryEntry<PlacedFeature> placed = PlacedFeatures.register(Updated.IDs(getTag() + "_placed_" + modifer.value),
                        treeSpawns.get(modifer.value), VegetationPlacedFeatures.modifiers(modifer.placementModifier));
                BiomeModifications.addFeature(modifer.key, GenerationStep.Feature.VEGETAL_DECORATION, placed.getKey().get());
            }
        }
    }

    private TreeRegistry(String tag, SaplingGenerator saplingGenerator) {
        super(tag, (byte) -50); // Features
        Registrate.addRegistry(this, (byte) 100); // Generation

        saplingRegistry = new BlockRegistry(tag + "_sapling", new SaplingBlock(saplingGenerator, FabricBlockSettings.copyOf(Blocks.OAK_SAPLING)))
                .addTag(BlockTags.SAPLINGS).setRenderLayer(RenderLayer.getCutout()).setItem(ItemGroup.DECORATIONS);
    }

    public TreeRegistry(String tag, SaplingGenerator saplingGenerator, TreeFeatureConfig.Builder builder) {
        this(tag, saplingGenerator);
        treeBuilder = builder;
    }

    public TreeRegistry(String tag, SaplingGenerator saplingGenerator, Block log, TrunkPlacer trunkPlacer, Block leaves, FoliagePlacer foliagePlacer, FeatureSize featureSize,
                        Optional<RootPlacer> rootPlacer, boolean flammable) {
        this(tag, saplingGenerator);
        isFlammable = flammable;
    
        treeBuilder = new TreeFeatureConfig.Builder(
                BlockStateProvider.of(new BlockRegistry(tag + "_log", log)
                        .setItem(ItemGroup.BUILDING_BLOCKS).setStrippable("stripped_" + tag + "_log").setFlammable(5, 5)
                        .addTags(BlockTags.LOGS_THAT_BURN, BlockTags.LOGS, BlockTags.COMPLETES_FIND_TREE_TUTORIAL, BlockTags.OVERWORLD_NATURAL_LOGS).block), trunkPlacer,
                BlockStateProvider.of(new BlockRegistry(tag + "_leaves", leaves)
                        .setItem(ItemGroup.DECORATIONS).setFlammable(30, 60)
                        .addTags(BlockTags.LEAVES, BlockTags.COMPLETES_FIND_TREE_TUTORIAL).block), foliagePlacer,
                rootPlacer, featureSize);
    
        new BlockRegistry(tag + "_wood", new PillarBlock(FabricBlockSettings.copyOf(Blocks.OAK_WOOD)))
                .setItem(ItemGroup.BUILDING_BLOCKS).setFlammable(5, 5).setStrippable("stripped_" + tag + "_wood")
                .addTags(BlockTags.LOGS_THAT_BURN, BlockTags.LOGS, BlockTags.COMPLETES_FIND_TREE_TUTORIAL);
        
        new BlockRegistry("stripped_" + tag + "_log", new PillarBlock(FabricBlockSettings.copyOf(Blocks.STRIPPED_OAK_LOG)))
                .setItem(ItemGroup.BUILDING_BLOCKS).setFlammable(5, 5)
                .addTags(BlockTags.LOGS_THAT_BURN, BlockTags.LOGS, BlockTags.COMPLETES_FIND_TREE_TUTORIAL);
        new BlockRegistry("stripped_" + tag + "_wood", new PillarBlock(FabricBlockSettings.copyOf(Blocks.STRIPPED_OAK_WOOD)))
                .setItem(ItemGroup.BUILDING_BLOCKS).setFlammable(5, 5)
                .addTags(BlockTags.LOGS_THAT_BURN, BlockTags.LOGS, BlockTags.COMPLETES_FIND_TREE_TUTORIAL);
    }
    
    public TreeRegistry(String tag, SaplingGenerator saplingGenerator, TrunkPlacer trunkPlacer, FoliagePlacer foliagePlacer, FeatureSize featureSize, Optional<RootPlacer> rootPlacer, boolean flammable) {
        this(tag, saplingGenerator, new PillarBlock(FabricBlockSettings.copyOf(Blocks.OAK_LOG)), trunkPlacer,
                new LeavesBlock(FabricBlockSettings.copyOf(Blocks.OAK_LEAVES)), foliagePlacer, featureSize, rootPlacer, flammable);
    }

    public TreeRegistry(String tag, SaplingGenerator saplingGenerator, TrunkPlacer trunkPlacer, FoliagePlacer foliagePlacer, FeatureSize featureSize, boolean flammable)
    { this(tag, saplingGenerator, trunkPlacer, foliagePlacer, featureSize, Optional.empty(), flammable); }

    protected TreeFeatureConfig.Builder treeBuilder;
    protected BlockRegistry saplingRegistry;
    protected boolean isFlammable = true;

    // Post-Registry
    public RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> tree;
    public RegistryEntry<PlacedFeature> treeChecked;
    public Map<Integer, RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>>> treeSpawns = new HashMap<>();
    
    public TreeRegistry generateWoodType(Block planks) {
        new BlockRegistry(getTag() + "_planks", planks)
                .setItem(ItemGroup.BUILDING_BLOCKS).setFlammable(5, 20, isFlammable)
                .addTag(BlockTags.PLANKS);
        new BlockRegistry(getTag() + "_stairs", new StairsBlock(planks.getDefaultState(), FabricBlockSettings.copyOf(Blocks.OAK_STAIRS)))
                .setItem(ItemGroup.BUILDING_BLOCKS).setFlammable(5, 20, isFlammable)
                .addTags(BlockTags.PLANKS, BlockTags.WOODEN_STAIRS);
        new BlockRegistry(getTag() + "_slab", new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_SLAB)))
                .setItem(ItemGroup.BUILDING_BLOCKS).setFlammable(5, 20, isFlammable)
                .addTags(BlockTags.PLANKS, BlockTags.WOODEN_SLABS);
    
        new BlockRegistry(getTag() + "_fence", new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE)))
                .setItem(ItemGroup.DECORATIONS).setFlammable(5, 20, isFlammable)
                .addTags(BlockTags.FENCES, BlockTags.WOODEN_FENCES);
        new BlockRegistry(getTag() + "_fence_gate", new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE_GATE)))
                .setItem(ItemGroup.DECORATIONS).setFlammable(5, 20, isFlammable)
                .addTags(BlockTags.FENCES, BlockTags.WOODEN_FENCES, BlockTags.FENCE_GATES);
        new BlockRegistry(getTag() + "_door", new DoorBlock(FabricBlockSettings.copyOf(Blocks.OAK_DOOR)))
                .setItem(ItemGroup.REDSTONE).setRenderLayer(RenderLayer.getCutout())
                .addTags(BlockTags.DOORS, BlockTags.WOODEN_DOORS);
        new BlockRegistry(getTag() + "_trapdoor", new TrapdoorBlock(FabricBlockSettings.copyOf(Blocks.OAK_TRAPDOOR)))
                .setItem(ItemGroup.REDSTONE).setRenderLayer(RenderLayer.getCutout())
                .addTags(BlockTags.TRAPDOORS, BlockTags.WOODEN_TRAPDOORS);
    
        new BlockRegistry(getTag() + "_button", new WoodenButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_BUTTON)))
                .setItem(ItemGroup.REDSTONE)
                .addTags(BlockTags.BUTTONS, BlockTags.WOODEN_BUTTONS);
        new BlockRegistry(getTag() + "_pressure_plate", new PressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PRESSURE_PLATE)))
                .setItem(ItemGroup.REDSTONE)
                .addTags(BlockTags.PRESSURE_PLATES, BlockTags.WOODEN_PRESSURE_PLATES);
    
        new SignRegistry(getTag(), Blocks.OAK_SIGN);
        
        // TODO boat
        
        return this;
    }
    
    public TreeRegistry generateWoodType() {
        this.generateWoodType(new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS)));
        return this;
    }
    
    public List<TreeSpawnModifer> treeSpawnModifiers = new ArrayList<>();
    public TreeRegistry createGeneration(float featureChance, PlacementModifier chanceModifier, Predicate<BiomeSelectionContext> key) {
        treeSpawnModifiers.add(new TreeSpawnModifer(treeSpawnModifiers.size(), featureChance, chanceModifier, key));
        return this;
    }
    
    // Generation Modifiers
    private class TreeSpawnModifer {
        public int value;
        public float featureChance;
        public PlacementModifier placementModifier;
        public Predicate<BiomeSelectionContext> key;
        public TreeSpawnModifer(int numerical, float featureChance, PlacementModifier placementModifier, Predicate<BiomeSelectionContext> key) {
            this.value = numerical;
            this.featureChance = featureChance;
            this.placementModifier = placementModifier;
            this.key = key;
        }
    }
}
