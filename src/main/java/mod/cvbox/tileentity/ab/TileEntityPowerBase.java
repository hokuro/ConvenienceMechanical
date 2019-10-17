package mod.cvbox.tileentity.ab;

import org.apache.commons.lang3.BooleanUtils;

import mod.cvbox.util.ModUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;

public abstract class TileEntityPowerBase extends TileEntity  implements IInventory, ITickableTileEntity, IPowerSwitchEntity, ITileEntityParameter{
	public static final int SLOT_BATTERY = 0;

	public static final int FIELD_BATTERY = 0;
	public static final int FIELD_POWER = 1;
	public static final int FIELD_TIMECNT = 2;
	public static final int FIELD_BATTERYMAX = 100;
	public static final int FIELD_OFFSET = 3;

	protected final int slottMax;
	protected final NonNullList<ItemStack> stacks;

	protected boolean power;
	protected int power_count;
	protected int tick_count;

	public TileEntityPowerBase(TileEntityType<?> teType, int slots){
		super(teType);
		slottMax = slots;
		stacks = NonNullList.<ItemStack>withSize(slottMax, ItemStack.EMPTY);
		power = false;
		power_count = 0;
		tick_count = 0;
	}

	@Override
	public void tick() {
		if (!getPower().isEmpty()) {
			if (power_count == 0){
				powerDown();
			}
			if (!world.isRemote){
				if (checkPowerOn() && canWork()) {
					tick_count++;
					if (tick_count >= getTickTime()) {
						onWork();
						tick_count = 0;
					}
					power_count++;
					if (power_count > 40){
						power_count = 0;
					}
				}else{
					tick_count = 0;
					power_count++;
					if (power_count > 2000){
						power_count = 0;
					}
				}
			}else{
				if (checkPowerOn()){
					if (Math.random() > 0.7){
						ModUtil.addParticleTypes(this.world, this.pos, RedstoneParticleData.REDSTONE_DUST);
					}
				}
			}
		}else {
			power_count = 0;
			tick_count = 0;
		}
	}

	protected boolean checkPowerOn(){
		ItemStack st = getPower();
		return (((!st.isEmpty()) && (st.getMaxDamage() -  st.getDamage() > 1)) && power);
	}

	protected void powerDown(){
		int damage = getPower().getDamage()+1;
		getPower().setDamage(damage);
	}

	@Override
    public void read(CompoundNBT compound) {
		super.read(compound);

		power = compound.getBoolean("power");
		power_count=compound.getInt("power_count");
		tick_count = compound.getInt("count");

		this.stacks.clear();
		ItemStackHelper.loadAllItems(compound, this.stacks);
    }

	@Override
    public CompoundNBT write(CompoundNBT compound) {
		compound = super.write(compound);

		compound.putBoolean("power", power);
		compound.putInt("power_count",power_count);
		compound.putInt("count", tick_count);

		ItemStackHelper.saveAllItems(compound, stacks);
        return compound;
    }

	@Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT cp = super.getUpdateTag();
        return this.write(cp);
    }

	@Override
    public void handleUpdateTag(CompoundNBT tag) {
		super.handleUpdateTag(tag);
		this.read(tag);
    }

	@Override
	public SUpdateTileEntityPacket getUpdatePacket()  {
        CompoundNBT CompoundNBT = new CompoundNBT();
        return new SUpdateTileEntityPacket(this.pos, 1,  this.write(CompoundNBT));
    }

	@Override
	public int getSizeInventory() {
		return this.slottMax;
	}

	@Override
	public boolean isEmpty() {
		boolean ret = true;
		for (ItemStack item : stacks) {
			if (!item.isEmpty()) {
				ret = false;
				break;
			}
		}
		return ret;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return this.stacks.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		return ItemStackHelper.getAndSplit(this.stacks, index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(this.stacks, index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		this.stacks.set(index, stack);
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUsableByPlayer(PlayerEntity player) {
		if (this.world.getTileEntity(this.pos) != this) {
			return false;
		}
		return player.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory(PlayerEntity player) {
	}

	@Override
	public void closeInventory(PlayerEntity player) {
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}

	@Override
	public int getField(int id) {
		int ret = 0;
		switch(id){
		case FIELD_POWER:
			ret = BooleanUtils.toInteger(power);
			break;
		case FIELD_BATTERY:
			ret = getPower().getDamage();
			break;
		case FIELD_TIMECNT:
			ret = tick_count;
			break;
		case FIELD_BATTERYMAX:
			ret = getPower().getMaxDamage();
			break;
		default:
			ret = additionalgetField(id);
			break;
		}
		return ret;
	}

	@Override
	public void setField(int id, int value) {
		switch(id){
		case FIELD_POWER:
			power = BooleanUtils.toBoolean(value);
			break;
		case FIELD_BATTERY:
			getPower().setDamage(value);
			break;
		case FIELD_TIMECNT:
			tick_count = value;
			break;
		default:
			additionalsetField(id, value);
			break;
		}
	}

	@Override
	public int getFieldCount() {
		return 3 + additionalFieldCount();
	}

	public int additionalgetField(int id) {
		return 0;
	}
	public void additionalsetField(int id, int value) {

	}
	public int additionalFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		stacks.clear();
	}

	@Override
	public void setPower(boolean value) {
		this.power = value;
	}

	public ItemStack getPower(){
		return this.stacks.get(0);
	}

	protected abstract boolean canWork();
	protected abstract void onWork();
	public abstract int getTickTime();
	public void Exec() {

	}

	public boolean isFullInventory() {
		return isFullInventory(1);
	}


	public ItemStack addItem(ItemStack item, int startIndex) {
		ItemStack ret = ItemStack.EMPTY;
		if (item.isEmpty()) {return ret;}
		for (int i = startIndex; i < stacks.size(); i++) {
			ItemStack slotItem = stacks.get(i);
			int maxSize = Math.min(slotItem.getMaxStackSize(), getInventoryStackLimit());
			if (slotItem.isEmpty()) {
				stacks.set(i, new ItemStack(item.getItem(),Math.min(item.getCount(),maxSize)));
				item.shrink(maxSize);
			} else if (item.getItem() == slotItem.getItem() && slotItem.getCount() < maxSize){
				int count = maxSize - slotItem.getCount();
				slotItem.grow(Math.min(item.getCount(),count));
				item.shrink(count);
			}
			if (item.isEmpty()) {break;}
		}
		if (!item.isEmpty()) {
			ret = item.copy();
		}
		return ret;
	}

	protected boolean isFullInventory(int startIndex) {
		boolean ret = true;
		for (int i = startIndex; i < this.stacks.size() && ret; i++) {
			ItemStack item = stacks.get(i);
			int maxSize = Math.min(item.getMaxStackSize(), getInventoryStackLimit());
			if (item.isEmpty() || item.getCount() < maxSize) {
				ret = false;
			}
		}
		return ret;
	}
}
