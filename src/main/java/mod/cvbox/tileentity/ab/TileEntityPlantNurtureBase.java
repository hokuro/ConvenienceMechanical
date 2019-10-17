package mod.cvbox.tileentity.ab;


import org.apache.commons.lang3.BooleanUtils;

import mod.cvbox.inventory.farm.ContainerPlantNurture.NURTUREKIND;
import mod.cvbox.util.ModUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class TileEntityPlantNurtureBase extends TileEntity  implements ITickableTileEntity, ISidedInventory, IPowerSwitchEntity, ITileEntityParameter{
	public static final int ROW_SLOT = 5;
	public static final int COL_SLOT = 9;

	public static final String NAME = "afforestation";
	public static final int SLOT_BATTERY = 0;
	public static final int SLOT_TOOL = 1;
	public static final int SLOT_MEAL = 2;

	public static final int FIELD_POWER = 0;
	public static final int FIELD_DELIVER = 1;
	public static final int FIELD_BATTERY = 2;
	public static final int FIELD_BATTERYMAX = 3;
	public static final int FIELD_NEXT_X = 4;
	public static final int FIELD_NEXT_Z = 5;
	public static final int FILED_NEXTPOS = 6;

	protected Inventory toolInventory;
	protected Inventory planterInventory;
	protected Inventory harvesterInventory;

	protected boolean power;
	protected int nextPos;
	protected int[] next_x;
	protected int[] next_z;
	protected boolean canDeliver;

	// SideInventory
    protected final int[] SLOTS_TOP;
    protected final int[] SLOTS_DOWN;
    protected int timeCnt;
    protected int power_count;
    protected NURTUREKIND nKind;

	public TileEntityPlantNurtureBase(TileEntityType<?> tetype, NURTUREKIND nKindIn) {
		super(tetype);
		toolInventory = new Inventory(3);
		planterInventory = new Inventory(COL_SLOT * COL_SLOT);
		harvesterInventory = new Inventory(ROW_SLOT * COL_SLOT);

		power = false;
		SLOTS_TOP = new int[toolInventory.getSizeInventory()+planterInventory.getSizeInventory()];
		SLOTS_DOWN = new int[toolInventory.getSizeInventory() + planterInventory.getSizeInventory() +harvesterInventory.getSizeInventory()];
		for (int i = 0; i < SLOTS_TOP.length; i++){
			SLOTS_TOP[i] = i;
		}
		for (int i = 0; i< SLOTS_DOWN.length; i++) {
			SLOTS_DOWN[i] = i;
		}

		makeArray();
		nextPos = 0;
		canDeliver = false;
		timeCnt = 0;
		power_count = 0;
		nKind = nKindIn;
	}

	protected abstract void makeArray();
	public Inventory getToolInventory() {
		return this.toolInventory;
	}

	public Inventory getPlanterInventory() {
		return this.planterInventory;
	}

	public Inventory getHarvesterInventory() {
		return this.harvesterInventory;
	}

	public boolean isFullHarvestBox() {
		boolean ret = true;
		for (int i = 0; i < harvesterInventory.getSizeInventory(); i++) {
			if (harvesterInventory.getStackInSlot(i).isEmpty() || harvesterInventory.getStackInSlot(i).getCount() < harvesterInventory.getStackInSlot(i).getMaxStackSize()) {
				ret = false;
				break;
			}
		}
		return ret;
	}

	@Override
	public void tick() {
		if (!getPower().isEmpty()) {
			// 電池の耐久力を減らす
			if (power_count == 0) {
				powerDown();
			}
			power_count++;
			if (!world.isRemote) {
				if (checkPowerOn()){
					Exec();
					// 40tic毎に電力消費
					if (power_count > 40){
						power_count = 0;
					}
				}else{
					// 2000tic毎に待機電力がかかる
					if (power_count > 2000){
						power_count = 0;
					}
				}

			}else {
				// iスイッチONならパーティクル表示
				if (checkPowerOn()){
					if (Math.random() > 0.7){
						ModUtil.addParticleTypes(this.world, this.pos.add(world.rand.nextDouble(), world.rand.nextDouble(), world.rand.nextDouble()), RedstoneParticleData.REDSTONE_DUST);
					}
				}
			}
		} else {
			power_count = 0;
		}
	}

	private boolean checkPowerOn(){
		ItemStack st = getPower();
		return (((!st.isEmpty()) && (st.getMaxDamage() - st.getDamage() > 1)) && power);
	}

	private void powerDown(){
		int damage = getPower().getDamage()+1;
		getPower().setDamage(damage);
	}

	public ItemStack getPower(){
		return this.toolInventory.getStackInSlot(SLOT_BATTERY);
	}

	public boolean canDeliver() {
		return this.canDeliver;
	}

	protected abstract void Exec();

	public abstract boolean isValidateTools(int slot, ItemStack stack);

	public abstract boolean isValidatePlanter(int slot, ItemStack stack);

	public abstract boolean isValidateHarvester(int slot, ItemStack stack);

	@Override
	public void read(CompoundNBT CompoundNBT){
		super.read(CompoundNBT);
		power = CompoundNBT.getBoolean("isrun");
		nextPos = CompoundNBT.getInt("next");
		if (next_x.length < nextPos) {
			nextPos = 0;
		}
		canDeliver = CompoundNBT.getBoolean("deliver");
		power_count=CompoundNBT.getInt("power_count");

		// ツールインベントリ
		ListNBT listnbt = CompoundNBT.getList("toolsInventory", 10);
		for(int i = 0; i < listnbt.size(); ++i) {
			CompoundNBT compoundnbt = listnbt.getCompound(i);
			int j = compoundnbt.getByte("Slot") & 255;
			if (j >= 0 && j < this.toolInventory.getSizeInventory()) {
				this.toolInventory.setInventorySlotContents(j, ItemStack.read(compoundnbt));
			}
		}

		// プランターインベントリ
		listnbt = CompoundNBT.getList("planterInventory", 10);
		for(int i = 0; i < listnbt.size(); ++i) {
			CompoundNBT compoundnbt = listnbt.getCompound(i);
			int j = compoundnbt.getByte("Slot") & 255;
			if (j >= 0 && j < this.toolInventory.getSizeInventory()) {
				this.planterInventory.setInventorySlotContents(j, ItemStack.read(compoundnbt));
			}
		}

		// ハーベスターインベントリ
		listnbt = CompoundNBT.getList("harvesterInventory", 10);
		for(int i = 0; i < listnbt.size(); ++i) {
			CompoundNBT compoundnbt = listnbt.getCompound(i);
			int j = compoundnbt.getByte("Slot") & 255;
			if (j >= 0 && j < this.toolInventory.getSizeInventory()) {
				this.harvesterInventory.setInventorySlotContents(j, ItemStack.read(compoundnbt));
			}
		}
	}

	@Override
	public CompoundNBT write(CompoundNBT CompoundNBT){
		super.write(CompoundNBT);
		CompoundNBT.putBoolean("isrun", power);
		CompoundNBT.putInt("next", nextPos);
		CompoundNBT.putBoolean("deliver",canDeliver);
		CompoundNBT.putInt("power_count",power_count);

		// ツールインベントリ
		ListNBT itemsTagList = new ListNBT();
		for(int i = 0; i < this.toolInventory.getSizeInventory(); ++i) {
			ItemStack item = this.toolInventory.getStackInSlot(i);
			if (!item.isEmpty()) {
				CompoundNBT itemTagCompound = new CompoundNBT();
				itemTagCompound.putByte("Slot",(byte)i);
				item.write(itemTagCompound);
				itemsTagList.add(itemTagCompound);
			}
		}
		CompoundNBT.put("toolsInventory",itemsTagList);

		// プランターインベントリ
		itemsTagList = new ListNBT();
		for(int i = 0; i < this.planterInventory.getSizeInventory(); ++i) {
			ItemStack item = this.planterInventory.getStackInSlot(i);
			if (!item.isEmpty()) {
				CompoundNBT itemTagCompound = new CompoundNBT();
				itemTagCompound.putByte("Slot",(byte)i);
				item.write(itemTagCompound);
				itemsTagList.add(itemTagCompound);
			}
		}
		CompoundNBT.put("planterInventory",itemsTagList);

		// ハーベスターインベントリ
		itemsTagList = new ListNBT();
		for(int i = 0; i < this.harvesterInventory.getSizeInventory(); ++i) {
			ItemStack item = this.harvesterInventory.getStackInSlot(i);
			if (!item.isEmpty()) {
				CompoundNBT itemTagCompound = new CompoundNBT();
				itemTagCompound.putByte("Slot",(byte)i);
				item.write(itemTagCompound);
				itemsTagList.add(itemTagCompound);
			}
		}
		CompoundNBT.put("harvesterInventory",itemsTagList);

		return CompoundNBT;
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
	public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT CompoundNBT = new CompoundNBT();
        return new SUpdateTileEntityPacket(this.pos, 1,  this.write(CompoundNBT));
    }

	@Override
	public int getSizeInventory() {
		return this.toolInventory.getSizeInventory() + this.planterInventory.getSizeInventory() + this.harvesterInventory.getSizeInventory();
	}

	@Override
	public boolean isEmpty() {
		if (this.toolInventory.isEmpty() && this.planterInventory.isEmpty() && this.harvesterInventory.isEmpty()) {
			return true;
		}
		return false;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		ItemStack stack = ItemStack.EMPTY;
		if (index < this.toolInventory.getSizeInventory()) {
			stack = this.toolInventory.getStackInSlot(index);
		} else if(index < (this.toolInventory.getSizeInventory() + this.planterInventory.getSizeInventory())){
			stack = this.planterInventory.getStackInSlot(index-this.toolInventory.getSizeInventory());
		}else if (index < (this.toolInventory.getSizeInventory() + this.planterInventory.getSizeInventory() + this.harvesterInventory.getSizeInventory())) {
			stack = this.harvesterInventory.getStackInSlot(index - (this.toolInventory.getSizeInventory() + this.planterInventory.getSizeInventory()));
		}
		return stack;
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		ItemStack stack = ItemStack.EMPTY;
		if (index < this.toolInventory.getSizeInventory()) {
			stack = this.toolInventory.decrStackSize(index,count);
		} else if(index < (this.toolInventory.getSizeInventory() + this.planterInventory.getSizeInventory())){
			stack = this.planterInventory.decrStackSize(index-this.toolInventory.getSizeInventory(),count);
		}else if (index < (this.toolInventory.getSizeInventory() + this.planterInventory.getSizeInventory() + this.harvesterInventory.getSizeInventory())) {
			stack = this.harvesterInventory.decrStackSize(index - (this.toolInventory.getSizeInventory() + this.planterInventory.getSizeInventory()),count);
		}
		return stack;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack stack = ItemStack.EMPTY;
		if (index < this.toolInventory.getSizeInventory()) {
			stack = this.toolInventory.removeStackFromSlot(index);
		} else if(index < (this.toolInventory.getSizeInventory() + this.planterInventory.getSizeInventory())){
			stack = this.planterInventory.removeStackFromSlot(index-this.toolInventory.getSizeInventory());
		}else if (index < (this.toolInventory.getSizeInventory() + this.planterInventory.getSizeInventory() + this.harvesterInventory.getSizeInventory())) {
			stack = this.harvesterInventory.removeStackFromSlot(index - (this.toolInventory.getSizeInventory() + this.planterInventory.getSizeInventory()));
		}
		return stack;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		if (index < this.toolInventory.getSizeInventory()) {
			this.toolInventory.setInventorySlotContents(index,stack);
		} else if(index < (this.toolInventory.getSizeInventory() + this.planterInventory.getSizeInventory())){
			this.planterInventory.setInventorySlotContents(index-this.toolInventory.getSizeInventory(),stack);
		}else if (index < (this.toolInventory.getSizeInventory() + this.planterInventory.getSizeInventory() + this.harvesterInventory.getSizeInventory())) {
			this.harvesterInventory.setInventorySlotContents(index - (this.toolInventory.getSizeInventory() + this.planterInventory.getSizeInventory()),stack);
		}
	}

	@Override
	public boolean isUsableByPlayer(PlayerEntity player) {
		return true;
	}

	@Override
	public void clear() {
		this.toolInventory.clear();
		this.planterInventory.clear();
		this.harvesterInventory.clear();
	}

	@Override
	public int getField(int id) {
		int ret = 0;
		switch(id){
		case FIELD_POWER:
			ret = BooleanUtils.toInteger(power);
			break;
		case FIELD_DELIVER:
			ret = BooleanUtils.toInteger(canDeliver);
			break;

		case FIELD_BATTERY:
			ret = getPower().getDamage();
			break;
		case FIELD_BATTERYMAX:
			ret = getPower().getMaxDamage();
			break;

		case FIELD_NEXT_X:
			ret = next_x[nextPos];
			break;

		case FIELD_NEXT_Z:
			ret = next_z[nextPos];
			break;
		case FILED_NEXTPOS:
			ret = nextPos;
			break;
		default:

		}
		return ret;
	}

	@Override
	public void setField(int id, int value) {
		switch(id){
		case FIELD_POWER:
			power = BooleanUtils.toBoolean(value);
			this.nextPos = 0;
			break;
		case FIELD_DELIVER:
			canDeliver = BooleanUtils.toBoolean(value);
			break;

		case FIELD_BATTERY:
			getPower().setDamage(value);
			break;

		case FILED_NEXTPOS:
			nextPos = value;
			break;
		default:
		}
	}

	@Override
	public int getFieldCount() {
		return 7;
	}

	@Override
	public void setPower(boolean value) {
		this.power = value;
	}

	@Override
	public int[] getSlotsForFace(Direction side) {
		if (side == Direction.DOWN) {
			return SLOTS_DOWN;
		}else {
			return SLOTS_TOP;
		}
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, Direction direction) {
		if (direction != Direction.DOWN) {
			if (index < this.toolInventory.getSizeInventory()) {
				return this.isValidateTools(index, itemStackIn);
			} else if(index < (this.toolInventory.getSizeInventory() + this.planterInventory.getSizeInventory())){
				return this.isValidatePlanter(index, itemStackIn);
			}
		}
		return false;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
		if ((this.toolInventory.getSizeInventory() + this.planterInventory.getSizeInventory()) <= index &&
			index < (this.toolInventory.getSizeInventory() + this.planterInventory.getSizeInventory() + this.harvesterInventory.getSizeInventory())) {
			return true;
		}
		return false;
	}

	public void dropInventory(World worldIn, BlockPos pos) {
		InventoryHelper.dropInventoryItems(worldIn, pos, this.toolInventory);
		InventoryHelper.dropInventoryItems(worldIn, pos, this.planterInventory);
		InventoryHelper.dropInventoryItems(worldIn, pos, this.harvesterInventory);
	}

	 public NURTUREKIND getKind() {
		 return nKind;
	 }

	 public void reset() {
		 nextPos = 0;
	 }
}
