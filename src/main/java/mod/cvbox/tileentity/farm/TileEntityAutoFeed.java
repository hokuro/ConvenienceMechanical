package mod.cvbox.tileentity.farm;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.mojang.authlib.GameProfile;

import mod.cvbox.entity.EntityCore;
import mod.cvbox.entity.EntityPlayerDummy;
import mod.cvbox.item.ItemBattery;
import mod.cvbox.tileentity.ab.TileEntityPowerBase;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;

public class TileEntityAutoFeed extends TileEntityPowerBase  implements ISidedInventory {
	public static final String NAME = "autofeed";
	public static final int COL = 3;
	public static final int ROW = 3;
	public static final int MAX_SLOT = (COL * ROW) + 1;

	public static final int FIELD_RANGEX = 0 + FIELD_OFFSET;
	public static final int FIELD_RANGEZ = 1+ FIELD_OFFSET;

	public static final int[] SLOT_SIDE = new int[COL*ROW+1];

	private static final int TICK_TIME = 6000;	// 1日に4回起動

    private int range_x;
    private int range_z;

    private EntityPlayerDummy dummy;

	public TileEntityAutoFeed(){
		super(EntityCore.AutoFeed, MAX_SLOT);
		range_x = 0;
		range_z = 0;
		for (int i = 0; i < (COL * ROW) +1; i++) {
			SLOT_SIDE[i] = i;
		}
	}

	@Override
	public void tick() {
		if (!world.isRemote) {
			if (dummy == null){
				dummy = new EntityPlayerDummy(this.world, new GameProfile(new UUID(0,0), "dummy"));
			}
		}
		super.tick();
	}

	@Override
	protected boolean canWork() {
		return hasFeed();
	}

	@Override
	public int getTickTime() {
		return TICK_TIME;
	}

	@Override
    protected void onWork() {
    	// 動物を探してループする
    	List<AnimalEntity> entitys = world.getEntitiesWithinAABB(AnimalEntity.class,
    			new AxisAlignedBB(this.pos.getX() - (range_x+1), this.pos.getY() - 5, this.pos.getZ() - (range_z+1), this.pos.getX() + (range_x+1), this.pos.getY() + 5, this.pos.getZ() + (range_z+1)),
    			(entity)->{return entity != null && entity.isAlive();});

    	if (entitys.size() > 0) {
	    	for (ItemStack item : stacks) {
	    		if (entitys.size() > 0) {
	    	    	ItemStack feed = ItemStack.EMPTY;
		    		if (!item.isEmpty() && !(item.getItem() instanceof ItemBattery)) {
		    			// 餌を使い果たしたか、前と違う餌なら持ち替えてばらまく
		    			if (feed.isEmpty() || feed.getItem() != item.getItem()) {
		    				feed = item;
		    				// 餌を手に持つ
		    				dummy.setHeldItem(Hand.MAIN_HAND, feed);
		    		    	Iterator<AnimalEntity> workList = entitys.iterator();
		    		    	while(workList.hasNext()) {
		    		    		AnimalEntity animal = workList.next();
		    		    		// 餌やりに成功したら削除
		    		    		if (animal.isBreedingItem(feed) && animal.processInitialInteract(dummy, Hand.MAIN_HAND)) {
		    		    			workList.remove();
		    		    		}
		    		    		if (feed.isEmpty()) {
		    		    			break;
		    		    		}
		    		    	}
		    			}
		    		}
	    		} else {
	    			break;
	    		}
	    	}
    	}
    }

	public boolean hasFeed() {
		boolean ret = false;
		for (ItemStack item : stacks) {
			if (!item.isEmpty()) {
				ret = true;
				break;
			}
		}
		return ret;
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

		range_x = compound.getInt("range_x");
		range_z = compound.getInt("range_z");
    }

	@Override
    public CompoundNBT write(CompoundNBT compound) {
		compound = super.write(compound);

		compound.putInt("range_x",range_x);
		compound.putInt("range_z",range_z);

        return compound;
    }

	@Override
	public int[] getSlotsForFace(Direction side) {
		return SLOT_SIDE;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, Direction direction) {
		return isItemValidForSlot(index, itemStackIn);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		boolean ret = false;
		if (index == SLOT_BATTERY) {
			ret = (stack.getItem() instanceof ItemBattery);
		} else if (index <= SLOT_SIDE[SLOT_SIDE.length-1]){
			ret = true;
		}
		return ret;
	}

	@Override
	public int getSizeInventory() {
		return this.stacks.size();
	}

	@Override
	public int additionalgetField(int id) {
		int ret = 0;
		switch(id){
		case FIELD_RANGEX:
			ret = range_x;
			break;
		case FIELD_RANGEZ:
			ret = range_z;
			break;
		}
		return ret;
	}

	@Override
	public void additionalsetField(int id, int value) {
		switch(id){
		case FIELD_RANGEX:
			range_x = value;
			break;
		case FIELD_RANGEZ:
			range_z = value;
			break;
		}
	}

	@Override
	public int additionalFieldCount() {
		return 2;
	}
}
