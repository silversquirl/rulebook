package vktec.rulebook.mixin;

import java.util.Random;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PillarBlock;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;

import vktec.rulebook.RulebookMod;

@Mixin(PillarBlock.class)
abstract class PillarBlockMixin extends Block {
	private static final Direction[] DIRECTION_CACHE = Direction.values(); // values allocates, so cache it

	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		if (world.getGameRules().getBoolean(RulebookMod.BONE_BLOCK_FERTILIZER)) return;
		if (world.isClient()) return; // Must be server
		if (!state.isOf(Blocks.BONE_BLOCK)) return; // Must be bone block
		if (state.isOf(oldState.getBlock())) return; // Must be replacing something else
		this.bonemealAround(world, pos);
	}

	private void bonemealAround(World world, BlockPos pos) {
		ItemStack stack = new ItemStack(null);
		Random rand = world.getRandom();

		for (Direction dir : DIRECTION_CACHE) {
			BlockPos offsetPos = pos.offset(dir);
			if (BoneMealItem.useOnFertilizable(stack, world, offsetPos)
				|| BoneMealItem.useOnGround(stack, world, offsetPos, null))
			{
				world.syncWorldEvent(2005, offsetPos, 0);
				if (rand.nextInt(9) < 1) { // 1/9 chance
					world.removeBlock(pos, false);
					break;
				}
			}
		}
	}

	private PillarBlockMixin(AbstractBlock.Settings settings) { super(settings); }
}
