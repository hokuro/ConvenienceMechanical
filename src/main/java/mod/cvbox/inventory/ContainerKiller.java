package mod.cvbox.inventory;

import mod.cvbox.item.ItemBattery;
import mod.cvbox.tileentity.TileEntityKiller;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerKiller extends Container implements IPowerSwitchContainer{

	private BlockPos pos;
	private World world;
	private IInventory inventory;
    private int power;
    private int battery;

	public ContainerKiller(IInventory player, IInventory killer, World world, BlockPos pos){
		this.pos = pos;
		this.world = world;
		this.inventory = killer;

		// バッテリー
		addSlotToContainer(
				new Slot(inventory, 0, 120, 6){
				    public boolean isItemValid(ItemStack stack)
				    {
				        return (stack.getItem() instanceof ItemBattery);
				    }
			  });

        this.addSlotToContainer(new Slot(killer, 1, 80,20){
            public boolean isItemValid(ItemStack stack)
            {
            	if (stack.getItem() instanceof ItemSword){
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

	public World getWorld(){
		return this.world;
	}

	public BlockPos getPos(){
		return this.pos;
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

	        TileEntityKiller cr = (TileEntityKiller)this.inventory;
	        for (int i = 0; i < this.listeners.size(); ++i)
	        {
	            IContainerListener icontainerlistener = this.listeners.get(i);

	            if (this.power != cr.getField(TileEntityKiller.FIELD_POWER))
	            {
	            	icontainerlistener.sendWindowProperty(this, TileEntityKiller.FIELD_POWER, cr.getField(TileEntityKiller.FIELD_POWER));
	            }
	            if (this.battery != cr.getField(TileEntityKiller.FIELD_BATTERY))
	            {
	            	icontainerlistener.sendWindowProperty(this, TileEntityKiller.FIELD_BATTERY, cr.getField(TileEntityKiller.FIELD_BATTERY));
	            }

	        }

	        this.power = cr.getField(TileEntityKiller.FIELD_POWER);
	        this.battery = cr.getField(TileEntityKiller.FIELD_BATTERY);
	    }


	    @SideOnly(Side.CLIENT)
	    public void updateProgressBar(int id, int data)
	    {
	    	((TileEntityKiller)this.inventory).setField(id, data);
	    }


	    @Override
	 public TileEntityKiller getTileEntity(){
		 return (TileEntityKiller)this.inventory;
	 }

}
