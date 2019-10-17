package mod.cvbox.inventory.factory;

import mod.cvbox.core.Mod_ConvenienceBox;
import mod.cvbox.inventory.ab.ContainerPowerBase;
import mod.cvbox.tileentity.factory.TileEntityVacumer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.tileentity.TileEntity;

public class ContainerVacumer extends ContainerPowerBase{

	public ContainerVacumer(int id, PlayerInventory playerInv, TileEntity te){
		super(Mod_ConvenienceBox.CONTIANER_VACUMER, id, playerInv, te);
	}

	@Override
	public void addOriginalSlot() {
		int col_max = TileEntityVacumer.COL;
		int row_max = TileEntityVacumer.ROW;
		for (int row = 0; row < row_max; row++) {
			for (int col = 0; col < col_max; col++) {
				addSlot(new Slot((IInventory)inventory, 1 + col + (row * col_max), 8 + col * 18, 36 + row * 18));
			}
		}
	}

	@Override
	public TileEntityVacumer getTileEntity(){
		return (TileEntityVacumer)this.inventory;
	}

}
