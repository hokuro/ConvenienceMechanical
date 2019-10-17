package mod.cvbox.tileentity.factory;

import mod.cvbox.entity.EntityCore;
import mod.cvbox.item.ItemBattery;
import mod.cvbox.tileentity.ab.TileEntityPowerBase;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;

public class TileEntityBedrockMaker extends TileEntityPowerBase  implements ISidedInventory {
	public static final String NAME = "bedrockmaker";
	public static final int FIELD_COUNT = 0 + FIELD_OFFSET;

	public static final int SLOT_IND = 1;
	public static final int SLOT_INO = 2;
	public static final int SLOT_OUT = 3;

	private int[] SLOT_SIDE = new int[]{SLOT_BATTERY, SLOT_IND, SLOT_INO};
	private int[] SLOT_BOTTOM = new int[]{SLOT_BATTERY, SLOT_IND, SLOT_INO, SLOT_OUT};

	public static final int WORK_TIME = 200;	// 10秒
	private int work_count;

	public TileEntityBedrockMaker(){
		super(EntityCore.BedrockMaker, 4);
		work_count = 0;
	}

	public boolean isWorking(){
		return this.work_count > 0 && checkPowerOn();
	}

	@Override
	public void tick() {
		super.tick();
		if (!getPower().isEmpty()) {
			if (checkPowerOn() && canWork()){
			}else{
				work_count = 0;
			}
		} else {
			work_count = 0;
		}
	}

	@Override
	protected boolean canWork() {
		boolean ret = false;
		if (checkSlotD() && checkSlotO() && checkSlotOut()) {
			ret = true;
		}else {
			if (isWorking()) {
				ret = true;
			}
		}
		return ret;
	}

	private boolean checkSlotD() {
		ItemStack item = stacks.get(SLOT_IND);
		return (!item.isEmpty() && item.getItem() == Items.DIAMOND_BLOCK);
	}

	private boolean checkSlotO() {
		ItemStack item = stacks.get(SLOT_INO);
		return (!item.isEmpty() && item.getItem() == Items.OBSIDIAN);
	}

	private boolean checkSlotOut() {
		ItemStack item = stacks.get(SLOT_OUT);
		return (item.isEmpty() || (item.getItem() == Items.BEDROCK && item.getCount() < Math.min(item.getMaxStackSize(), getInventoryStackLimit())));
	}

	@Override
	protected void onWork() {
		if (!isWorking()){
			stacks.get(SLOT_IND).shrink(1);
			stacks.get(SLOT_INO).shrink(1);
			work_count++;
		} else {
			work_count++;
		}
		if (WORK_TIME < work_count){
			if (stacks.get(SLOT_OUT).isEmpty()) {
				stacks.set(SLOT_OUT, new ItemStack(Items.BEDROCK,1));
			} else {
				stacks.get(SLOT_OUT).grow(1);
			}
			work_count = 0;
		}
	}

	@Override
	public int getTickTime() {
		return 0;
	}

	@Override
    public void read(CompoundNBT compound) {
		super.read(compound);
		work_count=compound.getInt("count");
    }

	@Override
    public CompoundNBT write(CompoundNBT compound) {
		compound = super.write(compound);
		compound.putInt("count", work_count);
        return compound;
    }

	@Override
	public int[] getSlotsForFace(Direction side) {
		if (side == Direction.DOWN){
			return SLOT_BOTTOM;
		}
		return SLOT_SIDE;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, Direction direction) {
		return isItemValidForSlot(index, itemStackIn);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
		if (direction == Direction.DOWN && index == SLOT_OUT){
			return true;
		}
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		boolean ret = false;
		if (index == SLOT_BATTERY){
			ret = (stack.getItem() instanceof ItemBattery);
		}else if (index == SLOT_IND){
			ret = (stack.getItem() == Items.DIAMOND_BLOCK);
		}else if (index == SLOT_INO) {
			ret = (stack.getItem() == Items.OBSIDIAN);
		}
		return ret;
	}

	@Override
	public int additionalgetField(int id) {
		int ret = 0;
		switch(id){
		case FIELD_COUNT:
			ret = work_count;
			break;
		}
		return ret;
	}

	@Override
	public void additionalsetField(int id, int value) {
		switch(id){
		case FIELD_COUNT:
			work_count = value;
			break;
		}
	}

	@Override
	public int additionalFieldCount() {
		return 1;
	}
}
