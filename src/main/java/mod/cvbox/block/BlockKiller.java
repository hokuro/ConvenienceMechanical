package mod.cvbox.block;

import mod.cvbox.block.ab.BlockPowerMachineContainer;
import mod.cvbox.intaractionobject.IntaractionObjectKiller;
import mod.cvbox.tileentity.TileEntityKiller;
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

public class BlockKiller extends BlockPowerMachineContainer {

	public BlockKiller() {
		super(Block.Properties.create(Material.GROUND));
	}

	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		TileEntityKiller ret = new TileEntityKiller();
		return ret;
	}

	@Override
    public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos,  EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		if (!super.onBlockActivated(state, worldIn, pos, playerIn, hand, facing, hitX, hitY, hitZ)){
	        if (!worldIn.isRemote)
	        {
	        	NetworkHooks.openGui((EntityPlayerMP)playerIn,
	        			new IntaractionObjectKiller(pos),
	        			(buf)->{
							buf.writeInt(pos.getX());
							buf.writeInt(pos.getY());
							buf.writeInt(pos.getZ());
						});
	        	//playerIn.openGui(Mod_ConvenienceBox.instance, ModCommon.GUIID_KILLER, worldIn, pos.getX(), pos.getY(), pos.getZ());
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
    public void onReplaced(IBlockState state, World worldIn, BlockPos pos, IBlockState newState, boolean isMoving)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof IInventory)
        {
            InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)tileentity);
            worldIn.updateComparatorOutputLevel(pos, this);
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

	@Override
	public void onWork(World worldIn, IBlockState state, BlockPos pos) {
		// TODO 自動生成されたメソッド・スタブ

	}

}
