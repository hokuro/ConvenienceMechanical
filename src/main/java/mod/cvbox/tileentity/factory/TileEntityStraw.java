package mod.cvbox.tileentity.factory;

import mod.cvbox.entity.EntityCore;
import mod.cvbox.item.ItemBattery;
import mod.cvbox.item.ItemCore;
import mod.cvbox.tileentity.ab.TileEntityPowerBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class TileEntityStraw extends TileEntityPowerBase  implements ISidedInventory {
	public static final String NAME = "straw";
	public static final int MAX_TANK = 1728;
	public static final int FIELD_TANK = 0 + FIELD_OFFSET;
	public static final int FIELD_WIDTH = 1 + FIELD_OFFSET;
	public static final int FIELD_DEPTH = 2 + FIELD_OFFSET;
	public static final int FIELD_X =3 + FIELD_OFFSET;
	public static final int FIELD_Y =4 + FIELD_OFFSET;
	public static final int FIELD_Z =5 + FIELD_OFFSET;
	public static final int FIELD_SIZE = 6 + FIELD_OFFSET;
	public static final int FIELD_KIND = 7 + FIELD_OFFSET;

	private int tank;
	public static final int SLOT_TANK = 1;
	private int[] SLOT_SIDE = new int[] {SLOT_BATTERY};
	private int[] SLOT_BOTTOM = new int[]{SLOT_BATTERY, SLOT_TANK};

	private static final int WORK_TIME = 40;	// 2秒に1回
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
		super(EntityCore.Straw, 2);
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
	public void tick() {
		super.tick();
	}

	@Override
	protected boolean canWork() {
		return checkStraw();
	}

	@Override
	protected void onWork() {
    	BlockPos strawpos = pos.add(next_x[index_x],next_y,next_z[index_z]);
    	if (strawpos.equals(pos.offset(Direction.DOWN))){
    		next_y=-2;
    		strawpos = pos.add(next_x[index_x],next_y,next_z[index_z]);
    	}
    	BlockState state = world.getBlockState(strawpos);
    	if (
    			// lava
    			(liquid == ItemCore.item_lavaball &&
    			(state.getBlock() == Blocks.LAVA && state.get(FlowingFluidBlock.LEVEL) == 0)) ||

    			// water
    			(liquid == ItemCore.item_waterball &&
    			(state.getBlock() == Blocks.WATER && state.get(FlowingFluidBlock.LEVEL) == 0))){
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

	@Override
	public int getTickTime() {
		return WORK_TIME;
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

	@Override
    public void read(CompoundNBT compound) {
		super.read(compound);
		tank = compound.getInt("tank");
		areaSize = compound.getInt("areaSize");

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
    }

	@Override
    public CompoundNBT write(CompoundNBT compound)
    {
		compound = super.write(compound);
		compound.putInt("tank",  tank);

		compound.putInt("areaSize",areaSize);
		compound.putInt("idx_x",index_x);
		compound.putInt("idx_z",index_z);
		compound.putInt("next_y",next_y);
		compound.putBoolean("updateY",updateY);

		compound.putInt("depth",depth);
		compound.putInt("width",width);
		compound.putInt("kind",kind);

        return compound;
    }

	@Override
	public int[] getSlotsForFace(Direction side) {
		if (side == Direction.DOWN) {
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
		if (index == SLOT_TANK){
			return true;
		}
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		boolean ret = false;
		if (index == SLOT_BATTERY) {
			ret = (stack.getItem() instanceof ItemBattery);
		}
		return ret;
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
	public int additionalgetField(int id) {
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
	public void additionalsetField(int id, int value) {
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
	public int additionalFieldCount() {
		return 8;
	}

	public Item getLiquid(){
		return liquid;
	}

	private boolean checkStraw(){
		boolean ret = false;
		if (tank < MAX_TANK){
			BlockState state =this.world.getBlockState(pos.offset(Direction.DOWN));
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
}
