package mod.cvbox.inventory.factory;

import mod.cvbox.core.Mod_ConvenienceBox;
import mod.cvbox.inventory.ab.ContainerPowerBase;
import mod.cvbox.tileentity.factory.TileEntityCompresser;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class ContainerComplesser extends ContainerPowerBase{

	public ContainerComplesser(int id, PlayerInventory playerInv, TileEntity te){
		super(Mod_ConvenienceBox.CONTIANER_COMPLESSER, id, playerInv, te);
	}

	@Override
	public void addOriginalSlot() {
		// インプット
		addSlot(new Slot(inventory, 1, 80, 17){
				public boolean isItemValid(ItemStack stack) {
					return !(Block.getBlockFromItem(stack.getItem()) == Blocks.IRON_BLOCK ||
							Block.getBlockFromItem(stack.getItem()) == Blocks.GOLD_BLOCK ||
							Block.getBlockFromItem(stack.getItem()) == Blocks.DIAMOND_BLOCK ||
							Block.getBlockFromItem(stack.getItem()) == Blocks.EMERALD_BLOCK ||
							Block.getBlockFromItem(stack.getItem()) == Blocks.REDSTONE_BLOCK ||
							Block.getBlockFromItem(stack.getItem()) == Blocks.LAPIS_BLOCK);
				}
			});
	  	// コンテナ
	  	for (int col = 0;  col < 6; col++){
	  		addSlot(new Slot(inventory, col+2, 13+27*(col-2), 40){
					public boolean isItemValid(ItemStack stack) {
						return inventory.isItemValidForSlot(this.slotNumber, stack);
					}
	  			});
	  	}

	}

	@Override
	public TileEntityCompresser getTileEntity(){
		return (TileEntityCompresser)this.inventory;
	}
}
