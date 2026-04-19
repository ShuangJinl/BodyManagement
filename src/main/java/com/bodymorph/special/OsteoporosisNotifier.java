package com.bodymorph.special;

import com.bodymorph.feedback.BodyMorphFeedback;
import com.bodymorph.feedback.BodyMorphText;
import net.minecraft.server.level.ServerPlayer;

/**
 * Client feedback for osteoporosis-related fall damage spikes.
 */
public final class OsteoporosisNotifier {
	private OsteoporosisNotifier() {
	}

	public static void notifyIfSevere(ServerPlayer player) {
		if (!BodyMorphFeedback.tryOsteoporosisHint(player)) {
			return;
		}
		player.displayClientMessage(BodyMorphText.osteoporosisWarning(), true);
		BodyMorphSounds.playOsteoporosisAlert(player);
	}
}
