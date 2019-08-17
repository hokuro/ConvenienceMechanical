package mod.cvbox.tileentity;

import java.util.UUID;

import org.apache.commons.lang3.BooleanUtils;

import com.mojang.authlib.GameProfile;

import mod.cvbox.config.ConfigValue;
import mod.cvbox.entity.EntityCore;
import mod.cvbox.inventory.ContainerAutoPlanting;
import mod.cvbox.inventory.SlotPlant;
import mod.cvbox.util.ModUtil;
import mod.cvbox.util.ModUtil.CompaierLevel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class TileEntityPlanter extends TileEntity  implements IInventory, ITickable, ISidedInventory, IPowerSwitchEntity{
	private final static int max = ContainerAutoPlanting.ROW_SLOT * ContainerAutoPlanting.COL_SLOT + 1;
	public static final String NAME = "autplanting";
	public static final int FIELD_POWER = 0;
	public static final int FIELD_DELIVER = 1;
	public static final int FIELD_BATTERY = 2;
	public static final int FIELD_BATTERYMAX = 3;
	public static final int FIELD_NEXT_X = 4;
	public static final int FIELD_NEXT_Z = 5;
	public static final int FILED_NEXTPOS = 6;
	private NonNullList<ItemStack> stacks;
	private String customName;
	private EntityPlayerDummy dummy;


	private boolean power;
	private boolean canDeliver;
	private int nextPos;
	private int[] next_x;
	private int[] next_z;

	// SideInventory
    private final int[] SLOTS_TOP;
    private int timeCnt;
	private int power_count;


	public TileEntityPlanter(){
		super(EntityCore.Planter);
		stacks = NonNullList.<ItemStack>withSize(max, ItemStack.EMPTY);
		power = false;
		SLOTS_TOP = new int[max];
		for (int i = 1; i < max; i++){
			SLOTS_TOP[i] = i;
		}
		makeArray();
		nextPos = 0;
		timeCnt = 0;
		power_count = 0;
	}

	public boolean canDeliver() {
		return canDeliver;
	}

	public void makeArray(){
		int size = (ConfigValue.planter.MaxDistance() * 2 + 1) * (ConfigValue.planter.MaxDistance() * 2 + 1) -1;
		next_x = new int[size];
		next_z = new int[size];

		int maxLp = ConfigValue.planter.MaxDistance() * 4 + 1;
		int len = 0;
		int x = 0;
		int z = 0;
		int idx = 0;
		for (int i = 0; i < maxLp; i ++){
			if (i%2 == 0 && i != maxLp-1){len++;}
			for (int j = 0; j < len; j++){
				switch(i%4){
				case 0: x++; break;
				case 1: z++;break;
				case 2: x--;break;
				case 3: z--;break;
				}


				next_x[idx] = x;
				next_z[idx] = z;
				idx++;
			}
		}
	}

	/////////////////////////////////////////////////////////////////////////////////
	// ITickable
	@Override
	public void tick() {
		if (!getPower().isEmpty() && power_count == 0){
			powerDown();
		}
		if (!world.isRemote){
			if (dummy == null){
				dummy = new EntityPlayerDummy(this.world, new GameProfile(new UUID(0,0), "dummy"));
			}
			if (checkPowerOn()){
				this.timeCnt++;
				if (timeCnt >= ConfigValue.planter.ExecTimeSpan()){
					for (int i = 0; i < ConfigValue.planter.OneTicPlant(); i++){
						if (Exec().isEmpty()){
							break;
						}
					}
					timeCnt = 0;
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


	public ItemStack Exec(){
		ItemStack seeds = nextItem();
		if (!seeds.isEmpty()){
			dummy.setHeldItem(EnumHand.MAIN_HAND, seeds);
			for (int y = -ConfigValue.planter.MaxHeight(); y <= ConfigValue.planter.MaxHeight(); y++){
				BlockPos plantPos = new BlockPos(this.pos.add(
						next_x[nextPos],
						y,
						next_z[nextPos]));
				IBlockState state = world.getBlockState(plantPos);
				// 空気ブロックの場合無視
				if (state.getMaterial() == Material.AIR){continue;}

				if (seeds.getItem() == Item.getItemFromBlock(Blocks.SUGAR_CANE)){
					// サトウキビ
					if (state.getBlock() != Blocks.SUGAR_CANE){
						// 見つけたブロックがサトウキビブロック以外なら設置
						seeds.onItemUse(new ItemUseContext(dummy, seeds, plantPos, EnumFacing.UP, 1.0f, 1.0f, 1.0f));
					}
				}else if (seeds.getItem() == Item.getItemFromBlock(Blocks.CACTUS)){
					// サボテン
					if (state.getBlock() != Blocks.CACTUS){
						// 見つけたブロックがサボテン以外なら設置
						seeds.onItemUse(new ItemUseContext(dummy, seeds, plantPos, EnumFacing.UP, 1.0f, 1.0f, 1.0f));
					}
				}else if (seeds.getItem() == Items.COCOA_BEANS){
					// カカオ
					seeds.onItemUse(new ItemUseContext(dummy, seeds, plantPos, EnumFacing.NORTH, 1.0f, 1.0f, 1.0f));
					if (seeds.getCount() != 0 && !seeds.isEmpty())
						seeds.onItemUse(new ItemUseContext(dummy, seeds, plantPos, EnumFacing.WEST, 1.0f, 1.0f, 1.0f));
					if (seeds.getCount() != 0 && !seeds.isEmpty())
						seeds.onItemUse(new ItemUseContext(dummy, seeds, plantPos, EnumFacing.EAST, 1.0f, 1.0f, 1.0f));
					if (seeds.getCount() != 0 && !seeds.isEmpty())
						seeds.onItemUse(new ItemUseContext(dummy, seeds, plantPos, EnumFacing.SOUTH, 1.0f, 1.0f, 1.0f));
				}else if (seeds.getItem() == Item.getItemFromBlock(Blocks.CHORUS_FLOWER)){
					// コーラルフラワー
					if (state.getBlock() == Blocks.END_STONE){
						seeds.onItemUse(new ItemUseContext(dummy, seeds, plantPos, EnumFacing.UP, 1.0f, 1.0f, 1.0f));
					}
				}else{
					if ( ConfigValue.planter.CanFarming()){
						// 耕せる土地なら強制的に耕す
						Block blk = state.getBlock();
						if ((Blocks.DIRT == blk || Blocks.GRASS_BLOCK == blk) && world.getBlockState(plantPos.offset(EnumFacing.UP)).getMaterial() == Material.AIR){
							world.setBlockState(plantPos,Blocks.FARMLAND.getDefaultState().with(BlockFarmland.MOISTURE, 7));
						}else if(Blocks.FARMLAND == blk){
							if (state.get(BlockFarmland.MOISTURE) < 7){
								world.setBlockState(plantPos, state.with(BlockFarmland.MOISTURE, 7));
							}
						}
					}
					// その他
					seeds.onItemUse(new ItemUseContext(dummy, seeds, plantPos, EnumFacing.UP, 1.0f, 1.0f, 1.0f));
				}
				if (seeds.getCount() <= 0 || seeds.isEmpty()){
					break;
				}
			}
			this.nextPos++;
			if (nextPos >= next_x.length){
				nextPos = 0;
			}
		}
		return seeds;
	}

	private ItemStack nextItem(){
		ItemStack ret = ItemStack.EMPTY;
		for (int i = 1; i < max; i++){
			if (!this.getStackInSlot(i).isEmpty()){
				ret = this.getStackInSlot(i);
			}
		}
		return ret;
	}

	private boolean isFullContainer(){
		boolean ret = false;
		for (ItemStack item : this.stacks){
			if (item.isEmpty()){
				ret = true;
				break;
			}
			if (item.getCount() >= item.getItem().getMaxStackSize()){
				ret = true;
				break;
			}
		}
		return ret;
	}

	@Override
	public int getSizeInventory(){
		return max;
	}

	@Override
	public void read(NBTTagCompound nbtTagCompound){
		super.read(nbtTagCompound);
		power = nbtTagCompound.getBoolean("isrun");
		nextPos = nbtTagCompound.getInt("next");
		canDeliver = nbtTagCompound.getBoolean("deliver");
		power_count=nbtTagCompound.getInt("power_count");

	    this.stacks = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
	    ItemStackHelper.loadAllItems(nbtTagCompound, this.stacks);
//		NBTTagList itemsTagList = nbtTagCompound.getTagList("Items",10);
//		for (int tagCounter = 0; tagCounter < itemsTagList.tagCount(); tagCounter++){
//			NBTTagCompound itemTagCompound = itemsTagList.getCompoundTagAt(tagCounter);
//
//			byte slotIndex =itemTagCompound.getByte("Slot");
//			if ((slotIndex >= 0) && (slotIndex < max)){
//				stacks.set(slotIndex, new ItemStack(itemTagCompound));
//			}
//		}
	}

	@Override
	public NBTTagCompound write(NBTTagCompound nbtTagCompound){
		super.write(nbtTagCompound);
		nbtTagCompound.setBoolean("isrun", power);
		nbtTagCompound.setInt("next", nextPos);
		nbtTagCompound.setBoolean("deliver",canDeliver);
		nbtTagCompound.setInt("power_count",power_count);

		NBTTagList itemsTagList = new NBTTagList();
		for (int slotIndex = 0; slotIndex < max; slotIndex++){
			if (!this.stacks.get(slotIndex).isEmpty()){
				NBTTagCompound itemTagCompound = new NBTTagCompound();

				itemTagCompound.setByte("Slot",(byte)slotIndex);
				this.stacks.get(slotIndex).write(itemTagCompound);
				itemsTagList.add(itemTagCompound);
			}
		}
		nbtTagCompound.setTag("Items",itemsTagList);
		return nbtTagCompound;
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
	public ItemStack getStackInSlot(int slotIndex){
		return this.stacks.get(slotIndex);
	}

	@Override
	public ItemStack decrStackSize(int slotIndex, int splitStackSize){
		 return ItemStackHelper.getAndSplit(this.stacks, slotIndex, splitStackSize);
	}

	@Override
	public void setInventorySlotContents(int idx, ItemStack stack){
		this.stacks.set(idx, stack);
	}

	@Override
	public int getInventoryStackLimit(){
		return 64;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer entityplayer){
		if (this.world.getTileEntity(this.pos) != this) {
			return false;
		}
		return entityplayer.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64.0D;
	}


	@Override
	public ITextComponent getName() {
		return this.hasCustomName() ? new TextComponentTranslation(this.customName) : new TextComponentTranslation("container.TileEntityAutoPlanting");
	}

	@Override
	public boolean hasCustomName() {
		return this.customName != null && this.customName.length() > 0;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(this.stacks, index);
	}

	@Override
	public void openInventory(EntityPlayer player) {
	}

	@Override
	public void closeInventory(EntityPlayer player) {
	}

	@Override
	public int getField(int id) {
		int ret = 0;
		switch(id){
		case FIELD_POWER:
			ret = BooleanUtils.toInteger(power);
			break;
		case FIELD_DELIVER:
			ret = BooleanUtils.toInteger(canDeliver);
			break;

		case FIELD_BATTERY:
			ret = getPower().getDamage();
			break;
		case FIELD_BATTERYMAX:
			ret = getPower().getMaxDamage();
			break;
		case FIELD_NEXT_X:
			ret = next_x[nextPos];
			break;

		case FIELD_NEXT_Z:
			ret = next_z[nextPos];
			break;
		case FILED_NEXTPOS:
			ret = nextPos;
			break;
		default:

		}
		return ret;
	}

	@Override
	public void setField(int id, int value) {
		switch(id){
		case FIELD_POWER:
			power = BooleanUtils.toBoolean(value);
			this.nextPos = 0;
			break;
		case FIELD_DELIVER:
			canDeliver = BooleanUtils.toBoolean(value);
			break;

		case FIELD_BATTERY:
			getPower().setDamage(value);
			break;
		case FILED_NEXTPOS:
			nextPos = value;
			break;
		default:

		}
	}

	@Override
	public int getFieldCount() {
		return 7;
	}

	@Override
	public void clear() {
		this.stacks.clear();
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return SlotPlant.checkExtends(stack);
	}

	@Override
	public ITextComponent getDisplayName() {
        return (this.hasCustomName() ? this.getName() : this.getName());
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

//	public net.minecraftforge.items.IItemHandler itemHandler = new net.minecraftforge.items.wrapper.InvWrapper(this);
//	@Override
//    public boolean hasCapability(net.minecraftforge.common.capabilities.Capability<?> capability, net.minecraft.util.EnumFacing facing)
//    {
//        return capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
//    }
//
//    @Override
//    public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, net.minecraft.util.EnumFacing facing)
//    {
//        if (capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
//        {
//            return (T) itemHandler;
//        }
//        return super.getCapability(capability, facing);
//    }






	/////////////////////////////////////////////////////////////////////////////////
	// ISideInventory
	@Override
	public int[] getSlotsForFace(EnumFacing side) {
        if (side != EnumFacing.DOWN)
        {
            return SLOTS_TOP;
        }
        return null;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		if (index != 0){
			return this.isItemValidForSlot(index, itemStackIn);
		}
		return false;
	}


	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return false;
	}


	public boolean Deliver(ItemStack stack) {
		boolean ret = false;
		if (!stack.isEmpty()){
			if (SlotPlant.checkExtends(stack)){
				ItemStack itemIn = stack.copy();
				itemIn.setCount(1);
				for (int i = 1; i < this.stacks.size(); i++){
					ItemStack item2 = this.stacks.get(i);
					if (item2.isEmpty()){
						this.stacks.set(i,itemIn);
						ret = true;
						break;
					}else if (ModUtil.compareItemStacks(stack,item2,CompaierLevel.LEVEL_EQUAL_ITEM) && item2.getCount() < item2.getMaxStackSize()){
						item2.grow(1);
						ret = true;
						break;
					}
				}
			}
		}
		return ret;
	}

	@Override
	public void setPower(boolean value) {
		this.power = value;
	}

	public ItemStack getPower(){
		return this.stacks.get(0);
	}

	public void reset() {
		nextPos = 0;
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