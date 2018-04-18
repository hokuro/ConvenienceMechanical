package mod.cvbox.inventory;

import mod.cvbox.tileentity.TileEntityCompresser;
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

public class ContainerComplesser extends Container {
	private IInventory complesser;

    private int crush_count;
    private int powder_iron;
    private int powder_gold;
    private int powder_diamond;
    private int powder_emerald;
    private int powder_redstone;
    private int powder_lapis;

	public ContainerComplesser(IInventory player, IInventory comp){
		this.complesser = comp;

		// インプット
  	  addSlotToContainer(
			  new Slot(comp, 0, 80, 17){
				    public boolean isItemValid(ItemStack stack)
				    {
				        return !(Block.getBlockFromItem(stack.getItem()) == Blocks.IRON_BLOCK ||
						        Block.getBlockFromItem(stack.getItem()) == Blocks.GOLD_BLOCK ||
						        Block.getBlockFromItem(stack.getItem()) == Blocks.DIAMOND_BLOCK ||
						        Block.getBlockFromItem(stack.getItem()) == Blocks.EMERALD_BLOCK ||
						        Block.getBlockFromItem(stack.getItem()) == Blocks.REDSTONE_BLOCK ||
						        Block.getBlockFromItem(stack.getItem()) == Blocks.LAPIS_BLOCK);
				    }
			  });
  	    // コンテナ
  	    for (int col = 1;  col < 7; col++){
	    	  addSlotToContainer(new Slot(comp, col, 13+27*(col-1), 40){
				    public boolean isItemValid(ItemStack stack)
				    {
				        return false;
				    }
  			  });
  	    }

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
		return this.complesser.isUsableByPlayer(playerIn);
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

		     if (index < 7)
		     {
		         if (!this.mergeItemStack(itemstack1, 7, this.inventorySlots.size(), true))
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
		 listener.sendAllWindowProperties(this, this.complesser);
	 }

	 @Override
	 public void detectAndSendChanges()
	 {
	        super.detectAndSendChanges();

	        TileEntityCompresser cr = (TileEntityCompresser)this.complesser;
	        for (int i = 0; i < this.listeners.size(); ++i)
	        {
	            IContainerListener icontainerlistener = this.listeners.get(i);

	            if (this.powder_iron != cr.getField(TileEntityCompresser.FIELD_IRON))
	            {
	                icontainerlistener.sendWindowProperty(this, TileEntityCompresser.FIELD_IRON, cr.getField(TileEntityCompresser.FIELD_IRON));
	            }
	            if (this.powder_gold != cr.getField(TileEntityCompresser.FIELD_GOLD))
	            {
	                icontainerlistener.sendWindowProperty(this, TileEntityCompresser.FIELD_GOLD, cr.getField(TileEntityCompresser.FIELD_GOLD));
	            }
	            if (this.powder_diamond != cr.getField(TileEntityCompresser.FIELD_DIAMOND))
	            {
	                icontainerlistener.sendWindowProperty(this, TileEntityCompresser.FIELD_DIAMOND, cr.getField(TileEntityCompresser.FIELD_DIAMOND));
	            }
	            if (this.powder_emerald != cr.getField(TileEntityCompresser.FIELD_EMERALD))
	            {
	                icontainerlistener.sendWindowProperty(this, TileEntityCompresser.FIELD_EMERALD, cr.getField(TileEntityCompresser.FIELD_EMERALD));
	            }
	            if (this.powder_redstone != cr.getField(TileEntityCompresser.FIELD_REDSTONE))
	            {
	                icontainerlistener.sendWindowProperty(this, TileEntityCompresser.FIELD_REDSTONE, cr.getField(TileEntityCompresser.FIELD_REDSTONE));
	            }
	            if (this.powder_lapis != cr.getField(TileEntityCompresser.FIELD_LAPIS))
	            {
	                icontainerlistener.sendWindowProperty(this, TileEntityCompresser.FIELD_LAPIS, cr.getField(TileEntityCompresser.FIELD_LAPIS));
	            }
	            if (this.crush_count != cr.getField(TileEntityCompresser.FIELD_COUNT))
	            {
	                icontainerlistener.sendWindowProperty(this, TileEntityCompresser.FIELD_COUNT, cr.getField(TileEntityCompresser.FIELD_COUNT));
	            }

	        }

	        this.crush_count = cr.getField(TileEntityCompresser.FIELD_COUNT);
	        this.powder_iron = cr.getField(TileEntityCompresser.FIELD_IRON);
	        this.powder_gold = cr.getField(TileEntityCompresser.FIELD_GOLD);
	        this.powder_diamond = cr.getField(TileEntityCompresser.FIELD_DIAMOND);
	        this.powder_emerald = cr.getField(TileEntityCompresser.FIELD_EMERALD);
	        this.powder_redstone = cr.getField(TileEntityCompresser.FIELD_REDSTONE);
	        this.powder_lapis = cr.getField(TileEntityCompresser.FIELD_LAPIS);
	    }


	    @SideOnly(Side.CLIENT)
	    public void updateProgressBar(int id, int data)
	    {
	    	((TileEntityCompresser)this.complesser).setField(id, data);
	    }


	 public TileEntityCompresser getTileEntity(){
		 return (TileEntityCompresser)this.complesser;
	 }

}
