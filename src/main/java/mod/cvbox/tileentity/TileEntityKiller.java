package mod.cvbox.tileentity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.BooleanUtils;

import com.mojang.authlib.GameProfile;

import mod.cvbox.config.ConfigValue;
import mod.cvbox.util.ModUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

public class TileEntityKiller extends TileEntity  implements IInventory, ITickable, ISidedInventory,IPowerSwitchEntity{
	public static final String NAME = "killer";
	public static final int FIELD_POWER = 0;
	public static final int FIELD_AREASIZEX =1;
	public static final int FIELD_AREASIZEY =2;
	public static final int FIELD_AREASIZEZ =3;
	public static final int FIELD_BATTERY = 4;
	public static final int FIELD_BATTERYMAX = 5;

	private EntityPlayer playerDummy;
	private boolean power;
	private int areaSizeX;
	private int areaSizeY;
	private int areaSizeZ;
	private List<String> targets;
	private NonNullList<ItemStack> stacks = NonNullList.<ItemStack>withSize(2, ItemStack.EMPTY);
	private int[] SLOT_SIDE = new int[]{1};

	private static final int KILLER_TIME = 10;
	private int killer_count;
	private int power_count;

	public TileEntityKiller(){
		super();
		playerDummy = null;
		power = false;
		areaSizeX = 0;
		areaSizeY = 0;
		areaSizeZ = 0;
		targets = new ArrayList<String>();
		killer_count = 0;
		power_count = 0;
	}

	@Override
	public void update() {
		if (!getPower().isEmpty() && power_count == 0){
			powerDown();
		}
		if (!world.isRemote){
			if (checkPowerOn()){
				killer_count++;
				if (KILLER_TIME < killer_count){
					if (playerDummy == null){
						playerDummy = new EntityPlayerDummy(this.world, new GameProfile(new UUID(0,0), "dummy"));
						playerDummy.getAttributeMap().applyAttributeModifiers(stacks.get(1).getAttributeModifiers(EntityEquipmentSlot.MAINHAND));
						playerDummy.setHeldItem(EnumHand.MAIN_HAND, this.stacks.get(1));
					}

					addEffectsToPlayers();
					killer_count = 0;
					power_count++;
					if (power_count > 20){
						power_count = 0;
					}
				}
			}else{
				killer_count = 0;
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
					ModUtil.spawnParticles(this.world, this.pos, EnumParticleTypes.REDSTONE);
				}
			}
		}
	}

	private boolean checkPowerOn(){
		ItemStack st = getPower();
		return (((!st.isEmpty()) && (st.getMaxDamage() -  st.getItemDamage() > 1)) &&
				power);
	}

	private void powerDown(){
		int damage = getPower().getItemDamage()+1;
		getPower().setItemDamage(damage);
	}

    private void addEffectsToPlayers()
    {
    	Entity[] list = new Entity[this.world.getLoadedEntityList().size()];
    	for (int i = 0; i < list.length; i++){
    		list[i] = this.world.getLoadedEntityList().get(i);
    	}
    	int x1 = pos.add(-ConfigValue.ExpCollector.AreaSize,0,0).getX();
    	int x2 = pos.add(ConfigValue.ExpCollector.AreaSize+1,0,0).getX();
    	int y1 = pos.add(0,-ConfigValue.ExpCollector.AreaSize,0).getY();
    	int y2 = pos.add(0,ConfigValue.ExpCollector.AreaSize+1,0).getY();
    	int z1 = pos.add(0,0,-ConfigValue.ExpCollector.AreaSize).getZ();
    	int z2 = pos.add(0,0,ConfigValue.ExpCollector.AreaSize+1).getZ();
        for (Entity ent : list){
        	if (ent != null && ent instanceof EntityLivingBase){
        		if ((x1 > ent.posX || x2 < ent.posX) &&
            		    (y1 > ent.posY || y2 < ent.posY) &&
            		    (z1 > ent.posZ || z2 < ent.posZ)){continue;}

            	if (EntityList.getKey(ent.getClass()) != null){
            		String name = EntityList.getKey(ent.getClass()).toString();
                	if (targets.contains(name)){
                    	playerDummy.attackTargetEntityWithCurrentItem(ent);
                	}
            	}
        	}
        }
    }

	@Override
    public void readFromNBT(NBTTagCompound compound)
    {
		super.readFromNBT(compound);
		power = compound.getBoolean("power");
        areaSizeX = compound.getInteger("sizex");
        areaSizeY = compound.getInteger("sizey");
        areaSizeZ = compound.getInteger("sizez");
        power_count = compound.getInteger("power_count");
		NBTTagList targetTagList = compound.getTagList("targets",10);
		for (int tagCounter = 0; tagCounter < targetTagList.tagCount(); tagCounter++){
			NBTTagCompound tag = targetTagList.getCompoundTagAt(tagCounter);
			String name = tag.getString("name");
			if (EntityList.getClassFromName(name) != null){
				targets.add(name);
			}
		}

		NBTTagList itemsTagList = compound.getTagList("Items",10);
		for (int tagCounter = 0; tagCounter < itemsTagList.tagCount(); tagCounter++){
			NBTTagCompound itemTagCompound = itemsTagList.getCompoundTagAt(tagCounter);

			byte slotIndex =itemTagCompound.getByte("Slot");
			if ((slotIndex >= 0) && (slotIndex < stacks.size())){
				stacks.set(slotIndex, new ItemStack(itemTagCompound));
			}
		}
    }

	@Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
		compound = super.writeToNBT(compound);
		compound.setBoolean("power", power);
		compound.setInteger("sizex", areaSizeX);
		compound.setInteger("sizey", areaSizeY);
		compound.setInteger("sizez", areaSizeZ);
		compound.setInteger("power_count",power_count);
		NBTTagList targetTagList = new NBTTagList();
		for (String name : targets){
			NBTTagCompound targetName = new NBTTagCompound();
			targetName.setString("name", name);
			targetTagList.appendTag(targetName);
		}
		compound.setTag("targets",targetTagList);

		NBTTagList itemsTagList = new NBTTagList();
		for (int slotIndex = 0; slotIndex < stacks.size(); slotIndex++){
			if (!this.stacks.get(slotIndex).isEmpty()){
				NBTTagCompound itemTagCompound = new NBTTagCompound();

				itemTagCompound.setByte("Slot",(byte)slotIndex);
				this.stacks.get(slotIndex).writeToNBT(itemTagCompound);
				itemsTagList.appendTag(itemTagCompound);
			}
		}
		compound.setTag("Items",itemsTagList);

        return compound;
    }

	@Override
    public NBTTagCompound getUpdateTag()
    {
        NBTTagCompound cp = super.getUpdateTag();
        return this.writeToNBT(cp);
    }

	@Override
    public void handleUpdateTag(NBTTagCompound tag)
    {
		super.handleUpdateTag(tag);
		this.readFromNBT(tag);
    }

	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
    {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        return new SPacketUpdateTileEntity(this.pos, 1,  this.writeToNBT(nbtTagCompound));
    }

	@Override
	public String getName() {
		return "tileentity.kirer";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return SLOT_SIDE;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return (itemStackIn.getItem() instanceof ItemSword);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return false;
	}

	@Override
	public int getSizeInventory() {
		return this.stacks.size();
	}

	@Override
	public boolean isEmpty() {
		return this.stacks.get(0).isEmpty() && this.stacks.get(1).isEmpty();
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
		if (this.playerDummy != null){
			playerDummy.getAttributeMap().applyAttributeModifiers(stack.getAttributeModifiers(EntityEquipmentSlot.MAINHAND));
			playerDummy.setHeldItem(EnumHand.MAIN_HAND, this.stacks.get(1));
		}

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
		return (stack.getItem() instanceof ItemSword);
	}

	@Override
	public int getField(int id) {
		int ret = 0;
		switch(id){
		case FIELD_POWER:
			ret = BooleanUtils.toInteger(power);
			break;
		case FIELD_AREASIZEX:
			ret = areaSizeX;
			break;
		case FIELD_AREASIZEY:
			ret = areaSizeY;
			break;
		case FIELD_AREASIZEZ:
			ret = areaSizeZ;
			break;
		case FIELD_BATTERY:
			ret = getPower().getItemDamage();
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
		case FIELD_AREASIZEX:
			areaSizeX = value;
			break;
		case FIELD_AREASIZEY:
			areaSizeY = value;
			break;
		case FIELD_AREASIZEZ:
			areaSizeZ = value;
			break;
		case FIELD_BATTERY:
			getPower().setItemDamage(value);
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

	public boolean ContainTarget(String name){
		return targets!=null?targets.contains(name):false;
	}

	public boolean ContainTarget(ResourceLocation name){
		return ContainTarget(name.toString());
	}

	public void updateTarget(String entityName) {
		if (ContainTarget(entityName)){
			targets.remove(entityName);
		}else{
			targets.add(entityName);
		}
	}

	@Override
	public void setPower(boolean value) {
		this.power = value;
	}

	public ItemStack getPower(){
		return this.stacks.get(0);
	}
}
