package mod.cvbox.block.factory;

import javax.annotation.Nullable;

import mod.cvbox.block.ab.BlockFacingMachineContainer;
import mod.cvbox.core.log.ModLog;
import mod.cvbox.inventory.factory.ContainerDeliverBox;
import mod.cvbox.tileentity.factory.TileEntityDeliverBox;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockDeliverBox extends BlockFacingMachineContainer {

	public static final DirectionProperty FACING =  BlockStateProperties.FACING;

	public BlockDeliverBox(Block.Properties materialIn) {
		super(materialIn);
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.DOWN));
	}

    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand handIn, BlockRayTraceResult hit) {
    	if (!super.onBlockActivated(state, worldIn, pos, playerIn, handIn, hit)){
    		if (!worldIn.isRemote) {
	        	if (playerIn.isSneaking()) {
	        		if (playerIn.getHeldItemMainhand().isEmpty() && handIn == Hand.MAIN_HAND) {
	        			try {
	        				TileEntityDeliverBox ent = (TileEntityDeliverBox)worldIn.getTileEntity(pos);
	        				Direction face = state.get(this.FACING);
	        				Direction next = Direction.byIndex(face.getIndex() + 1);
	        				worldIn.setBlockState(pos, state.with(FACING, next));
	        				ent.setDeliverFade(next);
	        			}catch(Throwable ex) {
	        				ModLog.log().error(ex.toString());
	        			}
	        		}
	        	}else {
	        		NetworkHooks.openGui((ServerPlayerEntity)playerIn,
	            			new INamedContainerProvider() {
	        					@Override
	        					@Nullable
	        					public Container createMenu(int id, PlayerInventory playerInv, PlayerEntity player) {
	        						TileEntity ent = worldIn.getTileEntity(pos);
	        						if (ent instanceof TileEntityDeliverBox) {
	        							return new ContainerDeliverBox(id, playerInv, ent);
	        						}
	        						return null;
	        					}

	        					@Override
	        					public ITextComponent getDisplayName() {
	        						return new TranslationTextComponent("container.deliverbox");
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
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		return new TileEntityDeliverBox();
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader worldIn) {
		Direction face = state.get(this.FACING);
		return new TileEntityDeliverBox(face);
	}

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
    	if (state.getBlock() != newState.getBlock()) {
	        TileEntity tileentity = worldIn.getTileEntity(pos);

	        if (tileentity instanceof IInventory) {
	            InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)tileentity);
	            worldIn.updateComparatorOutputLevel(pos, this);
	        }
	        super.onReplaced(state, worldIn, pos, newState, isMoving);
    	} else if (state.get(FACING) != newState.get(FACING)) {
	        TileEntity tileentity = worldIn.getTileEntity(pos);
	        if (tileentity instanceof TileEntityDeliverBox) {
	            ((TileEntityDeliverBox)tileentity).setDeliverFade(newState.get(FACING));
	        }
    	}
    }


}
