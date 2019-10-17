package mod.cvbox.tileentity.factory;

import java.util.HashMap;
import java.util.Map;

import mod.cvbox.entity.EntityCore;
import mod.cvbox.item.ItemBattery;
import mod.cvbox.tileentity.ab.TileEntityPowerBase;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

public class TileEntityCompresser extends TileEntityPowerBase  implements ISidedInventory {
	public static final String NAME = "compress";
	public static final int FIELD_COUNT = 0 + FIELD_OFFSET;
	public static final int FIELD_IRON = 1 + FIELD_OFFSET;
	public static final int FIELD_GOLD = 2 + FIELD_OFFSET;
	public static final int FIELD_DIAMOND = 3 + FIELD_OFFSET;
	public static final int FIELD_EMERALD = 4 + FIELD_OFFSET;
	public static final int FIELD_REDSTONE = 5 + FIELD_OFFSET;
	public static final int FIELD_LAPIS = 6 + FIELD_OFFSET;

	public static final int SLOT_ITEM = 1;

	public static final int POWDER_OFFSET = 2;
	public static final int POWDER_IRON = 0;
	public static final int POWDER_GOLD = 1;
	public static final int POWDER_DIAM = 2;
	public static final int POWDER_EMER = 3;
	public static final int POWDER_REDS = 4;
	public static final int POWDER_LAPS = 5;

	private final int[] SLOT_SIDE = new int[]{SLOT_BATTERY, SLOT_ITEM};
	private final int[] SLOT_BOTTOM = new int[]{SLOT_BATTERY, SLOT_ITEM, 2,3,4,5,6,7};

	private final int[] powder;
	private int work_count;

	public static final int WORK_TIME = 100;
	private ResourceLocation nowCrush;

	private Map<ResourceLocation,Compless> maps = new HashMap<ResourceLocation,Compless>();

	public TileEntityCompresser(){
		super(EntityCore.Compresser, 8);
		powder = new int[]{0,0,0,0,0,0};
		nowCrush = null;
		work_count = 0;
	}

	protected void makeResult() {
		if (Minecraft.getInstance() != null && Minecraft.getInstance().player != null && Minecraft.getInstance().player.world != null) {
			maps.clear();
			for (IRecipe recipe : Minecraft.getInstance().player.world.getRecipeManager().getRecipes()){
				if (recipe instanceof ShapedRecipe){
					makeResultMap(((ShapedRecipe)recipe).getRecipeOutput(), ((ShapedRecipe)recipe).getIngredients());
				}else if (recipe instanceof ShapelessRecipe){
					makeResultMap(((ShapelessRecipe)recipe).getRecipeOutput(), ((ShapelessRecipe)recipe).getIngredients());
				}
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

	public boolean isWorking(){
		return work_count > 0;
	}

	@Override
	public void tick() {
		super.tick();
		if (maps.size() <= 0) {
			makeResult();
		}
		if (!getPower().isEmpty()) {
			if (checkPowerOn() && canWork()){
			}else{
				if (work_count > 0){
					work_count--;
				}else{
					work_count = 0;
				}
			}
		} else {
			if (work_count > 0){
				work_count--;
			}else{
				work_count = 0;
			}
		}
	}

	@Override
	protected boolean canWork() {
		return !isFullInventory();
	}

	@Override
	protected void onWork() {
		if (!isWorking()){
			if (maps.containsKey(stacks.get(SLOT_ITEM).getItem().getRegistryName())){
				nowCrush = stacks.get(SLOT_ITEM).getItem().getRegistryName();
				stacks.get(SLOT_ITEM).shrink(1);
				work_count++;
			}
		}else {
			work_count++;
		}
		if (WORK_TIME < work_count){
			crush_block();
			work_count = 0;
		}
	}

	@Override
	public int getTickTime() {
		return 0;
	}

	private int[] kindTypes = new int[] {POWDER_IRON, POWDER_GOLD, POWDER_DIAM, POWDER_EMER, POWDER_REDS, POWDER_LAPS};
    private void crush_block() {
    	Compless cp = maps.get(nowCrush);
    	for (int i = 0; i < kindTypes.length && cp != null; i++) {
    		int kind = kindTypes[i];
    		this.powder[kind] += cp.getOre(kind);
    		while(this.powder[kind] >= 100) {
    			this.powder[kind] -= 100;
        		if (stacks.get(kind+POWDER_OFFSET).isEmpty()){
        			stacks.set(kind+POWDER_OFFSET, new ItemStack(cp.getMetal(kind),1));
        		}else{
        			stacks.get(kind+POWDER_OFFSET).grow(1);
        		}
    		}
    	}
    }

	@Override
    public void read(CompoundNBT compound) {
		super.read(compound);
		work_count=compound.getInt("count");

		String res = "";
		this.nowCrush = (res = compound.getString("res")).isEmpty()?null:new ResourceLocation(res);

		for (int i = 0; i < powder.length; i++){
			powder[i] = compound.getInt("powder"+i);
		}
    }

	@Override
    public CompoundNBT write(CompoundNBT compound) {
		compound = super.write(compound);
		compound.putInt("count", work_count);
		if (nowCrush != null){
			compound.putString("res", nowCrush.toString());
		}
		for ( int i = 0; i < powder.length; i++){
			compound.putInt("powder"+i, powder[i]);
		}

        return compound;
    }

	@Override
	public int[] getSlotsForFace(Direction side) {
		if (side == Direction.DOWN){
			return SLOT_BOTTOM;
		}
		return SLOT_SIDE;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, Direction direction) {
		return isItemValidForSlot(index, itemStackIn);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
		if (SLOT_BOTTOM[2] <= index && index <= SLOT_BOTTOM[SLOT_BOTTOM.length-1]){
			return true;
		}
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		boolean ret = false;
		if (index == SLOT_BATTERY) {
			ret = (stack.getItem() instanceof ItemBattery);
		}else if(index == SLOT_ITEM) {
			ret = true;
		}
		return ret;
	}

	@Override
	public int additionalgetField(int id) {
		int ret = 0;
		switch(id){
		case FIELD_COUNT:
			ret = work_count;
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
	public void additionalsetField(int id, int value) {
		switch(id){
		case FIELD_COUNT:
			work_count = value;
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
	public int additionalFieldCount() {
		return 7;
	}

	public static class Compless{
		public static final Map<Item,Integer[]> containItem = new HashMap<Item,Integer[]>(){
			{put(Items.IRON_INGOT,new Integer[]{POWDER_IRON,10});}
			{put(Items.GOLD_INGOT,new Integer[]{POWDER_GOLD,10});}
			{put(Items.DIAMOND,new Integer[]{POWDER_DIAM,10});}
			{put(Items.EMERALD,new Integer[]{POWDER_EMER,10});}
			{put(Items.REDSTONE,new Integer[]{POWDER_REDS,10});}
			{put(Items.LAPIS_LAZULI,new Integer[]{POWDER_LAPS,10});}
			{put(new ItemStack(Blocks.IRON_BLOCK).getItem(),new Integer[]{POWDER_IRON, 100});}
			{put(new ItemStack(Blocks.GOLD_BLOCK).getItem(),new Integer[]{POWDER_GOLD, 100});}
			{put(new ItemStack(Blocks.DIAMOND_BLOCK).getItem(),new Integer[]{POWDER_DIAM, 100});}
			{put(new ItemStack(Blocks.EMERALD_BLOCK).getItem(),new Integer[]{POWDER_EMER, 100});}
			{put(new ItemStack(Blocks.REDSTONE_BLOCK).getItem(),new Integer[]{POWDER_REDS, 100});}
			{put(new ItemStack(Blocks.LAPIS_BLOCK).getItem(),new Integer[]{POWDER_LAPS, 100});}
			{put(Items.IRON_NUGGET,new Integer[]{POWDER_IRON,1});}
			{put(Items.GOLD_NUGGET,new Integer[]{POWDER_GOLD,1});}
		};

		private ItemStack output;
		private int counter[] = new int[]{0,0,0,0,0,0};

		public Compless(ItemStack result){
			output = result;
		}

		public ItemStack getOutput(){return output;}
		public int getIron(){return counter[POWDER_IRON];}
		public int getGold(){return counter[POWDER_GOLD];}
		public int getDia(){return counter[POWDER_DIAM];}
		public int getEmerald(){return counter[POWDER_EMER];}
		public int getRedstone(){return counter[POWDER_REDS];}
		public int getlapis(){return counter[POWDER_LAPS];}
		public int getOre(int kind) {return counter[kind];}

		private final Item[] metal = new Item[] {Items.IRON_INGOT, Items.GOLD_INGOT, Items.DIAMOND, Items.EMERALD, Items.REDSTONE, Items.LAPIS_LAZULI};
		public Item getMetal(int kind) {
			return metal[kind];
		}


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


}
