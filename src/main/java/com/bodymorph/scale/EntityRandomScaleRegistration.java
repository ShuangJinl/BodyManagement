package com.bodymorph.scale;

import com.bodymorph.fatness.BodyMorphAttachments;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;

public final class EntityRandomScaleRegistration {
	private EntityRandomScaleRegistration() {
	}

	public static void register() {
		ServerEntityEvents.ENTITY_LOAD.register(EntityRandomScaleRegistration::onEntityLoad);
	}

	private static void onEntityLoad(Entity entity, ServerLevel world) {
		if (!(entity instanceof LivingEntity living)) {
			return;
		}
		if (living instanceof Player || living instanceof ArmorStand) {
			return;
		}
		if (Boolean.TRUE.equals(living.getAttachedOrElse(BodyMorphAttachments.ENTITY_RANDOM_SCALE_APPLIED, false))) {
			return;
		}
		EntityRandomScaleApplier.apply(living);
		living.setAttached(BodyMorphAttachments.ENTITY_RANDOM_SCALE_APPLIED, true);
	}
}
