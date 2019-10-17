package mod.cvbox.tileentity.factory;

import mod.cvbox.entity.EntityCore;
import mod.cvbox.item.ItemBattery;
import mod.cvbox.item.ItemCore;
import mod.cvbox.tileentity.ab.TileEntityPowerBase;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;

public class TileEntityIPSCellMaker extends TileEntityPowerBase  implements ISidedInventory {
	public static final String NAME = "ipscellmaker";
	public static final int FIELD_COUNT = 0 + FIELD_OFFSET;

	public static final int SLOT_F = 1;
	public static final int SLOT_C = 2;

	private int[] SLOT_SIDE = new int[]{SLOT_BATTERY, SLOT_F};
	private int[] SLOT_BOTTOM = new int[]{SLOT_BATTERY, SLOT_F, SLOT_C};

	private int work_count;

	public static final int WORK_TIME = 100;

	public TileEntityIPSCellMaker(){
		super(EntityCore.IPSCellMaker, 3);
		work_count = 0;
	}

	public boolean isWorking(){
		return work_count > 0;
	}

	@Override
	public void tick() {
		super.tick();
		if (!getPower().isEmpty()) {
			if (checkPowerOn() && canWork()){
			}else{
				if (work_count > 0){
					work_count--;
				}else{
					work_count = 0;
				}
			}
		} else {
			work_count = 0;
		}
	}

	@Override
	protected boolean canWork() {
		boolean ret = false;
		ItemStack itemF = stacks.get(SLOT_F);
		ItemStack itemC = stacks.get(SLOT_C);
		if (!itemF.isEmpty()) {
			if (itemF.getItem() == Items.ROTTEN_FLESH) {
				if (itemC.isEmpty()) {
					ret = true;
				} else if (itemC.getItem() == ItemCore.item_ipscell && itemC.getCount() < Math.min(itemC.getMaxStackSize(), getInventoryStackLimit())){
					ret = true;
				}
			}
		} else {
			if (isWorking()) {
				ret = true;
			}
		}
		return ret;
	}

	@Override
	protected void onWork() {
		if (!isWorking()){
			stacks.get(SLOT_F).shrink(1);
			work_count++;
		} else {
			work_count++;
		}
		if (WORK_TIME < work_count){
			create();
			work_count = 0;
		}
	}

	@Override
	public int getTickTime() {
		return 0;
	}

    private void create() {
		int next = world.rand.nextInt(12) + 4;
    	if (stacks.get(SLOT_C).isEmpty()) {
    		stacks.set(SLOT_C, new ItemStack(ItemCore.item_ipscell, next));
    	} else {
    		int max = Math.min(getInventoryStackLimit(), stacks.get(SLOT_C).getMaxStackSize());
    		int cont = Math.min(max - stacks.get(SLOT_C).getCount(), next);
    		stacks.get(SLOT_C).grow(cont);
    	}
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
		if (index == SLOT_C) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		boolean ret = false;
		if (index == SLOT_BATTERY) {
			ret = (stack.getItem() instanceof ItemBattery);
		}else if(index == SLOT_F) {
			ret = (stack.getItem() == Items.ROTTEN_FLESH);
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
