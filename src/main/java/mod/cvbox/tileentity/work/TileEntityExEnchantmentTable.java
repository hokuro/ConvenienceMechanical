package mod.cvbox.tileentity.work;

import java.util.Random;

import mod.cvbox.entity.EntityCore;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;

public class TileEntityExEnchantmentTable extends TileEntity {

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
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        return compound;
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
    }

	@Override
	public CompoundNBT serializeNBT() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		// TODO 自動生成されたメソッド・スタブ

	}
}
