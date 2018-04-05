package mod.cvbox.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
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
	    private final mod.cvbox.inventory.ContainerRepair anvil;

	    public SlotRepair(mod.cvbox.inventory.ContainerRepair containerRepairBA, IInventory iInventory, int slotIndex, int slotX, int slotY, World world, int blockX, int blockY, int blockZ) {
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

	    /**
	     * Return whether this slot's stack can be taken from this slot.
	     */
	    @Override
	    public boolean canTakeStack(EntityPlayer entityPlayer) {
	        return (entityPlayer.capabilities.isCreativeMode || entityPlayer.experienceLevel >= this.anvil.maximumCost) && (this.anvil.maximumCost > 0 || this.anvil.isRenamingOnly) && this.getHasStack();
	    }

//	    @Override
//	    public void onPickupFromSlot(EntityPlayer entityPlayer, ItemStack itemStack) {
//	        if (!entityPlayer.capabilities.isCreativeMode) {
//	            entityPlayer.addExperienceLevel(-this.anvil.maximumCost);
//	        }
//
//	        mod.cvbox.inventory.ContainerRepair.getRepairInputInventory(this.anvil).setInventorySlotContents(1, this.anvil.resultInputStack);
//	        mod.cvbox.inventory.ContainerRepair.getRepairInputInventory(this.anvil).setInventorySlotContents(0, this.anvil.resultInputStack1);
//	        this.anvil.maximumCost = 0;
//
//	        if (!entityPlayer.capabilities.isCreativeMode && !this.theWorld.isRemote &&
//	        		this.theWorld.getBlockState(blockPos).getBlock() instanceof BlockSuperAnvil) {
//	            int blockMetadata = this.theWorld.getBlockState(blockPos).getBlock().getMetaFromState(this.theWorld.getBlockState(blockPos));
//	            int blockOrientation = blockMetadata & 3;
//	            int blockDamage = blockMetadata >> 2;
//	            blockDamage = 2;
//
//	            if (blockDamage > 2) {
//	                this.theWorld.setBlockToAir(blockPos);
//	                this.theWorld.playAuxSFX(1020, blockPos, 0);
//	            } else {
//	                this.theWorld.setBlockState(blockPos,
//	                		theWorld.getBlockState(blockPos).withProperty(BlockAnvil.DAMAGE,blockOrientation | blockDamage << 2));
//	                this.theWorld.playAuxSFX(1021, blockPos, 0);
//	            }
//	        } else if (!this.theWorld.isRemote) {
//	            this.theWorld.playAuxSFX(1021, blockPos, 0);
//	        }
//	        this.anvil.detectAndSendChanges();
//	    }
}
