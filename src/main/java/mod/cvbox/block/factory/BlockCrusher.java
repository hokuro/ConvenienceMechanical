package mod.cvbox.block.factory;

import javax.annotation.Nullable;

import mod.cvbox.block.ab.BlockMachineContainer;
import mod.cvbox.inventory.factory.ContainerCrusher;
import mod.cvbox.tileentity.factory.TileEntityCrusher;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockCrusher extends BlockMachineContainer {

	public BlockCrusher(Block.Properties property) {
		super(property);
	}

	@Override
	public TileEntity createNewTileEntity(IBlockReader  worldIn) {
		TileEntityCrusher ret = new TileEntityCrusher();
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
	        						if (ent instanceof TileEntityCrusher) {
	        							return new ContainerCrusher(id, playerInv, ent);
	        						}
	        						return null;
	        					}

	        					@Override
	        					public ITextComponent getDisplayName() {
	        						return new TranslationTextComponent("container.crusher");
	        					}
	        				},
	            			(buf)->{
	    						buf.writeInt(pos.getX());
	    						buf.writeInt(pos.getY());
	    						buf.writeInt(pos.getZ());
	    					});
	            	return true;
            	}
            }
		}
        return false;
    }

    @Override
    public void onReplaced(BlockState state,World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
    	if (state.getBlock() != newState.getBlock()) {
	        TileEntity tileentity = worldIn.getTileEntity(pos);

	        if (tileentity instanceof IInventory) {
	            InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)tileentity);
	            ItemStack next = ItemStack.EMPTY;
	            if (!(next =((TileEntityCrusher)tileentity).NextDrop()).isEmpty()){
	            	InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), next);
	            }
	            worldIn.updateComparatorOutputLevel(pos, this);
	        }
	        super.onReplaced(state, worldIn, pos, newState, isMoving);
    	}
    }
}
