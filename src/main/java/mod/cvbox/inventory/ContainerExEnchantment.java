package mod.cvbox.inventory;

import mod.cvbox.block.BlockCore;
import mod.cvbox.config.ConfigValue;
import mod.cvbox.core.Mod_ConvenienceBox;
import mod.cvbox.network.MessageExEnchant_ClearParameter;
import mod.cvbox.util.ModUtil;
import mod.cvbox.util.ModUtil.CompaierLevel;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.stats.StatList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerExEnchantment extends Container {
    /** SlotEnchantmentTable object with ItemStack to be enchanted */
    public IInventory tableInventory;
    /** current world (for bookshelf counting) */
    private final World worldPointer;
    private final BlockPos position;
    private final EntityPlayer player;
	private final ItemStack[] level = new ItemStack[]{
			new ItemStack(Items.DIAMOND),
			new ItemStack(Items.EMERALD),
			new ItemStack(Items.NETHER_STAR)};
    public int enc_index;
    public int enc_level;


    @SideOnly(Side.CLIENT)
    public ContainerExEnchantment(InventoryPlayer playerInv, World worldIn)
    {
        this(playerInv, worldIn, BlockPos.ORIGIN);
    }

    public ContainerExEnchantment(InventoryPlayer playerInv, World worldIn, BlockPos pos)
    {
        this.worldPointer = worldIn;
        this.position = pos;
        player = playerInv.player;
        this.tableInventory = new InventoryBasicEx("Enchant", true, 2, new int[]{0})
        {
            /**
             * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended.
             */
            public int getInventoryStackLimit()
            {
                return 64;
            }
            /**
             * For tile entities, ensures the chunk containing the tile entity is saved to disk later - the game won't
             * think it hasn't changed and skip it.
             */
            public void markDirty()
            {
                super.markDirty();
                ContainerExEnchantment.this.onCraftMatrixChanged(this);
            }
        };

        this.addSlotToContainer(new Slot(this.tableInventory, 0, 15, 25)
        {
            /**
             * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
             */
            public boolean isItemValid(ItemStack stack)
            {
                return true;
            }
            /**
             * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1 in
             * the case of armor slots)
             */
            public int getSlotStackLimit()
            {
                return 1;
            }
        });
        this.addSlotToContainer(new Slot(this.tableInventory, 1, 35, 26)
        {
            public boolean isItemValid(ItemStack stack)
            {
            	return ModUtil.compareItemStacks(level[ConfigValue.ExEnchant.TributeItemLevel-1], stack, CompaierLevel.LEVEL_EQUAL_ITEM);
            }

            public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack)
            {
                return stack;
            }

            public void putStack(ItemStack stack)
            {
                this.inventory.setInventorySlotContents(this.getSlotIndex(), stack);
            }
        });

        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k)
        {
            this.addSlotToContainer(new Slot(playerInv, k, 8 + k * 18, 142));
        }
    }

	public int getEnchantIndex(){
    	return enc_index;
    }

    public int getEnchantLevel(){
    	return enc_level;
    }


    private ItemStack before_sucrifice = ItemStack.EMPTY;
    public void onCraftMatrixChanged(IInventory inventoryIn)
    {
    	if (!worldPointer.isRemote){
            if (inventoryIn == this.tableInventory)
            {
            	if (ModUtil.compareItemStacks(before_sucrifice, this.tableInventory.getStackInSlot(1), CompaierLevel.LEVEL_EQUAL_ALL)){
            		Mod_ConvenienceBox.Net_Instance.sendTo(new MessageExEnchant_ClearParameter(), (EntityPlayerMP)player);
            	}
            	before_sucrifice = this.tableInventory.getStackInSlot(1).copy();
            }
    	}
    }


    /**
     * Called when the container is closed.
     */
    public void onContainerClosed(EntityPlayer playerIn)
    {
        super.onContainerClosed(playerIn);

        if (!this.worldPointer.isRemote)
        {
            this.clearContainer(playerIn, playerIn.world, this.tableInventory);
        }
    }

    /**
     * Determines whether supplied player can use this container
     */
    public boolean canInteractWith(EntityPlayer playerIn)
    {
        if (this.worldPointer.getBlockState(this.position).getBlock() != BlockCore.block_exenchanter)
        {
            return false;
        }
        else
        {
            return playerIn.getDistanceSq((double)this.position.getX() + 0.5D, (double)this.position.getY() + 0.5D, (double)this.position.getZ() + 0.5D) <= 64.0D;
        }
    }

    /**
     * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
     * inventory and the other inventory(s).
     */
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index == 0)
            {
                if (!this.mergeItemStack(itemstack1, 2, 38, true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (index == 1)
            {
                if (!this.mergeItemStack(itemstack1, 2, 38, true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (itemstack1.getItem() == level[ConfigValue.ExEnchant.TributeItemLevel-1].getItem())
            {
                if (!this.mergeItemStack(itemstack1, 1, 2, true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else
            {
                if (((Slot)this.inventorySlots.get(0)).getHasStack() || !((Slot)this.inventorySlots.get(0)).isItemValid(itemstack1))
                {
                    return ItemStack.EMPTY;
                }

                if (itemstack1.hasTagCompound() && itemstack1.getCount() == 1)
                {
                    ((Slot)this.inventorySlots.get(0)).putStack(itemstack1.copy());
                    itemstack1.setCount(0);
                }
                else if (!itemstack1.isEmpty())
                {
                    ((Slot)this.inventorySlots.get(0)).putStack(new ItemStack(itemstack1.getItem(), 1, itemstack1.getMetadata()));
                    itemstack1.shrink(1);
                }
            }

            if (itemstack1.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }

    private boolean chktret = false;
    public boolean checkTributeItemCnt(){
    	ItemStack item = this.tableInventory.getStackInSlot(1);
    	if (!item.isEmpty() && item.getCount() >= ConfigValue.ExEnchant.TrubuteItemCount){
    		return true;
    	}else{
    		return false;
    	}
    }

    public int getNeedLevel(){
    	int ret = enc_level;
    	if (enc_index >= 0){
    		Enchantment enchant = Mod_ConvenienceBox.encList.get(enc_index);
        	switch(enchant.getMaxLevel()){
        	case 1:
        		ret = 5;
        		break;
        	case 3:
        		if (enc_level == 1){
        			ret = 1;
        		}else if (enc_level == 2){
        			ret = 3;
        		}else if (enc_level == 3){
        			ret = 5;
        		}
        		break;
        	default:
        		ret = enc_level;
        	}
    	}
    	return ret;

    }

	public boolean checkEnchantable(int enc_index) {
		boolean ret = false;
		ItemStack itemstack = tableInventory.getStackInSlot(0);
		if (!itemstack.isEmpty() && itemstack.getItem().isEnchantable(itemstack))
		{
			Enchantment enc = Mod_ConvenienceBox.encList.get(enc_index);
			// エンチャントの読出し
			Enchantment[] has_encs = readEnchant(itemstack);
			if (has_encs.length < 5){
				// エンチャントできる効果は5種類まで
				if (enc.canApply(itemstack)){
					ret = true;
					// アイテムに付加できる効果かどうかチェック
					for (Enchantment e : has_encs){
						if (!enc.isCompatibleWith(e)){
							// 一つでも一緒にできない効果がある場合終了する
							ret = false;
							break;
						}
					}
				}
			}
		}
		return ret;
	}

	protected Enchantment[] readEnchant(ItemStack stack){
		NBTTagList encs = stack.getEnchantmentTagList();
		Enchantment[] ret = new Enchantment[encs.tagCount()];

		for (int i = 0; i < ret.length; i++){
			NBTTagCompound nbt = encs.getCompoundTagAt(i);
			short id = nbt.getShort("id");
			ret[i] = Enchantment.getEnchantmentByID(id);
		}
		return ret;
	}

	public void setEnchantInfo(int index, int level){
		this.enc_index = index;
		this.enc_level = level;
	}



    public boolean enchantItem(EntityPlayer playerIn)
    {
        ItemStack itemstack = this.tableInventory.getStackInSlot(0);
        ItemStack itemstack1 = this.tableInventory.getStackInSlot(1);
        int itemCnt = ConfigValue.ExEnchant.TrubuteItemCount;

        if ((itemstack1.isEmpty() || itemstack1.getCount() < itemCnt) && !playerIn.capabilities.isCreativeMode)
        {
            return false;
        }
        else if (!itemstack.isEmpty() && (playerIn.experienceLevel >= getNeedLevel() || playerIn.capabilities.isCreativeMode))
        {
            if (!this.worldPointer.isRemote)
            {
            	Enchantment enc = Mod_ConvenienceBox.encList.get(enc_index);
                playerIn.onEnchant(itemstack, getNeedLevel());

                boolean flag = itemstack.getItem() == Items.BOOK;

                if (flag)
                {
                    itemstack = new ItemStack(Items.ENCHANTED_BOOK);
                    this.tableInventory.setInventorySlotContents(0, itemstack);
                }
                if (flag)
                {
                    ItemEnchantedBook.addEnchantment(itemstack, new EnchantmentData(enc,enc_level));
                }
                else
                {
                    itemstack.addEnchantment(enc, enc_level);
                }

                if (!playerIn.capabilities.isCreativeMode)
                {
                    itemstack1.shrink(itemCnt);

                    if (itemstack1.isEmpty())
                    {
                        this.tableInventory.setInventorySlotContents(1, ItemStack.EMPTY);
                    }
                }
                playerIn.addStat(StatList.ITEM_ENCHANTED);

                if (playerIn instanceof EntityPlayerMP)
                {
                    CriteriaTriggers.ENCHANTED_ITEM.trigger((EntityPlayerMP)playerIn, itemstack, itemCnt);
                }

                this.tableInventory.markDirty();
                this.onCraftMatrixChanged(this.tableInventory);
                this.worldPointer.playSound((EntityPlayer)null, this.position, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0F, this.worldPointer.rand.nextFloat() * 0.1F + 0.9F);
            }
            return true;

        }else{
        	return false;
        }
    }

	public static void updateEnchantment(int index, int level, EntityPlayer player) {
		if (player.openContainer instanceof ContainerExEnchantment){
			((ContainerExEnchantment)player.openContainer).setEnchantInfo(index,level);
		}
	}

	public static void execEnchant(EntityPlayer player) {
		if (player.openContainer instanceof ContainerExEnchantment){
			if (((ContainerExEnchantment)player.openContainer).enchantItem(player)){
				Mod_ConvenienceBox.Net_Instance.sendTo(new MessageExEnchant_ClearParameter(), (EntityPlayerMP) player);
			}
		}
	}

}

