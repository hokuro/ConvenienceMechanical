package mod.cvbox.tileentity;

import mod.cvbox.item.ItemCore;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class TileEntityStraw extends TileEntity  implements IInventory, ITickable, ISidedInventory{
	public static final String NAME = "straw";
	public static final int MAX_TANK = 1728;
	public static final int FIELD_TANK = 0;
	public static final int FIELD_WIDTH = 1;
	public static final int FIELD_DEPTH = 2;
	public static final int FIELD_X =3;
	public static final int FIELD_Y =4;
	public static final int FIELD_Z =5;
	public static final int FIELD_SIZE = 6;
	public static final int FIELD_KIND = 7;

	private int tank;
	private NonNullList<ItemStack> stacks = NonNullList.<ItemStack>withSize(1, ItemStack.EMPTY);
	private int[] SLOT_BOTTOM = new int[]{0};

	private static final int STRAW_TIME = 40;
	private int straw_count;
	private Item liquid;


	private int next_y = -1;
	private boolean updateY = false;
	private int areaSize;
    private int next_x[];
    private int next_z[];
    private int index_x;
    private int index_z;

    private int depth;
    private int width;

    private int[] dispPos = new int[3];
    private int kind;

	public TileEntityStraw(){
		super();
		straw_count = 0;
		areaSize = 0;

		index_x = 0;
		next_x = new int[]{0};

		index_z = 0;
		next_z = new int[]{0};
		liquid = Items.AIR;
		dispPos[0] = next_x[index_x];
		dispPos[2] = next_z[index_z];
		dispPos[1] = next_y;

		depth = 1;
		width = 0;
		kind = -1;
	}

	@Override
	public void update() {
		if (!world.isRemote){
			if (checkStraw()){
				straw_count++;
				if (STRAW_TIME < straw_count){
					work_on();
					straw_count = 0;
				}
			}else{
				straw_count = 0;
			}
		}

	}


    private void work_on()
    {
    	BlockPos strawpos = pos.add(next_x[index_x],next_y,next_z[index_z]);
    	if (strawpos.equals(pos.offset(EnumFacing.DOWN))){
    		next_y=-2;
    		strawpos = pos.add(next_x[index_x],next_y,next_z[index_z]);
    	}
    	IBlockState state = world.getBlockState(strawpos);
    	if (
    			// lava
    			(liquid == ItemCore.item_lavaball &&
    			((state.getBlock() == Blocks.FLOWING_LAVA && state.getBlock().getMetaFromState(state) == 0) ||
    			(state.getBlock() == Blocks.LAVA && state.getBlock().getMetaFromState(state) == 0))) ||

    			// water
    			((liquid == ItemCore.item_waterball &&
    			((state.getBlock() == Blocks.FLOWING_WATER && state.getBlock().getMetaFromState(state) == 0) ||
    			(state.getBlock() == Blocks.WATER && state.getBlock().getMetaFromState(state) == 0))))){
    		if (stacks.get(0).isEmpty()){
    			stacks.set(0, new ItemStack(liquid,1));
    		}else if (stacks.get(0).getCount() < 64){
    			stacks.get(0).grow(1);
    		}else{
    			tank++;
    		}
    		world.setBlockToAir(strawpos);
    		updateY = false;
    	}else{
    		if (!updateY){
    			next_y--;
    			if (-1* next_y > depth){
    				next_y = -1;
    			}
        		updateY = true;
    		}else{
    			next_y = -1;
    			updateY = false;

    			index_x++;
    			index_z++;
    			if (index_x >= next_x.length){
    				areaSize++;
    				if (areaSize > width){
    					areaSize = 0;
    				}
    				makeArray(areaSize);
    			}
    		}
    	}
		dispPos[0] = next_x[index_x];
		dispPos[2] = next_z[index_z];
		dispPos[1] = next_y;
    }


	public void makeArray(int distance){
		int size = (distance * 2 + 1) * 2 + (distance*2-1) * 2;
		int length = distance * 2;
		index_x = 0;
		index_z = 0;

		next_x = new int[size+1];
		next_z = new int[size+1];

		int x = -1 * distance;
		int z = -1 * distance;
		next_x[0] = x;
		next_z[0] = z;
		for (int i = 0; i < size-1; i++){
			switch(MathHelper.floor(i/(size/4))){
			case 0: x++;break;
			case 1: z++;break;
			case 2: x--;break;
			case 3: z--;break;
			}
			next_x[i+1] = x;
			next_z[i+1] = z;
		}

	}

//    @Override
//    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
//    {
//        return false;
//    }

	@Override
    public void readFromNBT(NBTTagCompound compound)
    {
		super.readFromNBT(compound);
		tank = compound.getInteger("tank");
		straw_count = compound.getInteger("count");
		areaSize = compound.getInteger("areaSize");
		if (areaSize != 0){
			makeArray(areaSize);
		}
		index_x = compound.getInteger("idx_x");
		index_z = compound.getInteger("idx_z");
		next_y = compound.getInteger("next_y");
		updateY = compound.getBoolean("updateY");
		depth = compound.getInteger("depth");
		width = compound.getInteger("width");
		kind = compound.getInteger("kind");
		if (kind == 0){
			liquid = ItemCore.item_lavaball;
		}else if (kind == 1){
			liquid = ItemCore.item_waterball;
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
		compound.setInteger("tank",  tank);
		compound.setInteger("count", straw_count);

		compound.setInteger("areaSize",areaSize);
		compound.setInteger("idx_x",index_x);
		compound.setInteger("idx_z",index_z);
		compound.setInteger("next_y",next_y);
		compound.setBoolean("updateY",updateY);

		 compound.setInteger("depth",depth);
		 compound.setInteger("width",width);
		 compound.setInteger("kind",kind);

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
		return "tileentity.straw";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return SLOT_BOTTOM;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return false;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return true;
	}

	@Override
	public int getSizeInventory() {
		return this.stacks.size();
	}

	@Override
	public boolean isEmpty() {
		return this.stacks.get(0).isEmpty();
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
		if (tank > 0){
			int amount = 0;
			if (stacks.get(0).isEmpty()){
				amount = tank>64?64:tank;
				stacks.set(0, new ItemStack(liquid,amount));
			}else if (stacks.get(0).getCount() < 64){
				amount = (64 - stacks.get(0).getCount() > tank)?tank:(64 - stacks.get(0).getCount());
				stacks.get(0).grow(amount);
			}
			tank -= amount;
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
		return true;
	}

	@Override
	public int getField(int id) {
		int ret = 0;
		switch(id){
		case FIELD_TANK:
			ret = tank;
			break;
		case FIELD_WIDTH:
			ret = width;
			break;
		case FIELD_DEPTH:
			ret = depth;
			break;
		case FIELD_X:
			ret = dispPos[0];
			break;
		case FIELD_Y:
			ret = dispPos[1];
			break;
		case FIELD_Z:
			ret = dispPos[2];
			break;
		case FIELD_SIZE:
			ret = areaSize;
			break;
		case FIELD_KIND:
			ret = this.kind;
			break;
		}
		return ret;
	}

	@Override
	public void setField(int id, int value) {
		switch(id){
		case FIELD_TANK:
			tank = value;
			break;
		case FIELD_WIDTH:
			width = value;
			break;
		case FIELD_DEPTH:
			depth = value;
			break;
		case FIELD_X:
			dispPos[0] = value;
			break;
		case FIELD_Y:
			dispPos[1] = value;
			break;
		case FIELD_Z:
			dispPos[2] = value;
			break;
		case FIELD_SIZE:
			areaSize = value;
			break;
		case FIELD_KIND:
			kind = value;
			break;
		}
	}

	@Override
	public int getFieldCount() {
		return 7;
	}

	@Override
	public void clear() {
		stacks.clear();
	}

	public Item getLiquid(){
		return liquid;
	}

	private boolean checkStraw(){
		boolean ret = false;
		if (tank < MAX_TANK){
			IBlockState state =this.world.getBlockState(pos.offset(EnumFacing.DOWN));
			if ((state.getBlock() == Blocks.LAVA || state.getBlock() == Blocks.FLOWING_LAVA) && (changeLiquid(Blocks.LAVA) == ItemCore.item_lavaball)){
				ret = true;
			}else if ((state.getBlock() == Blocks.WATER || state.getBlock() == Blocks.FLOWING_WATER) && (changeLiquid(Blocks.WATER) == ItemCore.item_waterball)){
				ret = true;
			}
		}
		return ret;
	}

	private Item changeLiquid(Block kind){
		if (this.tank == 0 && stacks.get(0).isEmpty()){
			if (kind == Blocks.LAVA){
				liquid = ItemCore.item_lavaball;
				this.kind = 0;
			}else if (kind == Blocks.WATER){
				liquid = ItemCore.item_waterball;
				this.kind = 1;
			}
		}
		return liquid;
	}

	public void resetSearchArea() {
		areaSize = 0;
		makeArray(areaSize);
		next_y = -1;
		updateY = false;
	}
}
