package mod.cvbox.client;

import mod.cvbox.core.CommonProxy;
import mod.cvbox.tileentity.TileEntityCompresser;
import mod.cvbox.tileentity.TileEntityCrusher;
import mod.cvbox.tileentity.TileEntityExEnchantmentTable;
import mod.cvbox.tileentity.TileEntityExpBank;
import mod.cvbox.tileentity.TileEntityExpCollector;
import mod.cvbox.tileentity.TileEntityHarvester;
import mod.cvbox.tileentity.TileEntityKiller;
import mod.cvbox.tileentity.TileEntityPlanter;
import mod.cvbox.tileentity.TileEntitySetter;
import mod.cvbox.tileentity.TileEntityStraw;
import mod.cvbox.tileentity.TileEntityWoodHarvester;
import mod.cvbox.tileentity.TileEntityWoodPlanter;
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
		GameRegistry.registerTileEntity(TileEntityPlanter.class, TileEntityPlanter.NAME);
		GameRegistry.registerTileEntity(TileEntityHarvester.class, TileEntityHarvester.NAME);
		GameRegistry.registerTileEntity(TileEntityWoodPlanter.class, TileEntityWoodPlanter.NAME);
		GameRegistry.registerTileEntity(TileEntityWoodHarvester.class, TileEntityWoodHarvester.NAME);
		GameRegistry.registerTileEntity(TileEntitySetter.class, TileEntitySetter.NAME);
		GameRegistry.registerTileEntity(TileEntityKiller.class, TileEntityKiller.NAME);
		GameRegistry.registerTileEntity(TileEntityExpCollector.class, TileEntityExpCollector.NAME);
		GameRegistry.registerTileEntity(TileEntityCrusher.class, TileEntityCrusher.NAME);
		GameRegistry.registerTileEntity(TileEntityCompresser.class, TileEntityCompresser.NAME);
		GameRegistry.registerTileEntity(TileEntityStraw.class, TileEntityStraw.NAME);
	}

	public void registerRender(){

	}

	@Override
    public EntityPlayer getEntityPlayerInstance() {
        return Minecraft.getMinecraft().player;
    }
}