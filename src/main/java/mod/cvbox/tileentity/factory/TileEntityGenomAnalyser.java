package mod.cvbox.tileentity.factory;

import mod.cvbox.entity.EntityCore;
import mod.cvbox.item.ItemBattery;
import mod.cvbox.item.ItemCore;
import mod.cvbox.item.ItemMobGenom;
import mod.cvbox.tileentity.ab.TileEntityPowerBase;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;

public class TileEntityGenomAnalyser extends TileEntityPowerBase  implements ISidedInventory {
	public static final String NAME = "genomanalyser";
	public static final int FIELD_COUNT = 0 + FIELD_OFFSET;

	public static final int SLOT_T = 1;
	public static final int SLOT_G = 2;

	private int[] SLOT_SIDE = new int[]{SLOT_BATTERY, SLOT_T};
	private int[] SLOT_BOTTOM = new int[]{SLOT_BATTERY, SLOT_T, SLOT_G};

	private int work_count;

	public static final int WORK_TIME = 100;
	private String genomName;

	public TileEntityGenomAnalyser(){
		super(EntityCore.GenomAnalyser, 3);
		work_count = 0;
		genomName = "";
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
		ItemStack itemT = stacks.get(SLOT_T);
		ItemStack itemG = stacks.get(SLOT_G);
		if (!itemT.isEmpty()) {
			if (itemT.getItem() == ItemCore.item_mobtissue) {
				if (itemG.isEmpty()) {
					ret = true;
				} else if (itemG.getCount() < Math.min(itemG.getMaxStackSize(), getInventoryStackLimit())){
					String tissue = ItemMobGenom.getMob(itemT);
					String genom = ItemMobGenom.getMob(itemG);
					if (tissue.equals(genom)) {
						ret = true;
					}
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
			genomName = ItemMobGenom.getMob(stacks.get(SLOT_T));
			stacks.get(SLOT_T).shrink(1);
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
    	if (stacks.get(SLOT_G).isEmpty()) {
    		ItemStack genom = new ItemStack(ItemCore.item_mobgenom,1);
    		ItemMobGenom.setMob(genom, genomName);
    		stacks.set(SLOT_G, genom);
    	} else {
    		stacks.get(SLOT_G).grow(1);
    	}
    }

	@Override
    public void read(CompoundNBT compound) {
		super.read(compound);
		work_count=compound.getInt("count");
		genomName = compound.getString("mobname");
	}

	@Override
    public CompoundNBT write(CompoundNBT compound) {
		compound = super.write(compound);
		compound.putInt("count", work_count);
		compound.putString("mobname", genomName);
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
		if (index == SLOT_G) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		boolean ret = false;
		if (index == SLOT_BATTERY) {
			ret = (stack.getItem() instanceof ItemBattery);
		}else if(index == SLOT_T) {
			ret = (stack.getItem() == ItemCore.item_mobtissue);
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
