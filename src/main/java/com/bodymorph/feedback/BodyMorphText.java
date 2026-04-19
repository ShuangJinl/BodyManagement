package com.bodymorph.feedback;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

/**
 * Colored chat / hotbar text for Body Morph feedback.
 */
public final class BodyMorphText {
	public static final int RGB_SUCCESS = 0x55FF88;
	public static final int RGB_STOP = 0xFF6B6B;
	public static final int RGB_INFO = 0x7EC8FF;
	public static final int RGB_WARN = 0xFFCC55;
	public static final int RGB_DANGER = 0xFF4444;
	public static final int RGB_THIN = 0x66D4FF;
	public static final int RGB_FAT = 0xFFAA55;
	public static final int RGB_MUTED = 0xB0B0B0;
	public static final int RGB_ACCENT = 0xD4A5FF;

	private BodyMorphText() {
	}

	public static MutableComponent rgb(String text, int rgb) {
		return Component.literal(text).withStyle(Style.EMPTY.withColor(TextColor.fromRgb(rgb)));
	}

	public static MutableComponent cmdStart() {
		return prefix()
			.append(rgb("玩法已开始", RGB_SUCCESS))
			.append(rgb(" — 在线玩家肥胖值已重置为默认。", RGB_MUTED));
	}

	public static MutableComponent cmdStop() {
		return prefix()
			.append(rgb("玩法已结束", RGB_STOP))
			.append(rgb("，体型与相关效果已恢复。", RGB_MUTED));
	}

	public static MutableComponent cmdStatus(boolean active) {
		MutableComponent line = prefix().append(rgb("当前状态：", RGB_INFO));
		if (active) {
			line.append(rgb("运行中", RGB_SUCCESS));
		} else {
			line.append(rgb("未开始", RGB_MUTED));
		}
		return line;
	}

	public static MutableComponent broadcastGameplayStarted() {
		return prefix()
			.append(rgb("全局玩法已", RGB_INFO))
			.append(rgb("开启", RGB_SUCCESS))
			.append(rgb("。体型与肥胖规则生效。", RGB_MUTED));
	}

	public static MutableComponent broadcastGameplayStopped() {
		return prefix()
			.append(rgb("全局玩法已", RGB_INFO))
			.append(rgb("关闭", RGB_STOP))
			.append(rgb("。", RGB_MUTED));
	}

	public static MutableComponent osteoporosisWarning() {
		return rgb("骨质疏松", RGB_DANGER)
			.append(rgb(" · 摔落伤害加重，注意高度！", RGB_WARN));
	}

	public static MutableComponent fatLandingShock(float fallBlocks, float blastPower) {
		return rgb("沉重落地！", RGB_FAT)
			.append(rgb(String.format(" 下落 %.1f 格 · 冲击 %.1f", fallBlocks, blastPower), RGB_WARN));
	}

	public static MutableComponent heartStressWarning() {
		return rgb("心脏负荷", RGB_WARN)
			.append(rgb(" · 肥胖状态下长跑很危险，放慢脚步！", RGB_MUTED));
	}

	public static MutableComponent heartAttackBroadcast(String playerName) {
		return Component.literal(playerName).withStyle(
				Style.EMPTY.withBold(true).withColor(TextColor.fromRgb(RGB_FAT))
			)
			.append(rgb(" 因剧烈运动引发 ", RGB_MUTED))
			.append(rgb("心脏骤停", RGB_DANGER))
			.append(rgb(" 倒地。", RGB_MUTED));
	}

	public static MutableComponent foodFatnessHint(float delta) {
		return rgb("进食", RGB_FAT)
			.append(rgb(String.format(" · 肥胖值 +%.1f", delta), RGB_WARN));
	}

	private static MutableComponent prefix() {
		return rgb("[体型演变] ", RGB_ACCENT);
	}
}
