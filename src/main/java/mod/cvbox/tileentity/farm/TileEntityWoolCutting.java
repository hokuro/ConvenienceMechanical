package mod.cvbox.tileentity.farm;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.mojang.authlib.GameProfile;

import mod.cvbox.entity.EntityCore;
import mod.cvbox.entity.EntityPlayerDummy;
import mod.cvbox.item.ItemBattery;
import mod.cvbox.tileentity.ab.TileEntityPowerBase;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;

public class TileEntityWoolCutting extends TileEntityPowerBase  implements ISidedInventory {
	public static final String NAME = "woolcutting";
	public static final int COL = 3;
	public static final int ROW = 3;
	public static final int MAX_SLOT = (COL * ROW) + 2;
	public static final int SLOT_SHEAR = 1;

	public static final int FIELD_RANGEX = 0 + FIELD_OFFSET;
	public static final int FIELD_RANGEZ = 1+ FIELD_OFFSET;

	public static final int[] SLOTS_TOP = new int[] {SLOT_SHEAR};
	public static final int[] SLOTS_BOTTOM = new int[MAX_SLOT];

	private static final int TICK_TIME = 3000;	// 1日に8回起動

    private int range_x;
    private int range_z;

    private EntityPlayerDummy dummy;

	public TileEntityWoolCutting(){
		super(EntityCore.WoolCutting, MAX_SLOT);
		range_x = 0;
		range_z = 0;
		for (int i = 0; i < MAX_SLOT; i++) {
			SLOTS_BOTTOM[i] = i;
		}
	}

	@Override
	public void tick() {
		if (!world.isRemote) {
			if (dummy == null){
				dummy = new EntityPlayerDummy(this.world, new GameProfile(new UUID(0,0), "dummy"));
			}
		}
		super.tick();
	}

	@Override
	protected boolean canWork() {
		return (checkCheasr() && !isFullInventory(2));
	}

	@Override
	public int getTickTime() {
		return TICK_TIME;
	}

	private boolean checkCheasr() {
		return (stacks.get(SLOT_SHEAR).getItem() instanceof ShearsItem);
	}

	@Override
    protected void onWork() {
    	// ひつじを探してループする
    	List<SheepEntity> entitys = world.getEntitiesWithinAABB(SheepEntity.class,
    			new AxisAlignedBB(this.pos.getX() - (range_x+1), this.pos.getY() - 5, this.pos.getZ() - (range_z+1), this.pos.getX() + (range_x+1), this.pos.getY() + 5, this.pos.getZ() + (range_z+1)),
    			(entity)->{return entity != null && entity.isAlive();});
    	// ハサミを持たせるよ
    	ItemStack sheasr = stacks.get(SLOT_SHEAR);
		dummy.setHeldItem(Hand.MAIN_HAND, stacks.get(SLOT_SHEAR));
    	for (SheepEntity ent : entitys) {
    		// ハサミを持ってひつじにタッチ
    		sheasr.interactWithEntity(dummy, ent, Hand.MAIN_HAND);
    		if (dummy.getHeldItemMainhand() == ItemStack.EMPTY) {
    			// はさみこわれた～
    			break;
    		}
    	}
    	// ハサミをインベントリに戻す
    	stacks.set(SLOT_SHEAR, dummy.getHeldItemMainhand());

    	// 刈った羊毛を探してループする
    	List<ItemEntity> itemEntitys = world.getEntitiesWithinAABB(ItemEntity.class,
    			new AxisAlignedBB(this.pos.getX() - (range_x+1), this.pos.getY() - 5, this.pos.getZ() - (range_z+1), this.pos.getX() + (range_x+1), this.pos.getY() + 5, this.pos.getZ() + (range_z+1)),
    			(entity)->{return entity != null && entity.isAlive();});
    	for (Entity ent : itemEntitys) {
			ItemEntity ie = (ItemEntity)ent;
			if (wools.contains(ie.getItem().getItem())) {
				ItemStack item = ie.getItem();
				if (this.addItem(item, 2).isEmpty()) {
					ie.remove();
				}
			}
    	}
    }

	@Override
	public void Exec() {
		if (!world.isRemote) {
			if (checkPowerOn() && canWork()) {
				onWork();
				tick_count = 0;
				power_count = 0;
			}
		}
	}

    @SuppressWarnings("deprecation")
	private final List<Item> wools = new ArrayList<Item>() {
    	{add(Item.getItemFromBlock(Blocks.WHITE_WOOL));}
    	{add(Item.getItemFromBlock(Blocks.ORANGE_WOOL));}
    	{add(Item.getItemFromBlock(Blocks.MAGENTA_WOOL));}
    	{add(Item.getItemFromBlock(Blocks.LIGHT_BLUE_WOOL));}
    	{add(Item.getItemFromBlock(Blocks.YELLOW_WOOL));}
    	{add(Item.getItemFromBlock(Blocks.LIME_WOOL));}
    	{add(Item.getItemFromBlock(Blocks.PINK_WOOL));}
    	{add(Item.getItemFromBlock(Blocks.GRAY_WOOL));}
    	{add(Item.getItemFromBlock(Blocks.LIGHT_GRAY_WOOL));}
    	{add(Item.getItemFromBlock(Blocks.CYAN_WOOL));}
    	{add(Item.getItemFromBlock(Blocks.PURPLE_WOOL));}
    	{add(Item.getItemFromBlock(Blocks.BLUE_WOOL));}
    	{add(Item.getItemFromBlock(Blocks.BROWN_WOOL));}
    	{add(Item.getItemFromBlock(Blocks.GREEN_WOOL));}
    	{add(Item.getItemFromBlock(Blocks.RED_WOOL));}
    	{add(Item.getItemFromBlock(Blocks.BLACK_WOOL));}
    };

	@Override
    public void read(CompoundNBT compound) {
		super.read(compound);

		range_x = compound.getInt("range_x");
		range_z = compound.getInt("range_z");
    }

	@Override
    public CompoundNBT write(CompoundNBT compound) {
		compound = super.write(compound);

		compound.putInt("range_x",range_x);
		compound.putInt("range_z",range_z);

        return compound;
    }

	@Override
	public int[] getSlotsForFace(Direction side) {
		if (side == Direction.DOWN) {
			return SLOTS_BOTTOM;
		}
		return SLOTS_TOP;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, Direction direction) {
		return isItemValidForSlot(index, itemStackIn);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
		if (index > SLOT_SHEAR && index < MAX_SLOT){
			return true;
		}
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		boolean ret = false;
		if (index == SLOT_BATTERY) {
			ret = (stack.getItem() instanceof ItemBattery);
		}else if (index == SLOT_SHEAR) {
			ret = (stack.getItem() instanceof ShearsItem);
		}
		return ret;
	}


	@Override
	public int getSizeInventory() {
		return this.stacks.size();
	}

	@Override
	public int additionalgetField(int id) {
		int ret = 0;
		switch(id){
		case FIELD_RANGEX:
			ret = range_x;
			break;
		case FIELD_RANGEZ:
			ret = range_z;
			break;
		}
		return ret;
	}

	@Override
	public void additionalsetField(int id, int value) {
		switch(id){
		case FIELD_RANGEX:
			range_x = value;
			break;
		case FIELD_RANGEZ:
			range_z = value;
			break;
		}
	}

	@Override
	public int additionalFieldCount() {
		return 2;
	}
}
