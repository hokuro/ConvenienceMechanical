package mod.cvbox.block;

import org.apache.commons.lang3.BooleanUtils;

import mod.cvbox.core.Mod_ConvenienceBox;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockPowerMachineContainer extends BlockContainer {
	protected static final PropertyBool POWER = PropertyBool.create("power");
	protected int nextUpdateTick = 200;
	protected int redstonePower = 0;

	public BlockPowerMachineContainer(Material materialIn) {
		super(materialIn);

		this.setDefaultState(this.blockState.getBaseState().withProperty(this.getPowerProperty(), false));
		this.setTickRandomly(true);
		this.setCreativeTab(Mod_ConvenienceBox.tabFactory);
		this.setHardness(0.5F);
		this.setSoundType(SoundType.METAL);
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


	@Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
		boolean flag =(this.redstonePower = getRedPower(worldIn, pos))>0;
		setPower(worldIn,pos,state,flag);
		if (!worldIn.isRemote){
			if (flag){
				setscheduleBlockUpdate(worldIn,pos);
			}
		}
    }

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
		BlockPos pos2 = pos.add(0, 1, 0);
		boolean flag =(this.redstonePower = getRedPower(worldIn, pos))>0;
		setPower(worldIn,pos,state,flag);
		if (!worldIn.isRemote){
			if (flag){
				setscheduleBlockUpdate(worldIn,pos);
			}
		}
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.withPower(this.getDefaultState(), BooleanUtils.toBoolean(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return this.getPower(state);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {POWER});
    }

    protected PropertyBool getPowerProperty()
    {
        return POWER;
    }

    public void setPower(World worldIn, BlockPos pos, IBlockState staet, boolean power){
    	worldIn.setBlockState(pos, this.withPower(staet,power), 2);
    }

    public int getPower(IBlockState state)
    {
        return BooleanUtils.toInteger(state.getValue(this.getPowerProperty()));
    }

    public IBlockState withPower(IBlockState staet,boolean power)
    {
        return staet.withProperty(this.getPowerProperty(), power);
    }

    protected int getRedPower(World worldIn, BlockPos pos){
		BlockPos pos2 = pos.add(0, 1, 0);
		int power1 = worldIn.isBlockIndirectlyGettingPowered(pos);
		int power2 = worldIn.isBlockIndirectlyGettingPowered(pos2);
		return Math.max(power1, power2);
    }

	public void setscheduleBlockUpdate(World worldIn, BlockPos pos){
		worldIn.scheduleBlockUpdate(pos, this, this.nextUpdateTick, 1);
	}


	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return null;
	}

	@Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }




	@Override
    public boolean shouldCheckWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
    {
        return false;
    }
}
