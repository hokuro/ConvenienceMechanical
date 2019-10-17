package mod.cvbox.tileentity.factory;

import mod.cvbox.entity.EntityCore;
import mod.cvbox.item.ItemBattery;
import mod.cvbox.tileentity.ab.TileEntityPowerBase;
import mod.cvbox.util.ModUtil;
import mod.cvbox.util.ModUtil.CompaierLevel;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;

public class TileEntityCrusher extends TileEntityPowerBase  implements ISidedInventory {
	public static final String NAME = "crusher";
	public static final int COL = 9;
	public static final int ROW = 3;
	public static final int SLOT_MAX = (COL * ROW) + 3;
	public static final int FIELD_SELECT = 0 + FIELD_OFFSET;
	public static final int FIELD_DIRT = 1 + FIELD_OFFSET;
	public static final int FIELD_GRAVE = 2 + FIELD_OFFSET;
	public static final int FIELD_SAND = 3 + FIELD_OFFSET;
	public static final int FIELD_COUNT = 4 + FIELD_OFFSET;

	public static final int SLOT_ITEM = 1;
	public static final int SLOT_BUCKET = 2;
	private int[] SLOT_SIDE = new int[]{SLOT_BATTERY, SLOT_ITEM, SLOT_BUCKET};
	private int[] SLOT_BOTTOM = new int[]{SLOT_BATTERY, SLOT_ITEM, SLOT_BUCKET,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29};

	private int select;
	private int powder_dirt;
	private int powder_grave;
	private int powder_sand;
	public static final int WORK_TIME = 100;
	private int work_count;
	private ItemStack nextDrop;

	public TileEntityCrusher(){
		super(EntityCore.Crusher, SLOT_MAX);
		powder_dirt = 0;
		powder_grave = 0;
		powder_sand = 0;
		select = 0;
		work_count = 0;
		nextDrop = ItemStack.EMPTY;
	}

	public boolean isWorking(){
		return this.work_count > 0 && checkPowerOn();
	}

	@Override
	public void tick() {
		super.tick();
		if (!getPower().isEmpty()) {
			if (checkPowerOn() && canWork()){
				if (!world.isRemote) {
					if (!nextDrop.isEmpty()){
						output(nextDrop);
					}
				}
			}else{
				if (work_count > 0){
					work_count--;
				}else{
					work_count = 0;
				}
			}
		} else {
			if (work_count > 0){
				work_count--;
			}else{
				work_count = 0;
			}
		}
	}

	@Override
	protected boolean canWork() {
		return ((select != 0) && !isFullInventory(3));
	}

	@Override
	protected void onWork() {
		if (!isWorking()){
			if (!stacks.get(1).isEmpty()){
				stacks.get(1).shrink(1);
				work_count++;
			}
		} else {
			work_count++;
		}
		if (WORK_TIME < work_count){
			crush_block();
			work_count = 0;
		}
	}

	@Override
	public int getTickTime() {
		return 0;
	}


    private void crush_block() {
    	switch(select){
    	case 1:
    		this.powder_dirt += 25;
    		if (powder_dirt >= 100){
    			powder_dirt = 0;
    			if (stacks.get(2).getItem() == Items.WATER_BUCKET){
    				output(new ItemStack(Items.CLAY_BALL,4));
    			}else{
    				output(new ItemStack(Blocks.DIRT,1));
    			}
    		}
    		break;
    	case 2:
    		this.powder_grave += 20;
    		if (powder_grave >= 100){
    			powder_grave = 0;
    			output(new ItemStack(Blocks.GRAVEL,1));
    		}
    		break;
    	case 3:
    		this.powder_sand += 10;
    		if (powder_sand >= 100){
    			powder_sand = 0;
    			output(new ItemStack(Blocks.SAND,1));
    		}
    		break;
    	}
    }

    private void output(ItemStack item){
    	for (int i = 3; i < stacks.size(); i++){
    		ItemStack item2 = stacks.get(i);
    		if (item2.isEmpty()){
    			this.setInventorySlotContents(i, item.copy());
    			item = ItemStack.EMPTY;
    			break;
    		}else if (ModUtil.compareItemStacks(item2, item, CompaierLevel.LEVEL_EQUAL_ITEM)){
    			if (item2.getCount()+item.getCount() <= item.getMaxStackSize()){
    				item2.grow(item.getCount());
    				item.shrink(item.getCount());
    				this.setInventorySlotContents(i, item2);
    				break;
    			}
    		}
    	}
    	if (item.getCount() > 0){
    		nextDrop = item.copy();
    	}
    }

	@Override
    public void read(CompoundNBT compound) {
		super.read(compound);
		powder_dirt=compound.getInt("dirt");
		powder_grave=compound.getInt("grave");
		powder_sand=compound.getInt("sand");
		select=compound.getInt("select");
		work_count=compound.getInt("count");
    }

	@Override
    public CompoundNBT write(CompoundNBT compound) {
		compound = super.write(compound);
		compound.putInt("dirt", powder_dirt);
		compound.putInt("grave", powder_grave);
		compound.putInt("sand", powder_sand);
		compound.putInt("select", select);
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
		if (SLOT_BOTTOM[3] <= index && index <= SLOT_BOTTOM[SLOT_BOTTOM.length-1]){
			return true;
		}
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		boolean ret = false;
		if (index == SLOT_BATTERY) {
			ret = (stack.getItem() instanceof ItemBattery);
		} else if (index == SLOT_ITEM){
			ret = Block.getBlockFromItem(stack.getItem()).getMaterial(Block.getBlockFromItem(stack.getItem()).getDefaultState()) == Material.ROCK;
		} else if (index == SLOT_BUCKET){
			ret = Items.WATER_BUCKET == stack.getItem();
		}
		return ret;
	}

	@Override
	public int additionalgetField(int id) {
		int ret = 0;
		switch(id){
		case FIELD_SELECT:
			ret = select;
			break;
		case FIELD_DIRT:
			ret = powder_dirt;
			break;
		case FIELD_GRAVE:
			ret = powder_grave;
			break;
		case FIELD_SAND:
			ret = powder_sand;
			break;
		case FIELD_COUNT:
			ret = work_count;
			break;
		}
		return ret;
	}

	@Override
	public void additionalsetField(int id, int value) {
		switch(id){
		case FIELD_SELECT:
			select = value;
			break;
		case FIELD_DIRT:
			powder_dirt = value;
			break;
		case FIELD_GRAVE:
			powder_grave = value;
			break;
		case FIELD_SAND:
			powder_sand = value;
			break;
		case FIELD_COUNT:
			work_count = value;
			break;
		}
	}

	@Override
	public int additionalFieldCount() {
		return 5;
	}

	public ItemStack NextDrop(){
		return nextDrop;
	}

	public boolean inBuket() {
		return stacks.get(2).getItem() == Items.WATER_BUCKET;
	}
}
