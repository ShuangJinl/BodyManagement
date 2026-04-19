package com.bodymorph.network;

import com.bodymorph.BodyMorphMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * Server → client: whether global Body Morph mechanics are enabled (for HUD / UX).
 */
public record BodyMorphGameplayPayload(boolean active) implements CustomPacketPayload {
	public static final CustomPacketPayload.Type<BodyMorphGameplayPayload> TYPE = new CustomPacketPayload.Type<>(
		ResourceLocation.fromNamespaceAndPath(BodyMorphMod.MOD_ID, "gameplay_state")
	);

	public static final StreamCodec<FriendlyByteBuf, BodyMorphGameplayPayload> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.BOOL,
		BodyMorphGameplayPayload::active,
		BodyMorphGameplayPayload::new
	);

	@Override
	public Type<BodyMorphGameplayPayload> type() {
		return TYPE;
	}
}
