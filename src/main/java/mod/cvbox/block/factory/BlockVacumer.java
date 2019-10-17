package mod.cvbox.block.factory;

import javax.annotation.Nullable;

import mod.cvbox.block.ab.BlockMachineContainer;
import mod.cvbox.inventory.factory.ContainerVacumer;
import mod.cvbox.tileentity.factory.TileEntityVacumer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
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
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockVacumer extends BlockMachineContainer {

	public BlockVacumer(Block.Properties property) {
		super(property);
	}

	@Override
	public TileEntity createNewTileEntity(IBlockReader world) {
		TileEntityVacumer ret = new TileEntityVacumer();
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
	        						if (ent instanceof TileEntityVacumer) {
	        							return new ContainerVacumer(id, playerInv, ent);
	        						}
	        						return null;
	        					}

	        					@Override
	        					public ITextComponent getDisplayName() {
	        						return new TranslationTextComponent("container.vacumer");
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
					if (ent instanceof TileEntityVacumer) {
						((TileEntityVacumer)ent).Exec();
					}
            	}
            }
        }
        return false;
    }
}
