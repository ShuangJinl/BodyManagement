package com.bodymorph.client;

import com.bodymorph.fatness.PlayerFatness;
import com.bodymorph.feedback.BodyMorphText;
import com.bodymorph.runtime.BodyMorphGameplayCache;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public final class BodyMorphHud {
	private BodyMorphHud() {
	}

	public static void register() {
		HudRenderCallback.EVENT.register(BodyMorphHud::render);
	}

	private static void render(GuiGraphics graphics, net.minecraft.client.DeltaTracker deltaTracker) {
		Minecraft client = Minecraft.getInstance();
		if (client.player == null || client.options.hideGui) {
			return;
		}

		float fatness = PlayerFatness.get(client.player);
		String valueStr = String.format(java.util.Locale.ROOT, "%.1f", fatness);
		String label = "肥胖值 ";
		String suffix = BodyMorphGameplayCache.isGameplayActive() ? "" : " (未开启)";

		int y = 6;
		int x = 6;
		graphics.drawString(client.font, label, x, y, 0xFF000000 | BodyMorphText.RGB_ACCENT, true);
		x += client.font.width(label);
		int valueColor = BodyMorphGameplayCache.isGameplayActive()
			? BodyMorphText.RGB_SUCCESS
			: BodyMorphText.RGB_WARN;
		graphics.drawString(client.font, valueStr, x, y, 0xFF000000 | valueColor, true);
		x += client.font.width(valueStr);
		graphics.drawString(client.font, suffix, x, y, 0xFF000000 | BodyMorphText.RGB_MUTED, true);
	}
}
