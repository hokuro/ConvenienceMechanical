package mod.cvbox.inventory;

import mod.cvbox.item.ItemBattery;
import mod.cvbox.tileentity.TileEntitySetter;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerSetter extends Container  implements IPowerSwitchContainer{
	   private final IInventory inventory;
	    private int power;
	    private int battery;

	    public ContainerSetter(IInventory playerInventory, IInventory setterInventoryIn)
	    {
	        this.inventory = setterInventoryIn;

			// バッテリー
			addSlotToContainer(
					new Slot(inventory, 0, 123, 6){
					    public boolean isItemValid(ItemStack stack)
					    {
					        return (stack.getItem() instanceof ItemBattery);
					    }
				  });


	        for (int i = 0; i < 3; ++i)
	        {
	            for (int j = 0; j < 3; ++j)
	            {
	                this.addSlotToContainer(new Slot(setterInventoryIn, 1 + j + i * 3, 62 + j * 18, 17 + i * 18){
	                    public boolean isItemValid(ItemStack stack)
	                    {
	                    	if (Block.getBlockFromItem(stack.getItem()) != null && Block.getBlockFromItem(stack.getItem()) != Blocks.AIR){
	                    		return true;
	                    	}
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
	        return this.inventory.isUsableByPlayer(playerIn);
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

	            if (index < 10)
	            {
	                if (!this.mergeItemStack(itemstack1, 10, 46, true))
	                {
	                    return ItemStack.EMPTY;
	                }
	            }
	            else if (!this.mergeItemStack(itemstack1, 0, 10, false))
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

		 @Override
		 public void addListener(IContainerListener listener)
		 {
			 super.addListener(listener);
			 listener.sendAllWindowProperties(this, this.inventory);
		 }

		 @Override
		 public void detectAndSendChanges()
		 {
		        super.detectAndSendChanges();

		        TileEntitySetter cr = (TileEntitySetter)this.inventory;
		        for (int i = 0; i < this.listeners.size(); ++i)
		        {
		            IContainerListener icontainerlistener = this.listeners.get(i);

		            if (this.power != cr.getField(TileEntitySetter.FIELD_POWER))
		            {
		            	icontainerlistener.sendWindowProperty(this, TileEntitySetter.FIELD_POWER, cr.getField(TileEntitySetter.FIELD_POWER));
		            }
		            if (this.battery != cr.getField(TileEntitySetter.FIELD_BATTERY))
		            {
		            	icontainerlistener.sendWindowProperty(this, TileEntitySetter.FIELD_BATTERY, cr.getField(TileEntitySetter.FIELD_BATTERY));
		            }

		        }

		        this.power = cr.getField(TileEntitySetter.FIELD_POWER);
		        this.battery = cr.getField(TileEntitySetter.FIELD_BATTERY);
		    }


		    @SideOnly(Side.CLIENT)
		    public void updateProgressBar(int id, int data)
		    {
		    	((TileEntitySetter)this.inventory).setField(id, data);
		    }

		    @Override
		 public TileEntitySetter getTileEntity(){
			 return (TileEntitySetter)this.inventory;
		 }
}
