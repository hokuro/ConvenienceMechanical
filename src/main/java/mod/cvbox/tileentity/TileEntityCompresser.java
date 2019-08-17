package mod.cvbox.tileentity;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.BooleanUtils;

import mod.cvbox.entity.EntityCore;
import mod.cvbox.util.ModUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class TileEntityCompresser extends TileEntity implements IInventory, ITickable, ISidedInventory, IPowerSwitchEntity{
	public static final String NAME = "compress";
	public static final int FIELD_POWER = 0;
	public static final int FIELD_COUNT = 1;
	public static final int FIELD_IRON = 2;
	public static final int FIELD_GOLD = 3;
	public static final int FIELD_DIAMOND = 4;
	public static final int FIELD_EMERALD = 5;
	public static final int FIELD_REDSTONE = 6;
	public static final int FIELD_LAPIS = 7;
	public static final int FIELD_BATTERY = 8;
	public static final int FIELD_BATTERYMAX = 9;

	public static final int POWDER_OFFSET = 2;
	public static final int POWDER_IRON = FIELD_IRON-2;
	public static final int POWDER_GOLD = FIELD_GOLD-2;
	public static final int POWDER_DIAM = FIELD_DIAMOND-2;
	public static final int POWDER_EMER = FIELD_EMERALD-2;
	public static final int POWDER_REDS = FIELD_REDSTONE-2;
	public static final int POWDER_LAPS = FIELD_LAPIS-2;

	private NonNullList<ItemStack> stacks = NonNullList.<ItemStack>withSize(8, ItemStack.EMPTY);
	private int[] SLOT_SIDE = new int[]{1};
	private int[] SLOT_BOTTOM = new int[]{2,3,4,5,6,7};

	private boolean power;
	private int[] powder;

	public static final int CRUSH_TIME = 100;
	private int crush_count;
	private int power_count;
	private ResourceLocation nowCrush;

	private Map<ResourceLocation,Compless> maps = new HashMap<ResourceLocation,Compless>();

	public TileEntityCompresser(){
		super(EntityCore.Compresser);
		power = false;
		powder = new int[]{0,0,0,0,0,0};
		crush_count = 0;
		nowCrush = null;

		for (IRecipe recipe : Minecraft.getInstance().player.world.getRecipeManager().getRecipes()){
			if (recipe instanceof ShapedRecipe){
				makeResultMap(((ShapedRecipe)recipe).getRecipeOutput(), ((ShapedRecipe)recipe).getIngredients());
			}else if (recipe instanceof ShapelessRecipe){
				makeResultMap(((ShapelessRecipe)recipe).getRecipeOutput(), ((ShapelessRecipe)recipe).getIngredients());
			}
		}
	}

	public void makeResultMap(ItemStack output, NonNullList<Ingredient> list){
		Compless cp = new Compless(output);
		for (Ingredient ing : list){
			for (ItemStack item: ing.getMatchingStacks()){
				cp.grow(item);
			}
		}
		if (cp.canCompless()){
			ResourceLocation rs = cp.getOutput().getItem().getRegistryName();
			if (!maps.containsKey(rs)){
				maps.put(rs, cp);
			}
		}
	}

	public boolean isCrushing(){
		return this.crush_count > 0;
	}

	@Override
	public void tick() {
		if (isCrushing()){
			crush_count++;
		}
		if (!getPower().isEmpty() && power_count == 0){
			powerDown();
		}
		if (!world.isRemote){
			if (checkPowerOn() && !isFull()){
				power_count++;
				if (!isCrushing()){
					if (maps.containsKey(stacks.get(1).getItem().getRegistryName())){
						nowCrush = stacks.get(1).getItem().getRegistryName();
						stacks.get(1).shrink(1);
						crush_count++;
						power_count++;
					}
				}else{
					power_count++;
				}
				if (CRUSH_TIME < crush_count){
					crush_block();
					crush_count = 0;
				}
				if (power_count > 40){
					power_count = 0;
				}
			}else{
				if (crush_count > 0){
					crush_count--;
				}else{
					crush_count = 0;
				}
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
    	Compless cp = maps.get(nowCrush);
    	this.powder[POWDER_IRON] += cp.getIron();
    	while(this.powder[POWDER_IRON] >= 100){
    		this.powder[POWDER_IRON] -= 100;
    		if (stacks.get(POWDER_IRON+POWDER_OFFSET).isEmpty()){
    			stacks.set(POWDER_IRON+POWDER_OFFSET, new ItemStack(Items.IRON_INGOT,1));
    		}else{
    			stacks.get(POWDER_IRON+POWDER_OFFSET).grow(1);
    		}
    	}

    	this.powder[POWDER_GOLD] += cp.getGold();
    	while(this.powder[POWDER_GOLD] >= 100){
    		this.powder[POWDER_GOLD] -= 100;
    		if (stacks.get(POWDER_GOLD+POWDER_OFFSET).isEmpty()){
    			stacks.set(POWDER_GOLD+POWDER_OFFSET, new ItemStack(Items.GOLD_INGOT,1));
    		}else{
    			stacks.get(POWDER_GOLD+POWDER_OFFSET).grow(1);
    		}
    	}

    	this.powder[POWDER_DIAM] += cp.getDia();
    	while(this.powder[POWDER_DIAM] >= 100){
    		this.powder[POWDER_DIAM] -= 100;
    		if (stacks.get(POWDER_DIAM+POWDER_OFFSET).isEmpty()){
    			stacks.set(POWDER_DIAM+POWDER_OFFSET, new ItemStack(Items.DIAMOND,1));
    		}else{
    			stacks.get(POWDER_DIAM+POWDER_OFFSET).grow(1);
    		}
    	}
    	this.powder[POWDER_EMER] += cp.getEmerald();
    	while(this.powder[POWDER_EMER] >= 100){
    		this.powder[POWDER_EMER] -= 100;
    		if (stacks.get(POWDER_EMER+POWDER_OFFSET).isEmpty()){
    			stacks.set(POWDER_EMER+POWDER_OFFSET, new ItemStack(Items.EMERALD,1));
    		}else{
    			stacks.get(POWDER_EMER+POWDER_OFFSET).grow(1);
    		}
    	}

    	this.powder[POWDER_REDS] += cp.getRedstone();
    	while(this.powder[POWDER_REDS] >= 100){
    		this.powder[POWDER_REDS] -= 100;
    		if (stacks.get(POWDER_IRON+POWDER_OFFSET).isEmpty()){
    			stacks.set(POWDER_REDS+POWDER_OFFSET, new ItemStack(Items.REDSTONE,1));
    		}else{
    			stacks.get(POWDER_REDS+POWDER_OFFSET).grow(1);
    		}
    	}

    	this.powder[POWDER_LAPS] += cp.getlapis();
    	while(this.powder[POWDER_LAPS] >= 100){
    		this.powder[POWDER_LAPS] -= 100;
    		if (stacks.get(POWDER_LAPS+POWDER_OFFSET).isEmpty()){
    			stacks.set(POWDER_LAPS+POWDER_OFFSET, new ItemStack(Items.LAPIS_LAZULI,1));
    		}else{
    			stacks.get(POWDER_LAPS+POWDER_OFFSET).grow(1);
    		}
    	}
    }

	@Override
    public void read(NBTTagCompound compound)
    {
		super.read(compound);
		power = compound.getBoolean("power");
		crush_count=compound.getInt("count");
		power_count=compound.getInt("power_count");

		String res = "";
		this.nowCrush = (res = compound.getString("res")).isEmpty()?null:new ResourceLocation(res);

		for (int i = 0; i < powder.length; i++){
			powder[i] = compound.getInt("powder"+i);
		}

	    this.stacks = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
	    ItemStackHelper.loadAllItems(compound, this.stacks);


//		NBTTagList itemsTagList = compound.getList("Items",10);
//		for (int tagCounter = 0; tagCounter < itemsTagList.size(); tagCounter++){
//			NBTTagCompound itemTagCompound = itemsTagList.getCompound(tagCounter);
//
//			byte slotIndex =itemTagCompound.getByte("Slot");
//			if ((slotIndex >= 0) && (slotIndex < stacks.size())){
//				stacks.set(slotIndex,  new ItemStack(itemTagCompound));
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
		if (nowCrush != null){
			compound.setString("res", nowCrush.toString());
		}
		for ( int i = 0; i < powder.length; i++){
			compound.setInt("powder"+i, powder[i]);
		}

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
		return  new TextComponentTranslation("tileentity.compresser");
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
		if (index > 1){
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
		case FIELD_IRON:
			ret = powder[POWDER_IRON];
			break;
		case FIELD_GOLD:
			ret = powder[POWDER_GOLD];
			break;
		case FIELD_DIAMOND:
			ret = powder[POWDER_DIAM];
			break;
		case FIELD_EMERALD:
			ret = powder[POWDER_EMER];
			break;
		case FIELD_REDSTONE:
			ret = powder[POWDER_REDS];
			break;
		case FIELD_LAPIS:
			ret = powder[POWDER_LAPS];
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
		case FIELD_IRON:
			powder[POWDER_IRON] = value;
			break;
		case FIELD_GOLD:
			powder[POWDER_GOLD] = value;
			break;
		case FIELD_DIAMOND:
			powder[POWDER_DIAM] = value;
			break;
		case FIELD_EMERALD:
			powder[POWDER_EMER] = value;
			break;
		case FIELD_REDSTONE:
			powder[POWDER_REDS] = value;
			break;
		case FIELD_LAPIS:
			powder[POWDER_LAPS] = value;
			break;
		case FIELD_BATTERY:
			getPower().setDamage(value);
			break;
		}
	}

	@Override
	public int getFieldCount() {
		return 10;
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


	public static class Compless{
		public static final Map<Item,Integer[]> containItem = new HashMap<Item,Integer[]>(){
			{put(Items.IRON_INGOT,new Integer[]{0,10});}
			{put(Items.GOLD_INGOT,new Integer[]{1,10});}
			{put(Items.DIAMOND,new Integer[]{2,10});}
			{put(Items.EMERALD,new Integer[]{3,10});}
			{put(Items.REDSTONE,new Integer[]{4,10});}
			{put(Items.LAPIS_LAZULI,new Integer[]{5,10});}
			{put(new ItemStack(Blocks.IRON_BLOCK).getItem(),new Integer[]{0, 100});}
			{put(new ItemStack(Blocks.GOLD_BLOCK).getItem(),new Integer[]{1, 100});}
			{put(new ItemStack(Blocks.DIAMOND_BLOCK).getItem(),new Integer[]{2, 100});}
			{put(new ItemStack(Blocks.EMERALD_BLOCK).getItem(),new Integer[]{3, 100});}
			{put(new ItemStack(Blocks.REDSTONE_BLOCK).getItem(),new Integer[]{4, 100});}
			{put(new ItemStack(Blocks.LAPIS_BLOCK).getItem(),new Integer[]{5, 100});}
			{put(Items.GOLD_NUGGET,new Integer[]{1,1});}
		};


		private ItemStack output;
		private int counter[] = new int[]{0,0,0,0,0,0};

		public Compless(ItemStack result){
			output = result;
		}

		public ItemStack getOutput(){return output;}
		public int getIron(){return counter[0];}
		public int getGold(){return counter[1];}
		public int getDia(){return counter[2];}
		public int getEmerald(){return counter[3];}
		public int getRedstone(){return counter[4];}
		public int getlapis(){return counter[5];}


		public void grow(ItemStack item){
			if ( containItem.containsKey(item.getItem()) || (containItem.containsKey(item.getItem())&& item.getItem() == Items.LAPIS_LAZULI)){
				Integer[] pair = containItem.get(item.getItem());
				counter[pair[0]] += pair[1];

			}
		}

		public boolean canCompless(){
			boolean ret = false;
			for (int check : counter){
				if (check != 0){
					ret = true;
					break;
				}
			}
			return ret;
		}
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
