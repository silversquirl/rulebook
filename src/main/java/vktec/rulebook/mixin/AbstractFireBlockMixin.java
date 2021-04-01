package vktec.rulebook.mixin;

import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import vktec.rulebook.RulebookMod;

@Mixin(value = AbstractFireBlock.class)
abstract class AbstractFireBlockMixin {
	@Redirect(
		method = "method_30032",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/block/BlockState;canPlaceAt(Lnet/minecraft/world/WorldView;Lnet/minecraft/util/math/BlockPos;)Z"
		)
	)
	private static boolean ruleLightAir(BlockState state, WorldView view, BlockPos pos, World world) {
		return world.getGameRules().getBoolean(RulebookMod.LIGHT_FIRES_IN_AIR) ||
			state.canPlaceAt(view, pos);
	}
}
