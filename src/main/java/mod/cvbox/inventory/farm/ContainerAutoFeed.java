package mod.cvbox.inventory.farm;

import mod.cvbox.core.Mod_ConvenienceBox;
import mod.cvbox.inventory.ab.ContainerPowerBase;
import mod.cvbox.tileentity.farm.TileEntityAutoFeed;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.tileentity.TileEntity;

public class ContainerAutoFeed extends ContainerPowerBase{

	public ContainerAutoFeed(int id, PlayerInventory playerInv, TileEntity te){
		super(Mod_ConvenienceBox.CONTAINER_AUTOFEED, id, playerInv, te);
	}

	@Override
	public void addOriginalSlot() {
		// インベントリ
		for (int row = 0; row < TileEntityAutoFeed.ROW; row ++) {
			for (int col = 0; col < TileEntityAutoFeed.COL; col++) {
				addSlot(new Slot(inventory, 1 + col + (TileEntityAutoFeed.COL * row), 8 + col * 18, 17 + row * 18));
			}
		}
	}

	@Override
	public TileEntityAutoFeed getTileEntity(){
		return (TileEntityAutoFeed)this.inventory;
	}

}
