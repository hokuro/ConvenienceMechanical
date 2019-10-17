package mod.cvbox.tileentity.farm;

import java.util.List;
import java.util.UUID;

import com.mojang.authlib.GameProfile;

import mod.cvbox.entity.EntityCore;
import mod.cvbox.entity.EntityPlayerDummy;
import mod.cvbox.item.ItemBattery;
import mod.cvbox.item.ItemCore;
import mod.cvbox.tileentity.ab.TileEntityPowerBase;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;

public class TileEntityMillking extends TileEntityPowerBase  implements ISidedInventory {
	public static final String NAME = "millking";
	public static final int MAX_TANK = 64 * 9 * 5;
	public static final int FIELD_TANK = 0 + FIELD_OFFSET;
	public static final int FIELD_RANGEX = 1 + FIELD_OFFSET;
	public static final int FIELD_RANGEZ = 2 + FIELD_OFFSET;
	public static final int SLOT_TANK = 1;

	private static final int TICK_TIME = 12000;	// 1日に2回起動

	private int[] SLOT_BOTTOM = new int[] {SLOT_BATTERY, SLOT_TANK};

	private int tank;

    private int range_x;
    private int range_z;

    private EntityPlayerDummy dummy;

	public TileEntityMillking(){
		super(EntityCore.Millking, 2);
		tick_count = 0;
		range_x = 0;
		range_z = 0;
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
		return (this.tank < MAX_TANK);
	}

	@Override
	public int getTickTime() {
		return TICK_TIME;
	}

	@Override
    protected void onWork() {
    	List<CowEntity> entitys = world.getEntitiesWithinAABB(CowEntity.class,
    			new AxisAlignedBB(this.pos.getX() - (range_x+1), this.pos.getY() - 5, this.pos.getZ() - (range_z+1), this.pos.getX() + (range_x+1), this.pos.getY() + 5, this.pos.getZ() + (range_z+1)),
    			(entity)->{return entity != null && entity.isAlive();});
    	for (CowEntity ent : entitys) {
    		// バケツを持って牛にタッチ
    		dummy.setHeldItem(Hand.MAIN_HAND, Items.BUCKET.getDefaultInstance());
    		ent.processInitialInteract(dummy, Hand.MAIN_HAND);
    		// 牛乳を入手出来たらタンクに入れる
    		if (dummy.getHeldItem(Hand.MAIN_HAND).getItem() == Items.MILK_BUCKET) {
    			this.tank++;
    			if (this.tank >= MAX_TANK) {
    				// いっぱいになったー
    				break;
    			}
    		}
    	}
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
		tank = compound.getInt("tank");;

		range_x = compound.getInt("range_x");
		range_z = compound.getInt("range_z");
    }

	@Override
    public CompoundNBT write(CompoundNBT compound)
    {
		compound = super.write(compound);
		compound.putInt("tank",  tank);

		compound.putInt("range_x",range_x);
		compound.putInt("range_z",range_z);

        return compound;
    }

	@Override
	public int[] getSlotsForFace(Direction side) {
		return SLOT_BOTTOM;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		this.stacks.set(index, stack);
		if (tank > 0){
			int amount = 0;
			if (stacks.get(1).isEmpty()){
				amount = tank>64?64:tank;
				stacks.set(1, new ItemStack(ItemCore.item_millkball,amount));
			}else if (stacks.get(1).getCount() < 64){
				amount = (64 - stacks.get(1).getCount() > tank)?tank:(64 - stacks.get(1).getCount());
				stacks.get(1).grow(amount);
			}
			tank -= amount;
		}
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, Direction direction) {
		return isItemValidForSlot(index, itemStackIn);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
		if (index == SLOT_TANK){
			return true;
		}
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		boolean ret = false;
		if (index == SLOT_BATTERY) {
			ret = (stack.getItem() instanceof ItemBattery);
		}
		return ret;
	}


	@Override
	public int additionalgetField(int id) {
		int ret = 0;
		switch(id){
		case FIELD_TANK:
			ret = tank;
			break;
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
		case FIELD_TANK:
			tank = value;
			break;
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
		return 3;
	}

	public Item getLiquid(){
		return ItemCore.item_millkball;
	}
}
