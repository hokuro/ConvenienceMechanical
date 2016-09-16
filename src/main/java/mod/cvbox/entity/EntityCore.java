package mod.cvbox.entity;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class EntityCore{
	public static final EntityCore instance = new EntityCore();

	public static final String NAME_TILE_AUTOPLANTING = "TileEntityAutoPlanting";


	private Map<String, Class<? extends TileEntity>> tileList;

	private EntityCore(){
		tileList = new HashMap<String,Class<?extends TileEntity>>();
		tileList.put(NAME_TILE_AUTOPLANTING,TileEntityAutoPlanting.class);
	}

	public void registerTileEntity(){
		for(String key : tileList.keySet()){
			GameRegistry.registerTileEntity(tileList.get(key),key);
		}
	}

}