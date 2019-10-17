package mod.cvbox.block.factory;

import javax.annotation.Nullable;

import mod.cvbox.block.ab.BlockMachineContainer;
import mod.cvbox.inventory.factory.ContainerStraw;
import mod.cvbox.tileentity.factory.TileEntityStraw;
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

public class BlockStraw extends BlockMachineContainer {

	public BlockStraw(Block.Properties property) {
		super(property);
	}

	@Override
	public TileEntity createNewTileEntity(IBlockReader world) {
		TileEntityStraw ret = new TileEntityStraw();
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
	        						if (ent instanceof TileEntityStraw) {
	        							return new ContainerStraw(id, playerInv, ent);
	        						}
	        						return null;
	        					}

	        					@Override
	        					public ITextComponent getDisplayName() {
	        						return new TranslationTextComponent("container.straw");
	        					}
	        				},
	        			(buf)->{
							buf.writeInt(pos.getX());
							buf.writeInt(pos.getY());
							buf.writeInt(pos.getZ());
						});
            	}
            }
        }
        return true;
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
    	if (state.getBlock() != newState.getBlock()) {
	        TileEntity tileentity = worldIn.getTileEntity(pos);

	        if (tileentity instanceof TileEntityStraw) {
	            InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)tileentity);
	            TileEntityStraw straw = (TileEntityStraw)tileentity;
	            int tank = straw.getField(TileEntityStraw.FIELD_TANK);
	            while(tank > 0){
	        		InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(straw.getLiquid(),tank>64?64:tank));
	            	tank -= 64;
	            }
	            worldIn.updateComparatorOutputLevel(pos, this);
	        }
	        super.onReplaced(state, worldIn, pos, newState,isMoving);
    	}
    }
}
