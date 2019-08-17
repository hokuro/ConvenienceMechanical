package mod.cvbox.block.ab;

import java.util.Random;

import mod.cvbox.item.ItemCore;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockFacingContainer extends BlockContainer {


	public static final DirectionProperty FACING =  BlockStateProperties.FACING;

	protected BlockFacingContainer(Block.Properties materialIn) {
		super(materialIn);
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, EnumFacing.NORTH));
	}

	@Override
	public void onBlockAdded(IBlockState state, World worldIn, BlockPos pos, IBlockState oldState) {

        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
        {
            BlockPos blockpos = pos.offset(enumfacing);
            IBlockState iblockstate = worldIn.getBlockState(blockpos);
        }
    }

	@Override
	public IBlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState().with(FACING,  context.getNearestLookingDirection().getOpposite().getOpposite());
	}



	@Override
    public IBlockState rotate(IBlockState state, Rotation rot)
    {
        return state.with(FACING, rot.rotate((EnumFacing)state.get(FACING)));
    }

	@Override
    public IBlockState mirror(IBlockState state, Mirror mirrorIn)
    {
        return state.rotate(mirrorIn.toRotation((EnumFacing)state.get(FACING)));
    }

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder) {
	   builder.add(FACING);
	}

    @Override
    public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    	ItemStack main = playerIn.getHeldItemMainhand();
    	if (main.getItem() == ItemCore.item_spana){
    		if(!worldIn.isRemote){
    			this.onReplaced(state, worldIn, pos, Blocks.AIR.getDefaultState(),false);
        		InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(this,1));
    		}
    		worldIn.playSound((double)((float)pos.getX() + 0.5F), (double)((float)pos.getY() + 0.5F), (double)((float)pos.getZ() + 0.5F), SoundEvents.BLOCK_ANVIL_BREAK, SoundCategory.BLOCKS, 1.0F + worldIn.rand.nextFloat(), worldIn.rand.nextFloat() * 0.7F + 0.3F, false);
    		worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
    		return true;
    	}
		return false;
    }

	@Override
    public int quantityDropped(IBlockState state, Random random)
    {
        return random.nextInt(3) + 1;
    }

	@Override
	 public IItemProvider getItemDropped(IBlockState state, World worldIn, BlockPos pos, int fortune)
   {
       return ItemCore.item_machinematter;
   }
}
