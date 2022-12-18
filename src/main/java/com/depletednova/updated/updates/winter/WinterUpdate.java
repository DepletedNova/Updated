package com.depletednova.updated.updates.winter;

import com.depletednova.updated.foundation.AbstractUpdate;
import com.depletednova.updated.foundation.registry.misc.EnchantmentRegistry;
import com.depletednova.updated.foundation.registry.misc.EntityRegistry;
import com.depletednova.updated.foundation.registry.misc.ItemRegistry;
import com.depletednova.updated.foundation.registry.world.ParticleRegistry;
import com.depletednova.updated.foundation.registry.world.TreeRegistry;
import com.depletednova.updated.foundation.registry.world.tree.FoliagePlacerRegistry;
import com.depletednova.updated.foundation.registry.world.tree.TrunkPlacerRegistry;
import com.depletednova.updated.updates.generic.level.placers.GiantRootedTrunkPlacer;
import com.depletednova.updated.updates.winter.entity.chillager.ChillagerEntity;
import com.depletednova.updated.updates.winter.entity.chillager.ChillagerModel;
import com.depletednova.updated.updates.winter.entity.chillager.ChillagerRenderer;
import com.depletednova.updated.updates.winter.entity.chillager.drop.IceEffigyItem;
import com.depletednova.updated.updates.winter.entity.chillager.summons.*;
import com.depletednova.updated.updates.winter.entity.frozen.FrozenEntity;
import com.depletednova.updated.updates.winter.entity.frozen.FrozenEntityRenderer;
import com.depletednova.updated.updates.winter.pine.PineLeavesBlock;
import com.depletednova.updated.updates.winter.pine.PineSaplingGenerator;
import com.depletednova.updated.updates.winter.pine.WinterPineFoliagePlacer;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.mixin.client.rendering.EntityModelLayersAccessor;
import net.minecraft.block.Blocks;
import net.minecraft.block.PillarBlock;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public final class WinterUpdate extends AbstractUpdate {
    @Override public void registerServer() {
        // Particles
        new ParticleRegistry("flurry", FluryParticle.Factory::new);
        
        // Trees
        new TrunkPlacerRegistry("giant_rooted_trunk_placer", GiantRootedTrunkPlacer.CODEC);
        new FoliagePlacerRegistry("winter_pine_foliage_placer", WinterPineFoliagePlacer.CODEC);
        new TreeRegistry("pine", new PineSaplingGenerator(),
                new PillarBlock(FabricBlockSettings.copyOf(Blocks.OAK_LOG)),
                new GiantRootedTrunkPlacer(14, 2, 6),
                new PineLeavesBlock(FabricBlockSettings.copyOf(Blocks.AZALEA_LEAVES)),
                new WinterPineFoliagePlacer(ConstantIntProvider.create(1), ConstantIntProvider.create(1)),
                new TwoLayersFeatureSize(1, 1, 2),
                Optional.empty(), true).generateWoodType()
                .createGeneration(0.5f, PlacedFeatures.createCountExtraModifier(0, 0.1f, 1),
                        BiomeSelectors.includeByKey(Arrays.asList(BiomeKeys.SNOWY_PLAINS, BiomeKeys.SNOWY_SLOPES, BiomeKeys.TAIGA, BiomeKeys.WINDSWEPT_FOREST)))
                .createGeneration(0.5f, PlacedFeatures.createCountExtraModifier(1, 0.2f, 4),
                        BiomeSelectors.includeByKey(Arrays.asList(BiomeKeys.SNOWY_TAIGA, BiomeKeys.GROVE)));
        
        // Enchantments
        new EnchantmentRegistry("frost", new FrostCurse());
        
        // Entities
        new EntityRegistry<ChillagerEntity>("iceologer", ChillagerEntity.class,
                FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, ChillagerEntity::new).dimensions(EntityDimensions.fixed(0.7f, 2.1f)),
                ChillagerRenderer::new,
                ChillagerRenderer.LAYER,
                Optional.of(ChillagerModel::getTexturedModelData),
                Optional.of(ChillagerEntity.createChillagerAttributes()))
                .setSpawnRestrictions(SpawnRestriction.Location.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HostileEntity::canSpawnInDark)
                .setSpawnEgg((entityType -> new ItemRegistry("iceologer_spawn_egg",
                        new SpawnEggItem((EntityType<? extends MobEntity>) entityType,
                                0xADC3CC, 0x0D3B4A,
                                new Item.Settings().group(ItemGroup.MISC)))));
        
        new EntityRegistry<FallingIceShardEntity>("falling_ice_shard", FallingIceShardEntity.class,
                FabricEntityTypeBuilder.create(SpawnGroup.MISC, FallingIceShardEntity::new).dimensions(EntityDimensions.changing(1.0f, 2.0f)),
                FallingIceShardRenderer::new,
                FallingIceShardModel.LAYER,
                Optional.of(FallingIceShardModel::getTexturedModelData),
                Optional.empty());
        
        new EntityRegistry<IceShardEntity>("ice_shard", IceShardEntity.class,
                FabricEntityTypeBuilder.create(SpawnGroup.MISC, IceShardEntity::new).dimensions(EntityDimensions.changing(1.2f, 1.35f)),
                IceShardRenderer::new,
                IceShardRenderer.LAYER,
                Optional.of(IceShardRenderer::getTexturedModelData),
                Optional.empty());
        
        /*new EntityRegistry<FrozenEntity>("frozen_entity", FrozenEntity.class,
                FabricEntityTypeBuilder.create(SpawnGroup.MISC, FrozenEntity::new).dimensions(EntityDimensions.changing(1.0f, 1.0f)),
                FrozenEntityRenderer::new,
                FrozenEntityRenderer.LAYER,
                Optional.empty(),
                Optional.empty());*/
        
        new ItemRegistry("ice_effigy", new IceEffigyItem());
    }
    
    @Override public void registerClient() {

    }
    
    @Override public String getUpdateID() { return "winter"; }
    @Override public int getIteration() { return 0; }
    @Override public int getTitleWidth() { return 151; }
    
    public static final List<RegistryKey<Biome>> appliedBiomes = Arrays.asList(
            BiomeKeys.SNOWY_TAIGA,
            BiomeKeys.SNOWY_PLAINS,
            BiomeKeys.SNOWY_SLOPES,
            BiomeKeys.SNOWY_BEACH,
            BiomeKeys.FROZEN_OCEAN,
            BiomeKeys.FROZEN_PEAKS,
            BiomeKeys.FROZEN_RIVER,
            BiomeKeys.JAGGED_PEAKS,
            BiomeKeys.GROVE,
            BiomeKeys.DEEP_COLD_OCEAN,
            BiomeKeys.DEEP_FROZEN_OCEAN,
            BiomeKeys.COLD_OCEAN
    );
    public static boolean matchBiomeKey(RegistryKey<Biome> biome) {
        for (RegistryKey<Biome> biomeToCheck : appliedBiomes) {
            if (biome != biomeToCheck) continue;
            return true;
        }
        return false;
    }
}
