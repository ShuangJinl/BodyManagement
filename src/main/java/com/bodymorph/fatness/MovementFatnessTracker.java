package com.bodymorph.fatness;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

/**
 * Tracks horizontal travel per player to apply periodic fatness loss.
 */
public final class MovementFatnessTracker {
	private static final Map<UUID, Vec3> LAST_POSITION = new ConcurrentHashMap<>();
	private static final Map<UUID, Float> ACCUMULATED_BLOCKS = new ConcurrentHashMap<>();

	private MovementFatnessTracker() {
	}

	public static void clear(ServerPlayer player) {
		UUID id = player.getUUID();
		LAST_POSITION.remove(id);
		ACCUMULATED_BLOCKS.remove(id);
	}

	public static void onPlayerTick(ServerPlayer player) {
		if (player.isSpectator() || player.isCreative() || player.isPassenger() || player.isFallFlying()) {
			LAST_POSITION.put(player.getUUID(), player.position());
			return;
		}

		Vec3 current = player.position();
		Vec3 last = LAST_POSITION.put(player.getUUID(), current);
		if (last == null) {
			return;
		}

		double dx = current.x - last.x;
		double dz = current.z - last.z;
		float horizontal = (float) Math.sqrt(dx * dx + dz * dz);
		if (horizontal > 16f) {
			horizontal = 16f;
		}

		float acc = ACCUMULATED_BLOCKS.getOrDefault(player.getUUID(), 0f) + horizontal;
		float threshold = FatnessConstants.MOVE_BLOCKS_PER_FATNESS_POINT;
		while (acc >= threshold) {
			PlayerFatness.add(player, -1f);
			acc -= threshold;
		}
		ACCUMULATED_BLOCKS.put(player.getUUID(), acc);
	}
}
