package mod.cvbox.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotChangeModifi extends Slot {

	private boolean isCkeckModifi;

    public SlotChangeModifi(IInventory inventoryIn, int index, int xPosition, int yPosition, boolean check)
    {
    	super(inventoryIn,index,xPosition,yPosition);
    	isCkeckModifi = check;
    }

    public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack)
    {
		if (isCkeckModifi){
    		this.onSlotChanged();
    	}
        return stack;
    }

    /**
     * Helper method to put a stack in the slot.
     */
    public void putStack(ItemStack stack)
    {
        this.inventory.setInventorySlotContents(this.getSlotIndex(), stack);
        if (isCkeckModifi){
        	this.onSlotChanged();
        }
    }
}
