package com.bodymorph.fatness;

import com.bodymorph.BodyMorphMod;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;

/**
 * Registers persistent, synced data attachments for Body Morph.
 */
public final class BodyMorphAttachments {
	public static final ResourceLocation PLAYER_FATNESS_ID = ResourceLocation.fromNamespaceAndPath(
		BodyMorphMod.MOD_ID,
		"player_fatness"
	);

	public static final AttachmentType<Float> PLAYER_FATNESS = AttachmentRegistry.create(
		PLAYER_FATNESS_ID,
		builder -> builder
			.persistent(Codec.FLOAT)
			.initializer(() -> FatnessConstants.DEFAULT)
			.syncWith(ByteBufCodecs.FLOAT, AttachmentSyncPredicate.all())
			.copyOnDeath()
	);

	public static final ResourceLocation ENTITY_RANDOM_SCALE_APPLIED_ID = ResourceLocation.fromNamespaceAndPath(
		BodyMorphMod.MOD_ID,
		"entity_random_scale_applied"
	);

	/** When {@code true}, one-shot random Pehkui scales have already been applied (persisted). */
	public static final AttachmentType<Boolean> ENTITY_RANDOM_SCALE_APPLIED = AttachmentRegistry.create(
		ENTITY_RANDOM_SCALE_APPLIED_ID,
		builder -> builder
			.persistent(Codec.BOOL)
			.initializer(() -> false)
	);

	private BodyMorphAttachments() {
	}
}
