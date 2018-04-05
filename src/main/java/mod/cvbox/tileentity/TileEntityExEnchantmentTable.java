package mod.cvbox.tileentity;

import java.util.Random;

import mod.cvbox.core.ModCommon;
import mod.cvbox.inventory.ContainerExEnchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IInteractionObject;

public class TileEntityExEnchantmentTable extends TileEntity implements IInteractionObject {
	public static final String NAME = "exenchantmenttable";


    public int tickCount;
    public float pageFlip;
    public float pageFlipPrev;
    public float flipT;
    public float flipA;

    public float tRot;
    private static final Random rand = new Random();


    private String customName;

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);

        if (this.hasCustomName())
        {
            compound.setString("CustomName", this.customName);
        }

        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);

        if (compound.hasKey("CustomName", 8))
        {
            this.customName = compound.getString("CustomName");
        }
    }


    @Override
    public String getName()
    {
        return this.hasCustomName() ? this.customName : "container.exenchant";
    }


    @Override
    public boolean hasCustomName()
    {
        return this.customName != null && !this.customName.isEmpty();
    }

    public void setCustomName(String customNameIn)
    {
        this.customName = customNameIn;
    }


    @Override
    public ITextComponent getDisplayName()
    {
        return (ITextComponent)(this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName(), new Object[0]));
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
    {
        return new ContainerExEnchantment(playerInventory, this.world, this.pos);
    }

    @Override
    public String getGuiID()
    {
        return ModCommon.MOD_ID + ":" + ModCommon.GUIID_EXENCHANTMENT;
    }
}