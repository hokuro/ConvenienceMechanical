package mod.cvbox.inventory.factory;

import mod.cvbox.core.Mod_ConvenienceBox;
import mod.cvbox.inventory.ab.ContainerPowerBase;
import mod.cvbox.tileentity.factory.TileEntityBedrockMaker;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class ContainerBedrockMaker extends ContainerPowerBase{

	public ContainerBedrockMaker(int id, PlayerInventory playerInv, TileEntity te){
		super(Mod_ConvenienceBox.CONTAINER_BEDROCKMAKER, id, playerInv, te);
	}

	@Override
	public void addOriginalSlot() {
		addSlot(new Slot(inventory, 1, 56, 17){
			public boolean isItemValid(ItemStack stack) {
				return inventory.isItemValidForSlot(this.slotNumber, stack);
			}
		});

		addSlot(new Slot(inventory, 2, 56, 53){
			public boolean isItemValid(ItemStack stack) {
				return inventory.isItemValidForSlot(this.slotNumber, stack);
			}
		});

		addSlot(new Slot(inventory, 3, 116, 35){
			public boolean isItemValid(ItemStack stack) {
				return inventory.isItemValidForSlot(this.slotNumber, stack);
			}
		});
	}

	@Override
	public TileEntityBedrockMaker getTileEntity(){
		return (TileEntityBedrockMaker)this.inventory;
	}
}
