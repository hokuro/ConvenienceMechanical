package mod.cvbox.core;

import mod.cvbox.tileentity.TileEntityExEnchantmentTable;
import mod.cvbox.tileentity.TileEntityExpBank;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy{
	public CommonProxy(){
	}

	public World getClientWorld(){
		return null;
	}

	public void registerTileEntity(){
		GameRegistry.registerTileEntity(TileEntityExpBank.class, TileEntityExpBank.REGISTER_NAME);
		GameRegistry.registerTileEntity(TileEntityExEnchantmentTable.class, TileEntityExEnchantmentTable.NAME);
	}

	public void registerRender(){

	}
	public EntityPlayer getEntityPlayerInstance() {return null;}
}