package mod.cvbox.inventory;

import mod.cvbox.core.ModCommon;
import mod.cvbox.tileentity.TileEntityCrusher;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerCrusher extends Container {
	private IInventory crusher;

    private int powder_dirt;
    private int powder_sand;
    private int powder_grave;
    private int crush_count;
    private int select;

	public ContainerCrusher(IInventory player, IInventory crusher){
		this.crusher = crusher;

		// インプット
  	  addSlotToContainer(
			  new Slot(crusher, 0,
			  80,17){
				    public boolean isItemValid(ItemStack stack)
				    {
				        return (Block.getBlockFromItem(stack.getItem()).getStateFromMeta(stack.getMetadata()).getMaterial() == Material.ROCK);
				    }
			  });
  	  // バケツ
	  addSlotToContainer(
			  new Slot(crusher, 1,
			  45, 40){
				    public boolean isItemValid(ItemStack stack)
				    {
				        return stack.getItem() == Items.WATER_BUCKET;
				    }
			  });

		// コンテナインベントリ
	    for (int row = 0; row < 3; row++) {
		      for (int col = 0; col < 9; col++) {
		    	  addSlotToContainer(
		    			  new Slot(crusher,
		    			  col + (row * 9)+2, 	// インデックス
		    			  8 + col * 18,
		    			  72 + row * 18){

		  				    public boolean isItemValid(ItemStack stack)
		  				    {
		  				        return false;
		  				    }
		    			  });
		      }
	    }


		// プレイヤーインベントリ
		int OFFSET = 74;
		for (int rows = 0; rows < 3; rows++){
			for ( int slotIndex = 0; slotIndex < 9; slotIndex++){
				addSlotToContainer(new Slot(player,
						slotIndex + (rows * 9) + 9,
						8 + slotIndex * 18,
						140 + rows * 18));
			}
		}

		// メインインベントリ
		for (int slotIndex = 0; slotIndex < ModCommon.PLANTER_MAX_SLOT; slotIndex++){
			addSlotToContainer(new Slot(player,
					slotIndex,
					8 + slotIndex * 18,
					198));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return this.crusher.isUsableByPlayer(playerIn);
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

		     if (index < 29)
		     {
		         if (!this.mergeItemStack(itemstack1, 29, this.inventorySlots.size(), true))
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
		 listener.sendAllWindowProperties(this, this.crusher);
	 }

	 @Override
	 public void detectAndSendChanges()
	 {
	        super.detectAndSendChanges();

	        TileEntityCrusher cr = (TileEntityCrusher)this.crusher;
	        for (int i = 0; i < this.listeners.size(); ++i)
	        {
	            IContainerListener icontainerlistener = this.listeners.get(i);

	            if (this.powder_dirt != cr.getField(TileEntityCrusher.FIELD_DIRT))
	            {
	                icontainerlistener.sendWindowProperty(this, TileEntityCrusher.FIELD_DIRT, cr.getField(TileEntityCrusher.FIELD_DIRT));
	            }
	            if (this.powder_grave != cr.getField(TileEntityCrusher.FIELD_GRAVE))
	            {
	                icontainerlistener.sendWindowProperty(this, TileEntityCrusher.FIELD_GRAVE, cr.getField(TileEntityCrusher.FIELD_GRAVE));
	            }
	            if (this.powder_sand != cr.getField(TileEntityCrusher.FIELD_SAND))
	            {
	                icontainerlistener.sendWindowProperty(this, TileEntityCrusher.FIELD_SAND, cr.getField(TileEntityCrusher.FIELD_SAND));
	            }
	            if (this.select != cr.getField(TileEntityCrusher.FIELD_SELECT))
	            {
	                icontainerlistener.sendWindowProperty(this, TileEntityCrusher.FIELD_SELECT, cr.getField(TileEntityCrusher.FIELD_SELECT));
	            }
	            if (this.crush_count != cr.getField(TileEntityCrusher.FIELD_COUNT))
	            {
	                icontainerlistener.sendWindowProperty(this, TileEntityCrusher.FIELD_COUNT, cr.getField(TileEntityCrusher.FIELD_COUNT));
	            }

	        }

	        this.powder_dirt = cr.getField(TileEntityCrusher.FIELD_DIRT);
	        this.powder_sand=cr.getField(TileEntityCrusher.FIELD_SAND);
	        this.powder_grave=cr.getField(TileEntityCrusher.FIELD_GRAVE);
	        this.crush_count=cr.getField(TileEntityCrusher.FIELD_COUNT);
	        this.select=cr.getField(TileEntityCrusher.FIELD_SELECT);
	    }


	    @SideOnly(Side.CLIENT)
	    public void updateProgressBar(int id, int data)
	    {
	    	((TileEntityCrusher)this.crusher).setField(id, data);
	    }


	 public TileEntityCrusher getTileEntity(){
		 return (TileEntityCrusher)this.crusher;
	 }

}
