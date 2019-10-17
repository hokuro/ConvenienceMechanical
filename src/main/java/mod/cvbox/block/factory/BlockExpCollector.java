package mod.cvbox.block.factory;

import javax.annotation.Nullable;

import mod.cvbox.block.ab.BlockMachineContainer;
import mod.cvbox.inventory.factory.ContainerExpCollector;
import mod.cvbox.tileentity.factory.TileEntityExpCollector;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockExpCollector extends BlockMachineContainer {

	public BlockExpCollector(Block.Properties property) {
		super(property);
	}

	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		TileEntityExpCollector ret = new TileEntityExpCollector();
		return ret;
	}

	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand handIn, BlockRayTraceResult hit) {
        if (!super.onBlockActivated(state, worldIn, pos, playerIn, handIn, hit)){
            if (!worldIn.isRemote) {
            	if (!playerIn.isSneaking()) {
	            	NetworkHooks.openGui((ServerPlayerEntity)playerIn,
	            			new INamedContainerProvider() {
	        					@Override
	        					@Nullable
	        					public Container createMenu(int id, PlayerInventory playerInv, PlayerEntity player) {
	        						TileEntity ent = worldIn.getTileEntity(pos);
	        						if (ent instanceof TileEntityExpCollector) {
	        							return new ContainerExpCollector(id, playerInv, ent);
	        						}
	        						return null;
	        					}

	        					@Override
	        					public ITextComponent getDisplayName() {
	        						return new TranslationTextComponent("container.expcollector");
	        					}
	        				},
							(buf)->{
										buf.writeInt(pos.getX());
										buf.writeInt(pos.getY());
										buf.writeInt(pos.getZ());
									});
					return true;
            	}else if (handIn == Hand.MAIN_HAND && playerIn.getHeldItemMainhand().isEmpty()) {
					TileEntity ent = worldIn.getTileEntity(pos);
					if (ent instanceof TileEntityExpCollector) {
						((TileEntityExpCollector)ent).Exec();
					}
            	}
			}
		}
        return false;
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
    	if (state.getBlock() != newState.getBlock()) {
	        TileEntity tileentity = worldIn.getTileEntity(pos);

	        if (tileentity instanceof TileEntityExpCollector) {
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
	                		Entity et = new ExperienceOrbEntity(worldIn, pos.getX()+f, pos.getY()+f1, pos.getZ()+f2, value);
	                        float f3 = 0.05F;
	                        et.setMotion(RANDOM.nextGaussian() * 0.05000000074505806D,
	                        				RANDOM.nextGaussian() * 0.05000000074505806D + 0.20000000298023224D,
	                        				RANDOM.nextGaussian() * 0.05000000074505806D);
	                		worldIn.addEntity(et);
	                		exp-=value;
	                	}
	            	}
	            }
	        }
	        super.onReplaced(state, worldIn, pos, newState, isMoving);
    	}
    }
}
