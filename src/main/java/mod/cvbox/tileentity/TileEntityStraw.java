package mod.cvbox.tileentity;

import org.apache.commons.lang3.BooleanUtils;

import mod.cvbox.entity.EntityCore;
import mod.cvbox.item.ItemCore;
import mod.cvbox.util.ModUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlowingFluid;
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
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class TileEntityStraw extends TileEntity  implements IInventory, ITickable, ISidedInventory,IPowerSwitchEntity{
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
	public static final int FIELD_BATTERY = 8;
	public static final int FIELD_BATTERYMAX = 9;
	public static final int FIELD_POWER = 10;

	private int tank;
	private NonNullList<ItemStack> stacks = NonNullList.<ItemStack>withSize(2, ItemStack.EMPTY);
	private int[] SLOT_BOTTOM = new int[]{1};

	private static final int STRAW_TIME = 40;
	private boolean power;
	private int straw_count;
	private int power_count;
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
		super(EntityCore.Straw);
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


		power = false;
		power_count = 0;
	}

	@Override
	public void tick() {
		if (!getPower().isEmpty() && power_count == 0){
			powerDown();
		}
		if (!world.isRemote){
			if (checkPowerOn()){
				if (checkStraw()){
					straw_count++;
					power_count++;
					if (STRAW_TIME < straw_count){
						work_on();
						straw_count = 0;
					}
				}else{
					straw_count = 0;
				}
				power_count++;
				if (power_count > 40){
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
    			(state.getBlock() == Blocks.LAVA && state.get(BlockFlowingFluid.LEVEL) == 0)) ||

    			// water
    			(liquid == ItemCore.item_waterball &&
    			(state.getBlock() == Blocks.WATER && state.get(BlockFlowingFluid.LEVEL) == 0))){
    		if (stacks.get(1).isEmpty()){
    			stacks.set(1, new ItemStack(liquid,1));
    		}else if (stacks.get(1).getCount() < 64){
    			stacks.get(1).grow(1);
    		}else{
    			tank++;
    		}
    		world.setBlockState(strawpos,Blocks.AIR.getDefaultState());
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
    public void read(NBTTagCompound compound)
    {
		super.read(compound);
		tank = compound.getInt("tank");
		straw_count = compound.getInt("count");
		areaSize = compound.getInt("areaSize");


		power = compound.getBoolean("power");
		power_count=compound.getInt("power_count");
		if (areaSize != 0){
			makeArray(areaSize);
		}
		index_x = compound.getInt("idx_x");
		index_z = compound.getInt("idx_z");
		next_y = compound.getInt("next_y");
		updateY = compound.getBoolean("updateY");
		depth = compound.getInt("depth");
		width = compound.getInt("width");
		kind = compound.getInt("kind");
		if (kind == 0){
			liquid = ItemCore.item_lavaball;
		}else if (kind == 1){
			liquid = ItemCore.item_waterball;
		}

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
		compound.setInt("tank",  tank);
		compound.setInt("count", straw_count);

		compound.setInt("areaSize",areaSize);
		compound.setInt("idx_x",index_x);
		compound.setInt("idx_z",index_z);
		compound.setInt("next_y",next_y);
		compound.setBoolean("updateY",updateY);

		 compound.setInt("depth",depth);
		 compound.setInt("width",width);
		 compound.setInt("kind",kind);

			compound.setBoolean("power", power);
			compound.setInt("power_count",power_count);

		NBTTagList itemsTagList = new NBTTagList();
		for (int slotIndex = 0; slotIndex < stacks.size(); slotIndex++){
			if (!this.stacks.get(slotIndex).isEmpty()){
				NBTTagCompound itemTagCompound = new NBTTagCompound();

				itemTagCompound.setByte("Slot",(byte)slotIndex);
				this.stacks.get(slotIndex).write(itemTagCompound);
				itemsTagList.add(itemTagCompound);
			}
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
		return new TextComponentTranslation("tileentity.straw");
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
		return this.stacks.get(1).isEmpty() && this.stacks.get(1).isEmpty();
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
			if (stacks.get(1).isEmpty()){
				amount = tank>64?64:tank;
				stacks.set(0, new ItemStack(liquid,amount));
			}else if (stacks.get(1).getCount() < 64){
				amount = (64 - stacks.get(1).getCount() > tank)?tank:(64 - stacks.get(1).getCount());
				stacks.get(1).grow(amount);
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

		case FIELD_POWER:
			ret = BooleanUtils.toInteger(power);
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
		case FIELD_POWER:
			power = BooleanUtils.toBoolean(value);
			break;
		case FIELD_BATTERY:
			getPower().setDamage(value);
			break;
		}
	}

	@Override
	public int getFieldCount() {
		return 11;
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
			if ((state.getBlock() == Blocks.LAVA || state.getBlock() == Blocks.LAVA) && (changeLiquid(Blocks.LAVA) == ItemCore.item_lavaball)){
				ret = true;
			}else if ((state.getBlock() == Blocks.WATER || state.getBlock() == Blocks.WATER) && (changeLiquid(Blocks.WATER) == ItemCore.item_waterball)){
				ret = true;
			}
		}
		return ret;
	}

	private Item changeLiquid(Block kind){
		if (this.tank == 0 && stacks.get(1).isEmpty()){
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
