package com.depletednova.updated.updates.winter.entity.chillager.drop;

import com.depletednova.updated.updates.winter.entity.chillager.summons.IceShardEntity;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class IceEffigyItem extends Item {
	public IceEffigyItem() {
		super(new FabricItemSettings().maxCount(1).group(ItemGroup.COMBAT).rarity(Rarity.RARE));
	}
	
	@Override public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack activeTotem = user.getStackInHand(hand);
		if (world.isClient) {
			MinecraftClient client = MinecraftClient.getInstance();
			client.gameRenderer.showFloatingItem(activeTotem);
			client.particleManager.addEmitter(user, ParticleTypes.SNOWFLAKE, 30);
			world.playSound(user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_TOTEM_USE, user.getSoundCategory(), 1.0f, 1.0f, false);
		} else {
			if (!user.isCreative()) activeTotem.decrement(1);
			IceShardEntity.SummonShardGroup(user, true, 1.5f, 6);
			IceShardEntity.SummonShardGroup(user, false, 3.0f, 10);
		}
		return super.use(world, user, hand);
	}
}
