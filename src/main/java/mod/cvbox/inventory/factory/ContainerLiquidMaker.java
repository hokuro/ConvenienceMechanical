package mod.cvbox.inventory.factory;

import mod.cvbox.core.Mod_ConvenienceBox;
import mod.cvbox.inventory.ab.ContainerPowerBase;
import mod.cvbox.tileentity.factory.TileEntityLiquidMaker;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class ContainerLiquidMaker extends ContainerPowerBase{

	public ContainerLiquidMaker(int id, PlayerInventory playerInv, TileEntity te){
		super(Mod_ConvenienceBox.CONTAINER_LIQUIDMAKER, id, playerInv, te);
	}

	@Override
	public void addOriginalSlot() {
		addSlot(new Slot(inventory, 1, 80, 17){
			public boolean isItemValid(ItemStack stack) {
				return inventory.isItemValidForSlot(this.slotNumber, stack);
			}
		});
	}

	@Override
	public TileEntityLiquidMaker getTileEntity(){
		return (TileEntityLiquidMaker)this.inventory;
	}

}
