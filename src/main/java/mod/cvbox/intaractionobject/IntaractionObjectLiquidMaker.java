package mod.cvbox.intaractionobject;

import mod.cvbox.core.ModCommon;
import mod.cvbox.inventory.ContainerLiquidMaker;
import mod.cvbox.tileentity.TileEntityLiquidMaker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IInteractionObject;

public class IntaractionObjectLiquidMaker implements IInteractionObject {
	private BlockPos entPos;

	public IntaractionObjectLiquidMaker(BlockPos pos){
		entPos = pos;
	}

	@Override
	public ITextComponent getName() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public boolean hasCustomName() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public ITextComponent getCustomName() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
		// TODO 自動生成されたメソッド・スタブ
		TileEntity ent = playerIn.world.getTileEntity(entPos);
		if (ent instanceof TileEntityLiquidMaker){
			return new ContainerLiquidMaker(playerIn.inventory, ((IInventory)ent));
		}
		return null;
	}

	@Override
	public String getGuiID() {
		// TODO 自動生成されたメソッド・スタブ
		return ModCommon.MOD_ID + ":" + ModCommon.GUIID_LIQUIDMAKER;
	}

}
