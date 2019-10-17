package mod.cvbox.inventory.farm;

import mod.cvbox.core.Mod_ConvenienceBox;
import mod.cvbox.inventory.ab.ContainerPowerBase;
import mod.cvbox.tileentity.farm.TileEntityMillking;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class ContainerMillking extends ContainerPowerBase{

	public ContainerMillking(int id, PlayerInventory playerInv, TileEntity te){
		super(Mod_ConvenienceBox.CONTAINER_MILLKING, id, playerInv, te);
	}

	@Override
	public void addOriginalSlot() {
		addSlot(new Slot(inventory, 1, 23, 54){
			public boolean isItemValid(ItemStack stack) {
				return false;
			}
		});
	}

	@Override
	public TileEntityMillking getTileEntity(){
		return (TileEntityMillking)this.inventory;
	}

}
