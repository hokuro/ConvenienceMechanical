package mod.cvbox.tileentity.factory;

import mod.cvbox.block.factory.BlockSetter;
import mod.cvbox.entity.EntityCore;
import mod.cvbox.item.ItemBattery;
import mod.cvbox.tileentity.ab.TileEntityPowerBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class TileEntitySetter extends TileEntityPowerBase  implements ISidedInventory {
	public static final String NAME="setter";

	public static final int WORK_TIME = 20;
	public static final int SLOT_ITEM = 1;
	private int[] SLOT_SIDE = new int[]{SLOT_BATTERY, SLOT_ITEM};

	public TileEntitySetter(){
		super(EntityCore.Setter, 2);;
	}

	@Override
	public void tick() {
		super.tick();
	}

	@Override
	protected boolean canWork() {
		return !this.isEmpty2() && canSpawan();
	}

	@Override
	protected void onWork() {
		Direction face = BlockSetter.getFacing(this.world.getBlockState(this.pos));
		BlockPos pos2 = this.pos.offset(face);
		BlockState state = this.world.getBlockState(pos2);
		if (state.getMaterial() != Material.AIR){
			return;
		}
		for (ItemStack item : stacks){
			if (!item.isEmpty()){
				Block setBlock = Block.getBlockFromItem(item.getItem());
				if (setBlock != Blocks.AIR){
					if (this.world.setBlockState(pos2, setBlock.getDefaultState())){
						item.shrink(1);
					}
					break;
				}
			}
		}
	}

	@Override
	public int getTickTime() {
		return WORK_TIME;
	}

	protected boolean canSpawan(){
		boolean ret = false;

		if (this.world.getBlockState(this.pos).getBlock() instanceof BlockSetter){
			Direction face = BlockSetter.getFacing(this.world.getBlockState(this.pos));
			if ( this.world.getBlockState(this.pos.offset(face)).getMaterial() == Material.AIR){
				ret = true;
			}
		}
		return ret;
	}

    public boolean isEmpty2() {
    	for (int i = 1; i < stacks.size(); i++){
    		if (!stacks.get(i).isEmpty()){
    			return false;
    		}
    	}
        return true;
    }

    public int getSettingSlot() {
        int i = -1;
        int j = 1;

        for (int k = 0; k < this.stacks.size(); ++k) {
            if (!((ItemStack)this.stacks.get(k)).isEmpty()) {
                i = k;
            }
        }
        return i;
    }

    public int addItemStack(ItemStack stack) {
        for (int i = 0; i < this.stacks.size(); ++i) {
            if (((ItemStack)this.stacks.get(i)).isEmpty()) {
                this.setInventorySlotContents(i, stack);
                return i;
            }
        }

        return -1;
    }


    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        return compound;
    }

	@Override
	public int[] getSlotsForFace(Direction side) {
		return SLOT_SIDE;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, Direction direction) {
		return isItemValidForSlot(index, itemStackIn);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
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
		return 0;
	}

	@Override
	public void additionalsetField(int id, int value) {
	}

	@Override
	public int additionalFieldCount() {
		return 0;
	}
}
