package mod.cvbox.core;

import mod.cvbox.block.BlockCore;
import mod.cvbox.item.ItemCore;
import mod.cvbox.network.MessageExEnchant_ClearParameter;
import mod.cvbox.network.MessageExEnchant_ExecEnchant;
import mod.cvbox.network.MessageExEnchant_UpdateParameter;
import mod.cvbox.network.MessageExpBank_ExecExperience;
import mod.cvbox.network.MessageExpBank_ExperienceInfo;
import mod.cvbox.network.MessageFarmer_UpdateDelivery;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class ModRegister {
	public static void RegisterBlock(FMLPreInitializationEvent event) {
		BlockCore.register(event);
	}

	public static void RegisterItem(FMLPreInitializationEvent event){
		ItemCore.register(event);
	}

	public static void RegisterEntity(CommonProxy proxy){
		// タイルエンティティはserverとclientで登録方法が違う為プロキシで分ける
		proxy.registerTileEntity();
	}

	public static void RegisterRender(CommonProxy proxy){
		// レンダーはクライアントの未登録
		proxy.registerRender();
	}

	public static void RegisterRecipe(){

		// 経験値ボックス
		GameRegistry.addShapedRecipe(BlockCore.block_expbank.getRegistryName(),
				new ResourceLocation(ModCommon.MOD_ID),
				new ItemStack(BlockCore.block_expbank,1),
				new Object[]
						{
						"III",
						"IDI",
						"IBI",
						'I',Items.IRON_INGOT,
						'D',Items.IRON_DOOR,
						'B',Items.GLASS_BOTTLE
						});
		// スーパーエンチャントテーブル
		GameRegistry.addShapedRecipe(BlockCore.block_exenchanter.getRegistryName(),
				new ResourceLocation(ModCommon.MOD_ID),
				new ItemStack(BlockCore.block_exenchanter,1),
				" S ",
				"DED",
				"OOO",
				'O',Blocks.OBSIDIAN,
				'D',Blocks.DIAMOND_BLOCK,
				'E',Blocks.EMERALD_BLOCK,
				'S',Items.NETHER_STAR);

		// スーパーアンビル
		GameRegistry.addShapedRecipe(BlockCore.block_exanvil.getRegistryName(),
				new ResourceLocation(ModCommon.MOD_ID),
				new ItemStack(BlockCore.block_exanvil,1),
				"GGG",
				" G ",
				"GGG",
				'G',Blocks.IRON_BLOCK
				);

		// 種まき機
		GameRegistry.addShapedRecipe(BlockCore.block_planter.getRegistryName(),
				new ResourceLocation(ModCommon.MOD_ID),
				new ItemStack(BlockCore.block_planter,1),
				"WDW",
				"WRW",
				"WKW",
				'R',Blocks.REDSTONE_BLOCK,
				'W',Blocks.PLANKS,
				'D',Blocks.DISPENSER,
				'K',Items.WOODEN_HOE
				);
		GameRegistry.addShapedRecipe(BlockCore.block_planter.getRegistryName(),
				new ResourceLocation(ModCommon.MOD_ID),
				new ItemStack(BlockCore.block_planter,1),
				"WDW",
				"WRW",
				"WKW",
				'R',Blocks.REDSTONE_BLOCK,
				'W',Blocks.PLANKS,
				'D',Blocks.DISPENSER,
				'K',Items.STONE_HOE
				);
		GameRegistry.addShapedRecipe(BlockCore.block_planter.getRegistryName(),
				new ResourceLocation(ModCommon.MOD_ID),
				new ItemStack(BlockCore.block_planter,1),
				"WDW",
				"WRW",
				"WKW",
				'R',Blocks.REDSTONE_BLOCK,
				'W',Blocks.PLANKS,
				'D',Blocks.DISPENSER,
				'K',Items.IRON_HOE
				);
		GameRegistry.addShapedRecipe(BlockCore.block_planter.getRegistryName(),
				new ResourceLocation(ModCommon.MOD_ID),
				new ItemStack(BlockCore.block_planter,1),
				"WDW",
				"WRW",
				"WKW",
				'R',Blocks.REDSTONE_BLOCK,
				'W',Blocks.PLANKS,
				'D',Blocks.DISPENSER,
				'K',Items.GOLDEN_HOE
				);
		GameRegistry.addShapedRecipe(BlockCore.block_planter.getRegistryName(),
				new ResourceLocation(ModCommon.MOD_ID),
				new ItemStack(BlockCore.block_planter,1),
				"WDW",
				"WRW",
				"WKW",
				'R',Blocks.REDSTONE_BLOCK,
				'W',Blocks.PLANKS,
				'D',Blocks.DISPENSER,
				'K',Items.DIAMOND_HOE
				);

		// 収穫機
		GameRegistry.addShapedRecipe(BlockCore.block_harvester.getRegistryName(),
				new ResourceLocation(ModCommon.MOD_ID),
				new ItemStack(BlockCore.block_harvester,1),
				"WDW",
				"WRW",
				"WKW",
				'R',Blocks.REDSTONE_BLOCK,
				'W',Blocks.PLANKS,
				'D',Blocks.HOPPER,
				'K',ItemCore.item_sickle_wood
				);
		GameRegistry.addShapedRecipe(BlockCore.block_harvester.getRegistryName(),
				new ResourceLocation(ModCommon.MOD_ID),
				new ItemStack(BlockCore.block_harvester,1),
				"WDW",
				"WRW",
				"WKW",
				'R',Blocks.REDSTONE_BLOCK,
				'W',Blocks.PLANKS,
				'D',Blocks.HOPPER,
				'K',ItemCore.item_sickle_stone
				);
		GameRegistry.addShapedRecipe(BlockCore.block_harvester.getRegistryName(),
				new ResourceLocation(ModCommon.MOD_ID),
				new ItemStack(BlockCore.block_harvester,1),
				"WDW",
				"WRW",
				"WKW",
				'R',Blocks.REDSTONE_BLOCK,
				'W',Blocks.PLANKS,
				'D',Blocks.HOPPER,
				'K',ItemCore.item_sickle_iron
				);
		GameRegistry.addShapedRecipe(BlockCore.block_harvester.getRegistryName(),
				new ResourceLocation(ModCommon.MOD_ID),
				new ItemStack(BlockCore.block_harvester,1),
				"WDW",
				"WRW",
				"WKW",
				'R',Blocks.REDSTONE_BLOCK,
				'W',Blocks.PLANKS,
				'D',Blocks.HOPPER,
				'K',ItemCore.item_sickle_gold
				);
		GameRegistry.addShapedRecipe(BlockCore.block_harvester.getRegistryName(),
				new ResourceLocation(ModCommon.MOD_ID),
				new ItemStack(BlockCore.block_harvester,1),
				"WDW",
				"WRW",
				"WKW",
				'R',Blocks.REDSTONE_BLOCK,
				'W',Blocks.PLANKS,
				'D',Blocks.HOPPER,
				'K',ItemCore.item_sickle_diamond
				);



		// 鎌
		GameRegistry.addShapedRecipe(ItemCore.item_sickle_wood.getRegistryName(),
				new ResourceLocation(ModCommon.MOD_ID),
				new ItemStack(ItemCore.item_sickle_wood,1),
				"WWW",
				"S W",
				"S  ",
				'S',Items.STICK,
				'W',Blocks.PLANKS
				);
		GameRegistry.addShapedRecipe(ItemCore.item_sickle_stone.getRegistryName(),
				new ResourceLocation(ModCommon.MOD_ID),
				new ItemStack(ItemCore.item_sickle_stone,1),
				"WWW",
				"S W",
				"S  ",
				'S',Items.STICK,
				'W',Blocks.COBBLESTONE
				);
		GameRegistry.addShapedRecipe(ItemCore.item_sickle_iron.getRegistryName(),
				new ResourceLocation(ModCommon.MOD_ID),
				new ItemStack(ItemCore.item_sickle_iron,1),
				"WWW",
				"S W",
				"S  ",
				'S',Items.STICK,
				'W',Items.IRON_INGOT
				);
		GameRegistry.addShapedRecipe(ItemCore.item_sickle_gold.getRegistryName(),
				new ResourceLocation(ModCommon.MOD_ID),
				new ItemStack(ItemCore.item_sickle_gold,1),
				"WWW",
				"S W",
				"S  ",
				'S',Items.STICK,
				'W',Items.GOLD_INGOT
				);
		GameRegistry.addShapedRecipe(ItemCore.item_sickle_diamond.getRegistryName(),
				new ResourceLocation(ModCommon.MOD_ID),
				new ItemStack(ItemCore.item_sickle_diamond,1),
				"WWW",
				"S W",
				"S  ",
				'S',Items.STICK,
				'W',Items.DIAMOND
				);


		// 植林機
		GameRegistry.addShapedRecipe(BlockCore.block_woodplanter.getRegistryName(),
				new ResourceLocation(ModCommon.MOD_ID),
				new ItemStack(BlockCore.block_woodplanter,1),
				"WDW",
				"WRW",
				"WWW",
				'R',Blocks.REDSTONE_BLOCK,
				'W',Blocks.PLANKS,
				'D',Blocks.DISPENSER
				);

		// 収穫機
		GameRegistry.addShapedRecipe(BlockCore.block_woodharvester.getRegistryName(),
				new ResourceLocation(ModCommon.MOD_ID),
				new ItemStack(BlockCore.block_woodharvester,1),
				"WDW",
				"WRW",
				"WKW",
				'R',Blocks.REDSTONE_BLOCK,
				'W',Blocks.PLANKS,
				'D',Blocks.HOPPER,
				'K',Items.WOODEN_AXE
				);
		GameRegistry.addShapedRecipe(BlockCore.block_woodharvester.getRegistryName(),
				new ResourceLocation(ModCommon.MOD_ID),
				new ItemStack(BlockCore.block_woodharvester,1),
				"WDW",
				"WRW",
				"WKW",
				'R',Blocks.REDSTONE_BLOCK,
				'W',Blocks.PLANKS,
				'D',Blocks.HOPPER,
				'K',Items.STONE_AXE
				);
		GameRegistry.addShapedRecipe(BlockCore.block_woodharvester.getRegistryName(),
				new ResourceLocation(ModCommon.MOD_ID),
				new ItemStack(BlockCore.block_woodharvester,1),
				"WDW",
				"WRW",
				"WKW",
				'R',Blocks.REDSTONE_BLOCK,
				'W',Blocks.PLANKS,
				'D',Blocks.HOPPER,
				'K',Items.IRON_AXE
				);
		GameRegistry.addShapedRecipe(BlockCore.block_woodharvester.getRegistryName(),
				new ResourceLocation(ModCommon.MOD_ID),
				new ItemStack(BlockCore.block_woodharvester,1),
				"WDW",
				"WRW",
				"WKW",
				'R',Blocks.REDSTONE_BLOCK,
				'W',Blocks.PLANKS,
				'D',Blocks.HOPPER,
				'K',Items.GOLDEN_AXE
				);
		GameRegistry.addShapedRecipe(BlockCore.block_woodharvester.getRegistryName(),
				new ResourceLocation(ModCommon.MOD_ID),
				new ItemStack(BlockCore.block_woodharvester,1),
				"WDW",
				"WRW",
				"WKW",
				'R',Blocks.REDSTONE_BLOCK,
				'W',Blocks.PLANKS,
				'D',Blocks.HOPPER,
				'K',Items.DIAMOND_AXE
				);

		// 機械の素
		GameRegistry.addShapedRecipe(ItemCore.item_machinematter.getRegistryName(),
				new ResourceLocation(ModCommon.MOD_ID),
				new ItemStack(ItemCore.item_machinematter,1),
				"IRI",
				"DGD",
				"GIG",
				'R',Items.REDSTONE,
				'D',Items.DIAMOND,
				'G',Items.GOLD_INGOT,
				'I',Items.IRON_INGOT
				);

		// 製氷機
		GameRegistry.addShapedRecipe(BlockCore.block_icemaker.getRegistryName(),
				new ResourceLocation(ModCommon.MOD_ID),
				new ItemStack(BlockCore.block_icemaker,1),
				"IKI",
				"KKK",
				"IRI",
				'I',Blocks.ICE,
				'R',Items.REDSTONE,
				'K',ItemCore.item_machinematter
				);
		//　溶解機
		GameRegistry.addShapedRecipe(BlockCore.block_lavamaker.getRegistryName(),
				new ResourceLocation(ModCommon.MOD_ID),
				new ItemStack(BlockCore.block_lavamaker,1),
				"MKM",
				"KKK",
				"MRM",
				'M',Blocks.MAGMA,
				'R',Items.REDSTONE,
				'K',ItemCore.item_machinematter
				);

//		public static final String NAME_DESTROYER = "destroyer";		// 破砕機
//		public static final String NAME_SETTER = "setter";				// 配置機
//		public static final String NAME_KIRER = "kirer";				// 屠殺機
//		public static final String NAME_EXCOLLECTOR = "excollector";	// 経験値収集機
//		public static final String NAME_CRUSSHER = "crussher";			// 粉砕機
//		public static final String NAME_COMPLESSER = "commpleser";		// 圧縮機
//		public static final String NAME_LIQUIDCSTRAWER = "straw";		// 給水機

		// 破砕機
		GameRegistry.addShapedRecipe(BlockCore.block_destroyer.getRegistryName(),
				new ResourceLocation(ModCommon.MOD_ID),
				new ItemStack(BlockCore.block_destroyer,1),
				"TTT",
				"KPK",
				"KRK",
				'T',Blocks.TNT,
				'R',Items.REDSTONE,
				'K',ItemCore.item_machinematter,
				'P',Blocks.PISTON
				);
		// 配置機
		GameRegistry.addShapedRecipe(BlockCore.block_setter.getRegistryName(),
		new ResourceLocation(ModCommon.MOD_ID),
		new ItemStack(BlockCore.block_setter,1),
		"KDK",
		"KRK",
		'D',Blocks.DROPPER,
		'R',Items.REDSTONE,
		'K',ItemCore.item_machinematter
		);
		// 屠殺機
		GameRegistry.addShapedRecipe(BlockCore.block_kirer.getRegistryName(),
				new ResourceLocation(ModCommon.MOD_ID),
				new ItemStack(BlockCore.block_kirer,1),
				" K ",
				"K K",
				" R ",
				'R',Items.REDSTONE,
				'K',ItemCore.item_machinematter
				);
		// 経験値収集機
		GameRegistry.addShapedRecipe(BlockCore.block_excollector.getRegistryName(),
				new ResourceLocation(ModCommon.MOD_ID),
				new ItemStack(BlockCore.block_excollector,1),
				"BKB",
				"KHK",
				"BRB",
				'B',Items.GLASS_BOTTLE,
				'R',Items.REDSTONE,
				'K',ItemCore.item_machinematter,
				'H',Blocks.HOPPER
				);
		// 粉砕機
		GameRegistry.addShapedRecipe(BlockCore.block_crusher.getRegistryName(),
				new ResourceLocation(ModCommon.MOD_ID),
				new ItemStack(BlockCore.block_crusher,1),
				"KKK",
				"KDK",
				"KRK",
				'D',Items.DIAMOND,
				'R',Items.REDSTONE,
				'K',ItemCore.item_machinematter
				);
		// 圧縮機
		GameRegistry.addShapedRecipe(BlockCore.block_compresser.getRegistryName(),
				new ResourceLocation(ModCommon.MOD_ID),
				new ItemStack(BlockCore.block_compresser,1),
				"KPK",
				"PRP",
				"KPK",
				'P',Blocks.PISTON,
				'R',Items.REDSTONE,
				'K',ItemCore.item_machinematter
				);

		// 給水機
		GameRegistry.addShapedRecipe(BlockCore.block_straw.getRegistryName(),
				new ResourceLocation(ModCommon.MOD_ID),
				new ItemStack(BlockCore.block_straw,1),
				"KKK",
				"KBK",
				"KRK",
				'B',Items.BUCKET,
				'R',Items.REDSTONE,
				'K',ItemCore.item_machinematter
				);

//		GameRegistry.addRecipe(new ItemStack(BlockCore.getBlock(BlockExpBank.NAME_EXPBANK), 1, 8),
//				new Object[] { "OGO", "ICI", "ERE", Character.valueOf('O'), Blocks.obsidian,
//						Character.valueOf('E'), Items.ender_pearl, Character.valueOf('I'), Items.iron_ingot,
//						Character.valueOf('R'), Items.redstone, Character.valueOf('C'), Blocks.chest,
//						Character.valueOf('G'), Blocks.glass_pane });
//		for (int n = 0; n <= 14; n++) {
//			for (int m = 0; m <= 16; m++) {
//				if (n != m) {
//					GameRegistry.addShapelessRecipe(
//							new ItemStack(BlockCore.getBlock(BlockExpBank.NAME_EXPBANK), 1, 16-m),
//							new Object[] {
//							new ItemStack(BlockCore.getBlock(BlockExpBank.NAME_EXPBANK), 1, n),
//							new ItemStack(Items.dye, 1, m) });
//				}
//			}
//		}
//
//		GameRegistry.addRecipe(new ItemStack(BlockCore.instance.getBlock(BlockCore.NAME_AUTOPLANTING),1,0),
//				new Object[]{"020",
//							 "010",
//							 "020",
//							'0', Items.wheat_seeds,
//							'1', Blocks.dropper,
//							'2', Items.redstone});
//			GameRegistry.addRecipe(new ItemStack(BlockCore.instance.getBlock(BlockCore.NAME_AUTOPLANTING),1,1),
//				new Object[]{"020",
//							 "010",
//							 "030",
//							'0', Items.wheat_seeds,
//							'1', Blocks.dropper,
//							'2', Items.redstone,
//							'3', Items.beef});
	}

	public static void RegisterMessage(){
		Mod_ConvenienceBox.Net_Instance.registerMessage(MessageExpBank_ExecExperience.class, MessageExpBank_ExecExperience.class, ModCommon.MESSID_MESSAGEECECECPERIENCE, Side.SERVER);
		Mod_ConvenienceBox.Net_Instance.registerMessage(MessageExpBank_ExperienceInfo.class, MessageExpBank_ExperienceInfo.class, ModCommon.MESSID_MESSAGEEXPERIENCEINFO, Side.CLIENT);
		Mod_ConvenienceBox.Net_Instance.registerMessage(MessageExEnchant_UpdateParameter.class, MessageExEnchant_UpdateParameter.class, ModCommon.MESSID_MESSAGEUPDATEENCHANT, Side.SERVER);
		Mod_ConvenienceBox.Net_Instance.registerMessage(MessageExEnchant_ClearParameter.class, MessageExEnchant_ClearParameter.class, ModCommon.MESSID_MESSAGECLEARENCHANT, Side.CLIENT);
		Mod_ConvenienceBox.Net_Instance.registerMessage(MessageExEnchant_ExecEnchant.class, MessageExEnchant_ExecEnchant.class, ModCommon.MESSID_MESSAGEEXECENCHANT, Side.SERVER);
		Mod_ConvenienceBox.Net_Instance.registerMessage(MessageFarmer_UpdateDelivery.class,MessageFarmer_UpdateDelivery.class, ModCommon.MESSID_MESSAGEFARMERUPDATE, Side.SERVER);

	}


}
