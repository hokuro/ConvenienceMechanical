package mod.cvbox.inventory.factory;

import java.util.List;

import mod.cvbox.core.Mod_ConvenienceBox;
import mod.cvbox.tileentity.ab.ITileEntityParameter;
import mod.cvbox.tileentity.factory.TileEntityDeliverBox;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class ContainerDeliverBox extends Container {

	protected final IInventory inventory;


	public ContainerDeliverBox(int id, PlayerInventory playerInv, TileEntity te){
		super(Mod_ConvenienceBox.CONTAINER_DELIVERBOX, id);
		this.inventory = (IInventory)te;

		// フィルター
		addSlot(new Slot(inventory, 0, 134, 35));

		int col_max = TileEntityDeliverBox.COL;
		int row_max = TileEntityDeliverBox.ROW;
		for (int row = 0; row < row_max; row++) {
			for (int col = 0; col < col_max; col++) {
				addSlot(new Slot(inventory, col + (row * col_max) + 1, 62 + (col * 18), 17 + (row * 18)));
			}
		}

        for (int k = 0; k < 3; ++k) {
            for (int i1 = 0; i1 < 9; ++i1) {
                this.addSlot(new Slot(playerInv, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
            }
        }

        for (int l = 0; l < 9; ++l) {
            this.addSlot(new Slot(playerInv, l, 8 + l * 18, 142));
        }
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return this.inventory.isUsableByPlayer(playerIn);
	}

	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index > inventory.getSizeInventory()) {
				// プレイヤーインベントリからコンテナへ
				if (!this.mergeItemStack(itemstack1, 1, inventory.getSizeInventory(), false)) {
					return ItemStack.EMPTY;
				}
			}else {
				// コンテナからプレイヤーインベントリへ
				if (!this.mergeItemStack(itemstack1, inventory.getSizeInventory(), this.inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			}
			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(playerIn, itemstack1);
		}

		return itemstack;
	}

	@Override
	public void addListener(IContainerListener listener) {
		super.addListener(listener);
		ITileEntityParameter te = (ITileEntityParameter)inventory;
		for (int i = 0; i < te.getFieldCount(); i++) {
			listener.sendWindowProperty(this, i, te.getField(i));
		}
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		ITileEntityParameter cr = (ITileEntityParameter)this.inventory;
		List<IContainerListener> listeners = ObfuscationReflectionHelper.getPrivateValue(Container.class, this, "listeners");
		for (int i = 0; i < listeners.size(); ++i) {
			for (int j = 0; j < cr.getFieldCount(); j++) {
				listeners.get(i).sendWindowProperty(this, j, cr.getField(j));
			}
		}
	}


	@OnlyIn(Dist.CLIENT)
	public void updateProgressBar(int id, int data) {
		((ITileEntityParameter)this.inventory).setField(id, data);
	}

	public TileEntityDeliverBox getTileEntity() {
		return (TileEntityDeliverBox)inventory;
	}
}
