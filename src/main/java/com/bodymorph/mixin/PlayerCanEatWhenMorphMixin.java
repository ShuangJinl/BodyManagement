package com.bodymorph.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.bodymorph.runtime.BodyMorphGameplayCache;
import com.bodymorph.runtime.BodyMorphRuntime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;

@Mixin(Player.class)
abstract class PlayerCanEatWhenMorphMixin {
	@Inject(method = "canEat", at = @At("HEAD"), cancellable = true)
	private void bodymorph$allowEatWhenFullDuringMorph(boolean alwaysEat, CallbackInfoReturnable<Boolean> cir) {
		if (alwaysEat) {
			return;
		}
		Player self = (Player) (Object) this;
		if (self.level().isClientSide()) {
			if (BodyMorphGameplayCache.isGameplayActive()) {
				cir.setReturnValue(true);
			}
		} else if (self instanceof ServerPlayer && BodyMorphRuntime.isActive()) {
			cir.setReturnValue(true);
		}
	}
}
