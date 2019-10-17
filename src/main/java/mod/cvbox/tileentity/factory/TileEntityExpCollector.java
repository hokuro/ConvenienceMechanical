package mod.cvbox.tileentity.factory;

import java.util.List;

import mod.cvbox.config.ConfigValue;
import mod.cvbox.entity.EntityCore;
import mod.cvbox.item.ItemBattery;
import mod.cvbox.tileentity.ab.TileEntityPowerBase;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;

public class TileEntityExpCollector extends TileEntityPowerBase  implements ISidedInventory {
	public static final String NAME = "expcollector";
	public static final int FIELD_EXPVALUE = 0 + FIELD_OFFSET;
	public static final int EXP_MAX = 2000000000;	// 最大20億まで
	public static final int SLOT_ITEM = 1;
	private int expvalue;
	private int[] SLOT_SIDE = new int[]{SLOT_BATTERY, SLOT_ITEM};
	private static final int WORK_TIME = 3 * 60 * 20; // 3分に1回起動

	public TileEntityExpCollector(){
		super(EntityCore.ExpCollector,2);
		expvalue = 0;
	}

	@Override
	public void tick() {
		super.tick();
		if (!world.isRemote) {
			mending();
		}
	}

	@Override
	protected boolean canWork() {
		return (expvalue < EXP_MAX);
	}

	@Override
	protected void onWork() {
		collect_exp();
	}

	@Override
	public int getTickTime() {
		return WORK_TIME;
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

    private void collect_exp() {
    	int areaSize = ConfigValue.expcollector.AreaSize();
    	List<ExperienceOrbEntity> list = this.world.getEntitiesWithinAABB(ExperienceOrbEntity.class,
    			new AxisAlignedBB(this.pos.getX()-areaSize, this.pos.getY()-areaSize, this.pos.getZ()-areaSize,
    					this.pos.getX()+areaSize, this.pos.getY()+areaSize, this.pos.getZ()+areaSize),
    			(entity)->{return entity != null && entity.isAlive();});

        for (ExperienceOrbEntity ent : list){
        	if (Integer.MAX_VALUE - ent.getXpValue() >= expvalue){
        		//　タンク内よゆーあり
        		expvalue += ent.getXpValue();
        		ent.remove();
        	}else if (Integer.MAX_VALUE != expvalue){
        		// ぴったんこまで入れる
        		expvalue = Integer.MAX_VALUE;
        		ent.remove();
        		break;
            }else{
            	// よゆーないからもう探さない
            	break;
            }
        }
    }

    private void mending(){
    	if (expvalue >= 10) {
    		ItemStack item = stacks.get(1);
    		if (item.isDamaged()){
    			item.setDamage(item.getDamage()-1);
    			this.expvalue -= 10;
    		}
    	}
    }

	@Override
    public void read(CompoundNBT compound) {
		super.read(compound);
        expvalue = compound.getInt("exp");
    }

	@Override
    public CompoundNBT write(CompoundNBT compound) {
		compound = super.write(compound);
		compound.putInt("exp", expvalue);

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
		if (index == SLOT_ITEM &&  stack.getDamage() == 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		boolean ret = false;
		if (index == SLOT_BATTERY) {
			ret = (stack.getItem() instanceof ItemBattery);
		}else if (index == SLOT_ITEM) {
			ret = (stack.getDamage() > 0);
		}
		return ret;
	}


	@Override
	public int additionalgetField(int id) {
		int ret = 0;
		switch(id){
		case FIELD_EXPVALUE:
			ret = expvalue;
			break;
		}
		return ret;
	}

	@Override
	public void additionalsetField(int id, int value) {
		switch(id){
		case FIELD_EXPVALUE:
			expvalue = value;
			break;
		}
	}

	@Override
	public int additionalFieldCount() {
		return 1;
	}
}
