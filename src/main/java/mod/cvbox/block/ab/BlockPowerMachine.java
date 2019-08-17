package mod.cvbox.block.ab;

import java.util.Random;

import org.apache.commons.lang3.BooleanUtils;

import mod.cvbox.item.ItemCore;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockPowerMachine extends Block implements IPowerMachine{

	protected static final BooleanProperty POWER = BooleanProperty.create("power");
	protected int nextUpdateTick = 200;
	protected int redstonePower = 0;

	public BlockPowerMachine(){
		this(Block.Properties.create(Material.GROUND));
	}

	public BlockPowerMachine(Block.Properties materialIn) {
		super(materialIn
				.needsRandomTick()
				.sound(SoundType.METAL)
				);

		this.setDefaultState(this.stateContainer.getBaseState().with(this.getPowerProperty(), false));
	}

	public int getRedPower(){
		return redstonePower;
	}

	public int getNextUpdateTic(){
		return this.nextUpdateTick;
	}

	public void setNextUpdateTick(int tick){
		this.nextUpdateTick = tick;
	}

//	public void setscheduleBlockUpdate(World worldIn, BlockPos pos){
//		worldIn.scheduleBlockUpdate(pos, this, this.nextUpdateTick, 1);
//	}

	@Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
		boolean flag =(this.redstonePower = getRedPower(worldIn, pos))>0;
		setPower(worldIn,pos,state,flag);
//		if (!worldIn.isRemote){
//			if (flag){
//				setscheduleBlockUpdate(worldIn,pos);
//			}
//		}
    }

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
		BlockPos pos2 = pos.add(0, 1, 0);
		boolean flag =(this.redstonePower = getRedPower(worldIn, pos))>0;
		setPower(worldIn,pos,state,flag);
//		if (!worldIn.isRemote){
//			if (flag){
//				setscheduleBlockUpdate(worldIn,pos);
//			}
//		}
    }

    @Override
    public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
    	ItemStack main = playerIn.getHeldItemMainhand();
    	if (main.getItem() == ItemCore.item_spana){
        	worldIn.setBlockState(pos,Blocks.AIR.getDefaultState());
        	if (!worldIn.isRemote){
        		InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(this,1));
        	}
        	worldIn.playSound((double)((float)pos.getX() + 0.5F), (double)((float)pos.getY() + 0.5F), (double)((float)pos.getZ() + 0.5F), SoundEvents.BLOCK_ANVIL_BREAK, SoundCategory.BLOCKS, 1.0F + worldIn.rand.nextFloat(), worldIn.rand.nextFloat() * 0.7F + 0.3F, false);
    	}
		return false;
    }

	@Override
    public void tick(IBlockState state, World worldIn, BlockPos pos, Random rand)
    {
		if (worldIn.getBlockState(pos).getBlock() == this){
			onWork(worldIn, state, pos);
		}
    }

	@Override
    public Item getItemDropped(IBlockState state, World worldIn, BlockPos pos, int fortune)
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

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder) {
	   builder.add(POWER);
	}


    protected BooleanProperty getPowerProperty()
    {
        return POWER;
    }

    public void setPower(World worldIn, BlockPos pos, IBlockState staet, boolean power){
    	worldIn.setBlockState(pos, this.withPower(staet,power), 2);
    }

    public int getPower(IBlockState state)
    {
        return BooleanUtils.toInteger(state.get(this.getPowerProperty()));
    }

    public IBlockState withPower(IBlockState staet,boolean power)
    {
        return staet.with(this.getPowerProperty(), power);
    }

    protected int getRedPower(World worldIn, BlockPos pos){
		BlockPos pos2 = pos.add(0, 1, 0);
		int power1 = worldIn.getRedstonePowerFromNeighbors(pos);
		int power2 = worldIn.getRedstonePowerFromNeighbors(pos2);
		return Math.max(power1, power2);
    }
}
