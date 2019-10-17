package mod.cvbox.inventory.farm;

import java.util.List;

import org.apache.commons.lang3.BooleanUtils;

import mod.cvbox.core.ModCommon;
import mod.cvbox.core.Mod_ConvenienceBox;
import mod.cvbox.inventory.IPowerSwitchContainer;
import mod.cvbox.item.ItemSickle;
import mod.cvbox.tileentity.ab.TileEntityPlantNurtureBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class ContainerPlantNurture extends Container implements IPowerSwitchContainer{

	private NURTURETYPE nType;
	private NURTUREKIND nKind;
	private final IInventory playerInventory;
	private final TileEntityPlantNurtureBase entity;
	public ContainerPlantNurture(int id, PlayerInventory playerInv, TileEntity ent, NURTURETYPE nTypeIn, NURTUREKIND nKindIn) {
		super(Mod_ConvenienceBox.CONTAINER_PLANTNURTURE, id);
		nType = nTypeIn;
		nKind = nKindIn;
		entity = (TileEntityPlantNurtureBase)ent;
		playerInventory = playerInv;

		// iツール
		IInventory tool = entity.getToolInventory();
		addSlot(new Slot(tool, 0, 123, 6){
				public boolean isItemValid(ItemStack stack) {
					return entity.isValidateTools(0, stack);
				}
			});

		addSlot(new Slot(tool, 1, 176, 36){
			public boolean isItemValid(ItemStack stack) {
				return entity.isValidateTools(1, stack);
			}
		});
		addSlot(new Slot(tool, 2, 176, 54){
			public boolean isItemValid(ItemStack stack) {
				return entity.isValidateTools(2, stack);
			}
		});

		// iメイン
		for (int row = 0; row < TileEntityPlantNurtureBase.ROW_SLOT; row++) {
			for (int col = 0; col < TileEntityPlantNurtureBase.COL_SLOT; col++) {
				addSlot(new Slot(nType.getMainInventory(entity), col + (row * TileEntityPlantNurtureBase.COL_SLOT), 8 + col * 18, 36 + row * 18) {
					public boolean isItemValid(ItemStack stack) {
						return nType.getMainInventoryValidation(this.slotNumber, stack, entity);
					}
				});
			}
		}

		// iプレイヤーインベントリ
		for (int rows = 0; rows < 3; rows++){
			for ( int slotIndex = 0; slotIndex < 9; slotIndex++){
				addSlot(new Slot(playerInventory, slotIndex + (rows * 9) + 9, 8 + slotIndex * 18, 139 + rows * 18));
			}
		}

		// iメインインベントリ2
		for (int slotIndex = 0; slotIndex < ModCommon.PLANTER_MAX_SLOT; slotIndex++){
			addSlot(new Slot(this.playerInventory, slotIndex, 8 + slotIndex * 18, 197));
		}
	}

	@Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < playerInventory.getSizeInventory()) {
                if (!this.mergeItemStack(itemstack1, playerInventory.getSizeInventory(), this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (itemstack.getItem() instanceof ItemSickle){
            	if (!this.mergeItemStack(itemstack1,55,56,false)){
                    if (!this.mergeItemStack(itemstack1, 0, playerInventory.getSizeInventory(), false)) {
                        return ItemStack.EMPTY;
                    }
            	}
            }else if (!this.mergeItemStack(itemstack1, 0, playerInventory.getSizeInventory(), false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    @Override
    public void onContainerClosed(PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);
        this.entity.closeInventory(playerIn);
    }

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return entity.isUsableByPlayer(playerIn);
	}

	public void setDeliverMode(Boolean deliver) {
		entity.setField(1, BooleanUtils.toInteger(deliver));
	}

	public World getWorld() {
		return entity.getWorld();
	}

	@Override
	public void addListener(IContainerListener listener) {
		super.addListener(listener);
		for (int i = 0; i < entity.getFieldCount(); i++) {
			listener.sendWindowProperty(this, i, entity.getField(i));
		}
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		List<IContainerListener> listeners = ObfuscationReflectionHelper.getPrivateValue(Container.class, this, "listeners");
		for (int i = 0; i < listeners.size(); ++i) {
			for (int j = 0; j < entity.getFieldCount(); j++) {
				listeners.get(i).sendWindowProperty(this, j, entity.getField(j));
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	public void updateProgressBar(int id, int data) {
		entity.setField(id, data);
	}

	@Override
	public TileEntityPlantNurtureBase getTileEntity(){
		return entity;
	}


	public static enum NURTURETYPE{
		PLANTER(0, (ent)->{return ent.getPlanterInventory();}, (slot, stack, ent)->{return ent.isValidatePlanter(slot,stack);}),
		HARVESTER(1, (ent)->{return ent.getHarvesterInventory();}, (slot, stack, ent)->{return ent.isValidateHarvester(slot,stack);});

		private final int id;
		private final NurtureTypeInventory<TileEntityPlantNurtureBase> func1;
		private NurtureTypeValidation<Integer, ItemStack, TileEntityPlantNurtureBase> func2;
		private NURTURETYPE(int index, NurtureTypeInventory<TileEntityPlantNurtureBase> f1, NurtureTypeValidation<Integer, ItemStack, TileEntityPlantNurtureBase> f2) {
			id = index;
			func1 = f1;
			func2 = f2;
		}

		public int getIndex() {return id;}
		public static NURTURETYPE getByIndex(int id) {
			if (id == 0) {
				return PLANTER;
			}else {
				return HARVESTER;
			}
		}

		public IInventory getMainInventory(TileEntityPlantNurtureBase ent) {
			return func1.test(ent);
		}

		public boolean getMainInventoryValidation(int slot, ItemStack stack, TileEntityPlantNurtureBase ent) {
			return func2.test(slot, stack, ent);
		}
	}

	public static enum NURTUREKIND{
		FARMING(0),
		SAPLING(1);

		private final int id;
		private NURTUREKIND(int index) {
			id = index;
		}

		public int getIndex() {return id;}
		public static NURTUREKIND getByIndex(int index) {
			if (index == FARMING.id) {
				return FARMING;
			}else if(index == SAPLING.id) {
				return SAPLING;
			}
			return FARMING;
		}

	}

	public interface NurtureTypeInventory<T> {
	    IInventory test(T t);
	}

	public interface NurtureTypeValidation<T,E,M>{
		boolean test(T t, E e, M m);
	}

}
