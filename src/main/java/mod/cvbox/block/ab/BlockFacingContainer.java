package mod.cvbox.block.ab;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockFacingContainer extends ContainerBlock {


	public static final DirectionProperty FACING =  BlockStateProperties.FACING;

	protected BlockFacingContainer(Block.Properties materialIn) {
		super(materialIn);
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH));
	}

	@Override
	public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        for (Direction enumfacing : Direction.Plane.HORIZONTAL) {
            BlockPos blockpos = pos.offset(enumfacing);
            BlockState iblockstate = worldIn.getBlockState(blockpos);
        }
    }

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState().with(FACING,  context.getNearestLookingDirection().getOpposite().getOpposite());
	}

	@Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate((Direction)state.get(FACING)));
    }

	@Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.toRotation((Direction)state.get(FACING)));
    }

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
}
