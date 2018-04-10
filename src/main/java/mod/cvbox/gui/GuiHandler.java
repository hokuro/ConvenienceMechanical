package mod.cvbox.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

//	@Override
//	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
//		if(ID == ModCommon.GUIID_REPAIR) {
//			return new mod.cvbox.inventory.ContainerExRepair(player.inventory, world, x, y, z, player);
//		}else if (ID == ModCommon.GUIID_PLANTER){
//			return new ContainerAutoPlanting(player, (TileEntityPlanter) world.getTileEntity(new BlockPos(x, y, z)), world, x, y,z);
//		}else if (ID == ModCommon.GUIID_EXPBANK){
//
//		}
//		return null;
//	}
//
//	@Override
//    @SideOnly(Side.CLIENT)
//	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
//		if(ID == ModCommon.GUIID_REPAIR) {
//			return new mod.cvbox.gui.GuiExRepair(player.inventory, world, z, z, z);
//		}else if (ID == ModCommon.GUIID_PLANTER){
//			return new GuiAutoPlanting(player, (TileEntityPlanter) world.getTileEntity(new BlockPos(x, y, z)), world, x, y, z);
//		}else if (ID == ModCommon.GUIID_EXPBANK){
//
//		}
//		return null;
//	}
}
