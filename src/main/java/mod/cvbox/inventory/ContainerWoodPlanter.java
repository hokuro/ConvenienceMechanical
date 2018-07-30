package mod.cvbox.inventory;

import org.apache.commons.lang3.BooleanUtils;

import mod.cvbox.block.BlockCore;
import mod.cvbox.core.ModCommon;
import mod.cvbox.item.ItemBattery;
import mod.cvbox.tileentity.TileEntityWoodPlanter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerWoodPlanter extends Container  implements IPowerSwitchContainer{
	public final static int ROW_SLOT = 5;
	public final static int COL_SLOT = 9;

	private final World world;
	private final IInventory playerInventory;
	private final IInventory inventory;
	private int xCoord;
	private int yCoord;
	private int zCoord;
	private int last_dt_selectPriority = 0;
    private int power;
    private int battery;
    private int nextx;
    private int nextz;
    private int nextPos;



	public ContainerWoodPlanter(EntityPlayer pl, TileEntity te, World wd, int x, int y, int z){
		playerInventory = pl.inventory;
		inventory = (TileEntityWoodPlanter)te;
		world = wd;
		xCoord = x;
		yCoord = y;
		zCoord = z;

		// バッテリー
		addSlotToContainer(
				new Slot(inventory, 0, 123, 6){
				    public boolean isItemValid(ItemStack stack)
				    {
				        return (stack.getItem() instanceof ItemBattery);
				    }
			  });

		// コンテナインベントリ
	    for (int row = 0; row < ROW_SLOT; row++) {
		      for (int col = 0; col < COL_SLOT; col++) {
		    	  addSlotToContainer(
		    			  new Slot((IInventory)te,
		    			  1+col + (row * COL_SLOT), 	// インデックス
		    			  8 + col * 18,
		    			  36 + row * 18){

		    		            public boolean isItemValid(ItemStack stack)
		    		            {
		    		                return (Block.getBlockFromItem(stack.getItem()) instanceof BlockSapling);
		    		            }
		    	  });
		      }
	    }

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

            if (index < ROW_SLOT * COL_SLOT + 1)
            {
                if (!this.mergeItemStack(itemstack1, ROW_SLOT * COL_SLOT + 1, this.inventorySlots.size(), true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 0, ROW_SLOT * COL_SLOT + 1, false))
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
        this.inventory.closeInventory(playerIn);
    }

	@Override
	public boolean canInteractWith(EntityPlayer entityPlayer){
		return world.getBlockState(new BlockPos(xCoord,yCoord,zCoord)).getBlock() == BlockCore.block_woodplanter;
	}

	public void setDeliverMode(Boolean deliver) {
		((TileEntityWoodPlanter)inventory).setField(1, BooleanUtils.toInteger(deliver));
	}

	public World getWorld() {
		return ((TileEntityWoodPlanter)inventory).getWorld();
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

	        TileEntityWoodPlanter cr = (TileEntityWoodPlanter)this.inventory;
	        for (int i = 0; i < this.listeners.size(); ++i)
	        {
	            IContainerListener icontainerlistener = this.listeners.get(i);

	            if (this.power != cr.getField(TileEntityWoodPlanter.FIELD_POWER))
	            {
	            	icontainerlistener.sendWindowProperty(this, TileEntityWoodPlanter.FIELD_POWER, cr.getField(TileEntityWoodPlanter.FIELD_POWER));
	            }
	            if (this.battery != cr.getField(TileEntityWoodPlanter.FIELD_BATTERY))
	            {
	            	icontainerlistener.sendWindowProperty(this, TileEntityWoodPlanter.FIELD_BATTERY, cr.getField(TileEntityWoodPlanter.FIELD_BATTERY));
	            }
	            if (this.nextx != cr.getField(TileEntityWoodPlanter.FIELD_NEXT_X))
	            {
	            	icontainerlistener.sendWindowProperty(this, TileEntityWoodPlanter.FIELD_NEXT_X, cr.getField(TileEntityWoodPlanter.FIELD_NEXT_X));
	            }
	            if (this.nextz != cr.getField(TileEntityWoodPlanter.FIELD_NEXT_Z))
	            {
	            	icontainerlistener.sendWindowProperty(this, TileEntityWoodPlanter.FIELD_NEXT_Z, cr.getField(TileEntityWoodPlanter.FIELD_NEXT_Z));
	            }
	            if (this.nextPos != cr.getField(TileEntityWoodPlanter.FILED_NEXTPOS))
	            {
	            	icontainerlistener.sendWindowProperty(this, TileEntityWoodPlanter.FILED_NEXTPOS, cr.getField(TileEntityWoodPlanter.FILED_NEXTPOS));
	            }

	        }

	        this.power = cr.getField(TileEntityWoodPlanter.FIELD_POWER);
	        this.battery = cr.getField(TileEntityWoodPlanter.FIELD_BATTERY);
	        this.nextx = cr.getField(TileEntityWoodPlanter.FIELD_NEXT_X);
	        this.nextz = cr.getField(TileEntityWoodPlanter.FIELD_NEXT_Z);
	        this.nextPos = cr.getField(TileEntityWoodPlanter.FILED_NEXTPOS);
	    }


	    @SideOnly(Side.CLIENT)
	    public void updateProgressBar(int id, int data)
	    {
	    	((TileEntityWoodPlanter)this.inventory).setField(id, data);
	    }

	    @Override
	 public TileEntityWoodPlanter getTileEntity(){
		 return (TileEntityWoodPlanter)this.inventory;
	 }

}