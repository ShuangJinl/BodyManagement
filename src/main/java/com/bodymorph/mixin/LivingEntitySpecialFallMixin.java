package com.bodymorph.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.bodymorph.special.FatLandingExplosion;
import com.bodymorph.runtime.BodyMorphRuntime;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

@Mixin(LivingEntity.class)
abstract class LivingEntitySpecialFallMixin {
	@Inject(method = "causeFallDamage", at = @At("HEAD"), cancellable = true)
	private void bodymorph$fatLandingExplosion(
		float fallDistance,
		float damagePerDistance,
		DamageSource damageSource,
		CallbackInfoReturnable<Boolean> cir
	) {
		LivingEntity self = (LivingEntity) (Object) this;
		if (self.level().isClientSide() || !(self instanceof ServerPlayer sp)) {
			return;
		}
		if (!BodyMorphRuntime.isActive()) {
			return;
		}
		if (FatLandingExplosion.tryIntercept(sp, fallDistance, damagePerDistance, damageSource)) {
			// Skip vanilla fall damage handling; return value matches "no fall damage applied".
			cir.setReturnValue(false);
		}
	}

	@ModifyVariable(method = "hurt", at = @At("HEAD"), argsOnly = true)
	private float bodymorph$fallDamageAdjustments(float amount, DamageSource source) {
		LivingEntity self = (LivingEntity) (Object) this;
		if (self.level().isClientSide() || !(self instanceof ServerPlayer sp)) {
			return amount;
		}
		if (!BodyMorphRuntime.isActive()) {
			return amount;
		}
		return FatLandingExplosion.adjustFallDamage(sp, source, amount);
	}
}
