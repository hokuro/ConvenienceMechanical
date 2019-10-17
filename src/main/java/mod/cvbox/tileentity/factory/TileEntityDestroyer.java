package mod.cvbox.tileentity.factory;

import org.apache.commons.lang3.BooleanUtils;

import mod.cvbox.block.factory.BlockDestroyer;
import mod.cvbox.entity.EntityCore;
import mod.cvbox.item.ItemBattery;
import mod.cvbox.tileentity.ab.TileEntityPowerBase;
import mod.cvbox.util.ModUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class TileEntityDestroyer extends TileEntityPowerBase{
	public static final String NAME = "destroyer";
	public static final int FIELD_TIME = 0 + FIELD_OFFSET;
	public static final int FIELD_MODE = 1 + FIELD_OFFSET;
	public static final int FIELD_ISRUN = 2 + FIELD_OFFSET;

	public static final int MODE_TIME = 0;
	public static final int MODE_CHANGE = 1;
	public static final int DEFAULT_TIME = 20; // デフォルト1秒に1回

	private int mode;
	private int onTime;

	private BlockState beforeState;

	public TileEntityDestroyer(){
		super(EntityCore.Destroyer, 1);
		beforeState = Blocks.AIR.getDefaultState();
		mode = MODE_TIME;
		onTime = DEFAULT_TIME;
	}

	@Override
	public void tick() {
		super.tick();
	}

	@Override
	protected boolean canWork() {
		return true;
	}

	@Override
	protected void onWork() {
		if (mode == MODE_CHANGE){
			BlockState state = world.getBlockState(pos);
			Direction face = state.get(BlockDestroyer.FACING);
			state = world.getBlockState(pos.offset(face));
			if (state.getMaterial() != Material.AIR ||
					state.getMaterial() != Material.LAVA ||
					state.getMaterial() != Material.WATER){
				exec();
			}
		}else {
			exec();
		}
	}

	@Override
	public int getTickTime() {
		return onTime;
	}

	private void exec(){
		BlockState state = world.getBlockState(pos);
		Direction face = state.get(BlockDestroyer.FACING);
		BlockPos pos2 = pos.offset(face);
		state = world.getBlockState(pos2);
		ItemStack drop = state.getBlock().getItem(this.world, pos2, state);
		if (!drop.isEmpty()){
			ModUtil.spawnItemStack(this.world, pos2.getX(),pos2.getY(),pos2.getZ(), drop, this.world.rand);
			world.destroyBlock(pos2, false);
		}
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
    public void read(CompoundNBT compound)
    {
		super.read(compound);
		mode=compound.getInt("mode");
		onTime = compound.getInt("ontime");
    }

	@Override
    public CompoundNBT write(CompoundNBT compound)
    {
		compound = super.write(compound);
		compound.putInt("mode",mode);
		compound.putInt("ontime",onTime);
        return compound;
    }

	@Override
	public int additionalgetField(int id) {
		int ret = 0;
		switch(id){
		case FIELD_TIME:
			ret = onTime;
			break;
		case FIELD_MODE:
			ret = this.mode;
			break;
		case FIELD_ISRUN:
			ret = BooleanUtils.toInteger(checkPowerOn());
			break;
		}
		return ret;
	}

	@Override
	public void additionalsetField(int id, int value) {
		switch(id){
		case FIELD_TIME:
			this.onTime = value;
			break;
		case FIELD_MODE:
			this.mode = value;
			break;
		}
	}

	@Override
	public int additionalFieldCount() {
		return 2;
	}

	public void updateValues(int mode2, float time) {
		if (mode != mode2) {
			mode = mode2;
			onTime = DEFAULT_TIME;
		}else {
			if (mode2 == MODE_TIME) {
				onTime = (int)(20.0F * time);
				if (onTime <= DEFAULT_TIME) {onTime = DEFAULT_TIME;}
			} else {
				onTime = DEFAULT_TIME;
			}
		}
	}
}
