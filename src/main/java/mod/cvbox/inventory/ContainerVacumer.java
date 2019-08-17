package mod.cvbox.inventory;

import mod.cvbox.item.ItemBattery;
import mod.cvbox.tileentity.TileEntityVacumer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ContainerVacumer extends Container implements IPowerSwitchContainer{
	public final static int ROW_SLOT = 1;
	public final static int COL_SLOT = 9;
	private IInventory inventory;

    private int crush_count;
    private int power;
    private int battery;

	public ContainerVacumer(IInventory player, IInventory comp){
		this.inventory = comp;

		// バッテリー
		addSlot(
				new Slot(inventory, 0, 123, 6){
				    public boolean isItemValid(ItemStack stack)
				    {
				        return (stack.getItem() instanceof ItemBattery);
				    }
			  });

		// コンテナインベントリ
	    for (int row = 0; row < ROW_SLOT; row++) {
		      for (int col = 0; col < COL_SLOT; col++) {
		    	  addSlot(
		    			  new Slot((IInventory)inventory,
		    			  1 + col + (row * COL_SLOT), 	// インデックス
		    			  8 + col * 18,
		    			  36 + row * 18));
		      }
	    }

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

		     if (index < ROW_SLOT*COL_SLOT+1)
		     {
		         if (!this.mergeItemStack(itemstack1, ROW_SLOT*COL_SLOT+1, this.inventorySlots.size(), true))
		         {
		             return ItemStack.EMPTY;
		         }
		     }
		     else if (!this.mergeItemStack(itemstack1, 0, ROW_SLOT*COL_SLOT+1, false))
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

	        TileEntityVacumer cr = (TileEntityVacumer)this.inventory;
	        for (int i = 0; i < this.listeners.size(); ++i)
	        {
	            IContainerListener icontainerlistener = this.listeners.get(i);

	            if (this.crush_count != cr.getField(TileEntityVacumer.FIELD_COUNT))
	            {
	                icontainerlistener.sendWindowProperty(this, TileEntityVacumer.FIELD_COUNT, cr.getField(TileEntityVacumer.FIELD_COUNT));
	            }
	            if (this.power != cr.getField(TileEntityVacumer.FIELD_POWER))
	            {
	            	icontainerlistener.sendWindowProperty(this, TileEntityVacumer.FIELD_POWER, cr.getField(TileEntityVacumer.FIELD_POWER));
	            }
	            if (this.battery != cr.getField(TileEntityVacumer.FIELD_BATTERY))
	            {
	            	icontainerlistener.sendWindowProperty(this, TileEntityVacumer.FIELD_BATTERY, cr.getField(TileEntityVacumer.FIELD_BATTERY));
	            }

	        }

	        this.crush_count = cr.getField(TileEntityVacumer.FIELD_COUNT);
	        this.power = cr.getField(TileEntityVacumer.FIELD_POWER);
	        this.battery = cr.getField(TileEntityVacumer.FIELD_BATTERY);
	    }


	    @OnlyIn(Dist.CLIENT)
	    public void updateProgressBar(int id, int data)
	    {
	    	((TileEntityVacumer)this.inventory).setField(id, data);
	    }

	    @Override
	 public TileEntityVacumer getTileEntity(){
		 return (TileEntityVacumer)this.inventory;
	 }

}
