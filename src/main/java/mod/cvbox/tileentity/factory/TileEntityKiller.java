package mod.cvbox.tileentity.factory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.mojang.authlib.GameProfile;

import mod.cvbox.entity.EntityCore;
import mod.cvbox.entity.EntityPlayerDummy;
import mod.cvbox.item.ItemBattery;
import mod.cvbox.tileentity.ab.TileEntityPowerBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.registry.Registry;

public class TileEntityKiller extends TileEntityPowerBase  implements ISidedInventory {
	public static final String NAME = "killer";
	public static final int FIELD_AREASIZEX = 0 + FIELD_OFFSET;
	public static final int FIELD_AREASIZEY = 1 + FIELD_OFFSET;
	public static final int FIELD_AREASIZEZ = 2 + FIELD_OFFSET;

	public static final int SLOT_SOWD = 1;
	private PlayerEntity playerDummy;
	private int areaSizeX;
	private int areaSizeY;
	private int areaSizeZ;
	private final List<String> targets;


	private int[] SLOT_SIDE = new int[]{SLOT_SOWD};
	private static final int WORK_TIME = 10; // 0.5秒に1回起動
	private final String allTarget;

	public TileEntityKiller(){
		super(EntityCore.Killer, 2);
		playerDummy = null;
		areaSizeX = 0;
		areaSizeY = 0;
		areaSizeZ = 0;
		targets = new ArrayList<String>();
		allTarget = EntityType.ARMOR_STAND.getRegistryName().toString();
	}

	@Override
	public void tick() {
		if (!world.isRemote) {
			if (playerDummy == null){
				playerDummy = new EntityPlayerDummy(this.world, new GameProfile(new UUID(0,0), "dummy"));
				playerDummy.getAttributes().applyAttributeModifiers(stacks.get(1).getAttributeModifiers(EquipmentSlotType.MAINHAND));
				playerDummy.setHeldItem(Hand.MAIN_HAND, this.stacks.get(1));
			}
		}
		super.tick();
	}

	@Override
	protected boolean canWork() {
		return true;
	}

	@Override
	protected void onWork() {

    	List<LivingEntity> list = this.world.getEntitiesWithinAABB(LivingEntity.class,
    			new AxisAlignedBB(this.pos.getX()-this.areaSizeX, this.pos.getY()-this.areaSizeY, this.pos.getZ()-this.areaSizeZ,
    					this.pos.getX()+this.areaSizeX, this.pos.getY()+this.areaSizeY, this.pos.getZ()+this.areaSizeZ),
    			(entity)->{return entity != null && entity.isAlive();});
        for (Entity ent : list){
        	String name = ent.getEntityString();
        	if (targets.contains(name) || (targets.contains(allTarget) && !(ent instanceof PlayerEntity))){
        		playerDummy.attackTargetEntityWithCurrentItem(ent);
        	}
        }
	}

	@Override
	public int getTickTime() {
		return WORK_TIME;
	}

	@Override
    public void read(CompoundNBT compound) {
		super.read(compound);
        areaSizeX = compound.getInt("sizex");
        areaSizeY = compound.getInt("sizey");
        areaSizeZ = compound.getInt("sizez");
        targets.clear();
		ListNBT targetTagList = compound.getList("targets",10);
		for (int tagCounter = 0; tagCounter < targetTagList.size(); tagCounter++){
			CompoundNBT tag = targetTagList.getCompound(tagCounter);
			String name = tag.getString("name");
				targets.add(name);
		}
		cleanTargetlist();
    }

	private void cleanTargetlist() {
		// 現在存在しないターゲットをリストから削除する
		List<String> work = new ArrayList<String>();
		Registry.ENTITY_TYPE.forEach((entType)->{
			work.add(entType.getRegistryName().toString());
		});
		Iterator<String> it = targets.iterator();
		while(it.hasNext()) {
			String name = it.next();
			if (!work.contains(name)) {
				it.remove();
			}
		}
	}

	@Override
    public CompoundNBT write(CompoundNBT compound) {
		compound = super.write(compound);
		compound.putInt("sizex", areaSizeX);
		compound.putInt("sizey", areaSizeY);
		compound.putInt("sizez", areaSizeZ);
		ListNBT targetTagList = new ListNBT();
		for (String name : targets){
			CompoundNBT targetName = new CompoundNBT();
			targetName.putString("name", name);
			targetTagList.add(targetName);
		}
		compound.put("targets",targetTagList);

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
	public void setInventorySlotContents(int index, ItemStack stack) {
		this.stacks.set(index, stack);
		if (this.playerDummy != null){
			playerDummy.getAttributes().applyAttributeModifiers(stack.getAttributeModifiers(EquipmentSlotType.MAINHAND));
			playerDummy.setHeldItem(Hand.MAIN_HAND, this.stacks.get(1));
		}
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		if (index == SLOT_BATTERY) {
			return (stack.getItem() instanceof ItemBattery);
		}else if (index == SLOT_SOWD){
			return (stack.getItem() instanceof SwordItem);
		}
		return false;
	}

	@Override
	public int additionalgetField(int id) {
		int ret = 0;
		switch(id){
		case FIELD_AREASIZEX:
			ret = areaSizeX;
			break;
		case FIELD_AREASIZEY:
			ret = areaSizeY;
			break;
		case FIELD_AREASIZEZ:
			ret = areaSizeZ;
			break;
		}
		return ret;
	}

	@Override
	public void additionalsetField(int id, int value) {
		switch(id){
		case FIELD_AREASIZEX:
			areaSizeX = value;
			break;
		case FIELD_AREASIZEY:
			areaSizeY = value;
			break;
		case FIELD_AREASIZEZ:
			areaSizeZ = value;
			break;
		}
	}

	@Override
	public int additionalFieldCount() {
		return 3;
	}

	public boolean ContainTarget(String name){
		return targets!=null?targets.contains(name):false;
	}

	public boolean ContainTarget(ResourceLocation name){
		return ContainTarget(name.toString());
	}

	public void updateTarget(String entityName) {
		if (ContainTarget(entityName)){
			targets.remove(entityName);
		}else{
			targets.add(entityName);
		}
	}
}
