package mod.cvbox.event;

import mod.cvbox.container.ContainerAutoPlanting;
import mod.cvbox.core.AutoPlanting;
import mod.cvbox.entity.TileEntityAutoPlanting;
import mod.cvbox.gui.GuiAutoPlanting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiTileEntityHandler implements IGuiHandler {
	public GuiTileEntityHandler() {
	}

	// クライアント用　GUIを作る
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntityAutoPlanting tileEntity = (TileEntityAutoPlanting) world.getTileEntity(new BlockPos(x, y, z));
		if ((ID == AutoPlanting.guiIdAutoSowSeed) && (tileEntity != null)) {
			return new GuiAutoPlanting(player, (TileEntityAutoPlanting) world.getTileEntity(new BlockPos(x, y, z)), world, x, y, z);
		}
		return null;
	}

	// サーバ用コンテナを取りダス
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntityAutoPlanting tileEntity = (TileEntityAutoPlanting) world.getTileEntity(new BlockPos(x, y, z));
		if ((ID == AutoPlanting.guiIdAutoSowSeed) && (tileEntity != null)) {
			return new ContainerAutoPlanting(player, (TileEntityAutoPlanting) world.getTileEntity(new BlockPos(x, y, z)), world, x, y,z);
		}
		return null;
	}
}
