package com.bodymorph;

import com.bodymorph.command.BodyMorphCommands;
import com.bodymorph.fatness.BodyMorphAttachments;
import com.bodymorph.fatness.FatnessGameEvents;
import com.bodymorph.network.BodyMorphNetworking;
import com.bodymorph.scale.EntityRandomScaleRegistration;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class BodyMorphMod implements ModInitializer {
	public static final String MOD_ID = "bodymorph";
	private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info(
			"Body Morph loaded (fatness attachment registered as {}).",
			BodyMorphAttachments.PLAYER_FATNESS.identifier()
		);
		FatnessGameEvents.register();
		BodyMorphCommands.register();
		BodyMorphNetworking.registerPayloadTypes();
		BodyMorphNetworking.registerServer();
		EntityRandomScaleRegistration.register();
	}
}
