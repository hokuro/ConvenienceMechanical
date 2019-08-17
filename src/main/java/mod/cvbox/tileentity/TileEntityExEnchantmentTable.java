package mod.cvbox.tileentity;

import java.util.Random;

import mod.cvbox.core.ModCommon;
import mod.cvbox.entity.EntityCore;
import mod.cvbox.inventory.ContainerExEnchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
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

	public TileEntityExEnchantmentTable() {
		super(EntityCore.ExEnchantmentTable);
		// TODO 自動生成されたコンストラクター・スタブ
	}

    @Override
    public NBTTagCompound write(NBTTagCompound compound)
    {
        super.write(compound);

        if (this.hasCustomName())
        {
            compound.setString("CustomName", this.customName);
        }

        return compound;
    }

    @Override
    public void read(NBTTagCompound compound)
    {
        super.read(compound);

        if (compound.hasKey("CustomName"))
        {
            this.customName = compound.getString("CustomName");
        }
    }


    @Override
    public ITextComponent getName()
    {
        return  new TextComponentTranslation( "container.exenchant");
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
        return getName();
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
    {
        return new ContainerExEnchantment(playerInventory, this.world, this.pos);
    }

    @Override
    public String getGuiID()
    {
        return ModCommon.MOD_ID + ":" + ModCommon.GUIID_EXENCHANTER;
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