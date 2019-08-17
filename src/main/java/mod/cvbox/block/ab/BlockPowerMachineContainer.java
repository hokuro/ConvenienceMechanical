package mod.cvbox.block.ab;

import java.util.Random;

import mod.cvbox.item.ItemCore;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public abstract class BlockPowerMachineContainer extends BlockContainer implements IPowerMachine{
	protected int nextUpdateTick = 200;
	protected int redstonePower = 0;

	public BlockPowerMachineContainer(Block.Properties materialIn) {
		super(materialIn
				.needsRandomTick()
				.hardnessAndResistance(0.5F,0.5F)
				.sound(SoundType.METAL));
	}

	public int getNextUpdateTic(){
		return this.nextUpdateTick;
	}

	public void setNextUpdateTick(int tick){
		this.nextUpdateTick = tick;
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
    public void tick(IBlockState state, World worldIn, BlockPos pos, Random random)
    {
		if (worldIn.getBlockState(pos).getBlock() == this){
			onWork(worldIn, state,pos);
		}
    }

	@Override
	 public IItemProvider getItemDropped(IBlockState state, World worldIn, BlockPos pos, int fortune)
    {
        return ItemCore.item_machinematter;
    }

	@Override
    public int quantityDropped(IBlockState state, Random random)
    {
        return random.nextInt(3) + 1;
    }

	@Override
    protected boolean canSilkHarvest()
    {
        return false;
    }

//	public void setscheduleBlockUpdate(World worldIn, BlockPos pos){
//		worldIn.scheduleBlockUpdate(pos, this, this.nextUpdateTick, 1);
//	}


	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn){
		return null;
	}

	@Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }
}
