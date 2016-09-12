package mod.cvmachine.core;

import mod.cvmachine.creative.CreativeTabCvMachine;
import net.minecraft.block.ModRegisterBlock;
import net.minecraft.item.ModRegisterItem;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

@Mod(modid = ModCommon.MOD_ID, name = ModCommon.MOD_NAME, version = ModCommon.MOD_VERSION)
public class CvMachine {
	@Mod.Instance(ModCommon.MOD_ID)
	public static CvMachine instance;
	@SidedProxy(clientSide = ModCommon.MOD_PACKAGE + ModCommon.MOD_CLIENT_SIDE, serverSide = ModCommon.MOD_PACKAGE + ModCommon.MOD_SERVER_SIDE)
	public static CommonProxy proxy;
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(ModCommon.MOD_CHANEL);
//	public static final McEventHandler mcEvent = new McEventHandler();

	// タブ
	public static final CreativeTabCvMachine tabExBombs = new CreativeTabCvMachine("ConvenienceMechanical");

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		// レンダー設定
		proxy.registerRenderInfomation();

		// エンティティ設定
		RegisterEntity();

		// ブロック登録
		ModRegisterBlock.registerBlock(event);
		// アイテム登録
		ModRegisterItem.RegisterItem(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		// レシピ追加
		this.RegisterRecipe();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event){

	}

	public static final String Entity_NCBomb = "EntityNuclearExplosivePrimed";
	public static final String Entity_TunnelBomb = "EntityTunnemExprimed";
	public static final String Entity_Bomb = "EntityBomb";
	public static final String Entity_WaterBomb = "EntityWaterBomb";
	public static final String Entity_Missile = "EntityMissile";
	public static final String TileEntity_Fuse = "EntityFuse";

	// エンティティを登録する
	public void RegisterEntity(){
			// タイルエンティティの登録
		proxy.registerCompnents();
	}

	/**
	 * レシピを追加する
	 */
	public void RegisterRecipe(){

	}


}
