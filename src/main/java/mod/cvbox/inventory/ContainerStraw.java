package mod.cvbox.inventory;

import mod.cvbox.tileentity.TileEntityStraw;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerStraw extends Container {
	private IInventory straw;

    private int tank_count;
    private int disp_x;
    private int disp_y;
    private int disp_z;
    private int areaSize;
    private int depth;
    private int width;
    private int kind;

	public ContainerStraw(IInventory player, IInventory st){
		this.straw = st;

		// インプット
  	  addSlotToContainer(new Slot(st, 0, 23, 54){
				    public boolean isItemValid(ItemStack stack)
				    {
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
		return this.straw.isUsableByPlayer(playerIn);
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
		     else if (!this.mergeItemStack(itemstack1, 0, this.inventorySlots.size(), false))
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
		 listener.sendAllWindowProperties(this, this.straw);
	 }

	 @Override
	 public void detectAndSendChanges()
	 {
	        super.detectAndSendChanges();

	        TileEntityStraw cr = (TileEntityStraw)this.straw;
	        for (int i = 0; i < this.listeners.size(); ++i)
	        {
	            IContainerListener icontainerlistener = this.listeners.get(i);

	            if (this.tank_count != cr.getField(TileEntityStraw.FIELD_TANK))
	            {
	                icontainerlistener.sendWindowProperty(this, TileEntityStraw.FIELD_TANK, cr.getField(TileEntityStraw.FIELD_TANK));
	            }

	            if (this.disp_x != cr.getField(TileEntityStraw.FIELD_X))
	            {
	                icontainerlistener.sendWindowProperty(this, TileEntityStraw.FIELD_X, cr.getField(TileEntityStraw.FIELD_X));
	            }

	            if (this.disp_y != cr.getField(TileEntityStraw.FIELD_Y))
	            {
	                icontainerlistener.sendWindowProperty(this, TileEntityStraw.FIELD_Y, cr.getField(TileEntityStraw.FIELD_Y));
	            }

	            if (this.disp_z != cr.getField(TileEntityStraw.FIELD_Z))
	            {
	                icontainerlistener.sendWindowProperty(this, TileEntityStraw.FIELD_Z, cr.getField(TileEntityStraw.FIELD_Z));
	            }

	            if (this.areaSize != cr.getField(TileEntityStraw.FIELD_SIZE))
	            {
	                icontainerlistener.sendWindowProperty(this, TileEntityStraw.FIELD_SIZE, cr.getField(TileEntityStraw.FIELD_SIZE));
	            }

	            if (this.depth != cr.getField(TileEntityStraw.FIELD_DEPTH))
	            {
	                icontainerlistener.sendWindowProperty(this, TileEntityStraw.FIELD_DEPTH, cr.getField(TileEntityStraw.FIELD_DEPTH));
	            }

	            if (this.width != cr.getField(TileEntityStraw.FIELD_WIDTH))
	            {
	                icontainerlistener.sendWindowProperty(this, TileEntityStraw.FIELD_WIDTH, cr.getField(TileEntityStraw.FIELD_WIDTH));
	            }

	            if (this.kind != cr.getField(TileEntityStraw.FIELD_KIND))
	            {
	                icontainerlistener.sendWindowProperty(this, TileEntityStraw.FIELD_KIND, cr.getField(TileEntityStraw.FIELD_KIND));
	            }

	        }

	        this.tank_count = cr.getField(TileEntityStraw.FIELD_TANK);
	        this.disp_x = cr.getField(TileEntityStraw.FIELD_X);
	        this.disp_y = cr.getField(TileEntityStraw.FIELD_Y);
	        this.disp_z = cr.getField(TileEntityStraw.FIELD_Z);
	        this.areaSize = cr.getField(TileEntityStraw.FIELD_SIZE);
	        this.depth = cr.getField(TileEntityStraw.FIELD_DEPTH);
	        this.width = cr.getField(TileEntityStraw.FIELD_WIDTH);
	        this.kind = cr.getField(TileEntityStraw.FIELD_KIND);
	    }


	    @SideOnly(Side.CLIENT)
	    public void updateProgressBar(int id, int data)
	    {
	    	((TileEntityStraw)this.straw).setField(id, data);
	    }


	 public TileEntityStraw getTileEntity(){
		 return (TileEntityStraw)this.straw;
	 }

}
