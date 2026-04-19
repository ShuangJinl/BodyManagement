package com.bodymorph.fatness;

import com.bodymorph.feedback.BodyMorphFeedback;
import com.bodymorph.special.HeartAttackTracker;
import com.bodymorph.runtime.BodyMorphRuntime;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.server.level.ServerPlayer;

/**
 * Server-side Fabric callbacks that mutate {@link PlayerFatness}.
 */
public final class FatnessGameEvents {
	private FatnessGameEvents() {
	}

	public static void register() {
		ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> {
			if (!BodyMorphRuntime.isActive()) {
				return;
			}
			if (entity instanceof ServerPlayer sp) {
				float fatness = PlayerFatness.get(sp);
				float delta = fatness < FatnessConstants.NORMAL_LOW
					? FatnessConstants.THIN_DEATH_FATNESS_DELTA
					: FatnessConstants.DEATH_FATNESS_DELTA;
				PlayerFatness.add(sp, delta);
			}
		});

		ServerPlayerEvents.LEAVE.register(player -> {
			MovementFatnessTracker.clear(player);
			FatnessMorphApplier.clear(player);
			HeartAttackTracker.clear(player);
			BodyMorphFeedback.clearPlayer(player);
		});

		ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
			if (alive) {
				MovementFatnessTracker.clear(newPlayer);
				HeartAttackTracker.clear(newPlayer);
				BodyMorphFeedback.clearPlayer(newPlayer);
			}
		});

		ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
			MovementFatnessTracker.clear(newPlayer);
			HeartAttackTracker.clear(newPlayer);
			BodyMorphFeedback.clearPlayer(newPlayer);
		});
	}

}
