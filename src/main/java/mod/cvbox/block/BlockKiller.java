package mod.cvbox.block;

import mod.cvbox.block.ab.BlockPowerMachineContainer;
import mod.cvbox.core.ModCommon;
import mod.cvbox.core.Mod_ConvenienceBox;
import mod.cvbox.tileentity.TileEntityKiller;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockKiller extends BlockPowerMachineContainer {

	public BlockKiller() {
		super(Material.GROUND);
		this.setTickRandomly(false);
	}

	@Override
	public void setscheduleBlockUpdate(World worldIn, BlockPos pos){
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		TileEntityKiller ret = new TileEntityKiller();
		ret.setField(TileEntityKiller.FIELD_POWER, meta);
		return ret;
	}

	@Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		if (!super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ)){
	        if (!worldIn.isRemote)
	        {
	        	playerIn.openGui(Mod_ConvenienceBox.instance, ModCommon.GUIID_KILLER, worldIn, pos.getX(), pos.getY(), pos.getZ());
	        }
		}
        return true;
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

        if (tileentity instanceof IInventory)
        {
            InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)tileentity);
            worldIn.updateComparatorOutputLevel(pos, this);
        }
        super.breakBlock(worldIn, pos, state);
    }

	@Override
	public void onWork(World worldIn, IBlockState state, BlockPos pos) {
		// TODO 自動生成されたメソッド・スタブ

	}

}
