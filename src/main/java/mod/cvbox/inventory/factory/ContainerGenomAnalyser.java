package mod.cvbox.inventory.factory;

import mod.cvbox.core.Mod_ConvenienceBox;
import mod.cvbox.inventory.ab.ContainerPowerBase;
import mod.cvbox.tileentity.factory.TileEntityGenomAnalyser;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class ContainerGenomAnalyser extends ContainerPowerBase{

	public ContainerGenomAnalyser(int id, PlayerInventory playerInv, TileEntity te){
		super(Mod_ConvenienceBox.CONTAINER_GENOMANALYSER, id, playerInv, te);
	}

	@Override
	public void addOriginalSlot() {
		addSlot(new Slot(inventory, 1, 80, 17){
			public boolean isItemValid(ItemStack stack) {
				return inventory.isItemValidForSlot(this.slotNumber, stack);
			}
		});

		addSlot(new Slot(inventory, 2, 80, 53){
			public boolean isItemValid(ItemStack stack) {
				return inventory.isItemValidForSlot(this.slotNumber, stack);
			}
		});
	}

	@Override
	public TileEntityGenomAnalyser getTileEntity(){
		return (TileEntityGenomAnalyser)this.inventory;
	}

}
