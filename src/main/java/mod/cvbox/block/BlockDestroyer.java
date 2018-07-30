package mod.cvbox.block;

import mod.cvbox.block.ab.BlockFacingContainer;
import mod.cvbox.core.ModCommon;
import mod.cvbox.core.Mod_ConvenienceBox;
import mod.cvbox.tileentity.TileEntityDestroyer;
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

public class BlockDestroyer extends BlockFacingContainer {

	public BlockDestroyer() {
		super(Material.GROUND);
		this.setCreativeTab(Mod_ConvenienceBox.tabFactory);
	}


	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityDestroyer();
	}

	@Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (!super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ)){
            if (!worldIn.isRemote)
            {
            	playerIn.openGui(Mod_ConvenienceBox.instance, ModCommon.GUIID_DESTROY, worldIn, pos.getX(), pos.getY(), pos.getZ());
            	return true;
            }
        }
        return false;
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
        }
        super.breakBlock(worldIn, pos, state);
    }


}
