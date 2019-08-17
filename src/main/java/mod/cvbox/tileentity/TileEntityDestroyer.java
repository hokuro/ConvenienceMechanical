package mod.cvbox.tileentity;

import org.apache.commons.lang3.BooleanUtils;

import mod.cvbox.block.BlockDestroyer;
import mod.cvbox.entity.EntityCore;
import mod.cvbox.util.ModUtil;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class TileEntityDestroyer extends TileEntity implements IInventory, ITickable, IPowerSwitchEntity{
	public static final String NAME = "destroyer";
	public static final int FIELD_POWER = 0;
	public static final int FIELD_TIME = 1;
	public static final int FIELD_MODE =2;
	public static final int FIELD_ISRUN = 3;
	public static final int FIELD_BATTERY = 8;
	public static final int FIELD_BATTERYMAX = 9;

	public static final int MODE_TIME = 0;
	public static final int MODE_CHANGE = 1;

	private NonNullList<ItemStack> stacks = NonNullList.<ItemStack>withSize(1, ItemStack.EMPTY);

	private boolean power;
	private int mode;
	private float time_base;
	private float time_value;
	private int time_count;
	private int power_count;

	private IBlockState beforeState;

	public TileEntityDestroyer(){
		super(EntityCore.Destroyer);
		power = false;
		time_base = 0.1F;
		time_value = 0.1F;
		time_count = 0;
		power_count = 0;
		beforeState = Blocks.AIR.getDefaultState();
		mode = MODE_TIME;
	}

	@Override
	public void tick() {
		if (checkPowerOn()){
			time_count++;
		}
		if (!getPower().isEmpty() && power_count == 0){
			powerDown();
		}
		if (!world.isRemote){
			if (checkPowerOn() && !isFull()){

				if (mode == MODE_TIME){
					if (time_count >= 2 ){
						time_count = 0;
						time_value -= 0.1F;
					}
					if (time_value <= 0){
						exec();
						time_value = time_base;
					}
				}else{
					if (time_count >= 2){
						time_count = 0;
						IBlockState state = world.getBlockState(pos);
						EnumFacing face = state.get(BlockDestroyer.FACING);
						state = world.getBlockState(pos.offset(face));
						if (state.getMaterial() != Material.AIR ||
								state.getMaterial() != Material.LAVA ||
								state.getMaterial() != Material.WATER){
							exec();
						}
					}
				}

				power_count++;
				if (power_count > 20){
					power_count = 0;
				}
			}else{
				if (!getPower().isEmpty()){
					power_count++;
					if (power_count > 2000){
						power_count = 0;
					}
				}else{
					power_count = 0;
				}
				time_count = 0;
				time_value = time_base;
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

	private void exec(){
		IBlockState state = world.getBlockState(pos);
		EnumFacing face = state.get(BlockDestroyer.FACING);
		BlockPos pos2 = pos.offset(face);
		state = world.getBlockState(pos2);
		ItemStack drop = state.getBlock().getItem(this.world, pos2, state);
		if (!drop.isEmpty()){
			ModUtil.spawnItemStack(this.world, pos2.getX(),pos2.getY(),pos2.getZ(), drop, this.world.rand);
			world.destroyBlock(pos2, false);
		}
	}

	@Override
    public void read(NBTTagCompound compound)
    {
		super.read(compound);
		power = compound.getBoolean("power");
		power_count=compound.getInt("power_count");
		mode=compound.getInt("mode");
		time_base=compound.getFloat("time_base");
		time_value=compound.getFloat("time_value");
		time_count=compound.getInt("time_count");


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
		compound.setInt("power_count",power_count);

		compound.setInt("mode",mode);
		compound.setFloat("time_base",time_base);
		compound.setFloat("time_value",time_value);
		compound.setInt("time_count",time_count);

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
		return new TextComponentTranslation("tileentity.compresser");
	}

	@Override
	public boolean hasCustomName() {
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
		case FIELD_BATTERY:
			ret = getPower().getDamage();
			break;
		case FIELD_BATTERYMAX:
			ret = getPower().getMaxDamage();
			break;

		case FIELD_TIME:
			ret = (int)(this.time_value*10.0F);
			break;
		case FIELD_MODE:
			ret = this.mode;
			break;
		case FIELD_ISRUN:
			ret = BooleanUtils.toInteger(checkPowerOn());
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
		case FIELD_TIME:
			this.time_value=(float)(value/10.0F);
			break;
		case FIELD_MODE:
			this.mode=value;
			break;
		}
	}

	@Override
	public int getFieldCount() {
		return 6;
	}

	@Override
	public void clear() {
		stacks.clear();
	}


	public boolean isFull(){
		boolean ret = false;
		for (int i = 1; i < stacks.size(); i++){
			ItemStack item = stacks.get(i);
			if (!item.isEmpty() && item.getCount() >= 64){
				ret = true;
			}
		}
		return ret;
	}

	@Override
	public void setPower(boolean value) {
		this.power = value;
	}

	public ItemStack getPower(){
		return this.stacks.get(0);
	}

	public void updateValues(int mode2, float time) {
		this.time_base = time;
		this.mode = mode2;
		if (!this.checkPowerOn()){
			time_value = time;
		}
		time_count = 0;
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
