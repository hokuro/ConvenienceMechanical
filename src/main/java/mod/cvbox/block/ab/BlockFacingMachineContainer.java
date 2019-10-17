package mod.cvbox.block.ab;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockFacingMachineContainer extends BlockMachineContainer {
	public static final DirectionProperty FACING =  BlockStateProperties.FACING;

	public BlockFacingMachineContainer(Block.Properties materialIn) {
		super(materialIn);
	}


	@Override
	public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onBlockAdded(state, worldIn, pos, oldState, isMoving);
        this.setDefaultDirection(worldIn, pos, state);
    }

	public Direction getDirection(BlockState state){
		return state.get(FACING);
	}

    private void setDefaultDirection(World worldIn, BlockPos pos, BlockState state) {
        if (!worldIn.isRemote) {
            Direction enumfacing = (Direction)state.get(FACING);

            if (enumfacing == Direction.NORTH) {
                enumfacing = Direction.SOUTH;
            } else if (enumfacing == Direction.SOUTH) {
                enumfacing = Direction.NORTH;
            } else {
                if (enumfacing == Direction.WEST) {
                    enumfacing = Direction.EAST;
                } else if (enumfacing == Direction.EAST) {
                    enumfacing = Direction.WEST;
                }
            }

//            worldIn.setBlockState(pos, state.with(FACING, enumfacing));
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING,  context.getNearestLookingDirection().getOpposite());
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
    	super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        worldIn.setBlockState(pos, state
        		.with(FACING, state.get(FACING)));
    }


    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate((Direction)state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.toRotation((Direction)state.get(FACING)));
    }

    public static Direction getFacing(BlockState state){
    	return state.get(FACING);
    }

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(FACING);
	}
}
