package mod.cvbox.block;

import mod.cvbox.block.ab.BlockPowerMachineContainer;
import mod.cvbox.intaractionobject.IntaractonObjectVacumer;
import mod.cvbox.tileentity.TileEntityVacumer;
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

public class BlockVacumer extends BlockPowerMachineContainer {

	public BlockVacumer() {
		super(Block.Properties.create(Material.GROUND));
	}

	@Override
	public TileEntity createNewTileEntity(IBlockReader world) {
		TileEntityVacumer ret = new TileEntityVacumer();
		return ret;
	}

	@Override
    public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (!super.onBlockActivated(state, worldIn, pos, playerIn, hand, facing, hitX, hitY, hitZ)){
            if (!worldIn.isRemote)
            {
            	NetworkHooks.openGui((EntityPlayerMP)playerIn,
            			new IntaractonObjectVacumer(pos),
            			(buf)->{
    						buf.writeInt(pos.getX());
    						buf.writeInt(pos.getY());
    						buf.writeInt(pos.getZ());
    					});
            	//playerIn.openGui(Mod_ConvenienceBox.instance, ModCommon.GUID_VACUMER, worldIn, pos.getX(), pos.getY(), pos.getZ());
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
    public void onReplaced(IBlockState state, World worldIn, BlockPos pos, IBlockState newstate, boolean isMoving)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof IInventory)
        {
            InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)tileentity);
            worldIn.updateComparatorOutputLevel(pos, this);
        }
        super.onReplaced(state, worldIn, pos, newstate, isMoving);
    }


	@Override
	public void onWork(World worldIn, IBlockState state, BlockPos pos) {
		// TODO 自動生成されたメソッド・スタブ

	}
}
