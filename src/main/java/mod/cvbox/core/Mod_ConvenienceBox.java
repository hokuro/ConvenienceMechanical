package mod.cvbox.core;

import java.util.ArrayList;
import java.util.List;

import mod.cvbox.block.BlockCore;
import mod.cvbox.client.ClientProcess;
import mod.cvbox.config.ConfigValue;
import mod.cvbox.creative.CreativeTabFactBox;
import mod.cvbox.creative.CreativeTabFarmarBox;
import mod.cvbox.creative.CreativeWorkerBox;
import mod.cvbox.entity.EntityCore;
import mod.cvbox.event.McEventHandler;
import mod.cvbox.item.ItemCore;
import mod.cvbox.network.MessageHandler;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;


@Mod(ModCommon.MOD_ID)
public class Mod_ConvenienceBox {

	// タブ
	public static final CreativeWorkerBox tabWorker = new CreativeWorkerBox("WorkerBox");
	public static final CreativeTabFarmarBox tabFarmer = new CreativeTabFarmarBox("FarmerBox");
	public static final CreativeTabFactBox tabFactory = new CreativeTabFactBox("FactoryBox");

	public static List<Enchantment> encList = new ArrayList<Enchantment>();
	public static ClientProcess proxy;
	public McEventHandler handler = new McEventHandler();


    public Mod_ConvenienceBox() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // コンフィグ読み込み
    	ModLoadingContext.get().
        registerConfig(
        		net.minecraftforge.fml.config.ModConfig.Type.COMMON,
        		ConfigValue.spec);
        // Register ourselves for server and other game events we are interested in
    	// メッセージ登録
    	MessageHandler.register();
    	// サウンド登録
    	// イベントバス登録
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(handler);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
    	IRegistry.field_212628_q.forEach((enc)->{
    		encList.add(enc);
    	});
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
    	proxy = new ClientProcess();
    	// エンティティレンダー登録(なし)
    	// タイルエンティティレンダー登録(なし)
    	proxy.registerEntityRender();

    	// GUI登録
    	proxy.guiHandler();
    }

    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
        	BlockCore.registerBlock(blockRegistryEvent);
        }

        @SubscribeEvent
        public static void onItemRegistry(final RegistryEvent.Register<Item> itemRegistryEvent) {
            BlockCore.registerItemBlock(itemRegistryEvent);
            ItemCore.register(itemRegistryEvent);
        }

        @SubscribeEvent
        public static void onEntityRegistry(final RegistryEvent.Register<EntityType<?>> etRegistryEvent){
        	EntityCore.register(etRegistryEvent);
        }

        @SubscribeEvent
        public static void onTERegistyr(final RegistryEvent.Register<TileEntityType<?>> teRegistryEvent){
        	EntityCore.registerTE(teRegistryEvent);
        	//Mod_ExBombs.proxy.registerCompnents(teRegistryEvent);

        }

        @SubscribeEvent
        public static void onSoundRegistyr(final RegistryEvent.Register<SoundEvent> teRegistryEvent){
        }
    }



//	@EventHandler
//	public void postInit(FMLPostInitializationEvent event){
////		// コンフィグ初期設定
////		ConfigValue.setting();
////
//		for (Enchantment enc : Enchantment.REGISTRY){
//			encList.add(enc);
//		}
//	}
}
