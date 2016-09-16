package mod.cvbox.event;

import mod.cvbox.gui.GuiExpBank;
import mod.cvbox.inventory.ContainerExpBank;
import mod.cvbox.tileentity.TileEntityExpBank;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class ExpBankGuiHandler implements IGuiHandler {

	public ExpBankGuiHandler(){

	}

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x,y,z);
		if (id == 1){
			TileEntity ent = world.getTileEntity(pos);
			if((ent instanceof TileEntityExpBank)){
				return new ContainerExpBank(player, world,x,y,z);
			}
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x,y,z);
		if (id == 1){
			TileEntity ent = world.getTileEntity(pos);
			if ((ent instanceof TileEntityExpBank)){
				return new GuiExpBank(player, world, x, y, z);
			}
		}
		return null;
	}

}
