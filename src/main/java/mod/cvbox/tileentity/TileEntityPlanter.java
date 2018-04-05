package mod.cvbox.tileentity;

import mod.cvbox.core.ModCommon;
import mod.cvbox.network.MessageHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class TileEntityPlanter extends TileEntity  implements IInventory {
	private final static int max = ModCommon.PLANTER_CONTENASIZE;
	private ItemStack[] itemStack = new ItemStack[max];
	private String customName;

	public TileEntityPlanter(){

	}

	@Override
	public int getSizeInventory(){
		return itemStack.length;
	}
//
//	@Override
//	public void readFromNBT(NBTTagCompound nbtTagCompound){
//		super.readFromNBT(nbtTagCompound);
//		NBTTagList itemsTagList = nbtTagCompound.getTagList("Items",10);
//
//		itemStack = new ItemStack[getSizeInventory()];
//		for (int tagCounter = 0; tagCounter < itemsTagList.tagCount(); tagCounter++){
//			NBTTagCompound itemTagCompound = itemsTagList.getCompoundTagAt(tagCounter);
//
//			byte slotIndex =itemTagCompound.getByte("Slot");
//			if ((slotIndex >= 0) && (slotIndex < this.itemStack.length)){
//				itemStack[slotIndex] = ItemStack.loadItemStackFromNBT(itemTagCompound);
//			}
//		}
//	}
//
//	@Override
//	public void writeToNBT(NBTTagCompound nbtTagCompound){
//		super.writeToNBT(nbtTagCompound);
//		NBTTagList itemsTagList = new NBTTagList();
//		for (int slotIndex = 0; slotIndex < itemStack.length; slotIndex++){
//			if (this.itemStack[slotIndex] != null){
//				NBTTagCompound itemTagCompound = new NBTTagCompound();
//
//				itemTagCompound.setByte("Slot",(byte)slotIndex);
//				itemStack[slotIndex].writeToNBT(itemTagCompound);
//				itemsTagList.appendTag(itemTagCompound);
//			}
//		}
//		nbtTagCompound.setTag("Items",itemsTagList);
//	}
//
//	@Override
//	public Packet getDescriptionPacket(){
//		sendItemInfo();
//		return null;
//	}
//
//	@Override
//	public ItemStack getStackInSlot(int slotIndex){
//		if((slotIndex >= 0) && (slotIndex < itemStack.length)){
//			return itemStack[slotIndex];
//		}
//		return null;
//	}
//
//	@Override
//	public ItemStack decrStackSize(int slotIndex, int splitStackSize){
//		if ( itemStack[slotIndex] != null){
//			if (itemStack[slotIndex].stackSize <= splitStackSize){
//				ItemStack tmpItemStack = itemStack[slotIndex];
//				itemStack[slotIndex] = null;
//				//onInventoryChanged();
//				sendPacket();
//				return tmpItemStack;
//			}
//			ItemStack splittedItemStack = itemStack[slotIndex].splitStack(splitStackSize);
//			if (itemStack[slotIndex].stackSize == 0){
//				itemStack[slotIndex] = null;
//			}
//			//onInventoryChanged();
//			sendPacket();
//			return splittedItemStack;
//		}
//		return null;
//	}
//
//	@Override
//	public void setInventorySlotContents(int idx, ItemStack stack){
//		itemStack[idx] = stack;
//		if ((stack != null) && (stack.stackSize > getInventoryStackLimit())){
//			stack.stackSize = getInventoryStackLimit();
//		}
//		//onInventoryChanged();
//		sendPacket();
//	}
//
//	@Override
//	public int getInventoryStackLimit(){
//		return 64;
//	}
//
//	@Override
//	public boolean isUseableByPlayer(EntityPlayer entityplayer){
//		if (this.worldObj.getTileEntity(this.pos) != this) {
//			return false;
//		}
//		return entityplayer.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64.0D;
//	}
//
//
//	@Override
//	public String getName() {
//		// TODO 自動生成されたメソッド・スタブ
//		return this.hasCustomName() ? this.customName : "container.TileEntityAutoPlanting";
//	}
//
//	@Override
//	public boolean hasCustomName() {
//		// TODO 自動生成されたメソッド・スタブ
//		return this.customName != null && this.customName.length() > 0;
//	}
//
//	@Override
//	public ItemStack removeStackFromSlot(int index) {
//		if (itemStack[index] != null){
//			ItemStack stack = itemStack[index];
//			itemStack[index] = null;
//			return stack;
//		}
//		return null;
//	}
//
//	@Override
//	public void openInventory(EntityPlayer player) {
//		// TODO 自動生成されたメソッド・スタブ
//
//	}
//
//	@Override
//	public void closeInventory(EntityPlayer player) {
//		// TODO 自動生成されたメソッド・スタブ
//
//	}
//
//	@Override
//	public int getField(int id) {
//		// TODO 自動生成されたメソッド・スタブ
//		return 0;
//	}
//
//	@Override
//	public void setField(int id, int value) {
//		// TODO 自動生成されたメソッド・スタブ
//
//	}
//
//	@Override
//	public int getFieldCount() {
//		// TODO 自動生成されたメソッド・スタブ
//		return 0;
//	}
//
//	@Override
//	public void clear() {
//		// TODO 自動生成されたメソッド・スタブ
//
//	}
//
//	@Override
//	public boolean isItemValidForSlot(int index, ItemStack stack) {
//		// TODO 自動生成されたメソッド・スタブ
//		return SlotPlant.checkExtends(stack);
//	}
//
//	@Override
//	public ITextComponent getDisplayName() {
//		// TODO 自動生成されたメソッド・スタブ
//		return null;
//	}
//
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
//
//	private void update(World world, int i, int j, int k){
//		if(world.isRemote){
//			return;
//		}
//		TileEntityPlanter tile =(TileEntityPlanter)world.getTileEntity(new BlockPos(i,j,k));
//	}
//
//	private void sendPacket(){
//		if(!worldObj.isRemote){
//			sendItemInfo();
//		}
//	}

	private void setItem(ItemStack is, int slot){
		setInventorySlotContents(slot, is);
	}

	private ItemStack getItem(int slot){
		return getStackInSlot(slot);
	}

	private void sendItemInfo(){
		int x = this.pos.getX();
		int y = this.pos.getY();
		int z = this.pos.getZ();
		MessageHelper.MessagePlanting(x, y, z);
	}

	@Override
	public boolean isEmpty() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public String getName() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public boolean hasCustomName() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public int getInventoryStackLimit() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public void openInventory(EntityPlayer player) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void closeInventory(EntityPlayer player) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public int getField(int id) {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public void setField(int id, int value) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public int getFieldCount() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public void clear() {
		// TODO 自動生成されたメソッド・スタブ

	}


}