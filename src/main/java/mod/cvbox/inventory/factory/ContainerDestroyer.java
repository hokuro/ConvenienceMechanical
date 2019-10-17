package mod.cvbox.inventory.factory;

import mod.cvbox.core.Mod_ConvenienceBox;
import mod.cvbox.inventory.ab.ContainerPowerBase;
import mod.cvbox.tileentity.factory.TileEntityDestroyer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.tileentity.TileEntity;

public class ContainerDestroyer extends ContainerPowerBase{

	public ContainerDestroyer(int id, PlayerInventory playerInv, TileEntity te){
		super(Mod_ConvenienceBox.CONTAINER_DESTROYER, id, playerInv, te);
	}

	@Override
	protected void addOriginalSlot() {
	}


	@Override
	public TileEntityDestroyer getTileEntity(){
		return (TileEntityDestroyer)this.inventory;
	}

}
