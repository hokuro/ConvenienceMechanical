package mod.cvbox.inventory.factory;

import mod.cvbox.core.Mod_ConvenienceBox;
import mod.cvbox.inventory.ab.ContainerPowerBase;
import mod.cvbox.tileentity.factory.TileEntitySetter;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class ContainerSetter extends ContainerPowerBase{

	public ContainerSetter(int id, PlayerInventory playerInv, TileEntity te){
		super(Mod_ConvenienceBox.CONTAINER_SETTER, id, playerInv, te);
	}

	@Override
	public void addOriginalSlot() {
		this.addSlot(new Slot(inventory, 1, 80, 17){
			public boolean isItemValid(ItemStack stack) {
				return inventory.isItemValidForSlot(this.slotNumber, stack);
			}
			});
	}

	@Override
	public TileEntitySetter getTileEntity(){
		return (TileEntitySetter)this.inventory;
	}
}
