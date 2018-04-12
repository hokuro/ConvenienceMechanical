package mod.cvbox.block;

import java.util.Random;

import org.apache.commons.lang3.BooleanUtils;

import mod.cvbox.config.ConfigValue;
import mod.cvbox.util.ModUtil;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockIceMaker extends BlockPowerMachine {


	public BlockIceMaker(Material materialIn) {
		super(materialIn);
		this.nextUpdateTick = ConfigValue.IceMaker.ExecSec * 20;
	}

	@Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		if (!worldIn.isRemote){
	        // レッドストーン入力がある場合周囲の氷をアイテム化する
			if (BooleanUtils.toBoolean(getPower(state))){
				for (int x = -1; x <= 1; x++){
					for (int y = -1; y <= 1; y++){
						for (int z = -1; z <= 1; z++){
							BlockPos changePos = pos.add(x,y,z);
							IBlockState bstate = worldIn.getBlockState(changePos);
							if (bstate.getBlock() == Blocks.ICE || bstate.getBlock() == Blocks.PACKED_ICE){
								ModUtil.spawnItemStack(worldIn, changePos.getX(), changePos.getY(), changePos.getZ(), new ItemStack(bstate.getBlock(),1), RANDOM);
								worldIn.destroyBlock(changePos, false);
							}
						}
					}
				}
			}
		}
		return false;
    }

	@Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
		if (!worldIn.isRemote){
			// 周囲の水源を凍らせる
			if (BooleanUtils.toBoolean(getPower(state))){
				for (int x = -1; x <= 1; x++){
					for (int y = -1; y <= 1; y++){
						for (int z = -1; z <= 1; z++){
							BlockPos changePos = pos.add(x,y,z);
							IBlockState bstate = worldIn.getBlockState(changePos);
							if (bstate.getBlock() == Blocks.FLOWING_WATER || (bstate.getBlock() == Blocks.WATER && bstate.getValue(BlockLiquid.LEVEL) == 0)){
								worldIn.setBlockState(changePos, Blocks.ICE.getDefaultState());
							}else if(bstate.getBlock() == Blocks.FLOWING_LAVA || (bstate.getBlock() == Blocks.LAVA && bstate.getValue(BlockLiquid.LEVEL) == 0)){
								worldIn.setBlockState(changePos, Blocks.STONE.getDefaultState());
							}else if (bstate.getMaterial() == Material.LAVA){
								worldIn.setBlockState(changePos, Blocks.COBBLESTONE.getDefaultState());
							}
						}
					}
				}
				setscheduleBlockUpdate(worldIn,pos);
			}
		}
    }

	@Override
	public void setscheduleBlockUpdate(World worldIn, BlockPos pos){

		worldIn.scheduleBlockUpdate(pos, this, this.nextUpdateTick+(this.nextUpdateTick/4*(15-this.redstonePower)), 1);
	}
}
