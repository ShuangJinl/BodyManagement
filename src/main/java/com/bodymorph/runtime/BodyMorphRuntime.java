package com.bodymorph.runtime;

import com.bodymorph.fatness.FatnessConstants;
import com.bodymorph.fatness.FatnessMorphApplier;
import com.bodymorph.fatness.FatnessStatusEffects;
import com.bodymorph.fatness.MovementFatnessTracker;
import com.bodymorph.fatness.PlayerFatness;
import com.bodymorph.feedback.BodyMorphText;
import com.bodymorph.network.BodyMorphNetworking;
import com.bodymorph.special.BodyMorphSounds;
import com.bodymorph.special.HeartAttackTracker;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

/**
 * Global runtime switch for enabling/disabling Body Morph gameplay mechanics.
 */
public final class BodyMorphRuntime {
	private static volatile boolean active = false;

	private BodyMorphRuntime() {
	}

	public static boolean isActive() {
		return active;
	}

	public static void start(MinecraftServer server) {
		active = true;
		for (ServerPlayer player : server.getPlayerList().getPlayers()) {
			PlayerFatness.set(player, FatnessConstants.DEFAULT);
			MovementFatnessTracker.clear(player);
			HeartAttackTracker.clear(player);
			FatnessMorphApplier.apply(player);
			FatnessStatusEffects.onPlayerTick(player);
			BodyMorphSounds.playGameplayToggle(player, true);
		}
		server.getPlayerList().broadcastSystemMessage(BodyMorphText.broadcastGameplayStarted(), false);
		BodyMorphNetworking.broadcastGameplayState(server, true);
	}

	public static void stop(MinecraftServer server) {
		active = false;
		for (ServerPlayer player : server.getPlayerList().getPlayers()) {
			deactivateFor(player);
			BodyMorphSounds.playGameplayToggle(player, false);
		}
		server.getPlayerList().broadcastSystemMessage(BodyMorphText.broadcastGameplayStopped(), false);
		BodyMorphNetworking.broadcastGameplayState(server, false);
	}

	public static void deactivateFor(ServerPlayer player) {
		MovementFatnessTracker.clear(player);
		HeartAttackTracker.clear(player);
		FatnessMorphApplier.deactivate(player);
		FatnessStatusEffects.clear(player);
	}
}
