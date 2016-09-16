package mod.cvbox.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == 0) {
			return new mod.cvbox.container.ContainerRepair(player.inventory, world, x, y, z, player);
		}
		return null;
	}

	@Override
    @SideOnly(Side.CLIENT)
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == 0) {
			return new mod.cvbox.gui.GuiRepair(player.inventory, world, z, z, z);
		}
		return null;
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == 0) {
			return new mod.cvbox.container.ContainerRepair(player.inventory, world, x, y, z, player);
		}
		return null;
	}

	@Override
    @SideOnly(Side.CLIENT)
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == 0) {
			return new mod.cvbox.gui.GuiRepair(player.inventory, world, z, z, z);
		}
		return null;
	}

}
