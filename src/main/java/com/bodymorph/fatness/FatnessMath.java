package com.bodymorph.fatness;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;

/**
 * Central place for fatness-related numeric mapping (curves can be refined later).
 */
public final class FatnessMath {
	private FatnessMath() {
	}

	public static float clamp01(float t) {
		return Math.max(0f, Math.min(1f, t));
	}

	public static float lerp(float a, float b, float t) {
		return a + (b - a) * t;
	}

	public static boolean isNormalFatness(float fatness) {
		return fatness >= FatnessConstants.NORMAL_LOW && fatness <= FatnessConstants.NORMAL_HIGH;
	}

	/**
	 * Obesity value used only for Pehkui width/height: each band {@code [k*step, k*step+step-1]} uses the scale for
	 * the <strong>top</strong> of that band (e.g. step=3 → 36..38 → 38). Attributes still use raw {@code fatness}.
	 */
	public static float fatnessQuantizedForMorph(float fatness) {
		float step = FatnessConstants.MORPH_FATNESS_STEP;
		double bandStart = Math.floor((double) fatness / (double) step) * step;
		return (float) (bandStart + (step - 1.0));
	}

	/**
	 * Fatness gained from consuming one food stack: {@code nutrition} (hunger / 饱食度 restored) ×
	 * {@link FatnessConstants#FOOD_NUTRITION_WEIGHT}. Saturation does not count.
	 */
	public static float fatnessFromFood(ItemStack stack) {
		FoodProperties food = stack.get(DataComponents.FOOD);
		if (food == null) {
			return 0f;
		}
		return food.nutrition() * FatnessConstants.FOOD_NUTRITION_WEIGHT;
	}

	/** Pehkui horizontal scale (width / hitbox X extent). */
	public static float widthScale(float fatness) {
		if (fatness < FatnessConstants.NORMAL_LOW) {
			float severity = FatnessConstants.NORMAL_LOW - fatness;
			float scale = 1f - FatnessConstants.THIN_WIDTH_PER_POINT * severity;
			return Math.max(FatnessConstants.THIN_WIDTH_MIN, scale);
		}
		if (fatness > FatnessConstants.NORMAL_HIGH) {
			float severity = fatness - FatnessConstants.NORMAL_HIGH;
			return 1f + FatnessConstants.FAT_WIDTH_PER_POINT * severity;
		}
		return 1f;
	}

	/** Pehkui vertical scale (height). */
	public static float heightScale(float fatness) {
		if (fatness < FatnessConstants.NORMAL_LOW) {
			float severity = FatnessConstants.NORMAL_LOW - fatness;
			return 1f + FatnessConstants.THIN_HEIGHT_PER_POINT * severity;
		}
		if (fatness > FatnessConstants.NORMAL_HIGH) {
			float severity = fatness - FatnessConstants.NORMAL_HIGH;
			return Math.max(FatnessConstants.FAT_HEIGHT_MIN, 1f - FatnessConstants.FAT_HEIGHT_PER_POINT * severity);
		}
		return 1f;
	}

	/** Additive delta for {@link net.minecraft.world.entity.ai.attributes.Attributes#GRAVITY}. */
	public static float gravityAttributeDelta(float fatness) {
		if (fatness < FatnessConstants.NORMAL_LOW) {
			float severity = FatnessConstants.NORMAL_LOW - fatness;
			return FatnessConstants.THIN_GRAVITY_DELTA_PER_POINT * severity;
		}
		if (fatness > FatnessConstants.NORMAL_HIGH) {
			float severity = fatness - FatnessConstants.NORMAL_HIGH;
			return FatnessConstants.FAT_GRAVITY_DELTA_PER_POINT * severity;
		}
		return 0f;
	}

	/** Additive delta for {@link net.minecraft.world.entity.ai.attributes.Attributes#JUMP_STRENGTH} when underweight. */
	public static float jumpStrengthDelta(float fatness) {
		if (fatness >= FatnessConstants.NORMAL_LOW) {
			return 0f;
		}
		float severity = FatnessConstants.NORMAL_LOW - fatness;
		return Math.min(
			FatnessConstants.THIN_JUMP_STRENGTH_DELTA_MAX,
			FatnessConstants.THIN_JUMP_STRENGTH_DELTA_PER_POINT * severity
		);
	}

	/** Additive delta for {@link net.minecraft.world.entity.ai.attributes.Attributes#KNOCKBACK_RESISTANCE}. */
	public static float knockbackResistanceDelta(float fatness) {
		if (fatness < FatnessConstants.NORMAL_LOW) {
			float severity = FatnessConstants.NORMAL_LOW - fatness;
			return clamp(
				FatnessConstants.THIN_KNOCKBACK_RESIST_PER_POINT * severity,
				FatnessConstants.KNOCKBACK_RESIST_DELTA_MIN,
				FatnessConstants.KNOCKBACK_RESIST_DELTA_MAX
			);
		}
		if (fatness > FatnessConstants.NORMAL_HIGH) {
			float severity = fatness - FatnessConstants.NORMAL_HIGH;
			return clamp(
				FatnessConstants.FAT_KNOCKBACK_RESIST_PER_POINT * severity,
				FatnessConstants.KNOCKBACK_RESIST_DELTA_MIN,
				FatnessConstants.KNOCKBACK_RESIST_DELTA_MAX
			);
		}
		return 0f;
	}

	private static float clamp(float v, float min, float max) {
		return Math.max(min, Math.min(max, v));
	}

	/**
	 * Fall damage multiplier when underweight ({@code fatness < NORMAL_LOW}), up to {@link FatnessConstants#OSTEOPOROSIS_MULT_MAX}.
	 */
	public static float osteoporosisFallDamageMultiplier(float fatness) {
		if (fatness >= FatnessConstants.NORMAL_LOW) {
			return 1f;
		}
		float severity = FatnessConstants.NORMAL_LOW - fatness;
		float span = FatnessConstants.NORMAL_LOW;
		float t = clamp01(severity / span);
		return lerp(FatnessConstants.OSTEOPOROSIS_MULT_AT_THRESHOLD, FatnessConstants.OSTEOPOROSIS_MULT_MAX, t);
	}

	/** Kill roll probability while sprinting and obese, in {@code [HEART_ATTACK_PROB_MIN, HEART_ATTACK_PROB_MAX]}. */
	public static float heartAttackKillProbability(float fatness) {
		if (fatness <= FatnessConstants.NORMAL_HIGH) {
			return FatnessConstants.HEART_ATTACK_PROB_MIN;
		}
		float t = clamp01((fatness - FatnessConstants.NORMAL_HIGH)
			/ (FatnessConstants.HEART_ATTACK_PROB_FULL_AT_FATNESS - FatnessConstants.NORMAL_HIGH));
		return lerp(FatnessConstants.HEART_ATTACK_PROB_MIN, FatnessConstants.HEART_ATTACK_PROB_MAX, t);
	}

	/** Explosion power for obese landing from fall distance and fatness. */
	public static float fatExplosionPower(float fatness) {
		float extra = Math.max(0f, fatness - FatnessConstants.NORMAL_HIGH);
		return Math.min(
			FatnessConstants.FAT_EXPLOSION_POWER_MAX,
			FatnessConstants.FAT_EXPLOSION_POWER_BASE + extra * FatnessConstants.FAT_EXPLOSION_POWER_PER_POINT
		);
	}

	public static int thinMiningFatigueAmplifier(float fatness) {
		if (fatness >= FatnessConstants.NORMAL_LOW) {
			return -1;
		}
		float t = clamp01((FatnessConstants.NORMAL_LOW - fatness) / FatnessConstants.NORMAL_LOW);
		return Math.min(FatnessConstants.THIN_MINING_FATIGUE_MAX_AMPLIFIER, (int) Math.floor(t * 3f));
	}

	public static int fatStrengthAmplifier(float fatness) {
		if (fatness <= FatnessConstants.NORMAL_HIGH) {
			return -1;
		}
		float t = clamp01((fatness - FatnessConstants.NORMAL_HIGH) / 40f);
		return Math.min(FatnessConstants.FAT_STRENGTH_MAX_AMPLIFIER, (int) Math.floor(t * 4f));
	}

	public static int fatSlownessAmplifier(float fatness) {
		if (fatness <= FatnessConstants.NORMAL_HIGH) {
			return -1;
		}
		float t = clamp01((fatness - FatnessConstants.NORMAL_HIGH) / 40f);
		return Math.min(FatnessConstants.FAT_SLOWNESS_MAX_AMPLIFIER, (int) Math.floor(t * 4f));
	}

	public static int fatHungerAmplifier(float fatness) {
		if (fatness <= FatnessConstants.NORMAL_HIGH) {
			return -1;
		}
		float t = clamp01((fatness - FatnessConstants.NORMAL_HIGH) / 40f);
		return Math.min(FatnessConstants.FAT_HUNGER_MAX_AMPLIFIER, (int) Math.floor(t * 10f));
	}

	/** Thin-player mining drop multiplier in [1, 3]. */
	public static float thinMiningDropMultiplier(float fatness) {
		if (fatness >= FatnessConstants.NORMAL_LOW) {
			return 1f;
		}
		float t = clamp01((FatnessConstants.NORMAL_LOW - fatness) / FatnessConstants.NORMAL_LOW);
		return lerp(1f, FatnessConstants.THIN_MINING_DROP_MULT_MAX, t);
	}

	/** Fat-player mob drop multiplier in [1, 3]. */
	public static float fatMobDropMultiplier(float fatness) {
		if (fatness <= FatnessConstants.NORMAL_HIGH) {
			return 1f;
		}
		float t = clamp01((fatness - FatnessConstants.NORMAL_HIGH)
			/ (FatnessConstants.HEART_ATTACK_PROB_FULL_AT_FATNESS - FatnessConstants.NORMAL_HIGH));
		return lerp(1f, FatnessConstants.FAT_MOB_DROP_MULT_MAX, t);
	}
}
