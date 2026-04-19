package com.bodymorph.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.bodymorph.fatness.FatnessMath;
import com.bodymorph.fatness.PlayerFatness;
import com.bodymorph.runtime.BodyMorphRuntime;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(Block.class)
abstract class BlockLootMultiplierMixin {
	@Inject(method = "playerDestroy", at = @At("TAIL"))
	private void bodymorph$thinMiningExtraDrops(
		Level level,
		Player player,
		BlockPos pos,
		BlockState state,
		BlockEntity blockEntity,
		ItemStack tool,
		CallbackInfo ci
	) {
		if (level.isClientSide() || !(player instanceof ServerPlayer sp)) {
			return;
		}
		if (!BodyMorphRuntime.isActive()) {
			return;
		}

		float multiplier = FatnessMath.thinMiningDropMultiplier(PlayerFatness.get(sp));
		if (multiplier <= 1f) {
			return;
		}

		int guaranteedExtraRolls = Mth.floor(multiplier - 1f);
		float fractionalChance = (multiplier - 1f) - guaranteedExtraRolls;

		for (int i = 0; i < guaranteedExtraRolls; i++) {
			Block.dropResources(state, level, pos, blockEntity, sp, tool);
		}
		if (fractionalChance > 0f && sp.getRandom().nextFloat() < fractionalChance) {
			Block.dropResources(state, level, pos, blockEntity, sp, tool);
		}
	}
}
