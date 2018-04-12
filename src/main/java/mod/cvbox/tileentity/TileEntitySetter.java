package mod.cvbox.tileentity;

import mod.cvbox.inventory.ContainerSetter;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntitySetter extends TileEntityLockableLoot {
	public static final String NAME="setter";
	private NonNullList<ItemStack> stacks = NonNullList.<ItemStack>withSize(9, ItemStack.EMPTY);


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

}
