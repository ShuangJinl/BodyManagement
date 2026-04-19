package com.bodymorph.scale;

import com.bodymorph.fatness.FatnessConstants;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleType;
import virtuoel.pehkui.api.ScaleTypes;

/**
 * One-shot random Pehkui scales for non-player mobs. Pehkui exposes {@link ScaleTypes#WIDTH} (horizontal, usually XZ)
 * and {@link ScaleTypes#HEIGHT} (Y) for hitbox/model size; there is no separate “length-only” body axis in this API, so
 * the third random is applied to {@link ScaleTypes#HELD_ITEM} (handheld visual scale; neutral for empty-handed mobs).
 */
public final class EntityRandomScaleApplier {
	private EntityRandomScaleApplier() {
	}

	public static void apply(LivingEntity entity) {
		var rng = entity.getRandom();
		float w = Mth.nextFloat(rng, FatnessConstants.RANDOM_MOB_SCALE_MIN, FatnessConstants.RANDOM_MOB_SCALE_MAX);
		float h = Mth.nextFloat(rng, FatnessConstants.RANDOM_MOB_SCALE_MIN, FatnessConstants.RANDOM_MOB_SCALE_MAX);
		float len = Mth.nextFloat(rng, FatnessConstants.RANDOM_MOB_SCALE_MIN, FatnessConstants.RANDOM_MOB_SCALE_MAX);

		set(entity, ScaleTypes.WIDTH, w);
		set(entity, ScaleTypes.MODEL_WIDTH, w);
		set(entity, ScaleTypes.HITBOX_WIDTH, w);
		set(entity, ScaleTypes.INTERACTION_BOX_WIDTH, w);

		set(entity, ScaleTypes.HEIGHT, h);
		set(entity, ScaleTypes.MODEL_HEIGHT, h);
		set(entity, ScaleTypes.HITBOX_HEIGHT, h);
		set(entity, ScaleTypes.EYE_HEIGHT, h);
		set(entity, ScaleTypes.INTERACTION_BOX_HEIGHT, h);

		set(entity, ScaleTypes.HELD_ITEM, len);
	}

	private static void set(LivingEntity entity, ScaleType type, float value) {
		ScaleData data = type.getScaleData(entity);
		data.setScaleTickDelay(0);
		data.setScale(value);
		data.markForSync(true);
	}
}
