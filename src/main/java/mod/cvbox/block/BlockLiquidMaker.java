package mod.cvbox.block;

import mod.cvbox.block.ab.BlockPowerMachineContainer;
import mod.cvbox.core.ModCommon;
import mod.cvbox.core.Mod_ConvenienceBox;
import mod.cvbox.tileentity.TileEntityLiquidMaker;
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

public class BlockLiquidMaker extends BlockPowerMachineContainer {



	public BlockLiquidMaker() {
		super(Material.GROUND);
		this.setCreativeTab(Mod_ConvenienceBox.tabFactory);
	}


	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		TileEntityLiquidMaker ret = new TileEntityLiquidMaker();
		return ret;
	}

	@Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (!super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ)){
            if (!worldIn.isRemote)
            {
            	playerIn.openGui(Mod_ConvenienceBox.instance, ModCommon.GUIID_LIQUIDMAKER, worldIn, pos.getX(), pos.getY(), pos.getZ());
            	return true;
            }
        }
        return false;
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
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

	@Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

	@Override
    public boolean isFullBlock(IBlockState state)
    {
        return false;
    }


	@Override
	public void onWork(World worldIn, IBlockState state, BlockPos pos) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.INVISIBLE;
    }
}
