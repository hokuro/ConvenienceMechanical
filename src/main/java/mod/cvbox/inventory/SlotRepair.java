package mod.cvbox.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SlotRepair extends Slot {
	private final World theWorld;

	private final int blockPosX;

	private final int blockPosY;

	private final int blockPosZ;
	private final BlockPos blockPos;

	/** The anvil this slot belongs to. */
	private final mod.cvbox.inventory.work.ContainerExRepair anvil;

	public SlotRepair(mod.cvbox.inventory.work.ContainerExRepair containerRepairBA, IInventory iInventory, int slotIndex, int slotX, int slotY, World world, int blockX, int blockY, int blockZ) {
		super(iInventory, slotIndex, slotX, slotY);
		this.anvil = containerRepairBA;
		this.theWorld = world;
		this.blockPosX = blockX;
		this.blockPosY = blockY;
		this.blockPosZ = blockZ;
		blockPos = new BlockPos(blockX,blockY,blockZ);
	}

	/**
	* Check if the stack is a valid item for this slot. Always true beside for the armor slots.
	*/
	@Override
	public boolean isItemValid(ItemStack itemStack) {
		return false;
	}
}
