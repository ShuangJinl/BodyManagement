package com.bodymorph.fatness;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

/**
 * Applies thin/fat state potion effects with dynamic amplifiers.
 */
public final class FatnessStatusEffects {
	private static final int EFFECT_DURATION_TICKS = 80;

	private FatnessStatusEffects() {
	}

	public static void onPlayerTick(ServerPlayer player) {
		float fatness = PlayerFatness.get(player);

		applyOrClear(player, MobEffects.DIG_SLOWDOWN, FatnessMath.thinMiningFatigueAmplifier(fatness));

		applyOrClear(player, MobEffects.DAMAGE_BOOST, FatnessMath.fatStrengthAmplifier(fatness));
		applyOrClear(player, MobEffects.MOVEMENT_SLOWDOWN, FatnessMath.fatSlownessAmplifier(fatness));
		applyOrClear(player, MobEffects.HUNGER, FatnessMath.fatHungerAmplifier(fatness));
	}

	public static void clear(ServerPlayer player) {
		player.removeEffect(MobEffects.DIG_SLOWDOWN);
		player.removeEffect(MobEffects.DAMAGE_BOOST);
		player.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
		player.removeEffect(MobEffects.HUNGER);
	}

	private static void applyOrClear(ServerPlayer player, net.minecraft.core.Holder<net.minecraft.world.effect.MobEffect> effect, int amplifier) {
		if (amplifier < 0) {
			player.removeEffect(effect);
			return;
		}
		player.addEffect(new MobEffectInstance(effect, EFFECT_DURATION_TICKS, amplifier, true, false, true));
	}
}
