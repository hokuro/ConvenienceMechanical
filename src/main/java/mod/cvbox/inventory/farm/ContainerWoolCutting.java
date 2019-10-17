package mod.cvbox.inventory.farm;

import mod.cvbox.core.Mod_ConvenienceBox;
import mod.cvbox.inventory.ab.ContainerPowerBase;
import mod.cvbox.tileentity.farm.TileEntityWoolCutting;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraft.tileentity.TileEntity;

public class ContainerWoolCutting extends ContainerPowerBase{

	public ContainerWoolCutting(int id, PlayerInventory playerInv, TileEntity te){
		super(Mod_ConvenienceBox.CONTAINER_WOOLCUTTING, id, playerInv, te);
	}

	@Override
	public void addOriginalSlot() {
		// ハサミ
		addSlot(new Slot(inventory, 1, 80, 17){
				public boolean isItemValid(ItemStack stack) {
					return (stack.getItem() instanceof ShearsItem);
				}
			});

		// インベントリ
		for (int row = 0; row < TileEntityWoolCutting.ROW; row ++) {
			for (int col = 0; col < TileEntityWoolCutting.COL; col++) {
				addSlot(new Slot(inventory, 2 + col + (TileEntityWoolCutting.COL * row), 8 + col * 18, 17 + row * 18) {
					public boolean isItemValid(ItemStack stack) {
						return false;
					}
				});
			}
		}
	}

	@Override
	public TileEntityWoolCutting getTileEntity(){
		return (TileEntityWoolCutting)this.inventory;
	}

}
