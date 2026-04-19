package com.bodymorph.fatness;

/**
 * Tunable constants for the fatness system (gameplay rules live in later steps).
 */
public final class FatnessConstants {
	/** Initial fatness for new players (and before first save). */
	public static final float DEFAULT = 50f;

	/** Inclusive range considered "normal" for body shape. */
	public static final float NORMAL_LOW = 40f;
	public static final float NORMAL_HIGH = 60f;

	/** Horizontal distance (blocks) that removes one fatness point while moving on foot. */
	public static final float MOVE_BLOCKS_PER_FATNESS_POINT = 32f;

	/** Fatness change on death when not underweight (negative = loss). */
	public static final float DEATH_FATNESS_DELTA = -5f;

	/** Fatness change on death when underweight ({@code fatness < NORMAL_LOW}); starvation / wasting theme. */
	public static final float THIN_DEATH_FATNESS_DELTA = 5f;

	/**
	 * Obesity gained per point of hunger restored by food ({@code FoodProperties#nutrition}, i.e. drumsticks / “饱食度”).
	 * Total fatness from one item = {@code nutrition * FOOD_NUTRITION_WEIGHT}.
	 */
	public static final float FOOD_NUTRITION_WEIGHT = 1f;

	/**
	 * Each tick, Pehkui width/height scales move this fraction of the remaining distance toward the target.
	 * Slightly higher so small target changes still settle quickly while amplitudes stay gentle.
	 */
	public static final float MORPH_SCALE_LERP_FACTOR = 0.45f;

	/**
	 * Pehkui targets use obesity in {@code MORPH_FATNESS_STEP}-wide bands; within each band the model matches the
	 * <strong>upper</strong> end (e.g. step 3 → 36–38 → 38).
	 */
	public static final float MORPH_FATNESS_STEP = 3f;

	/** Random Pehkui scale range for non-player mobs (inclusive). */
	public static final float RANDOM_MOB_SCALE_MIN = 0.3f;
	public static final float RANDOM_MOB_SCALE_MAX = 3f;

	/** Pehkui width multiplier per point below {@link #NORMAL_LOW} (thinner); kept small to avoid extreme stick figures. */
	public static final float THIN_WIDTH_PER_POINT = 0.0075f;

	/** Minimum Pehkui width scale when very thin (prevents unrealistically narrow silhouettes). */
	public static final float THIN_WIDTH_MIN = 0.48f;

	/** Pehkui height multiplier per point below {@link #NORMAL_LOW} (taller); gentler than legacy values. */
	public static final float THIN_HEIGHT_PER_POINT = 0.014f;

	/** Pehkui width multiplier per point above {@link #NORMAL_HIGH} (wider). */
	public static final float FAT_WIDTH_PER_POINT = 0.022f;

	/** Pehkui height multiplier per point above {@link #NORMAL_HIGH} (shorter). */
	public static final float FAT_HEIGHT_PER_POINT = 0.014f;

	/**
	 * Minimum Pehkui height scale when very fat: about half a block tall vs ~1.8 block vanilla player.
	 * Height does not shrink further once this floor is reached.
	 */
	public static final float FAT_HEIGHT_MIN = 0.5f / 1.8f;

	/** Additive gravity attribute delta per thin point (negative = lighter). */
	public static final float THIN_GRAVITY_DELTA_PER_POINT = -0.0022f;

	/** Additive gravity attribute delta per fat point (positive = heavier). */
	public static final float FAT_GRAVITY_DELTA_PER_POINT = 0.0028f;

	/** Additive knockback resistance per thin point (negative = more knockback). */
	public static final float THIN_KNOCKBACK_RESIST_PER_POINT = -0.012f;

	/** Additive knockback resistance per fat point (positive = less knockback). */
	public static final float FAT_KNOCKBACK_RESIST_PER_POINT = 0.018f;

	/** Additive {@link net.minecraft.world.entity.ai.attributes.Attributes#JUMP_STRENGTH} per thin point (higher = jump higher). */
	public static final float THIN_JUMP_STRENGTH_DELTA_PER_POINT = 0.0026f;

	/** Cap additive jump strength from thinness so values stay within sensible vanilla ranges. */
	public static final float THIN_JUMP_STRENGTH_DELTA_MAX = 0.12f;

	/** Clamp total additive knockback resistance so vanilla math stays reasonable. */
	public static final float KNOCKBACK_RESIST_DELTA_MIN = -0.35f;
	public static final float KNOCKBACK_RESIST_DELTA_MAX = 0.75f;

	// --- Step 5: special mechanics ---

	/** Minimum fall distance (blocks) for obese landing explosion. */
	public static final float FAT_EXPLOSION_MIN_FALL_BLOCKS = 5f;

	/** Explosion power baseline when just above obese threshold. */
	public static final float FAT_EXPLOSION_POWER_BASE = 2.6f;

	/** Explosion power per point of fatness above {@link #NORMAL_HIGH}. */
	public static final float FAT_EXPLOSION_POWER_PER_POINT = 0.12f;

	public static final float FAT_EXPLOSION_POWER_MAX = 12f;

	/** Osteoporosis fall damage multiplier at {@link #NORMAL_LOW} (no extra damage). */
	public static final float OSTEOPOROSIS_MULT_AT_THRESHOLD = 1f;

	/** Osteoporosis fall damage multiplier when fatness approaches 0 (worst case). */
	public static final float OSTEOPOROSIS_MULT_MAX = 2f;

	/** Show osteoporosis action bar when multiplier is at least this value. */
	public static final float OSTEOPOROSIS_NOTIFY_MULT = 1.5f;

	/** Minimum vanilla fall damage (before multiplier) to trigger osteoporosis UI. */
	public static final float OSTEOPOROSIS_NOTIFY_MIN_DAMAGE = 2f;

	/** Sprint ticks per heart-attack roll window (15 seconds). */
	public static final int HEART_ATTACK_SPRINT_TICKS = 20 * 15;

	/** Heart attack kill chance at {@link #NORMAL_HIGH} (lower bound). */
	public static final float HEART_ATTACK_PROB_MIN = 0.05f;

	/** Heart attack kill chance at high obesity (upper bound). */
	public static final float HEART_ATTACK_PROB_MAX = 0.80f;

	/** Fatness at which heart attack probability reaches {@link #HEART_ATTACK_PROB_MAX}. */
	public static final float HEART_ATTACK_PROB_FULL_AT_FATNESS = 100f;

	// --- Step 6: status effects and drop multipliers ---
	public static final int THIN_MINING_FATIGUE_MAX_AMPLIFIER = 2;
	public static final int FAT_STRENGTH_MAX_AMPLIFIER = 3;
	public static final int FAT_SLOWNESS_MAX_AMPLIFIER = 3;
	public static final int FAT_HUNGER_MAX_AMPLIFIER = 9;

	/** Maximum block-drop multiplier for thin players (1x..3x). */
	public static final float THIN_MINING_DROP_MULT_MAX = 3f;

	/** Maximum mob-drop multiplier for fat players (1x..3x). */
	public static final float FAT_MOB_DROP_MULT_MAX = 3f;

	private FatnessConstants() {
	}
}
