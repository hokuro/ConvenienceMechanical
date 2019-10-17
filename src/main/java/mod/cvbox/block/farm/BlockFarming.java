package mod.cvbox.block.farm;

import javax.annotation.Nullable;

import mod.cvbox.inventory.farm.ContainerPlantNurture;
import mod.cvbox.tileentity.farm.TileEntityFarming;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockFarming extends ContainerBlock  {

	private final ContainerPlantNurture.NURTUREKIND nKind;
	public BlockFarming(Block.Properties property) {
		super(property);
		nKind = ContainerPlantNurture.NURTUREKIND.FARMING;
	}

    @OnlyIn(Dist.CLIENT)
    public boolean hasCustomBreakingProgress(BlockState state) {
        return true;
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
    	if (state.getBlock() != newState.getBlock()) {
	        TileEntity tileentity = worldIn.getTileEntity(pos);

	        if (tileentity instanceof TileEntityFarming) {
	        	((TileEntityFarming)tileentity).dropInventory(worldIn, pos);
	            worldIn.updateComparatorOutputLevel(pos, this);
	        }

	        super.onReplaced(state, worldIn, pos, newState, isMoving);
    	}
    }

	@Override
	public TileEntity createNewTileEntity(IBlockReader world){
		return new TileEntityFarming();
	}

	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand handIn, BlockRayTraceResult hit) {
		if (!worldIn.isRemote){
			if (!playerIn.isSneaking()){
				// i空手通常タッチで種植えGUI
				final ContainerPlantNurture.NURTURETYPE nType = ContainerPlantNurture.NURTURETYPE.PLANTER;
				NetworkHooks.openGui((ServerPlayerEntity)playerIn,
            			new INamedContainerProvider() {
        					@Override
        					@Nullable
        					public Container createMenu(int id, PlayerInventory playerInv, PlayerEntity player) {
        						TileEntity ent = worldIn.getTileEntity(pos);
        						if (ent instanceof TileEntityFarming) {
        							return new ContainerPlantNurture(id, playerInv, ent, nType, nKind);
        						}
        						return null;
        					}

        					@Override
        					public ITextComponent getDisplayName() {
        						return new TranslationTextComponent("container.farming_planter");
        					}
        				},
            			(buf)->{
    						buf.writeInt(pos.getX());
    						buf.writeInt(pos.getY());
    						buf.writeInt(pos.getZ());
    						buf.writeInt(nType.getIndex());
    						buf.writeInt(nKind.getIndex());
    					});
			}else if (playerIn.getHeldItem(handIn).isEmpty()){
				final ContainerPlantNurture.NURTURETYPE nType = ContainerPlantNurture.NURTURETYPE.HARVESTER;
				// i空手スニークタッチで収穫GUI
				NetworkHooks.openGui((ServerPlayerEntity)playerIn,
            			new INamedContainerProvider() {
        					@Override
        					@Nullable
        					public Container createMenu(int id, PlayerInventory playerInv, PlayerEntity player) {
        						TileEntity ent = worldIn.getTileEntity(pos);
        						if (ent instanceof TileEntityFarming) {
        							return new ContainerPlantNurture(id, playerInv, ent, nType, nKind);
        						}
        						return null;
        					}

        					@Override
        					public ITextComponent getDisplayName() {
        						return new TranslationTextComponent("container.farming_harvester");
        					}
        				},
            			(buf)->{
    						buf.writeInt(pos.getX());
    						buf.writeInt(pos.getY());
    						buf.writeInt(pos.getZ());
    						buf.writeInt(nType.getIndex());
    						buf.writeInt(nKind.getIndex());
    					});
			}
		}
		return true;
	}

	@Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

	IFluidState fstate = Fluids.FLOWING_WATER.getDefaultState();
	public IFluidState getFluidState(BlockState state) {
		return fstate;
	}
}
