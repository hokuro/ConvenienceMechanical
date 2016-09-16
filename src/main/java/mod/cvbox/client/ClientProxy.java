package mod.cvbox.client;

import mod.cvbox.core.CommonProxy;
import mod.cvbox.entity.TileEntityAutoPlanting;
import mod.cvbox.entity.TileEntityAutoPlantingRender;
import mod.cvbox.tileentity.TileEntityEcpBankRender;
import mod.cvbox.tileentity.TileEntityExpBank;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy{
	public ClientProxy(){
	}

	public World getClientWorld(){
		return FMLClientHandler.instance().getClient().theWorld;
	}

	public void registerTileEntity(){
		ClientRegistry.registerTileEntity(TileEntityAutoPlanting.class, "AutoPlanting", new TileEntityAutoPlantingRender());
		ClientRegistry.registerTileEntity(TileEntityExpBank.class, "ExpBanking", new TileEntityEcpBankRender());
	}

	public void registerRender(){

	}
}