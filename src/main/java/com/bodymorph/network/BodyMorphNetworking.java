package com.bodymorph.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import com.bodymorph.runtime.BodyMorphRuntime;

public final class BodyMorphNetworking {
	private BodyMorphNetworking() {
	}

	public static void registerPayloadTypes() {
		PayloadTypeRegistry.playS2C().register(BodyMorphGameplayPayload.TYPE, BodyMorphGameplayPayload.STREAM_CODEC);
	}

	public static void registerServer() {
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
			ServerPlayNetworking.send(handler.player, new BodyMorphGameplayPayload(BodyMorphRuntime.isActive()))
		);
	}

	public static void broadcastGameplayState(MinecraftServer server, boolean active) {
		BodyMorphGameplayPayload payload = new BodyMorphGameplayPayload(active);
		for (ServerPlayer player : server.getPlayerList().getPlayers()) {
			ServerPlayNetworking.send(player, payload);
		}
	}
}
