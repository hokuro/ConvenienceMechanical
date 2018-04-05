package mod.cvbox.core;


import mod.cvbox.gui.GuiExEnchantment;
import mod.cvbox.gui.GuiExpBank;
import mod.cvbox.inventory.ContainerExEnchantment;
import mod.cvbox.inventory.ContainerExpBank;
import mod.cvbox.tileentity.TileEntityExpBank;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class ModGui implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x,y,z);
		switch(id){
		case ModCommon.GUIID_EXPBANK:
			TileEntity ent = world.getTileEntity(pos);
			if((ent instanceof TileEntityExpBank)){
				return new ContainerExpBank(player, world,x,y,z);
			}
			break;
		case ModCommon.GUIID_EXENCHANTER:
			return new ContainerExEnchantment(player.inventory, world, pos);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		switch(id){
		case ModCommon.GUIID_EXPBANK:
			BlockPos pos = new BlockPos(x,y,z);
			TileEntity ent = world.getTileEntity(pos);
			if ((ent instanceof TileEntityExpBank)){
				return new GuiExpBank(player, world, x, y, z);
			}
			break;
		case ModCommon.GUIID_EXENCHANTER:
			return new GuiExEnchantment(player.inventory, world);
		}
		return null;
	}

}