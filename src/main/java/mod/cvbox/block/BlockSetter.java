package mod.cvbox.block;

import mod.cvbox.block.ab.BlockFacingMachineContainer;
import mod.cvbox.intaractionobject.IntaractionObjectSetter;
import mod.cvbox.tileentity.TileEntitySetter;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockSetter extends BlockFacingMachineContainer {

	public BlockSetter() {
		super(Block.Properties.create(Material.GROUND));
	}

	@Override
    public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		if (!super.onBlockActivated(state, worldIn, pos, playerIn, hand, facing, hitX, hitY, hitZ)){
	        if (worldIn.isRemote)
	        {
	        }
	        else
	        {
	        	NetworkHooks.openGui((EntityPlayerMP)playerIn,
            			new IntaractionObjectSetter(pos),
            			(buf)->{
    						buf.writeInt(pos.getX());
    						buf.writeInt(pos.getY());
    						buf.writeInt(pos.getZ());
    					});
            	//playerIn.openGui(Mod_ConvenienceBox.instance, ModCommon.GUIID_SETTER, worldIn, pos.getX(), pos.getY(), pos.getZ());

	        }
		}
        return true;
    }

	@Override
    public void onBlockAdded(IBlockState state, World worldIn, BlockPos pos, IBlockState oldState)
    {
		super.onBlockAdded(state, worldIn, pos, oldState);
    }

	@Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
    	super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
    }


	@Override
	public TileEntity createNewTileEntity(IBlockReader world) {
		return new TileEntitySetter();
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

        if (tileentity instanceof TileEntitySetter)
        {
            InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntitySetter)tileentity);
            worldIn.updateComparatorOutputLevel(pos, this);
        }

        super.onReplaced(state, worldIn, pos, newstate, isMoving);
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
        		if (worldIn.setBlockState(pos2, setBlock.getDefaultState())){
        			itemstack.shrink(1);
        		}
        	}
        }
	}

	@Override
	public void onWork(World worldIn, IBlockState state, BlockPos pos) {

	}
}
