package mod.cvbox.tileentity;

import java.util.List;

import org.apache.commons.lang3.BooleanUtils;

import mod.cvbox.entity.EntityCore;
import mod.cvbox.inventory.ContainerVacumer;
import mod.cvbox.util.ModUtil;
import mod.cvbox.util.ModUtil.CompaierLevel;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class TileEntityVacumer extends TileEntity implements IInventory, ITickable, ISidedInventory, IPowerSwitchEntity{
	public static final String NAME = "compresser";
	public static final int FIELD_POWER = 0;
	public static final int FIELD_COUNT = 1;
	public static final int FIELD_BATTERY = 2;
	public static final int FIELD_BATTERYMAX = 3;


	private NonNullList<ItemStack> stacks = NonNullList.<ItemStack>withSize(ContainerVacumer.ROW_SLOT * ContainerVacumer.COL_SLOT + 1, ItemStack.EMPTY);
	private int[] SLOT_SIDE = new int[]{1};
	private int[] SLOT_BOTTOM;

	private boolean power;

	public static final int CRUSH_TIME = 10;
	private int crush_count;
	private int power_count;


	public TileEntityVacumer(){
		super(EntityCore.Vacumer);
		power = false;
		crush_count = 0;
		SLOT_BOTTOM =new int[ContainerVacumer.ROW_SLOT * ContainerVacumer.COL_SLOT];
		for ( int i = 1; i < ContainerVacumer.ROW_SLOT * ContainerVacumer.COL_SLOT + 1; i++){
			SLOT_BOTTOM[i-1]=i;
		}
	}


	public boolean isCrushing(){
		return this.crush_count > 0;
	}

	@Override
	public void tick() {
		if (!getPower().isEmpty() && power_count == 0){
			powerDown();
		}
		if (!world.isRemote){
			if (checkPowerOn()){
				power_count++;
				crush_count++;
				if (CRUSH_TIME < crush_count){
					crush_block();
					crush_count = 0;
				}
				if (power_count > 20){
					power_count = 0;
				}
			}else{
				crush_count = 0;
				if (!getPower().isEmpty()){
					power_count++;
					if (power_count > 2000){
						power_count = 0;
					}
				}else{
					power_count = 0;
				}
			}
		}else{
			if (checkPowerOn()){
				if (Math.random() > 0.7){
					ModUtil.spawnParticles(this.world, this.pos, RedstoneParticleData.REDSTONE_DUST);
				}
			}
		}
	}

	private boolean checkPowerOn(){
		ItemStack st = getPower();
		return (((!st.isEmpty()) && (st.getMaxDamage() -  st.getDamage() > 1)) &&
				power);
	}

	private void powerDown(){
		int damage = getPower().getDamage()+1;
		getPower().setDamage(damage);
	}

    private void crush_block()
    {
        for (EntityItem entityitem : getCaptureItems(this.getWorld(), this.pos.getX(), this.pos.getY(), this.pos.getZ()))
        {
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

    public static List<EntityItem> getCaptureItems(World worldIn, double x, double y, double z)
    {
        return worldIn.<EntityItem>getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(x - 3.5D, y, z - 3.5D, x + 3.5D, y + 3.5D, z + 3.5D), EntitySelectors.IS_ALIVE);
    }

	@Override
    public void read(NBTTagCompound compound)
    {
		super.read(compound);
		power = compound.getBoolean("power");
		crush_count=compound.getInt("count");
		power_count=compound.getInt("power_count");

	    this.stacks = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
	    ItemStackHelper.loadAllItems(compound, this.stacks);

//		NBTTagList itemsTagList = compound.getTagList("Items",10);
//		for (int tagCounter = 0; tagCounter < itemsTagList.tagCount(); tagCounter++){
//			NBTTagCompound itemTagCompound = itemsTagList.getCompoundTagAt(tagCounter);
//
//			byte slotIndex =itemTagCompound.getByte("Slot");
//			if ((slotIndex >= 0) && (slotIndex < stacks.size())){
//				stacks.set(slotIndex, new ItemStack(itemTagCompound));
//			}
//		}
    }

	@Override
    public NBTTagCompound write(NBTTagCompound compound)
    {
		compound = super.write(compound);
		compound.setBoolean("power", power);
		compound.setInt("count", crush_count);
		compound.setInt("power_count",power_count);

		NBTTagList itemsTagList = new NBTTagList();
		for (int slotIndex = 0; slotIndex < stacks.size(); slotIndex++){
			NBTTagCompound itemTagCompound = new NBTTagCompound();

			itemTagCompound.setByte("Slot",(byte)slotIndex);
			if (stacks.size()>slotIndex){
				this.stacks.get(slotIndex).write(itemTagCompound);
			}
			itemsTagList.add(itemTagCompound);
		}
		compound.setTag("Items",itemsTagList);

        return compound;
    }

	@Override
    public NBTTagCompound getUpdateTag()
    {
        NBTTagCompound cp = super.getUpdateTag();
        return this.write(cp);
    }

	@Override
    public void handleUpdateTag(NBTTagCompound tag)
    {
		super.handleUpdateTag(tag);
		this.read(tag);
    }

	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
    {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        return new SPacketUpdateTileEntity(this.pos, 1,  this.write(nbtTagCompound));
    }

	@Override
	public ITextComponent getName() {
		return new TextComponentTranslation("tileentity.vacumer");
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		if (side == EnumFacing.DOWN){
			return SLOT_BOTTOM;
		}
		return SLOT_SIDE;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		if (direction == EnumFacing.DOWN){
			return false;
		}else{
			return true;
		}
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		if (index >= 1){
			return true;
		}
		return false;
	}

	@Override
	public int getSizeInventory() {
		return this.stacks.size();
	}

	@Override
	public boolean isEmpty() {
        for (ItemStack itemstack : this.stacks)
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }

        return true;
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
	public boolean isUsableByPlayer(EntityPlayer player) {
		if (this.world.getTileEntity(this.pos) != this) {
			return false;
		}
		return player.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory(EntityPlayer player) {
	}

	@Override
	public void closeInventory(EntityPlayer player) {
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
		case FIELD_COUNT:
			ret = crush_count;
			break;
		case FIELD_BATTERY:
			ret = getPower().getDamage();
			break;
		case FIELD_BATTERYMAX:
			ret = getPower().getMaxDamage();
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
		case FIELD_COUNT:
			crush_count = value;
			break;
		case FIELD_BATTERY:
			getPower().setDamage(value);
			break;
		}
	}

	@Override
	public int getFieldCount() {
		return 4;
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

	@Override
	public ITextComponent getCustomName() {
		// TODO 自動生成されたメソッド・スタブ
		return getName();
	}

	@Override
	public NBTTagCompound serializeNBT() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		// TODO 自動生成されたメソッド・スタブ

	}
}
