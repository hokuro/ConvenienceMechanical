package mod.cvbox.block;

import mod.cvbox.block.ab.BlockFacingMachineContainer;
import mod.cvbox.core.ModCommon;
import mod.cvbox.core.Mod_ConvenienceBox;
import mod.cvbox.tileentity.TileEntitySetter;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockSetter extends BlockFacingMachineContainer {

	public BlockSetter() {
		super(Material.GROUND);
		this.setTickRandomly(true);
		this.nextUpdateTick = 20;
	}

	@Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		if (!super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ)){
	        if (worldIn.isRemote)
	        {
	        }
	        else
	        {
	        	playerIn.openGui(Mod_ConvenienceBox.instance, ModCommon.GUIID_SETTER, worldIn, pos.getX(), pos.getY(), pos.getZ());

	        }
		}
        return true;
    }

	@Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
		super.onBlockAdded(worldIn, pos, state);
    }

	@Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
    	super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
    }


	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntitySetter();
	}

	@Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

	@Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TileEntitySetter)
        {
            InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntitySetter)tileentity);
            worldIn.updateComparatorOutputLevel(pos, this);
        }

        super.breakBlock(worldIn, pos, state);
    }

	protected void SpawnBlock(World worldIn, BlockPos pos, EnumFacing front){
		BlockPos pos2 = pos.offset(front);
		IBlockState state = worldIn.getBlockState(pos2);
		if (state.getMaterial() != Material.AIR){
			return;
		}
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileEntitySetter){
        	TileEntitySetter seter = (TileEntitySetter)te;
        	int i = seter.getSettingSlot();
        	if (i >= 0){
        		ItemStack itemstack = seter.getStackInSlot(i);
        		Block setBlock = Block.getBlockFromItem(itemstack.getItem());
        		if (worldIn.setBlockState(pos2, setBlock.getStateFromMeta(itemstack.getMetadata()))){
        			itemstack.shrink(1);
        		}
        	}
        }
	}

	@Override
	public void onWork(World worldIn, IBlockState state, BlockPos pos) {
        if (!worldIn.isRemote)
        {
        	EnumFacing front = this.getDirection(state);
        	this.SpawnBlock(worldIn, pos, front);

            if (state.getValue(POWER))
            {
                this.setscheduleBlockUpdate(worldIn, pos);
            }
        }
	}
}
