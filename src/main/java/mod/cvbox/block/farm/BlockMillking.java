package mod.cvbox.block.farm;

import javax.annotation.Nullable;

import mod.cvbox.block.ab.BlockMachineContainer;
import mod.cvbox.inventory.farm.ContainerMillking;
import mod.cvbox.item.ItemCore;
import mod.cvbox.tileentity.farm.TileEntityMillking;
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

public class BlockMillking extends BlockMachineContainer {

	public BlockMillking(Block.Properties property) {
		super(property);
	}

	@Override
	public TileEntity createNewTileEntity(IBlockReader world) {
		return new TileEntityMillking();
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
	        						if (ent instanceof TileEntityMillking) {
	        							return new ContainerMillking(id, playerInv, ent);
	        						}
	        						return null;
	        					}

	        					@Override
	        					public ITextComponent getDisplayName() {
	        						return new TranslationTextComponent("container.millking");
	        					}
	        				},
	        			(buf)->{
							buf.writeInt(pos.getX());
							buf.writeInt(pos.getY());
							buf.writeInt(pos.getZ());
						});
            	}else if (handIn == Hand.MAIN_HAND && playerIn.getHeldItemMainhand().isEmpty()) {
					TileEntity ent = worldIn.getTileEntity(pos);
					if (ent instanceof TileEntityMillking) {
						((TileEntityMillking)ent).Exec();
					}
            	}
            }
        }
        return true;
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
    	if (state.getBlock() != newState.getBlock()) {
	        TileEntity tileentity = worldIn.getTileEntity(pos);

	        if (tileentity instanceof TileEntityMillking) {
	            InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)tileentity);
	            TileEntityMillking millking = (TileEntityMillking)tileentity;
	            int tank = millking.getField(TileEntityMillking.FIELD_TANK);
	            while(tank > 0){
	        		InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ItemCore.item_millkball,tank>64?64:tank));
	            	tank -= 64;
	            }
	            worldIn.updateComparatorOutputLevel(pos, this);
	        }
	        super.onReplaced(state, worldIn, pos, newState,isMoving);
    	}
    }
}
