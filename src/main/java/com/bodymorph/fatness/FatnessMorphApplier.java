package com.bodymorph.fatness;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.bodymorph.BodyMorphMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleType;
import virtuoel.pehkui.api.ScaleTypes;

/**
 * Maps fatness to Pehkui scales and vanilla attributes (gravity, knockback resistance).
 * Runs on the logical server each tick (after fatness mutations in the same tick).
 */
public final class FatnessMorphApplier {
	private static final ResourceLocation GRAVITY_MODIFIER_ID = ResourceLocation.fromNamespaceAndPath(
		BodyMorphMod.MOD_ID,
		"fatness_gravity"
	);
	private static final ResourceLocation KNOCKBACK_RESIST_MODIFIER_ID = ResourceLocation.fromNamespaceAndPath(
		BodyMorphMod.MOD_ID,
		"fatness_knockback_resistance"
	);
	private static final ResourceLocation JUMP_STRENGTH_MODIFIER_ID = ResourceLocation.fromNamespaceAndPath(
		BodyMorphMod.MOD_ID,
		"fatness_jump_strength"
	);

	private static final ScaleType[] MORPH_SCALE_TYPES = {
		ScaleTypes.WIDTH,
		ScaleTypes.HEIGHT,
		ScaleTypes.HITBOX_WIDTH,
		ScaleTypes.HITBOX_HEIGHT,
		ScaleTypes.MODEL_WIDTH,
		ScaleTypes.MODEL_HEIGHT,
		ScaleTypes.EYE_HEIGHT,
		ScaleTypes.INTERACTION_BOX_WIDTH,
		ScaleTypes.INTERACTION_BOX_HEIGHT,
	};

	private static final Map<UUID, Float> LAST_APPLIED_FATNESS = new ConcurrentHashMap<>();

	private FatnessMorphApplier() {
	}

	public static void clear(ServerPlayer player) {
		LAST_APPLIED_FATNESS.remove(player.getUUID());
	}

	public static void deactivate(ServerPlayer player) {
		clear(player);
		resetPehkuiScales(player);
		clearAttributeModifiers(player);
	}

	public static void apply(ServerPlayer player) {
		if (!player.isAlive()) {
			return;
		}

		float fatness = PlayerFatness.get(player);
		UUID uuid = player.getUUID();
		Float prev = LAST_APPLIED_FATNESS.get(uuid);
		boolean fatnessChanged = prev == null || Math.abs(prev - fatness) >= 1e-4f;
		if (fatnessChanged) {
			LAST_APPLIED_FATNESS.put(uuid, fatness);
		}

		if (FatnessMath.isNormalFatness(fatness)) {
			if (fatnessChanged) {
				clearAttributeModifiers(player);
			}
		} else {
			applyGravity(player, FatnessMath.gravityAttributeDelta(fatness));
			applyKnockbackResistance(player, FatnessMath.knockbackResistanceDelta(fatness));
			applyJumpStrength(player, FatnessMath.jumpStrengthDelta(fatness));
		}

		float morphFatness = FatnessMath.fatnessQuantizedForMorph(fatness);
		float w = FatnessMath.widthScale(morphFatness);
		float h = FatnessMath.heightScale(morphFatness);
		setScaleLerped(player, ScaleTypes.WIDTH, w);
		setScaleLerped(player, ScaleTypes.MODEL_WIDTH, w);
		setScaleLerped(player, ScaleTypes.HITBOX_WIDTH, w);
		setScaleLerped(player, ScaleTypes.INTERACTION_BOX_WIDTH, w);

		setScaleLerped(player, ScaleTypes.HEIGHT, h);
		setScaleLerped(player, ScaleTypes.MODEL_HEIGHT, h);
		setScaleLerped(player, ScaleTypes.HITBOX_HEIGHT, h);
		setScaleLerped(player, ScaleTypes.EYE_HEIGHT, h);
		setScaleLerped(player, ScaleTypes.INTERACTION_BOX_HEIGHT, h);
	}

	private static void setScaleLerped(LivingEntity entity, ScaleType type, float target) {
		ScaleData data = type.getScaleData(entity);
		float current = data.getScale();
		float t = FatnessConstants.MORPH_SCALE_LERP_FACTOR;
		float next = current + (target - current) * t;
		if (Math.abs(target - next) < 1e-3f) {
			next = target;
		}
		data.setScaleTickDelay(0);
		data.setScale(next);
		data.markForSync(true);
	}

	private static void resetPehkuiScales(LivingEntity entity) {
		for (ScaleType type : MORPH_SCALE_TYPES) {
			type.getScaleData(entity).resetScale(true);
		}
	}

	private static void clearAttributeModifiers(ServerPlayer player) {
		AttributeInstance gravity = player.getAttribute(Attributes.GRAVITY);
		if (gravity != null) {
			gravity.removeModifier(GRAVITY_MODIFIER_ID);
		}
		AttributeInstance kb = player.getAttribute(Attributes.KNOCKBACK_RESISTANCE);
		if (kb != null) {
			kb.removeModifier(KNOCKBACK_RESIST_MODIFIER_ID);
		}
		AttributeInstance jump = player.getAttribute(Attributes.JUMP_STRENGTH);
		if (jump != null) {
			jump.removeModifier(JUMP_STRENGTH_MODIFIER_ID);
		}
	}

	private static void applyGravity(ServerPlayer player, float delta) {
		AttributeInstance gravity = player.getAttribute(Attributes.GRAVITY);
		if (gravity == null) {
			return;
		}
		gravity.removeModifier(GRAVITY_MODIFIER_ID);
		if (Math.abs(delta) < 1e-6f) {
			return;
		}
		gravity.addTransientModifier(
			new AttributeModifier(GRAVITY_MODIFIER_ID, delta, AttributeModifier.Operation.ADD_VALUE)
		);
	}

	private static void applyKnockbackResistance(ServerPlayer player, float delta) {
		AttributeInstance kb = player.getAttribute(Attributes.KNOCKBACK_RESISTANCE);
		if (kb == null) {
			return;
		}
		kb.removeModifier(KNOCKBACK_RESIST_MODIFIER_ID);
		if (Math.abs(delta) < 1e-6f) {
			return;
		}
		kb.addTransientModifier(
			new AttributeModifier(KNOCKBACK_RESIST_MODIFIER_ID, delta, AttributeModifier.Operation.ADD_VALUE)
		);
	}

	private static void applyJumpStrength(ServerPlayer player, float delta) {
		AttributeInstance jump = player.getAttribute(Attributes.JUMP_STRENGTH);
		if (jump == null) {
			return;
		}
		jump.removeModifier(JUMP_STRENGTH_MODIFIER_ID);
		if (Math.abs(delta) < 1e-6f) {
			return;
		}
		jump.addTransientModifier(
			new AttributeModifier(JUMP_STRENGTH_MODIFIER_ID, delta, AttributeModifier.Operation.ADD_VALUE)
		);
	}
}
