package mod.cvbox.inventory;

import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiDispenser;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerSetter extends Container {
	   private final IInventory setterInventory;

	    public ContainerSetter(IInventory playerInventory, IInventory setterInventoryIn)
	    {
	        this.setterInventory = setterInventoryIn;

	        for (int i = 0; i < 3; ++i)
	        {
	            for (int j = 0; j < 3; ++j)
	            {
	                this.addSlotToContainer(new Slot(setterInventoryIn, j + i * 3, 62 + j * 18, 17 + i * 18){
	                    public boolean isItemValid(ItemStack stack)
	                    {
	                    	if (Block.getBlockFromItem(stack.getItem()) != null && Block.getBlockFromItem(stack.getItem()) != Blocks.AIR){
	                    		return true;
	                    	}
	                    	GuiDispenser x;
	                    	return false;
	                    }
	                });
	            }
	        }

	        for (int k = 0; k < 3; ++k)
	        {
	            for (int i1 = 0; i1 < 9; ++i1)
	            {
	                this.addSlotToContainer(new Slot(playerInventory, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
	            }
	        }

	        for (int l = 0; l < 9; ++l)
	        {
	            this.addSlotToContainer(new Slot(playerInventory, l, 8 + l * 18, 142));
	        }
	    }

	    @Override
	    public boolean canInteractWith(EntityPlayer playerIn)
	    {
	        return this.setterInventory.isUsableByPlayer(playerIn);
	    }

	    @Override
	    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	    {
	        ItemStack itemstack = ItemStack.EMPTY;
	        Slot slot = this.inventorySlots.get(index);

	        if (slot != null && slot.getHasStack())
	        {
	            ItemStack itemstack1 = slot.getStack();
	            itemstack = itemstack1.copy();

	            if (index < 9)
	            {
	                if (!this.mergeItemStack(itemstack1, 9, 45, true))
	                {
	                    return ItemStack.EMPTY;
	                }
	            }
	            else if (!this.mergeItemStack(itemstack1, 0, 9, false))
	            {
	                return ItemStack.EMPTY;
	            }

	            if (itemstack1.isEmpty())
	            {
	                slot.putStack(ItemStack.EMPTY);
	            }
	            else
	            {
	                slot.onSlotChanged();
	            }

	            if (itemstack1.getCount() == itemstack.getCount())
	            {
	                return ItemStack.EMPTY;
	            }

	            slot.onTake(playerIn, itemstack1);
	        }

	        return itemstack;
	    }
}
