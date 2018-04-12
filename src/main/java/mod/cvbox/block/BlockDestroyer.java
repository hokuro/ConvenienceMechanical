package mod.cvbox.block;

import org.apache.commons.lang3.BooleanUtils;

import mod.cvbox.util.ModUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockDestroyer extends BlockFacingMachine {

	public BlockDestroyer(Material materialIn) {
		super(materialIn);
		this.setTickRandomly(false);
	}

	public void setscheduleBlockUpdate(World worldIn, BlockPos pos){
		// タイマーでは動かない
	}

	@Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
		super.onBlockAdded(worldIn, pos, state);
		if (!worldIn.isRemote){
			if (BooleanUtils.toBoolean(this.getPower(state))){
				EnumFacing front = this.getDirection(state);
				this.DestroyBlock(worldIn,pos,front);
			}
		}
    }

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
		if (!worldIn.isRemote){
			try{
				if (worldIn.getBlockState(pos).getValue(POWER)){
					EnumFacing front = this.getDirection(state);
					this.DestroyBlock(worldIn,pos,front);
				}
			}
			catch(Exception ex){

			}
		}
    }

	protected void DestroyBlock(World worldIn, BlockPos pos, EnumFacing front){
		BlockPos pos2 = pos.offset(front);
		IBlockState target = worldIn.getBlockState(pos2);
		if (target.getMaterial() == Material.AIR ||
				target.getMaterial() == Material.WATER ||
				target.getMaterial() == Material.LAVA ||
				target.getBlock() == Blocks.BEDROCK){return;}
		ItemStack drop = ItemStack.EMPTY;
		drop = target.getBlock().getItem(worldIn, pos2, target);
		if (!drop.isEmpty()){
			ModUtil.spawnItemStack(worldIn, pos2.getX(),pos2.getY(),pos2.getZ(), drop, RANDOM);
			worldIn.destroyBlock(pos2, false);
		}
	}
}
