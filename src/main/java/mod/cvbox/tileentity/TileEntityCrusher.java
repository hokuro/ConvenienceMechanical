package mod.cvbox.tileentity;

import org.apache.commons.lang3.BooleanUtils;

import mod.cvbox.block.BlockPowerMachineContainer;
import mod.cvbox.util.ModUtil;
import mod.cvbox.util.ModUtil.CompaierLevel;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityCrusher extends TileEntity  implements IInventory, ITickable, ISidedInventory{
	public static final String NAME = "crusher";
	public static final int FIELD_POWER = 0;
	public static final int FIELD_SELECT = 1;
	public static final int FIELD_DIRT = 2;
	public static final int FIELD_GRAVE = 3;
	public static final int FIELD_SAND = 4;
	public static final int FIELD_COUNT = 5;

	private boolean power;

	private NonNullList<ItemStack> stacks = NonNullList.<ItemStack>withSize(29, ItemStack.EMPTY);
	private int[] SLOT_SIDE = new int[]{0,1};
	private int[] SLOT_BOTTOM = new int[]{2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28};


	private int select;
	private int powder_dirt;
	private int powder_grave;
	private int powder_sand;
	public static final int CRUSH_TIME = 100;
	private int crush_count;
	private ItemStack nextDrop;

	public TileEntityCrusher(){
		super();
		power = false;
		powder_dirt = 0;
		powder_grave = 0;
		powder_sand = 0;
		select = 0;
		crush_count = 0;
		nextDrop = ItemStack.EMPTY;
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
			if (!nextDrop.isEmpty()){
				output(nextDrop);
			}else if (power && select != 0){
				if (!isCrushing()){
					if (!stacks.get(0).isEmpty()){
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
    	switch(select){
    	case 1:
    		this.powder_dirt += 25;
    		if (powder_dirt >= 100){
    			powder_dirt = 0;
    			if (stacks.get(1).getItem() == Items.WATER_BUCKET){
    				output(new ItemStack(Items.CLAY_BALL,4,0));
    			}else{
    				output(new ItemStack(Blocks.DIRT,1,0));
    			}
    		}
    		break;
    	case 2:
    		this.powder_grave += 20;
    		if (powder_grave >= 100){
    			powder_grave = 0;
    			output(new ItemStack(Blocks.GRAVEL,1,0));
    		}
    		break;
    	case 3:
    		this.powder_sand += 10;
    		if (powder_sand >= 100){
    			powder_sand = 0;
    			output(new ItemStack(Blocks.SAND,1,0));
    		}
    		break;
    	}
    }

    private void output(ItemStack item){
    	for (int i = 2; i < stacks.size(); i++){
    		ItemStack item2 = stacks.get(i);
    		if (item2.isEmpty()){
    			this.setInventorySlotContents(i, item.copy());
    			item = ItemStack.EMPTY;
    			break;
    		}else if (ModUtil.compareItemStacks(item2, item, CompaierLevel.LEVEL_EQUAL_META)){
    			if (item2.getCount()+item.getCount() <= item.getMaxStackSize()){
    				item2.grow(item.getCount());
    				item.shrink(item.getCount());
    				this.setInventorySlotContents(i, item2);
    				break;
    			}
    		}
    	}
    	if (item.getCount() != 0){
    		nextDrop = item.copy();
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


		powder_dirt=compound.getInteger("dirt");
		powder_grave=compound.getInteger("grave");
		powder_sand=compound.getInteger("sand");
		select=compound.getInteger("select");
		crush_count=compound.getInteger("count");

		NBTTagList itemsTagList = compound.getTagList("Items",10);
		for (int tagCounter = 0; tagCounter < itemsTagList.tagCount(); tagCounter++){
			NBTTagCompound itemTagCompound = itemsTagList.getCompoundTagAt(tagCounter);

			byte slotIndex =itemTagCompound.getByte("Slot");
			if ((slotIndex >= 0) && (slotIndex < stacks.size())){
				stacks.set(slotIndex, new ItemStack(itemTagCompound));
			}else{
				nextDrop = new ItemStack(itemTagCompound);
			}
		}
    }

	@Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
		compound = super.writeToNBT(compound);
		compound.setBoolean("power", power);
		compound.setInteger("dirt", powder_dirt);
		compound.setInteger("grave", powder_grave);
		compound.setInteger("sand", powder_sand);
		compound.setInteger("select", select);
		compound.setInteger("count", crush_count);

		NBTTagList itemsTagList = new NBTTagList();
		for (int slotIndex = 0; slotIndex < stacks.size()+1; slotIndex++){
			NBTTagCompound itemTagCompound = new NBTTagCompound();

			itemTagCompound.setByte("Slot",(byte)slotIndex);
			if (stacks.size()>slotIndex){
				this.stacks.get(slotIndex).writeToNBT(itemTagCompound);
			}else{
				nextDrop.writeToNBT(itemTagCompound);
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
		return "tileentity.crusher";
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
			return Block.getBlockFromItem(itemStackIn.getItem()).getStateFromMeta(itemStackIn.getMetadata()).getMaterial() == Material.ROCK;
		}
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		if (index >1){
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
		if (index == 0){
			return Block.getBlockFromItem(stack.getItem()).getStateFromMeta(stack.getMetadata()).getMaterial() == Material.ROCK;
		}else if (index == 1){
			return Items.WATER_BUCKET == stack.getItem();
		}
		return true;
	}

	@Override
	public int getField(int id) {
		int ret = 0;
		switch(id){
		case FIELD_POWER:
			ret = BooleanUtils.toInteger(power);
			break;
		case FIELD_SELECT:
			ret = select;
			break;
		case FIELD_DIRT:
			ret = powder_dirt;
			break;
		case FIELD_GRAVE:
			ret = powder_grave;
			break;
		case FIELD_SAND:
			ret = powder_sand;
			break;
		case FIELD_COUNT:
			ret = crush_count;
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
		case FIELD_SELECT:
			select = value;
			break;
		case FIELD_DIRT:
			powder_dirt = value;
			break;
		case FIELD_GRAVE:
			powder_grave = value;
			break;
		case FIELD_SAND:
			powder_sand = value;
			break;
		case FIELD_COUNT:
			crush_count = value;
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

	public ItemStack NextDrop(){
		return nextDrop;
	}

	public boolean inBuket() {
		return stacks.get(1).getItem() == Items.WATER_BUCKET;
	}
}
