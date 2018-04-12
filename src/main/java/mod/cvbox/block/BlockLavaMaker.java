package mod.cvbox.block;

import java.util.Random;

import org.apache.commons.lang3.BooleanUtils;

import mod.cvbox.config.ConfigValue;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockLavaMaker extends BlockPowerMachine {

	public BlockLavaMaker(Material materialIn) {
		super(materialIn);
		this.nextUpdateTick = ConfigValue.LavaMaker.ExecSec * 20;
	}

	@Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
		if (!worldIn.isRemote){
			// 周囲のブロックを溶岩に変える
			if (BooleanUtils.toBoolean(getPower(state))){
				for (int x = -1; x <= 1; x++){
					for (int z = -1; z <= 1; z++){
						if (x == 0 && z == 0){continue;}
						BlockPos changePos = pos.add(x,0,z);
						IBlockState bstate = worldIn.getBlockState(changePos);
						if (!(bstate.getMaterial() == Material.AIR || bstate.getMaterial() == Material.LAVA)){
							worldIn.setBlockState(changePos, Blocks.FLOWING_LAVA.getDefaultState());
						}else if (bstate.getMaterial() == Material.WATER){
							worldIn.setBlockToAir(changePos);
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
