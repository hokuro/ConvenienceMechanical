package mod.cvbox.tileentity;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.BooleanUtils;

import mod.cvbox.block.ab.BlockPowerMachineContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityCompresser extends TileEntity implements IInventory, ITickable, ISidedInventory{
	public static final String NAME = "compresser";
	public static final int FIELD_POWER = 0;
	public static final int FIELD_COUNT = 1;
	public static final int FIELD_IRON = 2;
	public static final int FIELD_GOLD = 3;
	public static final int FIELD_DIAMOND = 4;
	public static final int FIELD_EMERALD = 5;
	public static final int FIELD_REDSTONE = 6;
	public static final int FIELD_LAPIS = 7;

	public static final int POWDER_IRON = FIELD_IRON-2;
	public static final int POWDER_GOLD = FIELD_GOLD-2;
	public static final int POWDER_DIAM = FIELD_DIAMOND-2;
	public static final int POWDER_EMER = FIELD_EMERALD-2;
	public static final int POWDER_REDS = FIELD_REDSTONE-2;
	public static final int POWDER_LAPS = FIELD_LAPIS-2;

	private boolean power;

	private NonNullList<ItemStack> stacks = NonNullList.<ItemStack>withSize(7, ItemStack.EMPTY);
	private int[] SLOT_SIDE = new int[]{0};
	private int[] SLOT_BOTTOM = new int[]{1,2,3,4,5,6};


	private int[] powder;

	public static final int CRUSH_TIME = 100;
	private int crush_count;
	private ResourceLocation nowCrush;

	private Map<ResourceLocation,Compless> maps = new HashMap<ResourceLocation,Compless>();

	public TileEntityCompresser(){
		super();
		power = false;
		powder = new int[]{0,0,0,0,0,0};
		crush_count = 0;
		nowCrush = null;

		for (ResourceLocation locate : CraftingManager.REGISTRY.getKeys()){
			IRecipe recipe = CraftingManager.REGISTRY.getObject(locate);
			if (recipe instanceof ShapedRecipes){
				makeResultMap(((ShapedRecipes)recipe).getRecipeOutput(), ((ShapedRecipes)recipe).recipeItems);
			}else if (recipe instanceof ShapelessRecipes){
				makeResultMap(((ShapelessRecipes)recipe).getRecipeOutput(), ((ShapelessRecipes)recipe).recipeItems);
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
	public void update() {
		if (isCrushing()){
			crush_count++;
		}
		if (!world.isRemote){
			if (power && !isFull()){
				if (!isCrushing()){
					if (maps.containsKey(stacks.get(0).getItem().getRegistryName())){
						nowCrush = stacks.get(0).getItem().getRegistryName();
						stacks.get(0).shrink(1);
						crush_count++;
					}
				}
				if (CRUSH_TIME < crush_count){
					crush_block();
					crush_count = 0;
				}
			}else{
				crush_count = 0;
			}
		}
	}

    private void crush_block()
    {
    	Compless cp = maps.get(nowCrush);
    	this.powder[POWDER_IRON] += cp.getIron();
    	while(this.powder[POWDER_IRON] >= 100){
    		this.powder[POWDER_IRON] -= 100;
    		if (stacks.get(POWDER_IRON+1).isEmpty()){
    			stacks.set(POWDER_IRON+1, new ItemStack(Items.IRON_INGOT,1));
    		}else{
    			stacks.get(POWDER_IRON+1).grow(1);
    		}
    	}

    	this.powder[POWDER_GOLD] += cp.getGold();
    	while(this.powder[POWDER_GOLD] >= 100){
    		this.powder[POWDER_GOLD] -= 100;
    		if (stacks.get(POWDER_GOLD+1).isEmpty()){
    			stacks.set(POWDER_GOLD+1, new ItemStack(Items.GOLD_INGOT,1));
    		}else{
    			stacks.get(POWDER_GOLD+1).grow(1);
    		}
    	}

    	this.powder[POWDER_DIAM] += cp.getDia();
    	while(this.powder[POWDER_DIAM] >= 100){
    		this.powder[POWDER_DIAM] -= 100;
    		if (stacks.get(POWDER_DIAM+1).isEmpty()){
    			stacks.set(POWDER_DIAM+1, new ItemStack(Items.DIAMOND,1));
    		}else{
    			stacks.get(POWDER_DIAM+1).grow(1);
    		}
    	}
    	this.powder[POWDER_EMER] += cp.getEmerald();
    	while(this.powder[POWDER_EMER] >= 100){
    		this.powder[POWDER_EMER] -= 100;
    		if (stacks.get(POWDER_EMER+1).isEmpty()){
    			stacks.set(POWDER_EMER+1, new ItemStack(Items.EMERALD,1));
    		}else{
    			stacks.get(POWDER_EMER+1).grow(1);
    		}
    	}

    	this.powder[POWDER_REDS] += cp.getRedstone();
    	while(this.powder[POWDER_REDS] >= 100){
    		this.powder[POWDER_REDS] -= 100;
    		if (stacks.get(POWDER_IRON+1).isEmpty()){
    			stacks.set(POWDER_REDS+1, new ItemStack(Items.REDSTONE,1));
    		}else{
    			stacks.get(POWDER_REDS+1).grow(1);
    		}
    	}

    	this.powder[POWDER_LAPS] += cp.getlapis();
    	while(this.powder[POWDER_LAPS] >= 100){
    		this.powder[POWDER_LAPS] -= 100;
    		if (stacks.get(POWDER_LAPS+1).isEmpty()){
    			stacks.set(POWDER_LAPS+1, new ItemStack(Items.DYE,1,EnumDyeColor.BLUE.getDyeDamage()));
    		}else{
    			stacks.get(POWDER_LAPS+1).grow(1);
    		}
    	}
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
    	if (newSate.getBlock() instanceof BlockPowerMachineContainer){
    		this.setField(FIELD_POWER, ((BlockPowerMachineContainer)newSate.getBlock()).getPower(newSate));
    	}
        return false;
    }

	@Override
    public void readFromNBT(NBTTagCompound compound)
    {
		super.readFromNBT(compound);
		power = compound.getBoolean("power");
		crush_count=compound.getInteger("count");

		String res = "";
		this.nowCrush = (res = compound.getString("res")).isEmpty()?null:new ResourceLocation(res);

		for (int i = 0; i < powder.length; i++){
			powder[i] = compound.getInteger("powder"+i);
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
		compound.setInteger("count", crush_count);
		if (nowCrush != null){
			compound.setString("res", nowCrush.toString());
		}
		for ( int i = 0; i < powder.length; i++){
			compound.setInteger("powder"+i, powder[i]);
		}


		NBTTagList itemsTagList = new NBTTagList();
		for (int slotIndex = 0; slotIndex < stacks.size(); slotIndex++){
			NBTTagCompound itemTagCompound = new NBTTagCompound();

			itemTagCompound.setByte("Slot",(byte)slotIndex);
			if (stacks.size()>slotIndex){
				this.stacks.get(slotIndex).writeToNBT(itemTagCompound);
			}
			itemsTagList.appendTag(itemTagCompound);
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
		return "tileentity.compresser";
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
		if (index > 0){
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
		}
	}

	@Override
	public int getFieldCount() {
		return 8;
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
			{put(Items.DYE,new Integer[]{5,10});}
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
			if ( containItem.containsKey(item.getItem()) || (containItem.containsKey(item.getItem())&& item.getItem() == Items.DYE && item.getMetadata() == EnumDyeColor.BLUE.getDyeDamage())){
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
}
