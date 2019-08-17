package mod.cvbox.tileentity;

import org.apache.commons.lang3.BooleanUtils;

import mod.cvbox.block.BlockSetter;
import mod.cvbox.entity.EntityCore;
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
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

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
		super(EntityCore.Setter);
		power = false;
		crush_count = 0;
        power_count = 0;
	}

	@Override
	public void tick() {
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
					ModUtil.spawnParticles(this.world, this.pos, RedstoneParticleData.REDSTONE_DUST);
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
	        		if (this.world.setBlockState(pos2, setBlock.getDefaultState())){
	        			item.shrink(1);
	        		}
	        		break;
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
    public ITextComponent getName()
    {
        return this.hasCustomName() ? this.customName : new TextComponentTranslation("container.setter");
    }


    @Override
    public void read(NBTTagCompound compound)
    {
        super.read(compound);
		power = compound.getBoolean("power");
		crush_count=compound.getInt("count");
		power_count=compound.getInt("power_count");

        this.stacks = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);

        if (!this.checkLootAndRead(compound))
        {
            ItemStackHelper.loadAllItems(compound, this.stacks);
        }

        if (compound.hasKey("CustomName"))
        {
            this.customName = new TextComponentTranslation(compound.getString("CustomName"));
        }
    }

//    @Override
//    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
//    {
//    	super.shouldRenderInPass(pass)
//        return false;
//    }

    @Override
    public NBTTagCompound write(NBTTagCompound compound)
    {
        super.write(compound);
		compound.setBoolean("power", power);
		compound.setInt("count", crush_count);
		compound.setInt("power_count",power_count);

        if (!this.checkLootAndWrite(compound))
        {
            ItemStackHelper.saveAllItems(compound, this.stacks);
        }

        if (this.hasCustomName())
        {
            compound.setString("CustomName", this.customName.toString());
        }

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
			ret = getPower().getDamage();
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
			getPower().setDamage(value);
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

	@Override
	protected void setItems(NonNullList<ItemStack> itemsIn) {
		// TODO 自動生成されたメソッド・スタブ

	}
}
