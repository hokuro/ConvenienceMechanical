package mod.cvbox.tileentity;

import org.apache.commons.lang3.BooleanUtils;

import mod.cvbox.entity.EntityCore;
import mod.cvbox.item.ItemCore;
import mod.cvbox.util.ModUtil;
import mod.cvbox.util.ModUtil.CompaierLevel;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.Particles;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class TileEntityLiquidMaker extends TileEntity implements IInventory, ITickable, IPowerSwitchEntity{
	public static final String NAME = "liquidmaker";
	public static final int FIELD_POWER = 0;
	public static final int FIELD_COUNT = 1;
	public static final int FIELD_BATTERY = 2;
	public static final int FIELD_BATTERYMAX = 3;
	public static final int FIELD_MODE = 4;

	public final int COUNT_MAX = 100;
	public static final int MODE_NONE = 0;
	public static final int MODE_ICE = 1;
	public static final int MODE_LAVA = 2;

	private NonNullList<ItemStack> stacks = NonNullList.<ItemStack>withSize(2, ItemStack.EMPTY);
	private boolean power;
	private int count;
	private int power_count;

	public TileEntityLiquidMaker(){
		super(EntityCore.LiquidMaker);
		power = false;
		count = 0;
		power_count = 0;
	}

	@Override
	public void tick() {
		if (!getPower().isEmpty() && power_count == 0){
			powerDown();
		}
		if (checkPowerOn()){
			count++;
			if (world.isRemote){
				if (Math.random() > 0.7){
					ModUtil.spawnParticles(this.world, this.pos, RedstoneParticleData.REDSTONE_DUST);
				}
			}
			if (count >= COUNT_MAX){
				count= 0;
				exec();
			}
			power_count++;
			if (power_count > 20){
				power_count = 0;
			}
		}else{
			count= 0;
			if (!getPower().isEmpty()){
				power_count++;
				if (power_count > 2000){
					power_count = 0;
				}
			}else{
				power_count = 0;
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

	private BlockPos[] offset = new BlockPos[]{
		new BlockPos(0,-1,0),
		new BlockPos(1,-1,0),
		new BlockPos(-1,-1,0),
		new BlockPos(0,-1,1),
		new BlockPos(0,-1,-1),
		new BlockPos(1,-1,1),
		new BlockPos(-1,-1,-1),
		new BlockPos(1,-1,-1),
		new BlockPos(-1,-1,1)
	};
    private void exec()
    {
    	int mode = getMode();
    	if (mode == MODE_NONE){return;}
		for (BlockPos p : offset){
			IBlockState state = this.world.getBlockState(this.pos.add(p.getX(),p.getY(),p.getZ()));
			if (mode == MODE_ICE){
				if (state.getMaterial() == Material.WATER){
					if (!world.isRemote){
						this.world.setBlockState(this.pos.add(p.getX(),p.getY(),p.getZ()), Blocks.ICE.getDefaultState());
					}
				}
			}else{
				if (state.getMaterial() == Material.CORAL ||
					state.getMaterial() == Material.GOURD ||
					state.getMaterial() == Material.GLASS ||
					state.getMaterial() == Material.ROCK ||
					state.getMaterial() == Material.SAND){
					if (!world.isRemote){
						this.world.setBlockState(this.pos.add(p.getX(),p.getY(),p.getZ()),  Blocks.LAVA.getDefaultState());
					}
			        double d0 = (double)pos.getX() + p.getX();
			        double d1 = (double)pos.getY() + p.getY();
			        double d2 = (double)pos.getZ() + p.getZ();
			        this.world.playSound(d0,d1,d2,
							SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS,
							0.5F, 2.6F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.8F, true);
			        for (int i = 0; i < 8; ++i)
			        {
			        	this.world.spawnParticle(Particles.LARGE_SMOKE, d0 + Math.random(), d1 + 1.2D, d2 + Math.random(), 0.0D, 0.0D, 0.0D);
			        }
				}else if (state.getBlock() != Blocks.LAVA && state.getMaterial() != Material.AIR){
					if (!world.isRemote){
						this.world.setBlockState(this.pos.add(p.getX(),p.getY(),p.getZ()), Blocks.AIR.getDefaultState());
					}
					double d0 = (double)pos.getX() + p.getX();
			        double d1 = (double)pos.getY() + p.getY();
			        double d2 = (double)pos.getZ() + p.getZ();
			        this.world.playSound(d0,d1,d2,
							SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS,
							0.5F, 2.6F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.8F, true);
			        for (int i = 0; i < 8; ++i)
			        {
			        	this.world.spawnParticle(Particles.LARGE_SMOKE, d0 + Math.random(), d1 + 1.2D, d2 + Math.random(), 0.0D, 0.0D, 0.0D);
			        }
				}
			}
		}
    }

	@Override
    public void read(NBTTagCompound compound)
    {
		super.read(compound);
		power = compound.getBoolean("power");
		power_count=compound.getInt("power_count");
		count=compound.getInt("count");

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
		compound.setBoolean("power", power);
		compound.setInt("power_count",power_count);
		compound.setInt("count",count);

		NBTTagList itemsTagList = new NBTTagList();
		for (int slotIndex = 0; slotIndex < stacks.size(); slotIndex++){
			NBTTagCompound itemTagCompound = new NBTTagCompound();

			itemTagCompound.setByte("Slot",(byte)slotIndex);
			if (stacks.size()>slotIndex){
				this.stacks.get(slotIndex).write(itemTagCompound);
			}
			itemsTagList.add(itemTagCompound);
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
		return new TextComponentTranslation("tileentity.compresser");
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}



	@Override
	public int getSizeInventory() {
		return this.stacks.size();
	}

	@Override
	public boolean isEmpty() {
        for (ItemStack itemstack : this.stacks)
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }

        return true;
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
	public int getInventoryStackLimit() {
		return 1;
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
		case FIELD_POWER:
			ret = BooleanUtils.toInteger(power);
			break;
		case FIELD_BATTERY:
			ret = getPower().getDamage();
			break;
		case FIELD_BATTERYMAX:
			ret = getPower().getMaxDamage();
			break;
		case FIELD_COUNT:
			ret = this.count;
			break;
		case FIELD_MODE:
			ret = getMode();
			break;
		}
		return ret;
	}

	@Override
	public void setField(int id, int value) {
		switch(id){
		case FIELD_POWER:
			power = BooleanUtils.toBoolean(value);
			break;
		case FIELD_COUNT:
			this.count = value;
			break;
		case FIELD_BATTERY:
			getPower().setDamage(value);
			break;
		}
	}

	@Override
	public int getFieldCount() {
		return 5;
	}

	@Override
	public void clear() {
		stacks.clear();
	}

	@Override
	public void setPower(boolean value) {
		this.power = value;
	}

	public static final ItemStack[] ice = new ItemStack[]{
			new ItemStack(Blocks.ICE),
			new ItemStack(Blocks.PACKED_ICE),
			new ItemStack(Blocks.BLUE_ICE),
			new ItemStack(Blocks.FROSTED_ICE)
	};
	public static final ItemStack[] lava = new ItemStack[]{
			new ItemStack(Items.LAVA_BUCKET),
			new ItemStack(ItemCore.item_lavaball)
	};
	public int getMode(){
		int ret = MODE_NONE;
		ItemStack stack = getModeItem();
		if (!stack.isEmpty()){
			for (ItemStack st : ice){
				if (ModUtil.compareItemStacks(stack, st, CompaierLevel.LEVEL_EQUAL_ITEM)){
					ret = MODE_ICE;
					break;
				}
			}
			if (ret == MODE_NONE){
				for (ItemStack st : lava){
					if (ModUtil.compareItemStacks(stack, st, CompaierLevel.LEVEL_EQUAL_ITEM)){
						ret = MODE_LAVA;
						break;
					}
				}
			}
		}
		return ret;
	}

	public ItemStack getPower(){
		return this.stacks.get(0);
	}

	private ItemStack getModeItem(){
		return this.stacks.get(1);
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
