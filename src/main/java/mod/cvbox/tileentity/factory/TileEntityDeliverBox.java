package mod.cvbox.tileentity.factory;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.BooleanUtils;

import mod.cvbox.block.factory.BlockDeliverBox;
import mod.cvbox.entity.EntityCore;
import mod.cvbox.tileentity.ab.ITileEntityParameter;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;

public class TileEntityDeliverBox extends TileEntity  implements IInventory, ITickableTileEntity, ITileEntityParameter{

	public static final String NAME = "deliverbox";
	public static final int COL = 3;
	public static final int ROW = 3;
	public static final int MAX_SLOT = COL * ROW + 1;
	public static final int FIELD_ISMATCH = 0;
	public static final int SLOT_FILTER = 0;

	private Direction deliverOffset;
	private final NonNullList<ItemStack> stacks;

	private boolean isMatch;


	public TileEntityDeliverBox() {
		this(Direction.DOWN);
	}

	public TileEntityDeliverBox(Direction face) {
		super(EntityCore.Deliverbox);
		stacks = NonNullList.withSize(MAX_SLOT, ItemStack.EMPTY);
		setDeliverFade(face);
		isMatch = true;
	}

	public void setDeliverFade(Direction face) {
		deliverOffset = face;
	}

	@Override
	public void clear() {
		stacks.clear();
	}

	@Override
	public void tick() {
		if (!world.isRemote) {
			onWork();
		}
	}


	private void onWork() {
		BlockState state = world.getBlockState(pos);
		deliverOffset = state.get(BlockDeliverBox.FACING);
		if (!isFullInventory()) {
			drawItem();
		}
		pushItem();
	}

	private void drawItem() {
		for (int i = 0; i < Direction.values().length; i++) {
			if (Direction.values()[i] != deliverOffset) {
				BlockPos searchPos = this.pos.offset(Direction.values()[i]);
				TileEntity ent = world.getTileEntity(searchPos);
				if (ent instanceof ISidedInventory) {
					ISidedInventory inv = (ISidedInventory)ent;
					int[] slots = inv.getSlotsForFace(Direction.DOWN);
					for (int sl = 0; sl < slots.length; sl++) {
						int idx = slots[sl];
						ItemStack item = inv.getStackInSlot(idx).copy();
						if (!item.isEmpty() && inv.canExtractItem(idx, item, Direction.DOWN)) {
							ItemStack over = this.addItem(item);
							inv.setInventorySlotContents(idx, over);
							break;
						}
					}
				}else if (ent instanceof IInventory && !(ent instanceof TileEntityDeliverBox)) {
					IInventory inv = (IInventory)ent;
					for (int sl = 0; sl < inv.getSizeInventory(); sl++) {
						ItemStack item = inv.getStackInSlot(sl).copy();
						if (!item.isEmpty()) {
							ItemStack over = this.addItem(item);
							inv.setInventorySlotContents(sl, over);
						}
					}
				}
			}
			if (isFullInventory()) {
				break;
			}
		}
	}

	private void pushItem() {
		boolean isSend = false;
		BlockPos searchPos = this.pos.offset(this.deliverOffset);
		TileEntity ent = world.getTileEntity(searchPos);
		if (ent instanceof ISidedInventory) {
			ISidedInventory inv = (ISidedInventory)ent;
			int[] slot_up = inv.getSlotsForFace(Direction.UP);
			int[] slot_es = inv.getSlotsForFace(Direction.EAST);
			int[] slot_ws = inv.getSlotsForFace(Direction.WEST);
			int[] slot_nt = inv.getSlotsForFace(Direction.NORTH);
			int[] slot_st = inv.getSlotsForFace(Direction.SOUTH);
			List<Integer> slots = new ArrayList<Integer>();
			makeList(slots, slot_up);
			makeList(slots, slot_es);
			makeList(slots, slot_ws);
			makeList(slots, slot_nt);
			makeList(slots, slot_st);
			for (int i = 1; i < stacks.size() && !isSend; i++) {
				ItemStack deliver = stacks.get(i).copy();
				if (!deliver.isEmpty()) {
					for (int sl = 0; sl < slots.size() && !isSend; sl++) {
						int index = slots.get(sl);
						if ((inv.canInsertItem(index, deliver, Direction.UP) ||
								inv.canInsertItem(index, deliver, Direction.EAST) ||
								inv.canInsertItem(index, deliver, Direction.WEST) ||
								inv.canInsertItem(index, deliver, Direction.SOUTH) ||
								inv.canInsertItem(index, deliver, Direction.NORTH)) &&
								inv.isItemValidForSlot(index, deliver)) {
							ItemStack item = inv.getStackInSlot(index).copy();
							if (item.isEmpty()) {
								int count = Math.min(inv.getInventoryStackLimit(), deliver.getCount());
								inv.setInventorySlotContents(index, new ItemStack(deliver.getItem(), count));
								deliver.shrink(count);
								stacks.set(i, deliver.isEmpty()?ItemStack.EMPTY:deliver.copy());
								isSend = true;
							}else if (item.getItem() == deliver.getItem() && item.getCount() < Math.min(item.getMaxStackSize(), inv.getInventoryStackLimit())) {
								int size = Math.min(item.getMaxStackSize(), inv.getInventoryStackLimit());
								int count = Math.min(size - item.getCount(), deliver.getCount());
								item.grow(count);
								deliver.shrink(count);
								inv.setInventorySlotContents(index, item.copy());
								stacks.set(i, deliver.isEmpty()?ItemStack.EMPTY:deliver.copy());
								isSend = true;
							}
						}
					}
				}
			}
		} else if (ent instanceof IInventory) {
			IInventory inv = (IInventory)ent;
			for (int i = 1; i < stacks.size() && !isSend; i++) {
				ItemStack deliver = stacks.get(i).copy();
				if (!deliver.isEmpty()) {
					for (int index = 0; index < inv.getSizeInventory() && !isSend; index++) {
						if (inv.isItemValidForSlot(index, deliver)) {
							ItemStack item = inv.getStackInSlot(index).copy();
							if (item.isEmpty()) {
								int count = Math.min(inv.getInventoryStackLimit(), deliver.getCount());
								inv.setInventorySlotContents(index, new ItemStack(deliver.getItem(), count));
								deliver.shrink(count);
								stacks.set(i, deliver.isEmpty()?ItemStack.EMPTY:deliver.copy());
								isSend = true;
							} else if (item.getItem() == deliver.getItem() && item.getCount() < Math.min(item.getMaxStackSize(), inv.getInventoryStackLimit())) {
								int size = Math.min(item.getMaxStackSize(), inv.getInventoryStackLimit());
								int count = Math.min(size - item.getCount(), deliver.getCount());
								item.grow(count);
								deliver.shrink(count);
								inv.setInventorySlotContents(index, item.copy());
								stacks.set(i, deliver.isEmpty()?ItemStack.EMPTY:deliver.copy());
								isSend = true;
							}
						}
					}
				}
			}
		}
	}

	private void makeList(List<Integer> index, int[] slots) {
		if (index == null) {index = new ArrayList<Integer>();}
		for (int i = 0; i < slots.length; i++) {
			if (!index.contains(slots[i])) {
				index.add(slots[i]);
			}
		}
	}

	@Override
    public void read(CompoundNBT compound) {
		super.read(compound);

		deliverOffset = Direction.byIndex(compound.getInt("deliver"));

		this.stacks.clear();
		ItemStackHelper.loadAllItems(compound, this.stacks);
    }

	@Override
    public CompoundNBT write(CompoundNBT compound) {
		compound = super.write(compound);
		compound.putInt("deliver", deliverOffset.getIndex());

		ItemStackHelper.saveAllItems(compound, stacks);
        return compound;
    }

	@Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT cp = super.getUpdateTag();
        return this.write(cp);
    }

	@Override
    public void handleUpdateTag(CompoundNBT tag) {
		super.handleUpdateTag(tag);
		this.read(tag);
    }

	@Override
	public SUpdateTileEntityPacket getUpdatePacket()  {
        CompoundNBT CompoundNBT = new CompoundNBT();
        return new SUpdateTileEntityPacket(this.pos, 1,  this.write(CompoundNBT));
    }


	@Override
	public int getSizeInventory() {
		return stacks.size();
	}

	@Override
	public boolean isEmpty() {
		boolean ret = true;
		for (ItemStack stack : stacks) {
			if (!stack.isEmpty()) {
				ret = false;
				break;
			}
		}
		return ret;
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
	public boolean isUsableByPlayer(PlayerEntity player) {
		if (this.world.getTileEntity(this.pos) != this) {
			return false;
		}
		return player.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64.0D;
	}

	public boolean isFullInventory() {
		boolean ret = true;
		for(ItemStack stack : stacks) {
			if (stack.isEmpty() || stack.getCount() < Math.min(stack.getMaxStackSize(), getInventoryStackLimit())) {
				ret = false;
				break;
			}
		}
		return ret;
	}

	public ItemStack addItem(ItemStack stack) {
		ItemStack item = stack.copy();
		ItemStack filter = stacks.get(SLOT_FILTER);
		// フィルターに一致するか
		if ((!filter.isEmpty() &&
				((isMatch && filter.getItem() != item.getItem()) ||
				 (!isMatch && filter.getItem() == item.getItem())))){
			return item;
		}

		for(int i = 1; i < this.stacks.size(); ++i) {
			ItemStack itemstack = this.getStackInSlot(i);
			if (ItemStack.areItemsEqual(itemstack, item)) {
				int size = Math.min(this.getInventoryStackLimit(), item.getMaxStackSize());
				int count = Math.min(itemstack.getCount(), size - item.getCount());
				if (count > 0) {
					item.grow(count);
					itemstack.shrink(count);
				}
				if (item.isEmpty()) {
					break;
				}
			}
		}
		if (item.isEmpty()) {
			return ItemStack.EMPTY;
		} else {
			for(int i = 1; i < this.stacks.size(); ++i) {
				ItemStack itemstack = this.getStackInSlot(i);
				if (itemstack.isEmpty()) {
					this.setInventorySlotContents(i, item.copy());
					item.setCount(0);
					break;
				}
			}
			return item.isEmpty() ? ItemStack.EMPTY : item;
		}
	}

	public boolean isItemValidForSlot(int index, ItemStack stack) {
		boolean ret = false;
		ItemStack filter = stacks.get(SLOT_FILTER);
		if (index != SLOT_FILTER) {
			if (filter.isEmpty()) {
				ret = true;
			}else if (((filter.getItem() == stack.getItem() && isMatch) ||
						 (filter.getItem() != stack.getItem() && !isMatch))){
				ret = true;
			}
		}
		return ret;
	}


	@Override
	public int getField(int id) {
		int ret = 0;
		switch(id) {
		case FIELD_ISMATCH:
			ret = BooleanUtils.toInteger(isMatch);
			break;
		}
		return ret;
	}

	@Override
	public void setField(int id, int value) {
		switch(id) {
		case FIELD_ISMATCH:
			isMatch = BooleanUtils.toBoolean(value);
			break;
		}
	}

	@Override
	public int getFieldCount() {
		// TODO 自動生成されたメソッド・スタブ
		return 1;
	}
}
