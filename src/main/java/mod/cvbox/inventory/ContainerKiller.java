package mod.cvbox.inventory;

import mod.cvbox.tileentity.TileEntityKiller;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ContainerKiller extends Container {

	private BlockPos pos;
	private World world;
	private IInventory killer;

	public ContainerKiller(IInventory player, IInventory killer, World world, BlockPos pos){
		this.pos = pos;
		this.world = world;
		this.killer = killer;

        this.addSlotToContainer(new Slot(killer, 0, 80,20){
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
		return this.killer.isUsableByPlayer(playerIn);
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
		         if (!this.mergeItemStack(itemstack1, 1, 36, true))
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

	 public TileEntityKiller getTileEntity(){
		 return (TileEntityKiller)this.killer;
	 }

}
