package mod.cvbox.block;

import mod.cvbox.block.ab.BlockFacingContainer;
import mod.cvbox.intaractionobject.IntaractionObjectDestroyer;
import mod.cvbox.tileentity.TileEntityDestroyer;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockDestroyer extends BlockFacingContainer {

	public BlockDestroyer() {
		super(Block.Properties.create(Material.GROUND));
	}


	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		return new TileEntityDestroyer();
	}

	@Override
	public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {

        if (!super.onBlockActivated(state, worldIn, pos, playerIn, hand, side, hitX, hitY, hitZ)){
            if (!worldIn.isRemote)
            {
            	NetworkHooks.openGui((EntityPlayerMP)playerIn,
            			new IntaractionObjectDestroyer(pos),
            			(buf)->{
    						buf.writeInt(pos.getX());
    						buf.writeInt(pos.getY());
    						buf.writeInt(pos.getZ());
    					});
            	//playerIn.openGui(Mod_ConvenienceBox.instance, ModCommon.GUIID_DESTROY, worldIn, pos.getX(), pos.getY(), pos.getZ());
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
    public void onReplaced(IBlockState state, World worldIn, BlockPos pos, IBlockState newState, boolean isMoving)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof IInventory)
        {
            InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)tileentity);
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }


}
