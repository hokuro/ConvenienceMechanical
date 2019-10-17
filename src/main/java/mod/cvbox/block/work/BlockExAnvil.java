package mod.cvbox.block.work;

import javax.annotation.Nullable;

import mod.cvbox.inventory.work.ContainerExRepair;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.Hand;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockExAnvil extends AnvilBlock {

    public BlockExAnvil(Block.Properties property) {
		super(property);
    }

    @Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand handIn, BlockRayTraceResult hit) {
            if (!worldIn.isRemote) {
            	if (!playerIn.isSneaking()) {
	            	NetworkHooks.openGui((ServerPlayerEntity)playerIn,
	            			new INamedContainerProvider() {
	        					@Override
	        					@Nullable
	        					public Container createMenu(int id, PlayerInventory playerInv, PlayerEntity player) {
	        						return new ContainerExRepair(id, playerInv, IWorldPosCallable.of(playerInv.player.world, pos));
	        					}

	        					@Override
	        					public ITextComponent getDisplayName() {
	        						return new TranslationTextComponent("container.exrepair");
	        					}
	        				},
	        			(buf)->{
							buf.writeInt(pos.getX());
							buf.writeInt(pos.getY());
							buf.writeInt(pos.getZ());
						});
            	}
            }

        return true;
    }

}
