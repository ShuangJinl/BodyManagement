package com.bodymorph.fatness;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

/**
 * Read/write API for the player's fatness attachment.
 * <p>
 * Mutations must run on the logical server; the value is replicated to tracking
 * clients via Fabric attachment sync.
 */
public final class PlayerFatness {
	private PlayerFatness() {
	}

	public static float get(Player player) {
		return player.getAttachedOrCreate(BodyMorphAttachments.PLAYER_FATNESS);
	}

	public static void set(Player player, float value) {
		if (player.level().isClientSide()) {
			throw new IllegalStateException("PlayerFatness.set is logical-server only");
		}
		player.setAttached(BodyMorphAttachments.PLAYER_FATNESS, value);
	}

	public static float add(Player player, float delta) {
		if (player.level().isClientSide()) {
			throw new IllegalStateException("PlayerFatness.add is logical-server only");
		}
		return player.modifyAttached(BodyMorphAttachments.PLAYER_FATNESS, current -> current + delta);
	}

	public static boolean has(Entity entity) {
		return entity.hasAttached(BodyMorphAttachments.PLAYER_FATNESS);
	}
}
