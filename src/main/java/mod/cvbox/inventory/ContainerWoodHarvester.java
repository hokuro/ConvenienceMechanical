package mod.cvbox.inventory;

import org.apache.commons.lang3.BooleanUtils;

import mod.cvbox.block.BlockCore;
import mod.cvbox.core.ModCommon;
import mod.cvbox.tileentity.TileEntityWoodHarvester;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ContainerWoodHarvester extends Container {
	public final static int ROW_SLOT = 6;
	public final static int COL_SLOT = 9;

	private final World world;
	private final IInventory playerInventory;
	private final IInventory seedInventory;
	private int xCoord;
	private int yCoord;
	private int zCoord;
	private int last_dt_selectPriority = 0;


	public ContainerWoodHarvester(EntityPlayer pl, TileEntity te, World wd, int x, int y, int z){
		playerInventory = pl.inventory;
		seedInventory = (TileEntityWoodHarvester)te;
		world = wd;
		xCoord = x;
		yCoord = y;
		zCoord = z;

		// コンテナインベントリ
	    for (int row = 0; row < ROW_SLOT; row++) {
		      for (int col = 0; col < COL_SLOT; col++) {
		    	  addSlotToContainer(
		    			  new Slot((IInventory)te,
		    			  col + (row * COL_SLOT), 	// インデックス
		    			  8 + col * 18,
		    			  18 + row * 18));
		      }
	    }
	    // ツールインベントリ
	    addSlotToContainer(new Slot(this.seedInventory, ROW_SLOT*COL_SLOT, 177, 18)
        {
            public boolean isItemValid(ItemStack stack)
            {
                if (stack.getItem() instanceof ItemAxe){
                	return true;
                }
                return false;
            }

            public int getSlotStackLimit()
            {
                return 1;
            }
        });

		// プレイヤーインベントリ
		int OFFSET = 74;
		for (int rows = 0; rows < 3; rows++){
			for ( int slotIndex = 0; slotIndex < 9; slotIndex++){
				addSlotToContainer(new Slot(playerInventory,
						slotIndex + (rows * 9) + 9,
						8 + slotIndex * 18,
						139 + rows * 18));
			}
		}

		// メインインベントリ
		for (int slotIndex = 0; slotIndex < ModCommon.PLANTER_MAX_SLOT; slotIndex++){
			addSlotToContainer(new Slot(this.playerInventory,
					slotIndex,
					8 + slotIndex * 18,
					197));
		}
	}

    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < ROW_SLOT * COL_SLOT)
            {
                if (!this.mergeItemStack(itemstack1, ROW_SLOT * 9, this.inventorySlots.size(), true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (itemstack.getItem() instanceof ItemAxe){
            	if (!this.mergeItemStack(itemstack1,54,55,false)){
                    if (!this.mergeItemStack(itemstack1, 0, ROW_SLOT * 9, false))
                    {
                        return ItemStack.EMPTY;
                    }
            	}
            }else if (!this.mergeItemStack(itemstack1, 0, ROW_SLOT * 9, false))
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
        }

        return itemstack;
    }

    /**
     * Called when the container is closed.
     */
    public void onContainerClosed(EntityPlayer playerIn)
    {
        super.onContainerClosed(playerIn);
        this.seedInventory.closeInventory(playerIn);
    }

	@Override
	public boolean canInteractWith(EntityPlayer entityPlayer){
		return world.getBlockState(new BlockPos(xCoord,yCoord,zCoord)).getBlock() == BlockCore.block_woodharvester;
	}

	public void setDeliverMode(Boolean deliver) {
		((TileEntityWoodHarvester)seedInventory).setField(1, BooleanUtils.toInteger(deliver));
	}

	public World getWorld() {
		return ((TileEntityWoodHarvester)seedInventory).getWorld();
	}
}