package vktec.rulebook.mixin;

import net.minecraft.block.Block;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import vktec.rulebook.RulebookMod;

@Mixin(World.class)
abstract class WorldMixin {
	@Shadow abstract GameRules getGameRules();
	@Shadow abstract void updateNeighbor(BlockPos to, Block block, BlockPos from);

	/**
	 * @reason Change update order
	 * @author vktec
	 */
	@Overwrite
	public void updateNeighborsAlways(BlockPos pos, Block block) {
		boolean xzy = this.getGameRules().getBoolean(RulebookMod.XZY_BLOCK_UPDATES);

		this.updateNeighbor(pos.west(), block, pos);
		this.updateNeighbor(pos.east(), block, pos);

		if (!xzy) {
			this.updateNeighbor(pos.down(), block, pos);
			this.updateNeighbor(pos.up(), block, pos);
		}

		this.updateNeighbor(pos.north(), block, pos);
		this.updateNeighbor(pos.south(), block, pos);

		if (xzy) {
			this.updateNeighbor(pos.down(), block, pos);
			this.updateNeighbor(pos.up(), block, pos);
		}
    }

	/**
	 * @reason Change update order
	 * @author vktec
	 */
	@Overwrite
    public void updateNeighborsExcept(BlockPos pos, Block sourceBlock, Direction direction) {
		boolean xzy = this.getGameRules().getBoolean(RulebookMod.XZY_BLOCK_UPDATES);

		if (direction != Direction.WEST) {
			this.updateNeighbor(pos.west(), sourceBlock, pos);
		}
		if (direction != Direction.EAST) {
			this.updateNeighbor(pos.east(), sourceBlock, pos);
		}

		if (!xzy) {
			if (direction != Direction.NORTH) {
				this.updateNeighbor(pos.north(), sourceBlock, pos);
			}
			if (direction != Direction.SOUTH) {
				this.updateNeighbor(pos.south(), sourceBlock, pos);
			}
	    }

		if (direction != Direction.DOWN) {
			this.updateNeighbor(pos.down(), sourceBlock, pos);
		}
		if (direction != Direction.UP) {
			this.updateNeighbor(pos.up(), sourceBlock, pos);
		}

		if (xzy) {
			if (direction != Direction.NORTH) {
				this.updateNeighbor(pos.north(), sourceBlock, pos);
			}
			if (direction != Direction.SOUTH) {
				this.updateNeighbor(pos.south(), sourceBlock, pos);
			}
	    }
    }
}
