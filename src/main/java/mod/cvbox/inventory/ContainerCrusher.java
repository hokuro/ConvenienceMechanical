package mod.cvbox.inventory;

import mod.cvbox.core.ModCommon;
import mod.cvbox.item.ItemBattery;
import mod.cvbox.tileentity.TileEntityCompresser;
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

public class ContainerCrusher extends Container implements IPowerSwitchContainer{
	private IInventory inventory;

    private int powder_dirt;
    private int powder_sand;
    private int powder_grave;
    private int crush_count;
    private int select;
    private int power;
    private int battery;

	public ContainerCrusher(IInventory player, IInventory crusher){
		this.inventory = crusher;

		// バッテリー
				addSlotToContainer(
						new Slot(crusher, 0, 123, 6){
						    public boolean isItemValid(ItemStack stack)
						    {
						        return (stack.getItem() instanceof ItemBattery);
						    }
					  });
		// インプット
  	  addSlotToContainer(
			  new Slot(crusher, 1,
			  80,17){
				    public boolean isItemValid(ItemStack stack)
				    {
				        return (Block.getBlockFromItem(stack.getItem()).getStateFromMeta(stack.getMetadata()).getMaterial() == Material.ROCK);
				    }
			  });
  	  // バケツ
	  addSlotToContainer(
			  new Slot(crusher, 2,
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
		    			  col + 1 + (row * 9)+2, 	// インデックス
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

		     if (index < 30)
		     {
		         if (!this.mergeItemStack(itemstack1, 30, this.inventorySlots.size(), true))
		         {
		             return ItemStack.EMPTY;
		         }
		     }
		     else if (!this.mergeItemStack(itemstack1, 0, 3, false))
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

	        TileEntityCrusher cr = (TileEntityCrusher)this.inventory;
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
	            if (this.power != cr.getField(TileEntityCompresser.FIELD_POWER))
	            {
	            	icontainerlistener.sendWindowProperty(this, TileEntityCompresser.FIELD_POWER, cr.getField(TileEntityCompresser.FIELD_POWER));
	            }
	            if (this.battery != cr.getField(TileEntityCompresser.FIELD_BATTERY))
	            {
	            	icontainerlistener.sendWindowProperty(this, TileEntityCompresser.FIELD_BATTERY, cr.getField(TileEntityCompresser.FIELD_BATTERY));
	            }
	        }

	        this.powder_dirt = cr.getField(TileEntityCrusher.FIELD_DIRT);
	        this.powder_sand=cr.getField(TileEntityCrusher.FIELD_SAND);
	        this.powder_grave=cr.getField(TileEntityCrusher.FIELD_GRAVE);
	        this.crush_count=cr.getField(TileEntityCrusher.FIELD_COUNT);
	        this.select=cr.getField(TileEntityCrusher.FIELD_SELECT);
	        this.power = cr.getField(TileEntityCompresser.FIELD_POWER);
	        this.battery = cr.getField(TileEntityCompresser.FIELD_BATTERY);
	    }


	    @SideOnly(Side.CLIENT)
	    public void updateProgressBar(int id, int data)
	    {
	    	((TileEntityCrusher)this.inventory).setField(id, data);
	    }


	    @Override
	 public TileEntityCrusher getTileEntity(){
		 return (TileEntityCrusher)this.inventory;
	 }

}
