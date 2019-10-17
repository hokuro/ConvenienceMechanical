package mod.cvbox.inventory.factory;

import mod.cvbox.core.Mod_ConvenienceBox;
import mod.cvbox.inventory.ab.ContainerPowerBase;
import mod.cvbox.tileentity.factory.TileEntityExpCollector;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class ContainerExpCollector extends ContainerPowerBase{

	public ContainerExpCollector(int id, PlayerInventory playerInv, TileEntity te){
		super(Mod_ConvenienceBox.CONTAINER_EXPCOLLECTOR, id, playerInv, te);
	}

	@Override
	protected void addOriginalSlot() {
        this.addSlot(new Slot(inventory, 1, 80,20){
            public boolean isItemValid(ItemStack stack) {
				return inventory.isItemValidForSlot(this.slotNumber, stack);
            }
        });
	}

	@Override
	public TileEntityExpCollector getTileEntity(){
		return (TileEntityExpCollector)this.inventory;
	}
}
