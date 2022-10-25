package com.depletednova.updated.mixin;

import com.depletednova.updated.Updated;
import com.depletednova.updated.foundation.registry.Registrate;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {
	@Shadow private long backgroundFadeStart;
	@Shadow @Final private boolean doBackgroundFade;
	
	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/util/Identifier;)V", opcode = 3, shift = At.Shift.BEFORE))
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		if (Updated.currentUpdate != null) {
			// Screen
			TitleScreen screen = (TitleScreen) (Object) this;
			int titleWidth = Updated.currentUpdate.getTitleWidth();
			
			// Fade
			float f = this.doBackgroundFade ? (float)(Util.getMeasuringTimeMs() - this.backgroundFadeStart) / 1000.0f : 1.0f;
			float g = this.doBackgroundFade ? MathHelper.clamp(f - 1.0f, 0.0f, 1.0f) : 1.0f;
			int l = MathHelper.ceil(g * 255.0f) << 24;
			if ((l & 0xFC000000) == 0) { return; }
			
			// Render Update Card
			RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, g);
			RenderSystem.setShaderTexture(0, Updated.currentUpdate.titleIdentifier);
			TitleScreen.drawTexture(matrices, screen.width / 2 - titleWidth / 2, 79, 0.0f, 0.0f, titleWidth, 14, 256, 16);
			
			// Render Update Note
			TitleScreen.drawStringWithShadow(matrices, MinecraftClient.getInstance().textRenderer,
					"Updated " + Registrate.UPDATES.size() + (Updated.devBuild ? "-DEV" : "." + Updated.currentUpdate.getIteration()),
					2, screen.height - 20, 0xFFFFFF | l);
		}
	}
}
