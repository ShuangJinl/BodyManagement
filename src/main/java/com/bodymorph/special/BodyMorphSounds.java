package com.bodymorph.special;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;

/**
 * Vanilla sound cues for Body Morph feedback (no custom .ogg assets required).
 */
public final class BodyMorphSounds {
	private BodyMorphSounds() {
	}

	public static void playOsteoporosisAlert(ServerPlayer player) {
		play(player, SoundEvents.BONE_BLOCK_BREAK, 0.55f, 0.82f);
	}

	public static void playFatLanding(ServerPlayer player) {
		play(player, SoundEvents.IRON_GOLEM_STEP, 0.9f, 0.65f);
		play(player, SoundEvents.GENERIC_EXPLODE.value(), 0.35f, 0.9f);
	}

	public static void playHeartStressWarning(ServerPlayer player) {
		play(player, SoundEvents.WARDEN_HEARTBEAT, 0.45f, 0.85f);
	}

	public static void playHeartAttackKill(ServerPlayer player) {
		play(player, SoundEvents.ELDER_GUARDIAN_CURSE, 0.5f, 1.15f);
	}

	public static void playFoodFatnessHint(ServerPlayer player) {
		play(player, SoundEvents.EXPERIENCE_ORB_PICKUP, 0.35f, 1.35f);
	}

	public static void playGameplayToggle(ServerPlayer player, boolean started) {
		if (started) {
			play(player, SoundEvents.NOTE_BLOCK_CHIME.value(), 0.55f, 1.2f);
		} else {
			play(player, SoundEvents.NOTE_BLOCK_BASS.value(), 0.5f, 0.75f);
		}
	}

	private static void play(ServerPlayer player, SoundEvent sound, float volume, float pitch) {
		Level level = player.level();
		level.playSound(null, player.getX(), player.getY(), player.getZ(), sound, SoundSource.PLAYERS, volume, pitch);
	}
}
