package mod.cvbox.block.ab;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IPowerMachine {
	public void onWork(World worldIn, IBlockState state, BlockPos pos);
}
