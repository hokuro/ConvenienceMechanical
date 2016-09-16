package mod.cvbox.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ContainerExpBank extends Container{
	private World world;
	private int x;
	private int y;
	private int z;

	public ContainerExpBank(EntityPlayer player, World world, int x, int y, int z){
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public boolean canInteractWith(EntityPlayer player){
		BlockPos pos = new BlockPos(x,y,z);
		return this.world.getBlockState(pos).getBlock() == mod.cvbox.block.BlockCore.block_expbank;
	}
}
