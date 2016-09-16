package mod.cvbox.client;

import mod.cvbox.core.CommonProxy;
import mod.cvbox.render.TileEntityEcpBankRender;
import mod.cvbox.render.TileEntityPlanterRender;
import mod.cvbox.tileentity.TileEntityPlanter;
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
		ClientRegistry.registerTileEntity(TileEntityPlanter.class, "AutoPlanting", new TileEntityPlanterRender());
		ClientRegistry.registerTileEntity(TileEntityExpBank.class, "ExpBanking", new TileEntityEcpBankRender());
	}

	public void registerRender(){

	}
}