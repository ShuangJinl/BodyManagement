package com.bodymorph.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.bodymorph.fatness.FatnessMorphApplier;
import com.bodymorph.fatness.FatnessStatusEffects;
import com.bodymorph.fatness.MovementFatnessTracker;
import com.bodymorph.runtime.BodyMorphRuntime;
import com.bodymorph.special.HeartAttackTracker;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

@Mixin(Player.class)
abstract class PlayerMovementFatnessMixin {
	@Inject(method = "tick", at = @At("TAIL"))
	private void bodymorph$tickMovementFatness(CallbackInfo ci) {
		Player self = (Player) (Object) this;
		if (self.level().isClientSide() || !(self instanceof ServerPlayer sp)) {
			return;
		}
		if (!BodyMorphRuntime.isActive()) {
			return;
		}
		MovementFatnessTracker.onPlayerTick(sp);
		FatnessMorphApplier.apply(sp);
		FatnessStatusEffects.onPlayerTick(sp);
		HeartAttackTracker.onPlayerTick(sp);
	}
}
