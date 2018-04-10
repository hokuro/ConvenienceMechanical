package mod.cvbox.tileentity;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.BooleanUtils;

import mod.cvbox.config.ConfigValue;
import mod.cvbox.inventory.ContainerAutoPlanting;
import mod.cvbox.util.ModUtil;
import mod.cvbox.util.ModUtil.CompaierLevel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class TileEntityWoodHarvester extends TileEntity  implements IInventory, ITickable, ISidedInventory {
	private final static int max = ContainerAutoPlanting.ROW_SLOT * ContainerAutoPlanting.COL_SLOT+1;
	public static final String NAME = "WoodHarvest";
	private final NonNullList<ItemStack> inventoryContents;
	private String customName;


	private boolean isRun;
	private int nextPos;
	private int[] next_x;
	private int[] next_z;

	private boolean findPlanter;
	private BlockPos planterPos;
    private boolean canDeliver;

	// SideInventory
    private final int[] SLOTS_TOP;
    private int timeCnt;
    private int fotuneLevel;
	private int silkLevel;


	public TileEntityWoodHarvester(){
		inventoryContents = NonNullList.<ItemStack>withSize(max, ItemStack.EMPTY);
		isRun = false;
		SLOTS_TOP = new int[max-1];
		for (int i = 0; i < max-1; i++){
			SLOTS_TOP[i] = i;
		}
		makeArray();
		nextPos = 0;
		findPlanter = false;
		planterPos = null;
		canDeliver = false;
		timeCnt = 0;
	}

	public boolean canDeliver() {
		return this.canDeliver;
	}

	public void makeArray(){
		int size = (ConfigValue.WoodHarvester.MaxDistance * 2 + 1) * (ConfigValue.WoodHarvester.MaxDistance * 2 + 1) -1;
		next_x = new int[size];
		next_z = new int[size];

		int maxLp = ConfigValue.WoodHarvester.MaxDistance * 4 + 1;
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
			if (this.isRun){
				this.timeCnt++;
				if (timeCnt >= ConfigValue.WoodHarvester.ExecTimeSpan){
					if (!fullInventory()){

						for (int i = 0; i < ConfigValue.WoodHarvester.OneTicPlant; i++){
							Exec();
						}
					}

					if (canDeliver){
						this.deliverPlanter();
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

	public void Exec(){
		List<ItemStack> drops;
		for (int y = -ConfigValue.WoodHarvester.MaxHeight; y <= ConfigValue.WoodHarvester.MaxHeight; y++){
			boolean canhavest = false;
			BlockPos plantPos = new BlockPos(this.pos.add(
					next_x[nextPos],
					y,
					next_z[nextPos]));
			IBlockState state = world.getBlockState(plantPos);
			Block block = state.getBlock();
			// 空気ブロックの場合無視
			if (state.getMaterial() == Material.AIR){continue;}

			BlockPos plantPos2 = plantPos.add(0,0,0);
			drops = new ArrayList<ItemStack>();
			while((block instanceof BlockLog) || (block instanceof BlockLeaves)){
				y = plantPos2.getY()-this.pos.getY();
				drops.clear();
				if (block instanceof BlockLog){
					drops.add(new ItemStack(block,1,block.getMetaFromState(state)));
					if (this.harvest(drops)){
						world.destroyBlock(plantPos2, false);
					}
				}else if (block instanceof BlockLeaves){
					 if (this.silkLevel > 0){
							drops.add(new ItemStack(block,1,block.damageDropped(state)<4?block.damageDropped(state):block.damageDropped(state)-4));
							if (this.harvest(drops)){
								world.destroyBlock(plantPos2, false);
								if (this.inventoryContents.get(max-1).attemptDamageItem(1, world.rand, null)){
									this.setInventorySlotContents(max-1, ItemStack.EMPTY);
								}
							}
					 }else{
						drops.addAll(block.getDrops(world, plantPos2, state, fotuneLevel));
						if (this.harvest(drops)){
							world.destroyBlock(plantPos2, false);
							if (fotuneLevel > 0){
								if (this.inventoryContents.get(max-1).attemptDamageItem(1, world.rand, null)){
									this.setInventorySlotContents(max-1, ItemStack.EMPTY);
								}
							}
						}
					}
				}
				plantPos2 = plantPos2.offset(EnumFacing.UP);
				state = world.getBlockState(plantPos2);
				block = state.getBlock();
			}
		}
		this.nextPos++;
		if (nextPos >= next_x.length){
			nextPos = 0;
		}

	}

	private void deliverPlanter(){
		if (findPlanter && this.planterPos != null){
			TileEntity te = world.getTileEntity(planterPos);
			if (!(te instanceof TileEntityWoodPlanter)){
				findPlanter = false;
				planterPos = null;
				planterPos = searchPlanter();
			}
		}else{
			planterPos = searchPlanter();
		}

		if (findPlanter){
			TileEntity te = world.getTileEntity(planterPos);
			try{
				TileEntityWoodPlanter planter = (TileEntityWoodPlanter)te;
				if (planter.canDeliver()){
					for (int i = 0; i < this.inventoryContents.size()-1; i++){
						if (planter.Deliver(this.inventoryContents.get(i))){
							this.inventoryContents.get(i).shrink(1);
							break;
						}
					}
				}else{
					findPlanter = false;
					planterPos = null;
				}
			}catch(Exception ex){
				// 急にプランターじゃなくなっていた
				this.findPlanter = false;
				this.planterPos = null;
			}
		}

	}

	private BlockPos searchPlanter(){
		BlockPos ret = null;
		int size = (ConfigValue.WoodHarvester.SearchPlanter * 2 + 1) * (ConfigValue.WoodHarvester.SearchPlanter * 2 + 1) -1;
		int maxLp = ConfigValue.WoodHarvester.SearchPlanter * 4 + 1;
		int len = 0;
		int x = 0;
		int z = 0;
		int idx = 0;

		for (int i = 0; i < maxLp && !findPlanter; i ++){
			if (i%2 == 0 && i != maxLp-1){len++;}
			for (int j = 0; j < len; j++){
				for (int y = -ConfigValue.WoodHarvester.SearchPlanter; y < ConfigValue.WoodHarvester.SearchPlanter+1 && !findPlanter; y++){

					TileEntity te = world.getTileEntity(this.pos.add(x,y,z));
					if (te instanceof TileEntityWoodPlanter){
						if (((TileEntityWoodPlanter)te).canDeliver())
						this.findPlanter = true;
						ret = this.pos.add(x,y,z);
					}
				}
				switch(i%4){
				case 0: x++; break;
				case 1: z++;break;
				case 2: x--;break;
				case 3: z--;break;
				}
			}
			for (int y = -ConfigValue.WoodHarvester.SearchPlanter; y < ConfigValue.WoodHarvester.SearchPlanter+1 && !findPlanter; y++){
				TileEntity te = world.getTileEntity(this.pos.add(x,y,z));
				if (te instanceof TileEntityWoodPlanter){
					if (((TileEntityWoodPlanter)te).canDeliver())
					this.findPlanter = true;
					ret = this.pos.add(x,y,z);
				}
			}
		}

		return ret;
	}

	private boolean harvest(List<ItemStack> drop){
		boolean ret = true;
		List<Integer> setIndex = new ArrayList<Integer>();
		NonNullList<ItemStack> dummy_inv = NonNullList.<ItemStack>withSize(max-1,ItemStack.EMPTY);
		for (int i = 0; i < max-1; i++){
			dummy_inv.set(i, this.inventoryContents.get(i).copy());
		}

		for (ItemStack d : drop){
			int collectNum = d.getCount();
			for (int i = 0; i < dummy_inv.size(); i++){
				ItemStack stack = dummy_inv.get(i);
				if (stack.isEmpty()){
					dummy_inv.set(i, d.copy());
					collectNum = 0;
					break;
				}else if (ModUtil.compareItemStacks(d, stack, CompaierLevel.LEVEL_EQUAL_META)){
					if (stack.getCount() < stack.getMaxStackSize()){
						if (stack.getCount() + collectNum < stack.getMaxStackSize()){
							dummy_inv.get(i).grow(collectNum);
							collectNum = 0;
							break;
						}else{
							int setNum = stack.getMaxStackSize() - stack.getCount();
							dummy_inv.get(i).grow(collectNum);
							collectNum -= setNum;
						}
					}
				}
			}
			if (collectNum != 0){
				ret = false;
				break;
			}
		}

		if (ret){
			for (int i = 0; i < max-1; i++){
				inventoryContents.set(i, dummy_inv.get(i).copy());
			}
		}
		return ret;
	}

	private boolean fullInventory(){
		boolean ret = true;
		for ( int i = 0; i < this.inventoryContents.size() -1; i++){
			ItemStack stack = this.inventoryContents.get(i);
			if (stack.isEmpty()){
				ret = false;
				break;
			}else if (stack.getCount() < stack.getMaxStackSize()){
				ret = false;
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
		this.fotuneLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, this.inventoryContents.get(max-1));
		this.silkLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, this.inventoryContents.get(max-1));
		findPlanter = nbtTagCompound.getBoolean("find");
		if (findPlanter){
			planterPos = new BlockPos(
					nbtTagCompound.getInteger("px"),
					nbtTagCompound.getInteger("py"),
					nbtTagCompound.getInteger("pz"));
		}else{
			planterPos = null;
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
		nbtTagCompound.setBoolean("find", this.findPlanter);
		if (findPlanter){
			nbtTagCompound.setInteger("px", planterPos.getX());
			nbtTagCompound.setInteger("py", planterPos.getY());
			nbtTagCompound.setInteger("pz", planterPos.getZ());
		}
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

		if (idx == max -1){
			this.fotuneLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE,stack);
			this.silkLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH,stack);
		}
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
		return this.hasCustomName() ? this.customName : "container.TileEntityWoodHarvest";
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
		if (index == 54){
			if (stack.getItem() instanceof ItemAxe){
				return true;
			}
			return false;
		}
		return true;
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

	/////////////////////////////////////////////////////////////////////////////////
	// ISideInventory
	@Override
	public int[] getSlotsForFace(EnumFacing side) {
        return SLOTS_TOP;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		if (index == max-1){return false;}
		return true;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		if (index == max-1){return false;}
        return true;
	}
}