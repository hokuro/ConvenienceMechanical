package mod.cvbox.core;

import java.util.ArrayList;
import java.util.List;

import mod.cvbox.block.BlockCore;
import mod.cvbox.config.ConfigValue;
import mod.cvbox.creative.CreativeTabFactBox;
import mod.cvbox.creative.CreativeTabFarmarBox;
import mod.cvbox.creative.CreativeWorkerBox;
import mod.cvbox.entity.EntityCore;
import mod.cvbox.gui.factory.GuiBedrockMaker;
import mod.cvbox.gui.factory.GuiCompresser;
import mod.cvbox.gui.factory.GuiCrusher;
import mod.cvbox.gui.factory.GuiDeliverBox;
import mod.cvbox.gui.factory.GuiDestroyer;
import mod.cvbox.gui.factory.GuiExpCollector;
import mod.cvbox.gui.factory.GuiGenomAnalyser;
import mod.cvbox.gui.factory.GuiIPSCellMaker;
import mod.cvbox.gui.factory.GuiKiller;
import mod.cvbox.gui.factory.GuiLiquidMaker;
import mod.cvbox.gui.factory.GuiLivingCreator;
import mod.cvbox.gui.factory.GuiSetter;
import mod.cvbox.gui.factory.GuiStraw;
import mod.cvbox.gui.factory.GuiVacumer;
import mod.cvbox.gui.farm.GuiAutoFeed;
import mod.cvbox.gui.farm.GuiMillking;
import mod.cvbox.gui.farm.GuiPlantNurture;
import mod.cvbox.gui.farm.GuiWoolCutting;
import mod.cvbox.gui.work.GuiExEnchantment;
import mod.cvbox.gui.work.GuiExRepair;
import mod.cvbox.gui.work.GuiExpBank;
import mod.cvbox.inventory.factory.ContainerBedrockMaker;
import mod.cvbox.inventory.factory.ContainerComplesser;
import mod.cvbox.inventory.factory.ContainerCrusher;
import mod.cvbox.inventory.factory.ContainerDeliverBox;
import mod.cvbox.inventory.factory.ContainerDestroyer;
import mod.cvbox.inventory.factory.ContainerExpCollector;
import mod.cvbox.inventory.factory.ContainerGenomAnalyser;
import mod.cvbox.inventory.factory.ContainerIPSCellMaker;
import mod.cvbox.inventory.factory.ContainerKiller;
import mod.cvbox.inventory.factory.ContainerLiquidMaker;
import mod.cvbox.inventory.factory.ContainerLivingCreator;
import mod.cvbox.inventory.factory.ContainerSetter;
import mod.cvbox.inventory.factory.ContainerStraw;
import mod.cvbox.inventory.factory.ContainerVacumer;
import mod.cvbox.inventory.farm.ContainerAutoFeed;
import mod.cvbox.inventory.farm.ContainerMillking;
import mod.cvbox.inventory.farm.ContainerPlantNurture;
import mod.cvbox.inventory.farm.ContainerPlantNurture.NURTUREKIND;
import mod.cvbox.inventory.farm.ContainerPlantNurture.NURTURETYPE;
import mod.cvbox.inventory.farm.ContainerWoolCutting;
import mod.cvbox.inventory.work.ContainerExEnchantment;
import mod.cvbox.inventory.work.ContainerExRepair;
import mod.cvbox.inventory.work.ContainerExpBank;
import mod.cvbox.item.ItemCore;
import mod.cvbox.network.MessageHandler;
import mod.cvbox.render.RenderLiquidMaker;
import mod.cvbox.tileentity.ab.TileEntityPlantNurtureBase;
import mod.cvbox.tileentity.factory.TileEntityBedrockMaker;
import mod.cvbox.tileentity.factory.TileEntityCompresser;
import mod.cvbox.tileentity.factory.TileEntityCrusher;
import mod.cvbox.tileentity.factory.TileEntityDeliverBox;
import mod.cvbox.tileentity.factory.TileEntityDestroyer;
import mod.cvbox.tileentity.factory.TileEntityExpCollector;
import mod.cvbox.tileentity.factory.TileEntityGenomAnalyser;
import mod.cvbox.tileentity.factory.TileEntityIPSCellMaker;
import mod.cvbox.tileentity.factory.TileEntityKiller;
import mod.cvbox.tileentity.factory.TileEntityLiquidMaker;
import mod.cvbox.tileentity.factory.TileEntityLivingCreator;
import mod.cvbox.tileentity.factory.TileEntitySetter;
import mod.cvbox.tileentity.factory.TileEntityStraw;
import mod.cvbox.tileentity.factory.TileEntityVacumer;
import mod.cvbox.tileentity.farm.TileEntityAutoFeed;
import mod.cvbox.tileentity.farm.TileEntityMillking;
import mod.cvbox.tileentity.farm.TileEntityWoolCutting;
import mod.cvbox.tileentity.work.TileEntityExEnchantmentTable;
import mod.cvbox.tileentity.work.TileEntityExpBank;
import net.minecraft.block.Block;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ObjectHolder;


@Mod(ModCommon.MOD_ID)
public class Mod_ConvenienceBox {

	// タブ
	public static final CreativeWorkerBox tabWorker = new CreativeWorkerBox("WorkerBox");
	public static final CreativeTabFarmarBox tabFarmer = new CreativeTabFarmarBox("FarmerBox");
	public static final CreativeTabFactBox tabFactory = new CreativeTabFactBox("FactoryBox");

	public static List<Enchantment> encList = new ArrayList<Enchantment>();

    public Mod_ConvenienceBox() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(ContainerType.class, this::onContainerRegistry);

        BlockCore.init();
        ItemCore.init();

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
    }

    private void setup(final FMLCommonSetupEvent event) {
    	Registry.ENCHANTMENT.forEach((enc)->{
    		encList.add(enc);
    	});
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
    	// エンティティレンダー登録(なし)
    	// タイルエンティティレンダー登録(なし)
    	registerEntityRender();

    	// GUI登録
    	registerGui();
    }

	public void registerEntityRender(){
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLiquidMaker.class, new RenderLiquidMaker());
	}

	public void registerGui(){
		ScreenManager.registerFactory(CONTIANER_COMPLESSER, GuiCompresser::new);
		ScreenManager.registerFactory(CONTAINER_CRUSHER, GuiCrusher::new);
		ScreenManager.registerFactory(CONTAINER_DESTROYER, GuiDestroyer::new);
		ScreenManager.registerFactory(CONTAINER_EXENCHANTMENT,GuiExEnchantment::new);
		ScreenManager.registerFactory(CONTAINER_EXPBANK,GuiExpBank::new);
		ScreenManager.registerFactory(CONTAINER_EXPCOLLECTOR,GuiExpCollector::new);
		ScreenManager.registerFactory(CONTAINER_EXREPAIR,GuiExRepair::new);
		ScreenManager.registerFactory(CONTAINER_KILLER,GuiKiller::new);
		ScreenManager.registerFactory(CONTAINER_LIQUIDMAKER, GuiLiquidMaker::new);
		ScreenManager.registerFactory(CONTAINER_SETTER, GuiSetter::new);
		ScreenManager.registerFactory(CONTAINER_STRAW, GuiStraw::new);
		ScreenManager.registerFactory(CONTIANER_VACUMER, GuiVacumer::new);
		ScreenManager.registerFactory(CONTAINER_PLANTNURTURE,GuiPlantNurture::new);
		ScreenManager.registerFactory(CONTAINER_MILLKING, GuiMillking::new);
		ScreenManager.registerFactory(CONTAINER_WOOLCUTTING, GuiWoolCutting::new);
		ScreenManager.registerFactory(CONTAINER_AUTOFEED, GuiAutoFeed::new);
		ScreenManager.registerFactory(CONTAINER_GENOMANALYSER, GuiGenomAnalyser::new);
		ScreenManager.registerFactory(CONTAINER_IPSCELLMAKER, GuiIPSCellMaker::new);
		ScreenManager.registerFactory(CONTAINER_LIVINGCREATOR, GuiLivingCreator::new);
		ScreenManager.registerFactory(CONTAINER_BEDROCKMAKER, GuiBedrockMaker::new);
		ScreenManager.registerFactory(CONTAINER_DELIVERBOX,GuiDeliverBox::new);
	}

	@ObjectHolder(ModCommon.MOD_ID + ":" + ModCommon.MOD_CONTAINERID_COMPLESSER)
	public static ContainerType<ContainerComplesser> CONTIANER_COMPLESSER;
	@ObjectHolder(ModCommon.MOD_ID + ":" + ModCommon.MOD_CONTAINERID_CRUSHER)
	public static ContainerType<ContainerCrusher> CONTAINER_CRUSHER;
	@ObjectHolder(ModCommon.MOD_ID + ":" + ModCommon.MOD_CONTAINERID_DESTROYER)
	public static ContainerType<ContainerDestroyer> CONTAINER_DESTROYER;
	@ObjectHolder(ModCommon.MOD_ID + ":" + ModCommon.MOD_CONTAINERID_EXENCHANTMENT)
	public static ContainerType<ContainerExEnchantment> CONTAINER_EXENCHANTMENT;
	@ObjectHolder(ModCommon.MOD_ID + ":" + ModCommon.MOD_CONTAINERID_EXPBANK)
	public static ContainerType<ContainerExpBank> CONTAINER_EXPBANK;
	@ObjectHolder(ModCommon.MOD_ID + ":" + ModCommon.MOD_CONTAINERID_EXPCOLLECTOR)
	public static ContainerType<ContainerExpCollector> CONTAINER_EXPCOLLECTOR;
	@ObjectHolder(ModCommon.MOD_ID + ":" + ModCommon.MOD_CONTAINERID_EXREPAIR)
	public static ContainerType<ContainerExRepair> CONTAINER_EXREPAIR;
	@ObjectHolder(ModCommon.MOD_ID + ":" + ModCommon.MOD_CONTAINERID_KILLER)
	public static ContainerType<ContainerKiller> CONTAINER_KILLER;
	@ObjectHolder(ModCommon.MOD_ID + ":" + ModCommon.MOD_CONTAINERID_LIQUIDMAKER)
	public static ContainerType<ContainerLiquidMaker> CONTAINER_LIQUIDMAKER;
	@ObjectHolder(ModCommon.MOD_ID + ":" + ModCommon.MOD_CONTAINERID_SETTER)
	public static ContainerType<ContainerSetter> CONTAINER_SETTER;
	@ObjectHolder(ModCommon.MOD_ID + ":" + ModCommon.MOD_CONTAINERID_STRAW)
	public static ContainerType<ContainerStraw> CONTAINER_STRAW;
	@ObjectHolder(ModCommon.MOD_ID + ":" + ModCommon.MOD_CONTAINERID_VACUMER)
	public static ContainerType<ContainerVacumer> CONTIANER_VACUMER;
	@ObjectHolder(ModCommon.MOD_ID + ":" + ModCommon.MOD_CONTAINERID_PLANTNURTURE)
	public static ContainerType<ContainerPlantNurture> CONTAINER_PLANTNURTURE;
	@ObjectHolder(ModCommon.MOD_ID + ":" + ModCommon.MOD_CONTAINERID_MILLKING)
	public static ContainerType<ContainerMillking> CONTAINER_MILLKING;
	@ObjectHolder(ModCommon.MOD_ID + ":" + ModCommon.MOD_CONTAINERID_WOOLCUTTING)
	public static ContainerType<ContainerWoolCutting> CONTAINER_WOOLCUTTING;
	@ObjectHolder(ModCommon.MOD_ID + ":" + ModCommon.MOD_CONTAINERID_AUTOFEED)
	public static ContainerType<ContainerAutoFeed> CONTAINER_AUTOFEED;
	@ObjectHolder(ModCommon.MOD_ID + ":" + ModCommon.MOD_CONTAINERID_GENOMANALYSER)
	public static ContainerType<ContainerGenomAnalyser> CONTAINER_GENOMANALYSER;
	@ObjectHolder(ModCommon.MOD_ID + ":" + ModCommon.MOD_CONTAINERID_IPSCELLMAKER)
	public static ContainerType<ContainerIPSCellMaker> CONTAINER_IPSCELLMAKER;
	@ObjectHolder(ModCommon.MOD_ID + ":" + ModCommon.MOD_CONTAINERID_LIVINGCREATOR)
	public static ContainerType<ContainerLivingCreator> CONTAINER_LIVINGCREATOR;
	@ObjectHolder(ModCommon.MOD_ID + ":" + ModCommon.MOD_CONTAINERID_BEDROCKMAKER)
	public static ContainerType<ContainerBedrockMaker> CONTAINER_BEDROCKMAKER;
	@ObjectHolder(ModCommon.MOD_ID + ":" + ModCommon.MOD_CONTAINERID_DELIVERBOX)
	public static ContainerType<ContainerDeliverBox> CONTAINER_DELIVERBOX;

    @SubscribeEvent
	public void onContainerRegistry(final RegistryEvent.Register<ContainerType<?>> event) {

		event.getRegistry().register(IForgeContainerType.create((wid, playerInv, extraData)->{
			int x = extraData.readInt();
			int y = extraData.readInt();
			int z = extraData.readInt();
			TileEntity ent = playerInv.player.world.getTileEntity(new BlockPos(x,y,z));
			if (ent instanceof TileEntityCompresser) {
				return new ContainerComplesser(wid, playerInv, ent);
			}
			return null;
		}).setRegistryName(ModCommon.MOD_ID + ":" + ModCommon.MOD_CONTAINERID_COMPLESSER));

		event.getRegistry().register(IForgeContainerType.create((wid, playerInv, extraData)->{
			int x = extraData.readInt();
			int y = extraData.readInt();
			int z = extraData.readInt();
			TileEntity ent = playerInv.player.world.getTileEntity(new BlockPos(x,y,z));
			if (ent instanceof TileEntityCrusher) {
				return new ContainerCrusher(wid, playerInv, ent);
			}
			return null;
		}).setRegistryName(ModCommon.MOD_ID + ":" + ModCommon.MOD_CONTAINERID_CRUSHER));

		event.getRegistry().register(IForgeContainerType.create((wid, playerInv, extraData)->{
			int x = extraData.readInt();
			int y = extraData.readInt();
			int z = extraData.readInt();
			TileEntity ent = playerInv.player.world.getTileEntity(new BlockPos(x,y,z));
			if (ent instanceof TileEntityDestroyer) {
				return new ContainerDestroyer(wid, playerInv, ent);
			}
			return null;
		}).setRegistryName(ModCommon.MOD_ID + ":" + ModCommon.MOD_CONTAINERID_DESTROYER));

		event.getRegistry().register(IForgeContainerType.create((wid, playerInv, extraData)->{
			int x = extraData.readInt();
			int y = extraData.readInt();
			int z = extraData.readInt();
			TileEntity ent = playerInv.player.world.getTileEntity(new BlockPos(x,y,z));
			if (ent instanceof TileEntityExEnchantmentTable) {
				return new ContainerExEnchantment(wid, playerInv, new BlockPos(x,y,z));
			}
			return null;
		}).setRegistryName(ModCommon.MOD_ID + ":" + ModCommon.MOD_CONTAINERID_EXENCHANTMENT));

		event.getRegistry().register(IForgeContainerType.create((wid, playerInv, extraData)->{
			int x = extraData.readInt();
			int y = extraData.readInt();
			int z = extraData.readInt();
			TileEntity ent = playerInv.player.world.getTileEntity(new BlockPos(x,y,z));
			if (ent instanceof TileEntityExpBank) {
				return new ContainerExpBank(wid, playerInv, ent);
			}
			return null;
		}).setRegistryName(ModCommon.MOD_ID + ":" + ModCommon.MOD_CONTAINERID_EXPBANK));

		event.getRegistry().register(IForgeContainerType.create((wid, playerInv, extraData)->{
			int x = extraData.readInt();
			int y = extraData.readInt();
			int z = extraData.readInt();
			TileEntity ent = playerInv.player.world.getTileEntity(new BlockPos(x,y,z));
			if (ent instanceof TileEntityExpCollector) {
				return new ContainerExpCollector(wid, playerInv, ent);
			}
			return null;
		}).setRegistryName(ModCommon.MOD_ID + ":" + ModCommon.MOD_CONTAINERID_EXPCOLLECTOR));

		event.getRegistry().register(IForgeContainerType.create((wid, playerInv, extraData)->{
			int x = extraData.readInt();
			int y = extraData.readInt();
			int z = extraData.readInt();
			return new ContainerExRepair(wid, playerInv,  IWorldPosCallable.of(playerInv.player.world, new BlockPos(x,y,z)));
		}).setRegistryName(ModCommon.MOD_ID + ":" + ModCommon.MOD_CONTAINERID_EXREPAIR));

		event.getRegistry().register(IForgeContainerType.create((wid, playerInv, extraData)->{
			int x = extraData.readInt();
			int y = extraData.readInt();
			int z = extraData.readInt();
			TileEntity ent = playerInv.player.world.getTileEntity(new BlockPos(x,y,z));
			if (ent instanceof TileEntityKiller) {
				return new ContainerKiller(wid, playerInv, ent);
			}
			return null;
		}).setRegistryName(ModCommon.MOD_ID + ":" + ModCommon.MOD_CONTAINERID_KILLER));

		event.getRegistry().register(IForgeContainerType.create((wid, playerInv, extraData)->{
			int x = extraData.readInt();
			int y = extraData.readInt();
			int z = extraData.readInt();
			TileEntity ent = playerInv.player.world.getTileEntity(new BlockPos(x,y,z));
			if (ent instanceof TileEntityLiquidMaker) {
				return new ContainerLiquidMaker(wid, playerInv, ent);
			}
			return null;
		}).setRegistryName(ModCommon.MOD_ID + ":" + ModCommon.MOD_CONTAINERID_LIQUIDMAKER));

		event.getRegistry().register(IForgeContainerType.create((wid, playerInv, extraData)->{
			int x = extraData.readInt();
			int y = extraData.readInt();
			int z = extraData.readInt();
			TileEntity ent = playerInv.player.world.getTileEntity(new BlockPos(x,y,z));
			if (ent instanceof TileEntitySetter) {
				return new ContainerSetter(wid, playerInv, ent);
			}
			return null;
		}).setRegistryName(ModCommon.MOD_ID + ":" + ModCommon.MOD_CONTAINERID_SETTER));

		event.getRegistry().register(IForgeContainerType.create((wid, playerInv, extraData)->{
			int x = extraData.readInt();
			int y = extraData.readInt();
			int z = extraData.readInt();
			TileEntity ent = playerInv.player.world.getTileEntity(new BlockPos(x,y,z));
			if (ent instanceof TileEntityStraw) {
				return new ContainerStraw(wid, playerInv, ent);
			}
			return null;
		}).setRegistryName(ModCommon.MOD_ID + ":" + ModCommon.MOD_CONTAINERID_STRAW));

		event.getRegistry().register(IForgeContainerType.create((wid, playerInv, extraData)->{
			int x = extraData.readInt();
			int y = extraData.readInt();
			int z = extraData.readInt();
			TileEntity ent = playerInv.player.world.getTileEntity(new BlockPos(x,y,z));
			if (ent instanceof TileEntityVacumer) {
				return new ContainerVacumer(wid, playerInv, ent);
			}
			return null;
		}).setRegistryName(ModCommon.MOD_ID + ":" + ModCommon.MOD_CONTAINERID_VACUMER));

		event.getRegistry().register(IForgeContainerType.create((wid, playerInv, extraData)->{
			int x = extraData.readInt();
			int y = extraData.readInt();
			int z = extraData.readInt();
			int type = extraData.readInt();
			int target = extraData.readInt();
			TileEntity ent = playerInv.player.world.getTileEntity(new BlockPos(x,y,z));
			if (ent instanceof TileEntityPlantNurtureBase) {
				return new ContainerPlantNurture(wid, playerInv, ent, NURTURETYPE.getByIndex(type), NURTUREKIND.getByIndex(target));
			}
			return null;
		}).setRegistryName(ModCommon.MOD_ID + ":" + ModCommon.MOD_CONTAINERID_PLANTNURTURE));

		event.getRegistry().register(IForgeContainerType.create((wid, playerInv, extraData)->{
			int x = extraData.readInt();
			int y = extraData.readInt();
			int z = extraData.readInt();
			TileEntity ent = playerInv.player.world.getTileEntity(new BlockPos(x,y,z));
			if (ent instanceof TileEntityMillking) {
				return new ContainerMillking(wid, playerInv, ent);
			}
			return null;
		}).setRegistryName(ModCommon.MOD_ID + ":" + ModCommon.MOD_CONTAINERID_MILLKING));

		event.getRegistry().register(IForgeContainerType.create((wid, playerInv, extraData)->{
			int x = extraData.readInt();
			int y = extraData.readInt();
			int z = extraData.readInt();
			TileEntity ent = playerInv.player.world.getTileEntity(new BlockPos(x,y,z));
			if (ent instanceof TileEntityWoolCutting) {
				return new ContainerWoolCutting(wid, playerInv, ent);
			}
			return null;
		}).setRegistryName(ModCommon.MOD_ID + ":" + ModCommon.MOD_CONTAINERID_WOOLCUTTING));

		event.getRegistry().register(IForgeContainerType.create((wid, playerInv, extraData)->{
			int x = extraData.readInt();
			int y = extraData.readInt();
			int z = extraData.readInt();
			TileEntity ent = playerInv.player.world.getTileEntity(new BlockPos(x,y,z));
			if (ent instanceof TileEntityAutoFeed) {
				return new ContainerAutoFeed(wid, playerInv, ent);
			}
			return null;
		}).setRegistryName(ModCommon.MOD_ID + ":" + ModCommon.MOD_CONTAINERID_AUTOFEED));

		event.getRegistry().register(IForgeContainerType.create((wid, playerInv, extraData)->{
			int x = extraData.readInt();
			int y = extraData.readInt();
			int z = extraData.readInt();
			TileEntity ent = playerInv.player.world.getTileEntity(new BlockPos(x,y,z));
			if (ent instanceof TileEntityGenomAnalyser) {
				return new ContainerGenomAnalyser(wid, playerInv, ent);
			}
			return null;
		}).setRegistryName(ModCommon.MOD_ID + ":" + ModCommon.MOD_CONTAINERID_GENOMANALYSER));

		event.getRegistry().register(IForgeContainerType.create((wid, playerInv, extraData)->{
			int x = extraData.readInt();
			int y = extraData.readInt();
			int z = extraData.readInt();
			TileEntity ent = playerInv.player.world.getTileEntity(new BlockPos(x,y,z));
			if (ent instanceof TileEntityIPSCellMaker) {
				return new ContainerIPSCellMaker(wid, playerInv, ent);
			}
			return null;
		}).setRegistryName(ModCommon.MOD_ID + ":" + ModCommon.MOD_CONTAINERID_IPSCELLMAKER));

		event.getRegistry().register(IForgeContainerType.create((wid, playerInv, extraData)->{
			int x = extraData.readInt();
			int y = extraData.readInt();
			int z = extraData.readInt();
			TileEntity ent = playerInv.player.world.getTileEntity(new BlockPos(x,y,z));
			if (ent instanceof TileEntityLivingCreator) {
				return new ContainerLivingCreator(wid, playerInv, ent);
			}
			return null;
		}).setRegistryName(ModCommon.MOD_ID + ":" + ModCommon.MOD_CONTAINERID_LIVINGCREATOR));

		event.getRegistry().register(IForgeContainerType.create((wid, playerInv, extraData)->{
			int x = extraData.readInt();
			int y = extraData.readInt();
			int z = extraData.readInt();
			TileEntity ent = playerInv.player.world.getTileEntity(new BlockPos(x,y,z));
			if (ent instanceof TileEntityBedrockMaker) {
				return new ContainerBedrockMaker(wid, playerInv, ent);
			}
			return null;
		}).setRegistryName(ModCommon.MOD_ID + ":" + ModCommon.MOD_CONTAINERID_BEDROCKMAKER));

		event.getRegistry().register(IForgeContainerType.create((wid, playerInv, extraData)->{
			int x = extraData.readInt();
			int y = extraData.readInt();
			int z = extraData.readInt();
			TileEntity ent = playerInv.player.world.getTileEntity(new BlockPos(x,y,z));
			if (ent instanceof TileEntityDeliverBox) {
				return new ContainerDeliverBox(wid, playerInv, ent);
			}
			return null;
		}).setRegistryName(ModCommon.MOD_ID + ":" + ModCommon.MOD_CONTAINERID_DELIVERBOX));
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
            BlockCore.registerBlockItem(itemRegistryEvent);
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
}
