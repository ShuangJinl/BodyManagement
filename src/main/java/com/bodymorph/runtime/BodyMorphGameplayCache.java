package com.bodymorph.runtime;

/**
 * Client-side mirror of whether Body Morph gameplay is active (set from server packets).
 * Lives in main sources so mixins can consult it without depending on the client entrypoint package.
 */
public final class BodyMorphGameplayCache {
	private static volatile boolean gameplayActive;

	private BodyMorphGameplayCache() {
	}

	public static void setGameplayActive(boolean active) {
		gameplayActive = active;
	}

	public static boolean isGameplayActive() {
		return gameplayActive;
	}
}
