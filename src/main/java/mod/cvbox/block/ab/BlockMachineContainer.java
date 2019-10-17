package mod.cvbox.block.ab;

import java.util.ArrayList;
import java.util.List;

import mod.cvbox.item.ItemCore;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.SoundType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;

public abstract class BlockMachineContainer extends ContainerBlock{
	public BlockMachineContainer(Block.Properties materialIn) {
		super(materialIn.sound(SoundType.METAL));
	}

    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand handIn, BlockRayTraceResult hit) {
    	ItemStack main = playerIn.getHeldItemMainhand();
    	if (main.getItem() == ItemCore.item_spana){
    		if(!worldIn.isRemote){
    			this.onReplaced(state, worldIn, pos, Blocks.AIR.getDefaultState(),false);
        		InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(this,1));
    		}
    		worldIn.playSound((double)((float)pos.getX() + 0.5F), (double)((float)pos.getY() + 0.5F), (double)((float)pos.getZ() + 0.5F), SoundEvents.BLOCK_ANVIL_BREAK, SoundCategory.BLOCKS, 1.0F + worldIn.rand.nextFloat(), worldIn.rand.nextFloat() * 0.7F + 0.3F, false);
    		worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
    		return true;
    	}
		return false;
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
    	}
    }

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		List<ItemStack> ret = new ArrayList<ItemStack>();
		ret.add(new ItemStack(ItemCore.item_machinematter, this.RANDOM.nextInt(3)+1));
		return ret;
	}

	@Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
