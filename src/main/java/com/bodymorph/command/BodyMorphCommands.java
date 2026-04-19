package com.bodymorph.command;

import com.bodymorph.feedback.BodyMorphText;
import com.bodymorph.runtime.BodyMorphRuntime;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
public final class BodyMorphCommands {
	private BodyMorphCommands() {
	}

	public static void register() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> register(dispatcher));
	}

	private static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(
			Commands.literal("bodymorph")
				.requires(source -> source.hasPermission(2))
				.then(
					Commands.literal("start")
						.executes(ctx -> {
							BodyMorphRuntime.start(ctx.getSource().getServer());
							ctx.getSource().sendSuccess(() -> BodyMorphText.cmdStart(), true);
							return 1;
						})
				)
				.then(
					Commands.literal("stop")
						.executes(ctx -> {
							BodyMorphRuntime.stop(ctx.getSource().getServer());
							ctx.getSource().sendSuccess(() -> BodyMorphText.cmdStop(), true);
							return 1;
						})
				)
				.then(
					Commands.literal("status")
						.executes(ctx -> {
							boolean active = BodyMorphRuntime.isActive();
							ctx.getSource().sendSuccess(() -> BodyMorphText.cmdStatus(active), false);
							return active ? 1 : 0;
						})
				)
		);
	}
}
