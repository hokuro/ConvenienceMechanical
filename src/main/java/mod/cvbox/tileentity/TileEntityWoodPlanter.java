package mod.cvbox.tileentity;

import java.util.UUID;

import org.apache.commons.lang3.BooleanUtils;

import com.mojang.authlib.GameProfile;

import mod.cvbox.config.ConfigValue;
import mod.cvbox.inventory.ContainerAutoPlanting;
import mod.cvbox.util.ModUtil;
import mod.cvbox.util.ModUtil.CompaierLevel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class TileEntityWoodPlanter extends TileEntity  implements IInventory, ITickable, ISidedInventory {
	private final static int max = ContainerAutoPlanting.ROW_SLOT * ContainerAutoPlanting.COL_SLOT;
	public static final String NAME = "WoodPlanting";
	private final NonNullList<ItemStack> inventoryContents;
	private String customName;
	private EntityPlayerDummy dummy;


	private boolean isRun;
	private boolean canDeliver;
	private int nextPos;
	private int[] next_x;
	private int[] next_z;

	// SideInventory
    private final int[] SLOTS_TOP;
    private int timeCnt;


	public TileEntityWoodPlanter(){
		inventoryContents = NonNullList.<ItemStack>withSize(max, ItemStack.EMPTY);
		isRun = false;
		SLOTS_TOP = new int[max];
		for (int i = 0; i < max; i++){
			SLOTS_TOP[i] = i;
		}
		makeArray();
		nextPos = 0;
		timeCnt = 0;
	}

	public boolean canDeliver() {
		return canDeliver;
	}

	public void makeArray(){
		int size = (ConfigValue.WoodPlanter.MaxDistance * 2 + 1) * (ConfigValue.WoodPlanter.MaxDistance * 2 + 1) -1;
		next_x = new int[size];
		next_z = new int[size];

		int maxLp = ConfigValue.WoodPlanter.MaxDistance * 4 + 1;
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
	public void update() {
		if (!world.isRemote){
			if (dummy == null){
				dummy = new EntityPlayerDummy(this.world, new GameProfile(new UUID(0,0), "dummy"));
			}
			if (this.isRun){
				this.timeCnt++;
				if (timeCnt >= ConfigValue.WoodPlanter.ExecTimeSpan){
					for (int i = 0; i < ConfigValue.WoodPlanter.OneTicPlant; i++){
						if (Exec().isEmpty()){
							break;
						}
					}
					timeCnt = 0;
				}
			}
		}else{
			if (isRun){
                double d3 = (double)pos.getX() + world.rand.nextDouble() * 0.10000000149011612D;
                double d8 = (double)pos.getY() + world.rand.nextDouble();
                double d13 = (double)pos.getZ() + world.rand.nextDouble();
				world.spawnParticle(EnumParticleTypes.REDSTONE, d3, d8, d13, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	public ItemStack Exec(){
		ItemStack seeds = nextItem();
		if (!seeds.isEmpty()){
			dummy.setHeldItem(EnumHand.MAIN_HAND, seeds);
			for (int y = -ConfigValue.WoodPlanter.MaxHeight; y <= ConfigValue.WoodPlanter.MaxHeight; y++){
				BlockPos plantPos = new BlockPos(this.pos.add(
						next_x[nextPos],
						y,
						next_z[nextPos]));
				IBlockState state = world.getBlockState(plantPos);
				// 空気ブロックの場合無視
				if (state.getMaterial() == Material.AIR){continue;}

				// 植樹
				seeds.onItemUse(dummy, world, plantPos, EnumHand.MAIN_HAND, EnumFacing.UP, 1.0f, 1.0f, 1.0f);
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
		for (int i = 0; i < max; i++){
			if (!this.getStackInSlot(i).isEmpty()){
				ret = this.getStackInSlot(i);
			}
		}
		return ret;
	}

	private boolean isFullContainer(){
		boolean ret = false;
		for (ItemStack item : this.inventoryContents){
			if (item.isEmpty()){
				ret = true;
				break;
			}
			if (item.getCount() >= item.getItem().getItemStackLimit()){
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
	public void readFromNBT(NBTTagCompound nbtTagCompound){
		super.readFromNBT(nbtTagCompound);
		isRun = nbtTagCompound.getBoolean("isrun");
		nextPos = nbtTagCompound.getInteger("next");
		canDeliver = nbtTagCompound.getBoolean("deliver");
		NBTTagList itemsTagList = nbtTagCompound.getTagList("Items",10);

		for (int tagCounter = 0; tagCounter < itemsTagList.tagCount(); tagCounter++){
			NBTTagCompound itemTagCompound = itemsTagList.getCompoundTagAt(tagCounter);

			byte slotIndex =itemTagCompound.getByte("Slot");
			if ((slotIndex >= 0) && (slotIndex < max)){
				inventoryContents.set(slotIndex, new ItemStack(itemTagCompound));
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound){
		super.writeToNBT(nbtTagCompound);
		nbtTagCompound.setBoolean("isrun", isRun);
		nbtTagCompound.setInteger("next", nextPos);
		nbtTagCompound.setBoolean("deliver",canDeliver);

		NBTTagList itemsTagList = new NBTTagList();
		for (int slotIndex = 0; slotIndex < max; slotIndex++){
			if (!this.inventoryContents.get(slotIndex).isEmpty()){
				NBTTagCompound itemTagCompound = new NBTTagCompound();

				itemTagCompound.setByte("Slot",(byte)slotIndex);
				this.inventoryContents.get(slotIndex).writeToNBT(itemTagCompound);
				itemsTagList.appendTag(itemTagCompound);
			}
		}
		nbtTagCompound.setTag("Items",itemsTagList);
		return nbtTagCompound;
	}

	@Override
    public NBTTagCompound getUpdateTag()
    {
        NBTTagCompound cp = super.getUpdateTag();
        return this.writeToNBT(cp);
    }

	@Override
    public void handleUpdateTag(NBTTagCompound tag)
    {
		super.handleUpdateTag(tag);
		this.readFromNBT(tag);
    }

	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
    {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        return new SPacketUpdateTileEntity(this.pos, 1,  this.writeToNBT(nbtTagCompound));
    }

	@Override
	public ItemStack getStackInSlot(int slotIndex){
		return this.inventoryContents.get(slotIndex);
	}

	@Override
	public ItemStack decrStackSize(int slotIndex, int splitStackSize){
		 return ItemStackHelper.getAndSplit(this.inventoryContents, slotIndex, splitStackSize);
	}

	@Override
	public void setInventorySlotContents(int idx, ItemStack stack){
		this.inventoryContents.set(idx, stack);
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
	public String getName() {
		return this.hasCustomName() ? this.customName : "container.TileEntityAutoPlanting";
	}

	@Override
	public boolean hasCustomName() {
		return this.customName != null && this.customName.length() > 0;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(this.inventoryContents, index);
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
		case 0:
			ret = BooleanUtils.toInteger(isRun);
			break;
		case 1:
			ret = BooleanUtils.toInteger(canDeliver);
			break;
		default:

		}
		return ret;
	}

	@Override
	public void setField(int id, int value) {
		switch(id){
		case 0:
			isRun = BooleanUtils.toBoolean(value);
			this.nextPos = 0;
			break;
		case 1:
			canDeliver = BooleanUtils.toBoolean(value);
			break;
		default:

		}
	}

	@Override
	public int getFieldCount() {
		return 2;
	}

	@Override
	public void clear() {
		this.inventoryContents.clear();
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return (Block.getBlockFromItem(stack.getItem()) instanceof BlockSapling);
	}

	@Override
	public ITextComponent getDisplayName() {
        return (ITextComponent)(this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName(), new Object[0]));
	}

	@Override
	public boolean isEmpty() {
        for (ItemStack itemstack : this.inventoryContents)
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
		return this.isItemValidForSlot(index, itemStackIn);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return false;
	}


	public boolean Deliver(ItemStack stack) {
		boolean ret = false;
		if (!stack.isEmpty()){
			if (isItemValidForSlot(0,stack)){
				ItemStack itemIn = stack.copy();
				itemIn.setCount(1);
				for (int i = 0; i < this.inventoryContents.size(); i++){
					ItemStack item2 = this.inventoryContents.get(i);
					if (item2.isEmpty()){
						this.inventoryContents.set(i,itemIn);
						ret = true;
						break;
					}else if (ModUtil.compareItemStacks(stack,item2,CompaierLevel.LEVEL_EQUAL_META) && item2.getCount() < item2.getMaxStackSize()){
						item2.grow(1);
						ret = true;
						break;
					}
				}
			}
		}
		return ret;
	}

}