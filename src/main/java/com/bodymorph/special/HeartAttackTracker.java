package com.bodymorph.special;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.bodymorph.fatness.FatnessConstants;
import com.bodymorph.fatness.FatnessMath;
import com.bodymorph.fatness.PlayerFatness;
import com.bodymorph.feedback.BodyMorphText;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;

/**
 * Obese sprinting players periodically roll for lethal cardiac arrest.
 */
public final class HeartAttackTracker {
	private static final Map<UUID, Integer> SPRINT_TICKS = new ConcurrentHashMap<>();

	private HeartAttackTracker() {
	}

	public static void clear(ServerPlayer player) {
		SPRINT_TICKS.remove(player.getUUID());
	}

	public static void onPlayerTick(ServerPlayer player) {
		if (player.level().isClientSide()) {
			return;
		}
		UUID id = player.getUUID();
		if (player.isCreative() || player.isSpectator()) {
			SPRINT_TICKS.remove(id);
			return;
		}
		float fatness = PlayerFatness.get(player);
		if (fatness <= FatnessConstants.NORMAL_HIGH || !player.isSprinting()) {
			SPRINT_TICKS.remove(id);
			return;
		}

		int ticks = SPRINT_TICKS.getOrDefault(id, 0) + 1;
		int warnAt = FatnessConstants.HEART_ATTACK_SPRINT_TICKS * 3 / 4;
		if (ticks == warnAt) {
			player.displayClientMessage(BodyMorphText.heartStressWarning(), true);
			BodyMorphSounds.playHeartStressWarning(player);
		}
		if (ticks >= FatnessConstants.HEART_ATTACK_SPRINT_TICKS) {
			SPRINT_TICKS.put(id, 0);
			tryHeartAttackRoll(player, fatness);
		} else {
			SPRINT_TICKS.put(id, ticks);
		}
	}

	private static void tryHeartAttackRoll(ServerPlayer player, float fatness) {
		float chance = FatnessMath.heartAttackKillProbability(fatness);
		if (player.getRandom().nextFloat() >= chance) {
			return;
		}

		if (player.getServer() != null) {
			String name = player.getGameProfile().getName();
			Component broadcast = BodyMorphText.heartAttackBroadcast(name);
			player.getServer().getPlayerList().broadcastSystemMessage(broadcast, false);
		}

		BodyMorphSounds.playHeartAttackKill(player);
		DamageSource kill = player.damageSources().genericKill();
		player.hurt(kill, Float.MAX_VALUE);
	}
}
