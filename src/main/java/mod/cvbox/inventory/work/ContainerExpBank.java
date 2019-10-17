package mod.cvbox.inventory.work;

import mod.cvbox.core.Mod_ConvenienceBox;
import mod.cvbox.tileentity.work.TileEntityExpBank;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntity;

public class ContainerExpBank extends Container{
	private final PlayerInventory playerInventory;
	private final TileEntityExpBank tileEntity;
	public ContainerExpBank(int id, PlayerInventory playerInv, TileEntity ent){
		super(Mod_ConvenienceBox.CONTAINER_EXPBANK, id);
		tileEntity = (TileEntityExpBank)ent;
		playerInventory = playerInv;
	}

	public boolean canInteractWith(PlayerEntity player){
		return (playerInventory.player.world.getTileEntity(tileEntity.getPos()) instanceof TileEntityExpBank);
	}

	public TileEntityExpBank getTileEntity() {
		return tileEntity;
	}
}
