package com.bodymorph.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.bodymorph.fatness.FatnessMath;
import com.bodymorph.fatness.PlayerFatness;
import com.bodymorph.feedback.BodyMorphFeedback;
import com.bodymorph.runtime.BodyMorphRuntime;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

@Mixin(LivingEntity.class)
abstract class LivingEntityFatnessConsumeMixin {
	@Unique
	private ItemStack bodymorph$capturedUseItem;

	@Inject(method = "completeUsingItem", at = @At("HEAD"))
	private void bodymorph$captureUseItem(CallbackInfo ci) {
		LivingEntity self = (LivingEntity) (Object) this;
		this.bodymorph$capturedUseItem = self.getUseItem().copy();
	}

	@Inject(method = "completeUsingItem", at = @At("TAIL"))
	private void bodymorph$applyFoodFatness(CallbackInfo ci) {
		LivingEntity self = (LivingEntity) (Object) this;
		ItemStack consumed = this.bodymorph$capturedUseItem;
		this.bodymorph$capturedUseItem = null;

		if (self.level().isClientSide() || !(self instanceof ServerPlayer sp) || consumed == null || consumed.isEmpty()) {
			return;
		}
		if (!BodyMorphRuntime.isActive()) {
			return;
		}

		float gain = FatnessMath.fatnessFromFood(consumed);
		if (gain != 0f) {
			PlayerFatness.add(sp, gain);
			BodyMorphFeedback.tryFoodFatnessHint(sp, gain);
		}
	}
}
