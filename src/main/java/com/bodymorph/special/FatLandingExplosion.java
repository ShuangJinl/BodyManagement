package com.bodymorph.special;

import com.bodymorph.fatness.FatnessConstants;
import com.bodymorph.fatness.FatnessMath;
import com.bodymorph.fatness.PlayerFatness;
import com.bodymorph.feedback.BodyMorphText;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.level.Level;

/**
 * Obese players cancel vanilla high fall damage and detonate an explosion instead.
 */
public final class FatLandingExplosion {
	private FatLandingExplosion() {
	}

	/**
	 * @return {@code true} if vanilla fall handling for this landing should be skipped.
	 */
	public static boolean tryIntercept(ServerPlayer player, float fallDistance, float damagePerDistance, DamageSource damageSource) {
		if (player.level().isClientSide()) {
			return false;
		}
		float fatness = PlayerFatness.get(player);
		if (fatness <= FatnessConstants.NORMAL_HIGH) {
			return false;
		}
		if (fallDistance < FatnessConstants.FAT_EXPLOSION_MIN_FALL_BLOCKS) {
			return false;
		}

		float power = FatnessMath.fatExplosionPower(fatness);
		ServerLevel level = player.serverLevel();
		level.explode(
			player,
			null,
			null,
			player.position(),
			power,
			false,
			Level.ExplosionInteraction.TNT
		);
		player.displayClientMessage(BodyMorphText.fatLandingShock(fallDistance, power), true);
		BodyMorphSounds.playFatLanding(player);
		player.fallDistance = 0f;
		return true;
	}

	public static float adjustFallDamage(ServerPlayer player, DamageSource source, float amount) {
		if (!source.is(DamageTypes.FALL)) {
			return amount;
		}
		float fatness = PlayerFatness.get(player);
		if (fatness > FatnessConstants.NORMAL_HIGH) {
			return 0f;
		}
		if (fatness >= FatnessConstants.NORMAL_LOW) {
			return amount;
		}

		float mult = FatnessMath.osteoporosisFallDamageMultiplier(fatness);
		float out = amount * mult;
		if (mult >= FatnessConstants.OSTEOPOROSIS_NOTIFY_MULT && amount >= FatnessConstants.OSTEOPOROSIS_NOTIFY_MIN_DAMAGE) {
			OsteoporosisNotifier.notifyIfSevere(player);
		}
		return out;
	}
}
