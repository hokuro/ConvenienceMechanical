package mod.cvbox.tileentity.factory;

import mod.cvbox.entity.EntityCore;
import mod.cvbox.item.ItemBattery;
import mod.cvbox.item.ItemCore;
import mod.cvbox.item.ItemMobGenom;
import mod.cvbox.tileentity.ab.TileEntityPowerBase;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;

public class TileEntityLivingCreator extends TileEntityPowerBase  implements ISidedInventory {
	public static final String NAME = "livingcreator";

	public static final int SLOT_U = 1;
	public static final int SLOT_B = 2;

	private int[] SLOT_SIDE = new int[]{SLOT_BATTERY, SLOT_U, SLOT_B};

	public static final int WORK_TIME = 100;	// 5秒に1回

	public TileEntityLivingCreator(){
		super(EntityCore.LivingCreater, 3);
	}

	@Override
	public void tick() {
		super.tick();
	}

	@Override
	protected boolean canWork() {
		boolean ret = false;
		ItemStack itemF = stacks.get(SLOT_U);
		ItemStack itemC = stacks.get(SLOT_B);
		if ((!itemF.isEmpty()) &&  (!itemC.isEmpty())) {
			if ((itemF.getItem() == ItemCore.item_mobgenom && itemC.getItem() == ItemCore.item_ipscell) ||
					(itemF.getItem() == ItemCore.item_ipscell && itemC.getItem() == ItemCore.item_mobgenom)) {
				ret = true;
			}
		}
		return ret;
	}

	@Override
	protected void onWork() {
		Vec3d spawnPos = new Vec3d(this.pos.getX() + 0.5D, this.pos.getY(), this.pos.getZ() + 0.5D);
		ItemStack genom;
		ItemStack cell;
		if (stacks.get(SLOT_U).getItem() == ItemCore.item_mobgenom) {
			// ゲノムが上にあるなら上にスポーン
			genom = stacks.get(SLOT_U);
			cell = stacks.get(SLOT_B);
			spawnPos.add(0,5.0D,0);
		}else {
			// ゲノムが下にあるなら下にスポーン
			genom = stacks.get(SLOT_B);
			cell = stacks.get(SLOT_U);
			spawnPos.add(0, -3.0D, 0);
		}

		String genomName = ItemMobGenom.getMob(genom);
		Entity living = Registry.ENTITY_TYPE.getOrDefault(new ResourceLocation(genomName)).create(world);
		living.setPosition(spawnPos.x, spawnPos.y, spawnPos.z);
		if (world.addEntity(living)) {
			cell.shrink(1);
		}
	}

	@Override
	public int getTickTime() {
		return WORK_TIME;
	}

	@Override
    public void read(CompoundNBT compound) {
		super.read(compound);
	}

	@Override
    public CompoundNBT write(CompoundNBT compound) {
		compound = super.write(compound);
        return compound;
    }

	@Override
	public int[] getSlotsForFace(Direction side) {
		return SLOT_SIDE;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, Direction direction) {
		boolean ret = false;
		if (index == SLOT_BATTERY) {
			ret = (itemStackIn.getItem() instanceof ItemBattery);
		}else if (index > 0 && index < stacks.size()) {
			if (itemStackIn.getItem() == ItemCore.item_ipscell) {
				if (index == SLOT_B && stacks.get(SLOT_U).getItem() == ItemCore.item_mobgenom){
					ret = true;
				}else if (index == SLOT_U && stacks.get(SLOT_B).getItem() == ItemCore.item_mobgenom){
					ret = true;
				}
			}else if (itemStackIn.getItem() == ItemCore.item_mobgenom) {
				if (direction == Direction.UP && stacks.get(SLOT_U).isEmpty() && stacks.get(SLOT_B).getItem() != ItemCore.item_mobgenom) {
					ret = true;
				}else if (direction == Direction.DOWN && stacks.get(SLOT_B).isEmpty() && stacks.get(SLOT_U).getItem() != ItemCore.item_mobgenom) {
					ret = true;
				}
			}
		}
		return ret;
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
		}else if(index == SLOT_U || index == SLOT_B) {
			ret = ((stack.getItem() == ItemCore.item_mobgenom) || (stack.getItem() == ItemCore.item_ipscell));
		}
		return ret;
	}
}
