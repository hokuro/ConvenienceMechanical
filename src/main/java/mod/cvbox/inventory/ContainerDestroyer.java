package mod.cvbox.inventory;

import mod.cvbox.item.ItemBattery;
import mod.cvbox.tileentity.TileEntityDestroyer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerDestroyer extends Container implements IPowerSwitchContainer{
	private IInventory inventory;

    private int power;
    private int battery;
    private int mode;
    private int time;
    private int isrun;

	public ContainerDestroyer(IInventory player, IInventory comp){
		this.inventory = comp;

		// バッテリー
		addSlotToContainer(
				new Slot(inventory, 0, 123, 6){
				    public boolean isItemValid(ItemStack stack)
				    {
				        return (stack.getItem() instanceof ItemBattery);
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

		     if (index < 1)
		     {
		         if (!this.mergeItemStack(itemstack1, 1, this.inventorySlots.size(), true))
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

	        TileEntityDestroyer cr = (TileEntityDestroyer)this.inventory;
	        for (int i = 0; i < this.listeners.size(); ++i)
	        {
	            IContainerListener icontainerlistener = this.listeners.get(i);

	            if (this.power != cr.getField(TileEntityDestroyer.FIELD_POWER))
	            {
	            	icontainerlistener.sendWindowProperty(this, TileEntityDestroyer.FIELD_POWER, cr.getField(TileEntityDestroyer.FIELD_POWER));
	            }
	            if (this.battery != cr.getField(TileEntityDestroyer.FIELD_BATTERY))
	            {
	            	icontainerlistener.sendWindowProperty(this, TileEntityDestroyer.FIELD_BATTERY, cr.getField(TileEntityDestroyer.FIELD_BATTERY));
	            }
	            if (this.mode != cr.getField(TileEntityDestroyer.FIELD_MODE))
	            {
	            	icontainerlistener.sendWindowProperty(this, TileEntityDestroyer.FIELD_MODE, cr.getField(TileEntityDestroyer.FIELD_MODE));
	            }
	            if (this.time != cr.getField(TileEntityDestroyer.FIELD_TIME))
	            {
	            	icontainerlistener.sendWindowProperty(this, TileEntityDestroyer.FIELD_TIME, cr.getField(TileEntityDestroyer.FIELD_TIME));
	            }
	            if (this.isrun != cr.getField(TileEntityDestroyer.FIELD_ISRUN))
	            {
	            	icontainerlistener.sendWindowProperty(this, TileEntityDestroyer.FIELD_ISRUN, cr.getField(TileEntityDestroyer.FIELD_ISRUN));
	            }

	        }
	        this.power = cr.getField(TileEntityDestroyer.FIELD_POWER);
	        this.battery = cr.getField(TileEntityDestroyer.FIELD_BATTERY);
	        this.mode = cr.getField(TileEntityDestroyer.FIELD_MODE);
	        this.time = cr.getField(TileEntityDestroyer.FIELD_TIME);
	        this.isrun = cr.getField(TileEntityDestroyer.FIELD_ISRUN);
	    }


	    @SideOnly(Side.CLIENT)
	    public void updateProgressBar(int id, int data)
	    {
	    	((TileEntityDestroyer)this.inventory).setField(id, data);
	    }

	    @Override
	 public TileEntityDestroyer getTileEntity(){
		 return (TileEntityDestroyer)this.inventory;
	 }

}