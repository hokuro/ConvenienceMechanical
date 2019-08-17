package mod.cvbox.inventory;

import mod.cvbox.item.ItemBattery;
import mod.cvbox.tileentity.TileEntityExpCollector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ContainerExpCollector extends Container implements IPowerSwitchContainer{
	private IInventory inventory;
    private int power;
    private int battery;

	public ContainerExpCollector(IInventory player, IInventory comp){

		this.inventory = comp;
		// バッテリー
		addSlot(
				new Slot(inventory, 0, 122, 8){
				    public boolean isItemValid(ItemStack stack)
				    {
				        return (stack.getItem() instanceof ItemBattery);
				    }
			  });

        this.addSlot(new Slot(inventory, 1, 80,20){
            public boolean isItemValid(ItemStack stack)
            {
            	if ((stack.isEnchantable() || stack.isEnchanted()) && stack.getItem().isDamageable()){
            		return true;
            	}
            	return false;
            }
        });

        for (int k = 0; k < 3; ++k)
        {
            for (int i1 = 0; i1 < 9; ++i1)
            {
                this.addSlot(new Slot(player, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
            }
        }

        for (int l = 0; l < 9; ++l)
        {
            this.addSlot(new Slot(player, l, 8 + l * 18, 142));
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

		     if (index < 2)
		     {
		         if (!this.mergeItemStack(itemstack1, 2, 37, true))
		         {
		             return ItemStack.EMPTY;
		         }
		     }
		     else if (!this.mergeItemStack(itemstack1, 0, 2, false))
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

	        TileEntityExpCollector cr = (TileEntityExpCollector)this.inventory;
	        for (int i = 0; i < this.listeners.size(); ++i)
	        {
	            IContainerListener icontainerlistener = this.listeners.get(i);
	            if (this.power != cr.getField(TileEntityExpCollector.FIELD_POWER))
	            {
	            	icontainerlistener.sendWindowProperty(this, TileEntityExpCollector.FIELD_POWER, cr.getField(TileEntityExpCollector.FIELD_POWER));
	            }
	            if (this.battery != cr.getField(TileEntityExpCollector.FIELD_BATTERY))
	            {
	            	icontainerlistener.sendWindowProperty(this, TileEntityExpCollector.FIELD_BATTERY, cr.getField(TileEntityExpCollector.FIELD_BATTERY));
	            }

	        }
	        this.power = cr.getField(TileEntityExpCollector.FIELD_POWER);
	        this.battery = cr.getField(TileEntityExpCollector.FIELD_BATTERY);
	    }


	    @OnlyIn(Dist.CLIENT)
	    public void updateProgressBar(int id, int data)
	    {
	    	((TileEntityExpCollector)this.inventory).setField(id, data);
	    }

	    @Override
	 public TileEntityExpCollector getTileEntity(){
		 return (TileEntityExpCollector)this.inventory;
	 }

}
