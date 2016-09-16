package mod.cvbox.core;

import mod.cvbox.creative.CreativeTabCvBox;
import mod.cvbox.event.ExpBankGuiHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

@Mod(modid = ModCommon.MOD_ID, name = ModCommon.MOD_NAME, version = ModCommon.MOD_VERSION)
public class Mod_ConvienienceBox {
	@Mod.Instance(ModCommon.MOD_ID)
	public static Mod_ConvienienceBox instance;
	@SidedProxy(clientSide = ModCommon.MOD_PACKAGE + ModCommon.MOD_CLIENT_SIDE, serverSide = ModCommon.MOD_PACKAGE + ModCommon.MOD_SERVER_SIDE)
	public static CommonProxy proxy;
	public static final SimpleNetworkWrapper Net_Instance = NetworkRegistry.INSTANCE.newSimpleChannel(ModCommon.MOD_CHANEL);

	// タブ
	public static final CreativeTabCvBox tabCvBox = new CreativeTabCvBox("ConvenienceBoxes");

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		// レンダー設定
		ModRegister.RegisterRender(proxy);

		// エンティティ設定
		ModRegister.RegisterEntity(proxy);

		// ブロック登録
		ModRegister.RegisterBlock(event);
		// アイテム登録
		ModRegister.RegisterItem(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		// レシピ追加
		ModRegister.RegisterRecipe();



		proxy.registerTileEntity();

		NetworkRegistry.INSTANCE.registerGuiHandler(this, new ExpBankGuiHandler());
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





//
//
//
//	public static final int guiIdAutoSowSeed = 0;
//
//	public static boolean classInheritanceCheck = false;
//	public static boolean rightClick = true;
//	public static World worldDM;
//
//	public AutoPlanting(){}
//
//	@EventHandler
//	public void preInit(FMLPreInitializationEvent event){
//		ConfigValue.init(event);
//		BlockCore.instance.registerBlock(event);
//		RecipieCore.instance.registerRecipe(event);
//	}
//
//	@EventHandler
//	public void init(FMLInitializationEvent event){
//		EntityCore.instance.registerTileEntity();
//		if(FMLCommonHandler.instance().getSide().isClient()){
//			proxy.registerCompnents();
//		}
//		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiTileEntityHandler());
//
//	//	packetPipeline1.init(ModCommon.MOD_CHANNEL1);
//		//packetPipeline1.registerPacket(PacketGuiButton.class);
//
//		packetPipeline2.init(ModCommon.MOD_CHANNEL2);
//		packetPipeline2.registerPacket(PacketAutoPlanting.class);
//	}
//
//	@EventHandler
//	public void postInit(FMLPostInitializationEvent event){
//	//	packetPipeline1.postInit();
//		packetPipeline2.postInit();
//	}


}
