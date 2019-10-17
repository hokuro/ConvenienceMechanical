package mod.cvbox.tileentity.factory;

import java.util.List;

import mod.cvbox.entity.EntityCore;
import mod.cvbox.tileentity.ab.TileEntityPowerBase;
import mod.cvbox.util.ModUtil;
import mod.cvbox.util.ModUtil.CompaierLevel;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;

public class TileEntityVacumer extends TileEntityPowerBase  implements ISidedInventory {
	public static final String NAME = "vacumer";
	public static final int COL = 9;
	public static final int ROW = 1;
	public static final int MAX_SLOT = (COL*ROW) + 1;
	private int[] SLOT_BOTTOM;

	private boolean power;

	public static final int WORK_TIME = 3 * 60 * 20;	// 3分煮1回動く
	private int crush_count;
	private int power_count;


	public TileEntityVacumer(){
		super(EntityCore.Vacumer, MAX_SLOT);
		power = false;
		crush_count = 0;
		SLOT_BOTTOM =new int[MAX_SLOT];
		for ( int i = 0; i < MAX_SLOT; i++){
			SLOT_BOTTOM[i]=i;
		}
	}

	@Override
	protected boolean canWork() {
		return !isFullInventory();
	}

	@Override
	protected void onWork() {
		List<ItemEntity> entitys = world.getEntitiesWithinAABB(ItemEntity.class,
				new AxisAlignedBB(pos.getX() - 5.0D, pos.getY() - 5.0D, pos.getZ() - 5.0D, pos.getX() + 5.0D, pos.getY() + 5.0D, pos.getZ() + 5.0D),
				(entity) -> {
					return entity != null && entity.isAlive();
				});
        for (ItemEntity entityitem : entitys) {
        	if (entityitem != null){
        		ItemStack itemstack = entityitem.getItem().copy();
        		for (int i = 1; i < stacks.size(); i++){
        			ItemStack st = stacks.get(i);

        			if (st.isEmpty() || (ModUtil.compareItemStacks(st, itemstack, CompaierLevel.LEVEL_EQUAL_ITEM) && st.getCount()<st.getMaxStackSize())){
        				if (st.isEmpty()){
        					stacks.set(i, itemstack);
        					entityitem.remove();
        					break;
        				}else{
        					int size = itemstack.getCount();
        					int size2 = st.getCount();
        					if (size2+size <= st.getMaxStackSize()){
        						st.grow(size);
        						stacks.set(i, st);
            					entityitem.remove();
            					break;
        					}else{
        						st.setCount(st.getMaxStackSize());
        						stacks.set(i, st);
        						itemstack.setCount(st.getMaxStackSize() - size2);
        						entityitem.setItem(itemstack);
        					}
        				}
        			}
        		}
        	}
        }
	}

	@Override
	public int getTickTime() {
		return WORK_TIME;
	}

	@Override
	public void Exec() {
		if (!world.isRemote) {
			if (checkPowerOn() && canWork()) {
				onWork();
				tick_count = 0;
				power_count = 0;
			}
		}
	}

	@Override
    public void read(CompoundNBT compound) {
		super.read(compound);
    }

	@Override
    public CompoundNBT write(CompoundNBT compound) {
		compound = super.write(compound);
        return compound;
    }

	@Override
	public int[] getSlotsForFace(Direction side) {
		return SLOT_BOTTOM;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, Direction direction) {
		return false;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
		boolean ret = false;
		if (index != SLOT_BATTERY) {
			ret = true;
		}
		return ret;
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}

	@Override
	public int additionalgetField(int id) {
		return 0;
	}

	@Override
	public void additionalsetField(int id, int value) {
	}

	@Override
	public int additionalFieldCount() {
		return 0;
	}
}
