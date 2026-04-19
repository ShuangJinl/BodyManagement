package com.bodymorph.client;

import com.bodymorph.network.BodyMorphGameplayPayload;
import com.bodymorph.runtime.BodyMorphGameplayCache;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public final class BodyMorphClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(BodyMorphGameplayPayload.TYPE, (payload, context) ->
			context.client().execute(() -> BodyMorphGameplayCache.setGameplayActive(payload.active()))
		);
		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) ->
			client.execute(() -> BodyMorphGameplayCache.setGameplayActive(false))
		);
		BodyMorphHud.register();
	}
}
