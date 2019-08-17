package mod.cvbox.inventory;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class InventoryBasicEx implements IInventory {
	    private ITextComponent inventoryTitle;
	    private final int slotsCount;
	    private final NonNullList<ItemStack> inventoryContents;
	    /** Listeners notified when any item in this inventory is changed. */
	    private List<IInventoryChangedListener> changeListeners;
	    private boolean hasCustomName;
		private final boolean[] changeSlot;

	    public InventoryBasicEx(String title, boolean customName, int slotCount, int[] detectChangeSlot)
	    {
	        this.inventoryTitle = new TextComponentString(title);
	        this.hasCustomName = customName;
	        this.slotsCount = slotCount;
	        this.inventoryContents = NonNullList.<ItemStack>withSize(slotCount, ItemStack.EMPTY);
	    	changeSlot = new boolean[slotCount];
	    	for(int s : detectChangeSlot){
	    		changeSlot[s] = true;
	    	}
	    }

	    @OnlyIn(Dist.CLIENT)
	    public InventoryBasicEx(ITextComponent title, int slotCount, int[] detectChangeSlot)
	    {
	        this(title.getFormattedText(), true, slotCount, detectChangeSlot);
	    }



	    public void addInventoryChangeListener(IInventoryChangedListener listener)
	    {
	        if (this.changeListeners == null)
	        {
	            this.changeListeners = Lists.<IInventoryChangedListener>newArrayList();
	        }

	        this.changeListeners.add(listener);
	    }


	    public void removeInventoryChangeListener(IInventoryChangedListener listener)
	    {
	        this.changeListeners.remove(listener);
	    }

	    @Override
	    public ItemStack getStackInSlot(int index)
	    {
	        return index >= 0 && index < this.inventoryContents.size() ? (ItemStack)this.inventoryContents.get(index) : ItemStack.EMPTY;
	    }

	    @Override
	    public ItemStack decrStackSize(int index, int count)
	    {
	        ItemStack itemstack = ItemStackHelper.getAndSplit(this.inventoryContents, index, count);

	        if (!itemstack.isEmpty() && changeSlot[index])
	        {
	            this.markDirty();
	        }

	        return itemstack;
	    }

	    @Override
	    public ItemStack removeStackFromSlot(int index)
	    {
	        ItemStack itemstack = this.inventoryContents.get(index);

	        if (itemstack.isEmpty())
	        {
	            return ItemStack.EMPTY;
	        }
	        else
	        {
	            this.inventoryContents.set(index, ItemStack.EMPTY);
	            return itemstack;
	        }
	    }

	    @Override
	    public void setInventorySlotContents(int index, ItemStack stack)
	    {
	        this.inventoryContents.set(index, stack);

	        if (!stack.isEmpty() && stack.getCount() > this.getInventoryStackLimit())
	        {
	            stack.setCount(this.getInventoryStackLimit());
	        }

	        if (changeSlot[index]){
	        	this.markDirty();
	        }
	    }

	    @Override
	    public int getSizeInventory()
	    {
	        return this.slotsCount;
	    }

	    @Override
	    public boolean isEmpty()
	    {
	        for (ItemStack itemstack : this.inventoryContents)
	        {
	            if (!itemstack.isEmpty())
	            {
	                return false;
	            }
	        }

	        return true;
	    }

	    @Override
	    public ITextComponent getName()
	    {
	        return inventoryTitle;
	    }

	    @Override
	    public boolean hasCustomName()
	    {
	        return this.hasCustomName;
	    }

	    public void setCustomName(String inventoryTitleIn)
	    {
	        this.hasCustomName = true;
	        this.inventoryTitle = new TextComponentString( inventoryTitleIn);
	    }

	    @Override
	    public ITextComponent getDisplayName()
	    {
	        return (this.hasCustomName() ? this.getName() : this.getCustomName());
	    }

	    @Override
	    public int getInventoryStackLimit()
	    {
	        return 64;
	    }

	    @Override
	    public void markDirty()
	    {
	        if (this.changeListeners != null)
	        {
	            for (int i = 0; i < this.changeListeners.size(); ++i)
	            {
	                ((IInventoryChangedListener)this.changeListeners.get(i)).onInventoryChanged(this);
	            }
	        }
	    }

	    @Override
	    public boolean isUsableByPlayer(EntityPlayer player)
	    {
	        return true;
	    }

	    @Override
	    public void openInventory(EntityPlayer player)
	    {
	    }

	    @Override
	    public void closeInventory(EntityPlayer player)
	    {
	    }

	    @Override
	    public boolean isItemValidForSlot(int index, ItemStack stack)
	    {
	        return true;
	    }

	    @Override
	    public int getField(int id)
	    {
	        return 0;
	    }

	    @Override
	    public void setField(int id, int value)
	    {
	    }

	    @Override
	    public int getFieldCount()
	    {
	        return 0;
	    }

	    @Override
	    public void clear()
	    {
	        this.inventoryContents.clear();
	    }

		@Override
		public ITextComponent getCustomName() {
			// TODO 自動生成されたメソッド・スタブ
			return this.inventoryTitle;
		}
}
