package mod.cvbox.inventory;

import mod.cvbox.item.ItemBattery;
import mod.cvbox.tileentity.TileEntityLiquidMaker;
import mod.cvbox.util.ModUtil;
import mod.cvbox.util.ModUtil.CompaierLevel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ContainerLiquidMaker extends Container implements IPowerSwitchContainer{
	private IInventory inventory;

    private int power;
    private int battery;

	public ContainerLiquidMaker(IInventory player, IInventory comp){
		this.inventory = comp;

		// バッテリー
		addSlot(
				new Slot(inventory, 0, 123, 6){
				    public boolean isItemValid(ItemStack stack)
				    {
				        return (stack.getItem() instanceof ItemBattery);
				    }
			  });

		// インプット
  	    addSlot(
			  new Slot(inventory, 1, 80, 17){
				    public boolean isItemValid(ItemStack stack)
				    {
				        for (ItemStack st : TileEntityLiquidMaker.ice){
				        	if (ModUtil.compareItemStacks(st, stack, CompaierLevel.LEVEL_EQUAL_ITEM)){
				        		return true;
				        	}
				        }
				        for (ItemStack st : TileEntityLiquidMaker.lava){
				        	if (ModUtil.compareItemStacks(st, stack, CompaierLevel.LEVEL_EQUAL_ITEM)){
				        		return true;
				        	}
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
		         if (!this.mergeItemStack(itemstack1, 2, this.inventorySlots.size(), true))
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

	        TileEntityLiquidMaker cr = (TileEntityLiquidMaker)this.inventory;
	        for (int i = 0; i < this.listeners.size(); ++i)
	        {
	            IContainerListener icontainerlistener = this.listeners.get(i);
	            if (this.power != cr.getField(TileEntityLiquidMaker.FIELD_POWER))
	            {
	            	icontainerlistener.sendWindowProperty(this, TileEntityLiquidMaker.FIELD_POWER, cr.getField(TileEntityLiquidMaker.FIELD_POWER));
	            }
	            if (this.battery != cr.getField(TileEntityLiquidMaker.FIELD_BATTERY))
	            {
	            	icontainerlistener.sendWindowProperty(this, TileEntityLiquidMaker.FIELD_BATTERY, cr.getField(TileEntityLiquidMaker.FIELD_BATTERY));
	            }

	        }

	        this.power = cr.getField(TileEntityLiquidMaker.FIELD_POWER);
	        this.battery = cr.getField(TileEntityLiquidMaker.FIELD_BATTERY);
	    }


	    @OnlyIn(Dist.CLIENT)
	    public void updateProgressBar(int id, int data)
	    {
	    	((TileEntityLiquidMaker)this.inventory).setField(id, data);
	    }

	    @Override
	 public TileEntityLiquidMaker getTileEntity(){
		 return (TileEntityLiquidMaker)this.inventory;
	 }

}
