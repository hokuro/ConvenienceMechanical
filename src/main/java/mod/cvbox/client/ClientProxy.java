package mod.cvbox.client;

import mod.cvbox.core.CommonProxy;
import mod.cvbox.tileentity.TileEntityExEnchantmentTable;
import mod.cvbox.tileentity.TileEntityExpBank;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy{
	public ClientProxy(){
	}

	public World getClientWorld(){
		return FMLClientHandler.instance().getClient().world;
	}

	public void registerTileEntity(){

		GameRegistry.registerTileEntity(TileEntityExpBank.class, TileEntityExpBank.REGISTER_NAME);
		GameRegistry.registerTileEntity(TileEntityExEnchantmentTable.class, TileEntityExEnchantmentTable.NAME);
//		ClientRegistry.registerTileEntity(TileEntityPlanter.class, "AutoPlanting", new TileEntityPlanterRender());
	}

	public void registerRender(){

	}

	@Override
    public EntityPlayer getEntityPlayerInstance() {
        return Minecraft.getMinecraft().player;
    }
}