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

public abstract class BlockHorizontalContainer extends ContainerBlock {

	public static final DirectionProperty FACING =  BlockStateProperties.FACING;

	protected BlockHorizontalContainer(Block.Properties property) {
		super(property);
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH));
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
	}

    private void setDefaultFacing(World worldIn, BlockPos pos, BlockState state) {
        if (!worldIn.isRemote) {
            BlockState iblockstate = worldIn.getBlockState(pos.north());
            BlockState iblockstate1 = worldIn.getBlockState(pos.south());
            BlockState iblockstate2 = worldIn.getBlockState(pos.west());
            BlockState iblockstate3 = worldIn.getBlockState(pos.east());
            Direction enumfacing = (Direction)state.get(FACING);

            if (enumfacing == Direction.NORTH) {
                enumfacing = Direction.SOUTH;
            } else if (enumfacing == Direction.SOUTH) {
                enumfacing = Direction.NORTH;
            } else if (enumfacing == Direction.WEST) {
                enumfacing = Direction.EAST;
            } else if (enumfacing == Direction.EAST) {
                enumfacing = Direction.WEST;
            }

            worldIn.setBlockState(pos, state.with(FACING, enumfacing), 2);
        }
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
