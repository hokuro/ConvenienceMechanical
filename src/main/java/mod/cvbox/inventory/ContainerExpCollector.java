package mod.cvbox.inventory;

import mod.cvbox.tileentity.TileEntityExpCollector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerExpCollector extends Container {
	private IInventory collector;

	public ContainerExpCollector(IInventory player, IInventory collector){

		this.collector = collector;

        this.addSlotToContainer(new Slot(collector, 0, 80,20){
            public boolean isItemValid(ItemStack stack)
            {
            	if (stack.isItemEnchantable() && stack.getItem().isDamageable()){
            		return true;
            	}
            	return false;
            }
        });

        for (int k = 0; k < 3; ++k)
        {
            for (int i1 = 0; i1 < 9; ++i1)
            {
                this.addSlotToContainer(new Slot(player, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
            }
        }

        for (int l = 0; l < 9; ++l)
        {
            this.addSlotToContainer(new Slot(player, l, 8 + l * 18, 142));
        }
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return this.collector.isUsableByPlayer(playerIn);
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

		     if (index < 1)
		     {
		         if (!this.mergeItemStack(itemstack1, 1, 36, true))
		         {
		             return ItemStack.EMPTY;
		         }
		     }
		     else if (!this.mergeItemStack(itemstack1, 0, 1, false))
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

	 public TileEntityExpCollector getTileEntity(){
		 return (TileEntityExpCollector)this.collector;
	 }

}
