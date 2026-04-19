package com.bodymorph.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.bodymorph.fatness.FatnessMath;
import com.bodymorph.fatness.PlayerFatness;
import com.bodymorph.runtime.BodyMorphRuntime;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;

@Mixin(LivingEntity.class)
abstract class LivingEntityLootMultiplierMixin {
	@Shadow
	protected abstract void dropFromLootTable(DamageSource damageSource, boolean causedByPlayer);

	@Inject(method = "dropAllDeathLoot", at = @At("TAIL"))
	private void bodymorph$fatMobExtraDrops(ServerLevel level, DamageSource source, CallbackInfo ci) {
		LivingEntity self = (LivingEntity) (Object) this;
		if (!(self instanceof Monster) || level.isClientSide()) {
			return;
		}
		if (!BodyMorphRuntime.isActive()) {
			return;
		}
		if (!(source.getEntity() instanceof ServerPlayer killer)) {
			return;
		}

		float multiplier = FatnessMath.fatMobDropMultiplier(PlayerFatness.get(killer));
		if (multiplier <= 1f) {
			return;
		}

		int guaranteedExtraRolls = Mth.floor(multiplier - 1f);
		float fractionalChance = (multiplier - 1f) - guaranteedExtraRolls;

		for (int i = 0; i < guaranteedExtraRolls; i++) {
			this.dropFromLootTable(source, true);
		}
		if (fractionalChance > 0f && killer.getRandom().nextFloat() < fractionalChance) {
			this.dropFromLootTable(source, true);
		}
	}
}
