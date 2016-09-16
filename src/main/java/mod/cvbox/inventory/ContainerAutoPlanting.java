package mod.cvbox.inventory;

import mod.cvbox.block.BlockCore;
import mod.cvbox.core.ModCommon;
import mod.cvbox.tileentity.TileEntityPlanter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ContainerAutoPlanting extends Container {
	private World world;
	private int xCoord;
	private int yCoord;
	private int zCoord;
	private EntityPlayer player;
	private IInventory playerInventory;
	private TileEntityPlanter TileEntityAP;
	private int last_dt_selectPriority = 0;


	public ContainerAutoPlanting(EntityPlayer pl, TileEntityPlanter te, World wd, int x, int y, int z){
		player = pl;
		playerInventory = player.inventory;
		world = wd;
		xCoord = x;
		yCoord = y;
		zCoord = z;
		TileEntityAP = te;

		// コンテナインベントリ
	    for (int row = 0; row < ModCommon.PLANTER_MAX_ROW_SLOT; row++) {
		      for (int col = 0; col < ModCommon.PLANTER_MAX_COL_SLOT; col++) {
		    	  addSlotToContainer(
		    			  new SlotPlant(te,
		    			  (row *ModCommon.PLANTER_MAX_ROW_SLOT) + col, 	// インデックス
		    			  8 + col * 18, 						// x
		    			  14 + row * 18							// y
		    			  ));
		      }
	    }

		// プレイヤーインベントリ
		int OFFSET = 74;
		for (int rows = 0; rows < 3; rows++){
			for ( int slotIndex = 0; slotIndex < 9; slotIndex++){
				addSlotToContainer(new Slot(playerInventory,
						slotIndex + (rows * 9) + 9,
						8 + slotIndex * 18,
						135 + rows * 18));
			}
		}

		// メインインベントリ
		for (int slotIndex = 0; slotIndex < ModCommon.PLANTER_MAX_SLOT; slotIndex++){
			addSlotToContainer(new Slot(this.playerInventory,
					slotIndex,
					8 + slotIndex * 18,
					193));
		}
	}

	@Override
	public void onContainerClosed(EntityPlayer pl){
		super.onContainerClosed(pl);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer pl, int slotIndex){
		Slot slot = (Slot)inventorySlots.get(slotIndex);
		ItemStack srcItemStack = null;

		if ((slot != null) && (slot.getHasStack()))
		{
			ItemStack destItemStack = slot.getStack();
			srcItemStack = destItemStack.copy();
			if (slotIndex < this.TileEntityAP.getSizeInventory())
			{
				if (!mergeItemStack(destItemStack, this.TileEntityAP.getSizeInventory(), this.inventorySlots.size(), true))
				{
					return null;
				}
			} else {
				if ((!SlotPlant.checkExtends(destItemStack)))
					return null;
				if (!mergeItemStack(destItemStack, 0, this.TileEntityAP.getSizeInventory(), false)){
					return null;
				}
			}
			if (destItemStack.stackSize == 0) {
				slot.putStack(null);
			} else {
				slot.onSlotChanged();
			}
		}
		return srcItemStack;
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityPlayer){
		return world.getBlockState(new BlockPos(xCoord,yCoord,zCoord)).getBlock() == BlockCore.block_planter;
	}
}