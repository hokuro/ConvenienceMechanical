package mod.cvbox.tileentity;

import org.apache.commons.lang3.BooleanUtils;

import mod.cvbox.block.BlockSetter;
import mod.cvbox.inventory.ContainerSetter;
import mod.cvbox.util.ModUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntitySetter extends TileEntityLockableLoot implements IInventory, ITickable, ISidedInventory, IPowerSwitchEntity{
	public static final String NAME="setter";
	public static final int FIELD_POWER = 0;
	public static final int FIELD_BATTERY = 1;
	public static final int FIELD_BATTERYMAX = 2;
	public static final int CRUSH_TIME = 20;
	private NonNullList<ItemStack> stacks = NonNullList.<ItemStack>withSize(10, ItemStack.EMPTY);
	private int crush_count;
	private int power_count;
	private boolean power;

	private int[] SLOT_SIDE = new int[]{1,2,3,4,5,6,7,8,9};
	private int[] SLOT_BOTTOM = new int[]{};


	public TileEntitySetter(){
		power = false;
		crush_count = 0;
        power_count = 0;
	}

	@Override
	public void update() {
		if (isCrushing()){
			crush_count++;
		}
		if (!getPower().isEmpty() && power_count == 0){
			powerDown();
		}
		if (!world.isRemote){
			if (checkPowerOn()){
				if (!this.isEmpty2()){
					power_count++;
					if (CRUSH_TIME < crush_count){
						if (canSpawan()){
							SpawnBlock();
							crush_count = 0;
						}else{
							crush_count = CRUSH_TIME;
						}
					}else if (crush_count == 0){
						crush_count++;
					}
				}
				power_count++;
				if (power_count > 40){
					power_count = 0;
				}
			}else{
				if (crush_count > 0){
					crush_count--;
				}else{
					crush_count = 0;
				}
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
					ModUtil.spawnParticles(this.world, this.pos, EnumParticleTypes.REDSTONE);
				}
			}
		}
	}

	protected boolean canSpawan(){
		boolean ret = false;

		if (this.world.getBlockState(this.pos).getBlock() instanceof BlockSetter){
			EnumFacing face = BlockSetter.getFacing(this.world.getBlockState(this.pos));
			if ( this.world.getBlockState(this.pos.offset(face)).getMaterial() == Material.AIR){
				ret = true;
			}
		}
		return ret;
	}

	protected void SpawnBlock(){
		EnumFacing face = BlockSetter.getFacing(this.world.getBlockState(this.pos));
		BlockPos pos2 = this.pos.offset(face);
		IBlockState state = this.world.getBlockState(pos2);
		if (state.getMaterial() != Material.AIR){
			return;
		}
		for (ItemStack item : stacks){
			if (!item.isEmpty()){
				Block setBlock = Block.getBlockFromItem(item.getItem());
				if (setBlock != Blocks.AIR){
	        		if (this.world.setBlockState(pos2, setBlock.getStateFromMeta(item.getMetadata()))){
	        			item.shrink(1);
	        		}
	        		break;
				}
			}
		}
	}

	private boolean checkPowerOn(){
		ItemStack st = getPower();
		return (((!st.isEmpty()) && (st.getMaxDamage() -  st.getItemDamage() > 1)) &&
				power);
	}

	private void powerDown(){
		int damage = getPower().getItemDamage()+1;
		getPower().setItemDamage(damage);
	}

	public boolean isCrushing(){
		return this.crush_count > 0;
	}


	@Override
    public int getSizeInventory()
    {
        return stacks.size();
    }

    @Override
    public boolean isEmpty()
    {
        for (ItemStack itemstack : this.stacks)
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }

        return true;
    }
    public boolean isEmpty2()
    {
    	for (int i = 1; i < stacks.size(); i++){
    		if (!stacks.get(i).isEmpty()){
    			return false;
    		}
    	}

        return true;
    }

    public int getSettingSlot()
    {
        this.fillWithLoot((EntityPlayer)null);
        int i = -1;
        int j = 1;

        for (int k = 0; k < this.stacks.size(); ++k)
        {
            if (!((ItemStack)this.stacks.get(k)).isEmpty())
            {
                i = k;
            }
        }

        return i;
    }

    public int addItemStack(ItemStack stack)
    {
        for (int i = 0; i < this.stacks.size(); ++i)
        {
            if (((ItemStack)this.stacks.get(i)).isEmpty())
            {
                this.setInventorySlotContents(i, stack);
                return i;
            }
        }

        return -1;
    }

    @Override
    public String getName()
    {
        return this.hasCustomName() ? this.customName : "container.setter";
    }


    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
		power = compound.getBoolean("power");
		crush_count=compound.getInteger("count");
		power_count=compound.getInteger("power_count");

        this.stacks = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);

        if (!this.checkLootAndRead(compound))
        {
            ItemStackHelper.loadAllItems(compound, this.stacks);
        }

        if (compound.hasKey("CustomName", 8))
        {
            this.customName = compound.getString("CustomName");
        }
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return false;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
		compound.setBoolean("power", power);
		compound.setInteger("count", crush_count);
		compound.setInteger("power_count",power_count);

        if (!this.checkLootAndWrite(compound))
        {
            ItemStackHelper.saveAllItems(compound, this.stacks);
        }

        if (this.hasCustomName())
        {
            compound.setString("CustomName", this.customName);
        }

        return compound;
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
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		if (side == EnumFacing.DOWN){
			return SLOT_BOTTOM;
		}
		return SLOT_SIDE;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		if (direction == EnumFacing.DOWN){
			return false;
		}else{
			return true;
		}
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return false;
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
			ret = getPower().getItemDamage();
			break;
		case FIELD_BATTERYMAX:
			ret = getPower().getMaxDamage();
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
		case FIELD_BATTERY:
			getPower().setItemDamage(value);
			break;
		}
	}

	@Override
	public int getFieldCount() {
		return 3;
	}


    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    protected NonNullList<ItemStack> getItems()
    {
        return this.stacks;
    }

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
		ContainerSetter setter = new ContainerSetter(playerInventory,this);
		for (int i = 0; i < this.stacks.size(); i++){
			setter.putStackInSlot(i, this.stacks.get(i));
		}
		return setter;
	}

	@Override
	public String getGuiID() {
		return "cvbox:setter";
	}

	@Override
	public void setPower(boolean value) {
		this.power = value;
	}

	public ItemStack getPower(){
		return this.stacks.get(0);
	}
}
