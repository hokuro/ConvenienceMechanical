package mod.cvbox.core;

import mod.cvbox.tileentity.TileEntityExEnchantmentTable;
import mod.cvbox.tileentity.TileEntityExpBank;
import mod.cvbox.tileentity.TileEntityExpCollector;
import mod.cvbox.tileentity.TileEntityHarvester;
import mod.cvbox.tileentity.TileEntityKiller;
import mod.cvbox.tileentity.TileEntityPlanter;
import mod.cvbox.tileentity.TileEntitySetter;
import mod.cvbox.tileentity.TileEntityWoodHarvester;
import mod.cvbox.tileentity.TileEntityWoodPlanter;
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
		GameRegistry.registerTileEntity(TileEntityPlanter.class, TileEntityPlanter.NAME);
		GameRegistry.registerTileEntity(TileEntityHarvester.class, TileEntityHarvester.NAME);
		GameRegistry.registerTileEntity(TileEntityWoodPlanter.class, TileEntityWoodPlanter.NAME);
		GameRegistry.registerTileEntity(TileEntityWoodHarvester.class, TileEntityWoodHarvester.NAME);
		GameRegistry.registerTileEntity(TileEntitySetter.class, TileEntitySetter.NAME);
		GameRegistry.registerTileEntity(TileEntityKiller.class, TileEntityKiller.NAME);
		GameRegistry.registerTileEntity(TileEntityExpCollector.class, TileEntityExpCollector.NAME);
	}

	public void registerRender(){

	}
	public EntityPlayer getEntityPlayerInstance() {return null;}
}