package mod.cvbox.block;

import mod.cvbox.block.ab.BlockPowerMachineContainer;
import mod.cvbox.intaractionobject.IntaractionObjectExpCollector;
import mod.cvbox.tileentity.TileEntityExpCollector;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockExpCollector extends BlockPowerMachineContainer {

	public BlockExpCollector() {
		super(Block.Properties.create(Material.GROUND));
	}



	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		TileEntityExpCollector ret = new TileEntityExpCollector();
		return ret;
	}

	@Override
	public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

		if (!super.onBlockActivated(state, worldIn, pos, playerIn, hand, facing, hitX, hitY, hitZ)){
	        if (!worldIn.isRemote)
	        {
	        	NetworkHooks.openGui((EntityPlayerMP)playerIn,
	        			new IntaractionObjectExpCollector(pos),
	        			(buf)->{
							buf.writeInt(pos.getX());
							buf.writeInt(pos.getY());
							buf.writeInt(pos.getZ());
						});
	        	//playerIn.openGui(Mod_ConvenienceBox.instance, ModCommon.GUIID_EXPCOLLECTOR, worldIn, pos.getX(), pos.getY(), pos.getZ());
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

        if (tileentity instanceof TileEntityExpCollector)
        {
            InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)tileentity);
            worldIn.updateComparatorOutputLevel(pos, this);

            if (!worldIn.isRemote){
            	// 中に残っている経験値をオーブとしてばらまく
            	TileEntityExpCollector collector = (TileEntityExpCollector)tileentity;
            	int exp = collector.getField(TileEntityExpCollector.FIELD_EXPVALUE);
            	if (exp > 0){
                	int value = (int)MathHelper.floor(exp/50);
                	for ( int i = 0; i < 49; i++){
                        float f = RANDOM.nextFloat() * 0.8F + 0.1F;
                        float f1 = RANDOM.nextFloat() * 0.8F + 0.1F;
                        float f2 = RANDOM.nextFloat() * 0.8F + 0.1F;
                		Entity et = new EntityXPOrb(worldIn, pos.getX()+f, pos.getY()+f1, pos.getZ()+f2, value);
                        float f3 = 0.05F;
                        et.motionX = RANDOM.nextGaussian() * 0.05000000074505806D;
                        et.motionY = RANDOM.nextGaussian() * 0.05000000074505806D + 0.20000000298023224D;
                        et.motionZ = RANDOM.nextGaussian() * 0.05000000074505806D;
                		worldIn.spawnEntity(et);
                		exp-=value;
                	}
            	}
            }
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }


	@Override
	public void onWork(World worldIn, IBlockState state, BlockPos pos) {

	}
}
