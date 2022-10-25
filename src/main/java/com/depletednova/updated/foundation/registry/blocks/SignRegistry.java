package com.depletednova.updated.foundation.registry.blocks;

import com.depletednova.updated.Updated;
import com.depletednova.updated.foundation.registry.Registrate;
import com.depletednova.updated.foundation.registry.RegistryType;
import com.depletednova.updated.mixin.accessors.SignTypeAccessor;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.SignBlock;
import net.minecraft.block.WallSignBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SignItem;
import net.minecraft.util.SignType;
import net.minecraft.util.registry.Registry;

public class SignRegistry extends RegistryType {
	@Override public void register(byte registryType) {
		if (registryType == (byte) -25) {
			// Sign type registry
			signType = SignTypeAccessor.registerNew(SignTypeAccessor.newSignType(getTag()));
		} else if (registryType == (byte) 10) {
			// Block creation
			sign = new SignBlock(FabricBlockSettings.copyOf(base), signType);
			wallSign = new WallSignBlock(FabricBlockSettings.copyOf(base), signType);
			
			// Block registry
			Registry.register(Registry.BLOCK, Updated.ID(getTag() + "_sign"), sign);
			Registry.register(Registry.BLOCK, Updated.ID(getTag() + "_wall_sign"), wallSign);
		} else if (registryType == (byte) 40) {
			// Item registry
			Registry.register(Registry.ITEM, Updated.ID(getTag() + "_sign"),
					new SignItem(new Item.Settings().group(ItemGroup.DECORATIONS).maxCount(16), sign, wallSign));
		}
	}
	
	public SignRegistry(String tag, Block baseSign) {
		super(tag, (byte) -25);
		Registrate.addRegistry(this, (byte) 10);
		Registrate.addRegistry(this, (byte) 40);
		
		base = baseSign;
	}
	
	protected Block base;
	
	// Post-registry
	protected SignType signType;
	public SignBlock sign;
	public WallSignBlock wallSign;
}
