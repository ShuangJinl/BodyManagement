package com.bodymorph.feedback;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.bodymorph.special.BodyMorphSounds;
import net.minecraft.server.level.ServerPlayer;

/**
 * Rate-limited hotbar hints (food gain, etc.).
 */
public final class BodyMorphFeedback {
	private static final Map<UUID, Long> LAST_FOOD_HINT_TICK = new ConcurrentHashMap<>();
	private static final Map<UUID, Long> LAST_OSTEOPOROSIS_TICK = new ConcurrentHashMap<>();

	private static final long FOOD_HINT_COOLDOWN_TICKS = 24L;
	private static final long OSTEOPOROSIS_COOLDOWN_TICKS = 64L;

	private BodyMorphFeedback() {
	}

	public static void clearPlayer(ServerPlayer player) {
		UUID id = player.getUUID();
		LAST_FOOD_HINT_TICK.remove(id);
		LAST_OSTEOPOROSIS_TICK.remove(id);
	}

	public static void tryFoodFatnessHint(ServerPlayer player, float gain) {
		if (gain < 0.75f) {
			return;
		}
		long now = player.level().getGameTime();
		UUID id = player.getUUID();
		Long last = LAST_FOOD_HINT_TICK.get(id);
		if (last != null && now - last < FOOD_HINT_COOLDOWN_TICKS) {
			return;
		}
		LAST_FOOD_HINT_TICK.put(id, now);
		player.displayClientMessage(BodyMorphText.foodFatnessHint(gain), true);
		BodyMorphSounds.playFoodFatnessHint(player);
	}

	public static boolean tryOsteoporosisHint(ServerPlayer player) {
		long now = player.level().getGameTime();
		UUID id = player.getUUID();
		Long last = LAST_OSTEOPOROSIS_TICK.get(id);
		if (last != null && now - last < OSTEOPOROSIS_COOLDOWN_TICKS) {
			return false;
		}
		LAST_OSTEOPOROSIS_TICK.put(id, now);
		return true;
	}
}
