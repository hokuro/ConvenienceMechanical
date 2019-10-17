package mod.cvbox.inventory.factory;

import mod.cvbox.core.Mod_ConvenienceBox;
import mod.cvbox.inventory.ab.ContainerPowerBase;
import mod.cvbox.tileentity.factory.TileEntityCrusher;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class ContainerCrusher extends ContainerPowerBase{

	public ContainerCrusher(int id, PlayerInventory playerInv, TileEntity te){
		super(Mod_ConvenienceBox.CONTAINER_CRUSHER, id, playerInv, te);
	}

	@Override
	protected void addOriginalSlot() {
		// インプット
		addSlot(new Slot(inventory, 1, 80,17){
				public boolean isItemValid(ItemStack stack) {
					return inventory.isItemValidForSlot(this.slotNumber, stack);
				}
			});
		// バケツ
		addSlot(new Slot(inventory, 2, 45, 40){
				public boolean isItemValid(ItemStack stack) {
					return inventory.isItemValidForSlot(this.slotNumber, stack);
				}
			});

		// コンテナインベントリ
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 9; col++) {
				addSlot(new Slot(inventory, col + 1 + (row * 9)+2, 8 + col * 18,72 + row * 18){
					public boolean isItemValid(ItemStack stack){
						return inventory.isItemValidForSlot(this.slotNumber, stack);
					}
				});
			}
		}
	}

	@Override
	protected void addPlayerSlot() {
		// プレイヤーインベントリ
		int OFFSET = 74;
		for (int rows = 0; rows < 3; rows++){
			for ( int slotIndex = 0; slotIndex < 9; slotIndex++){
				addSlot(new Slot(playerInv, slotIndex + (rows * 9) + 9, 8 + slotIndex * 18, 140 + rows * 18));
			}
		}

		// メインインベントリ
		for (int slotIndex = 0; slotIndex < 9; slotIndex++){
			addSlot(new Slot(playerInv, slotIndex, 8 + slotIndex * 18, 198));
		}
	}

	@Override
	public TileEntityCrusher getTileEntity(){
		return (TileEntityCrusher)this.inventory;
	}

}
