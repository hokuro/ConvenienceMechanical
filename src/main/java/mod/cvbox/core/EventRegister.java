package mod.cvbox.core;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.CraftingHelper.ShapedPrimer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class EventRegister {

    public static void addShapedRecipe(RegistryEvent.Register<IRecipe> event, ResourceLocation name, ResourceLocation group, @Nonnull ItemStack output, Object... params)
    {
        ShapedPrimer primer = CraftingHelper.parseShaped(params);
        event.getRegistry().register(new ShapedRecipes(group == null ? "" : group.toString(), primer.width, primer.height, primer.input, output).setRegistryName(name));
    }

    public static void addShapelessRecipe(RegistryEvent.Register<IRecipe> event, ResourceLocation name, ResourceLocation group, @Nonnull ItemStack output, Ingredient... params)
    {
        NonNullList<Ingredient> lst = NonNullList.create();
        for (Ingredient i : params)
            lst.add(i);
        event.getRegistry().register((new ShapelessRecipes(group == null ? "" : group.toString(), output, lst).setRegistryName(name)));
    }

	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event){
//		// 経験値ボックス
//		addShapedRecipe(event,BlockCore.block_expbank.getRegistryName(),
//				new ResourceLocation(ModCommon.MOD_ID),
//				new ItemStack(BlockCore.block_expbank,1),
//				new Object[]
//						{
//						"III",
//						"IDI",
//						"IBI",
//						'I',Items.IRON_INGOT,
//						'D',Items.IRON_DOOR,
//						'B',Items.GLASS_BOTTLE
//						});
//
//		// スーパーエンチャントテーブル
//		addShapedRecipe(event,BlockCore.block_exenchanter.getRegistryName(),
//				new ResourceLocation(ModCommon.MOD_ID),
//				new ItemStack(BlockCore.block_exenchanter,1),
//				" S ",
//				"DED",
//				"OOO",
//				'O',Blocks.OBSIDIAN,
//				'D',Blocks.DIAMOND_BLOCK,
//				'E',Blocks.EMERALD_BLOCK,
//				'S',Items.NETHER_STAR);
//
//		// スーパーアンビル
//		addShapedRecipe(event,BlockCore.block_exanvil.getRegistryName(),
//				new ResourceLocation(ModCommon.MOD_ID),
//				new ItemStack(BlockCore.block_exanvil,1),
//				"GGG",
//				" G ",
//				"GGG",
//				'G',Blocks.IRON_BLOCK
//				);
//
//		// 種まき機
//		addShapedRecipe(event,
//				new ResourceLocation(BlockCore.block_planter.getRegistryName().getResourceDomain() + ":" + BlockCore.block_planter.getRegistryName().getResourcePath()+"_wood"),
//				new ResourceLocation(ModCommon.MOD_ID),
//				new ItemStack(BlockCore.block_planter,1),
//				"WDW",
//				"WRW",
//				"WKW",
//				'R',Blocks.REDSTONE_BLOCK,
//				'W',Blocks.PLANKS,
//				'D',Blocks.DISPENSER,
//				'K',Items.WOODEN_HOE
//				);
//		addShapedRecipe(event,
//				new ResourceLocation(BlockCore.block_planter.getRegistryName().getResourceDomain() + ":" + BlockCore.block_planter.getRegistryName().getResourcePath()+"_stone"),
//				new ResourceLocation(ModCommon.MOD_ID),
//				new ItemStack(BlockCore.block_planter,1),
//				"WDW",
//				"WRW",
//				"WKW",
//				'R',Blocks.REDSTONE_BLOCK,
//				'W',Blocks.PLANKS,
//				'D',Blocks.DISPENSER,
//				'K',Items.STONE_HOE
//				);
//		addShapedRecipe(event,
//				new ResourceLocation(BlockCore.block_planter.getRegistryName().getResourceDomain() + ":" + BlockCore.block_planter.getRegistryName().getResourcePath()+"_iron"),
//				new ResourceLocation(ModCommon.MOD_ID),
//				new ItemStack(BlockCore.block_planter,1),
//				"WDW",
//				"WRW",
//				"WKW",
//				'R',Blocks.REDSTONE_BLOCK,
//				'W',Blocks.PLANKS,
//				'D',Blocks.DISPENSER,
//				'K',Items.IRON_HOE
//				);
//		addShapedRecipe(event,
//				new ResourceLocation(BlockCore.block_planter.getRegistryName().getResourceDomain() + ":" + BlockCore.block_planter.getRegistryName().getResourcePath()+"_gold"),
//				new ResourceLocation(ModCommon.MOD_ID),
//				new ItemStack(BlockCore.block_planter,1),
//				"WDW",
//				"WRW",
//				"WKW",
//				'R',Blocks.REDSTONE_BLOCK,
//				'W',Blocks.PLANKS,
//				'D',Blocks.DISPENSER,
//				'K',Items.GOLDEN_HOE
//				);
//		addShapedRecipe(event,
//				new ResourceLocation(BlockCore.block_planter.getRegistryName().getResourceDomain() + ":" + BlockCore.block_planter.getRegistryName().getResourcePath()+"_diamond"),
//				new ResourceLocation(ModCommon.MOD_ID),
//				new ItemStack(BlockCore.block_planter,1),
//				"WDW",
//				"WRW",
//				"WKW",
//				'R',Blocks.REDSTONE_BLOCK,
//				'W',Blocks.PLANKS,
//				'D',Blocks.DISPENSER,
//				'K',Items.DIAMOND_HOE
//				);
//
//		// 収穫機
//		addShapedRecipe(event,
//				new ResourceLocation(BlockCore.block_harvester.getRegistryName().getResourceDomain() + ":" + BlockCore.block_harvester.getRegistryName().getResourcePath()+"_wood"),
//				new ResourceLocation(ModCommon.MOD_ID),
//				new ItemStack(BlockCore.block_harvester,1),
//				"WDW",
//				"WRW",
//				"WKW",
//				'R',Blocks.REDSTONE_BLOCK,
//				'W',Blocks.PLANKS,
//				'D',Blocks.HOPPER,
//				'K',ItemCore.item_sickle_wood
//				);
//		addShapedRecipe(event,
//				new ResourceLocation(BlockCore.block_harvester.getRegistryName().getResourceDomain() + ":" + BlockCore.block_harvester.getRegistryName().getResourcePath()+"_stone"),
//				new ResourceLocation(ModCommon.MOD_ID),
//				new ItemStack(BlockCore.block_harvester,1),
//				"WDW",
//				"WRW",
//				"WKW",
//				'R',Blocks.REDSTONE_BLOCK,
//				'W',Blocks.PLANKS,
//				'D',Blocks.HOPPER,
//				'K',ItemCore.item_sickle_stone
//				);
//		addShapedRecipe(event,
//				new ResourceLocation(BlockCore.block_harvester.getRegistryName().getResourceDomain() + ":" + BlockCore.block_harvester.getRegistryName().getResourcePath()+"_iron"),
//				new ResourceLocation(ModCommon.MOD_ID),
//				new ItemStack(BlockCore.block_harvester,1),
//				"WDW",
//				"WRW",
//				"WKW",
//				'R',Blocks.REDSTONE_BLOCK,
//				'W',Blocks.PLANKS,
//				'D',Blocks.HOPPER,
//				'K',ItemCore.item_sickle_iron
//				);
//		addShapedRecipe(event,
//				new ResourceLocation(BlockCore.block_harvester.getRegistryName().getResourceDomain() + ":" + BlockCore.block_harvester.getRegistryName().getResourcePath()+"_gold"),
//				new ResourceLocation(ModCommon.MOD_ID),
//				new ItemStack(BlockCore.block_harvester,1),
//				"WDW",
//				"WRW",
//				"WKW",
//				'R',Blocks.REDSTONE_BLOCK,
//				'W',Blocks.PLANKS,
//				'D',Blocks.HOPPER,
//				'K',ItemCore.item_sickle_gold
//				);
//		addShapedRecipe(event,
//				new ResourceLocation(BlockCore.block_harvester.getRegistryName().getResourceDomain() + ":" + BlockCore.block_harvester.getRegistryName().getResourcePath()+"_diamond"),
//				new ResourceLocation(ModCommon.MOD_ID),
//				new ItemStack(BlockCore.block_harvester,1),
//				"WDW",
//				"WRW",
//				"WKW",
//				'R',Blocks.REDSTONE_BLOCK,
//				'W',Blocks.PLANKS,
//				'D',Blocks.HOPPER,
//				'K',ItemCore.item_sickle_diamond
//				);
//
//
//
//		// 鎌
//		addShapedRecipe(event,ItemCore.item_sickle_wood.getRegistryName(),
//				new ResourceLocation(ModCommon.MOD_ID),
//				new ItemStack(ItemCore.item_sickle_wood,1),
//				"WWW",
//				"S W",
//				"S  ",
//				'S',Items.STICK,
//				'W',Blocks.PLANKS
//				);
//		addShapedRecipe(event,ItemCore.item_sickle_stone.getRegistryName(),
//				new ResourceLocation(ModCommon.MOD_ID),
//				new ItemStack(ItemCore.item_sickle_stone,1),
//				"WWW",
//				"S W",
//				"S  ",
//				'S',Items.STICK,
//				'W',Blocks.COBBLESTONE
//				);
//		addShapedRecipe(event,ItemCore.item_sickle_iron.getRegistryName(),
//				new ResourceLocation(ModCommon.MOD_ID),
//				new ItemStack(ItemCore.item_sickle_iron,1),
//				"WWW",
//				"S W",
//				"S  ",
//				'S',Items.STICK,
//				'W',Items.IRON_INGOT
//				);
//		addShapedRecipe(event,ItemCore.item_sickle_gold.getRegistryName(),
//				new ResourceLocation(ModCommon.MOD_ID),
//				new ItemStack(ItemCore.item_sickle_gold,1),
//				"WWW",
//				"S W",
//				"S  ",
//				'S',Items.STICK,
//				'W',Items.GOLD_INGOT
//				);
//		addShapedRecipe(event,ItemCore.item_sickle_diamond.getRegistryName(),
//				new ResourceLocation(ModCommon.MOD_ID),
//				new ItemStack(ItemCore.item_sickle_diamond,1),
//				"WWW",
//				"S W",
//				"S  ",
//				'S',Items.STICK,
//				'W',Items.DIAMOND
//				);
//
//
//		// 植林機
//		addShapedRecipe(event,BlockCore.block_woodplanter.getRegistryName(),
//				new ResourceLocation(ModCommon.MOD_ID),
//				new ItemStack(BlockCore.block_woodplanter,1),
//				"WDW",
//				"WRW",
//				"WWW",
//				'R',Blocks.REDSTONE_BLOCK,
//				'W',Blocks.PLANKS,
//				'D',Blocks.DISPENSER
//				);
//
//		// 収穫機
//		addShapedRecipe(event,
//				new ResourceLocation(BlockCore.block_woodharvester.getRegistryName().getResourceDomain() + ":" + BlockCore.block_woodharvester.getRegistryName().getResourcePath()+"_wood"),
//				new ResourceLocation(ModCommon.MOD_ID),
//				new ItemStack(BlockCore.block_woodharvester,1),
//				"WDW",
//				"WRW",
//				"WKW",
//				'R',Blocks.REDSTONE_BLOCK,
//				'W',Blocks.PLANKS,
//				'D',Blocks.HOPPER,
//				'K',Items.WOODEN_AXE
//				);
//		addShapedRecipe(event,
//				new ResourceLocation(BlockCore.block_woodharvester.getRegistryName().getResourceDomain() + ":" + BlockCore.block_woodharvester.getRegistryName().getResourcePath()+"_stone"),
//				new ResourceLocation(ModCommon.MOD_ID),
//				new ItemStack(BlockCore.block_woodharvester,1),
//				"WDW",
//				"WRW",
//				"WKW",
//				'R',Blocks.REDSTONE_BLOCK,
//				'W',Blocks.PLANKS,
//				'D',Blocks.HOPPER,
//				'K',Items.STONE_AXE
//				);
//		addShapedRecipe(event,
//				new ResourceLocation(BlockCore.block_woodharvester.getRegistryName().getResourceDomain() + ":" + BlockCore.block_woodharvester.getRegistryName().getResourcePath()+"_iron"),
//				new ResourceLocation(ModCommon.MOD_ID),
//				new ItemStack(BlockCore.block_woodharvester,1),
//				"WDW",
//				"WRW",
//				"WKW",
//				'R',Blocks.REDSTONE_BLOCK,
//				'W',Blocks.PLANKS,
//				'D',Blocks.HOPPER,
//				'K',Items.IRON_AXE
//				);
//		addShapedRecipe(event,
//				new ResourceLocation(BlockCore.block_woodharvester.getRegistryName().getResourceDomain() + ":" + BlockCore.block_woodharvester.getRegistryName().getResourcePath()+"_gold"),
//				new ResourceLocation(ModCommon.MOD_ID),
//				new ItemStack(BlockCore.block_woodharvester,1),
//				"WDW",
//				"WRW",
//				"WKW",
//				'R',Blocks.REDSTONE_BLOCK,
//				'W',Blocks.PLANKS,
//				'D',Blocks.HOPPER,
//				'K',Items.GOLDEN_AXE
//				);
//		addShapedRecipe(event,new ResourceLocation(
//				BlockCore.block_woodharvester.getRegistryName().getResourceDomain() + ":" + BlockCore.block_woodharvester.getRegistryName().getResourcePath()+"_diamond"),
//				new ResourceLocation(ModCommon.MOD_ID),
//				new ItemStack(BlockCore.block_woodharvester,1),
//				"WDW",
//				"WRW",
//				"WKW",
//				'R',Blocks.REDSTONE_BLOCK,
//				'W',Blocks.PLANKS,
//				'D',Blocks.HOPPER,
//				'K',Items.DIAMOND_AXE
//				);
//
//		// 機械の素
//		addShapedRecipe(event,ItemCore.item_machinematter.getRegistryName(),
//				new ResourceLocation(ModCommon.MOD_ID),
//				new ItemStack(ItemCore.item_machinematter,1),
//				"IRI",
//				"DGD",
//				"GIG",
//				'R',Items.REDSTONE,
//				'D',Items.DIAMOND,
//				'G',Items.GOLD_INGOT,
//				'I',Items.IRON_INGOT
//				);
//
//		// 製氷機
//		addShapedRecipe(event,BlockCore.block_icemaker.getRegistryName(),
//				new ResourceLocation(ModCommon.MOD_ID),
//				new ItemStack(BlockCore.block_icemaker,1),
//				"IKI",
//				"KKK",
//				"IRI",
//				'I',Blocks.ICE,
//				'R',Items.REDSTONE,
//				'K',ItemCore.item_machinematter
//				);
//		//　溶解機
//		addShapedRecipe(event,BlockCore.block_lavamaker.getRegistryName(),
//				new ResourceLocation(ModCommon.MOD_ID),
//				new ItemStack(BlockCore.block_lavamaker,1),
//				"MKM",
//				"KKK",
//				"MRM",
//				'M',Blocks.MAGMA,
//				'R',Items.REDSTONE,
//				'K',ItemCore.item_machinematter
//				);
//
//		// 破砕機
//		addShapedRecipe(event,BlockCore.block_destroyer.getRegistryName(),
//				new ResourceLocation(ModCommon.MOD_ID),
//				new ItemStack(BlockCore.block_destroyer,1),
//				"TTT",
//				"KPK",
//				"KRK",
//				'T',Blocks.TNT,
//				'R',Items.REDSTONE,
//				'K',ItemCore.item_machinematter,
//				'P',Blocks.PISTON
//				);
//		// 配置機
//		addShapedRecipe(event,BlockCore.block_setter.getRegistryName(),
//		new ResourceLocation(ModCommon.MOD_ID),
//		new ItemStack(BlockCore.block_setter,1),
//		"KDK",
//		"KRK",
//		'D',Blocks.DROPPER,
//		'R',Items.REDSTONE,
//		'K',ItemCore.item_machinematter
//		);
//		// 屠殺機
//		addShapedRecipe(event,BlockCore.block_killer.getRegistryName(),
//				new ResourceLocation(ModCommon.MOD_ID),
//				new ItemStack(BlockCore.block_killer,1),
//				" K ",
//				"K K",
//				" R ",
//				'R',Items.REDSTONE,
//				'K',ItemCore.item_machinematter
//				);
//		// 経験値収集機
//		addShapedRecipe(event,BlockCore.block_excollector.getRegistryName(),
//				new ResourceLocation(ModCommon.MOD_ID),
//				new ItemStack(BlockCore.block_excollector,1),
//				"BKB",
//				"KHK",
//				"BRB",
//				'B',Items.GLASS_BOTTLE,
//				'R',Items.REDSTONE,
//				'K',ItemCore.item_machinematter,
//				'H',Blocks.HOPPER
//				);
//		// 粉砕機
//		addShapedRecipe(event,BlockCore.block_crusher.getRegistryName(),
//				new ResourceLocation(ModCommon.MOD_ID),
//				new ItemStack(BlockCore.block_crusher,1),
//				"KKK",
//				"KDK",
//				"KRK",
//				'D',Items.DIAMOND,
//				'R',Items.REDSTONE,
//				'K',ItemCore.item_machinematter
//				);
//		// 圧縮機
//		addShapedRecipe(event,BlockCore.block_compresser.getRegistryName(),
//				new ResourceLocation(ModCommon.MOD_ID),
//				new ItemStack(BlockCore.block_compresser,1),
//				"KPK",
//				"PRP",
//				"KPK",
//				'P',Blocks.PISTON,
//				'R',Items.REDSTONE,
//				'K',ItemCore.item_machinematter
//				);
//
//		// 給水機
//		addShapedRecipe(event,BlockCore.block_straw.getRegistryName(),
//				new ResourceLocation(ModCommon.MOD_ID),
//				new ItemStack(BlockCore.block_straw,1),
//				"KBK",
//				"KRK",
//				'B',Items.BUCKET,
//				'R',Items.REDSTONE,
//				'K',ItemCore.item_machinematter
//				);
//
//		// 水バケツ
//		addShapelessRecipe(event,new ResourceLocation(ModCommon.MOD_ID+":"+"warterbukett"),
//				new ResourceLocation(ModCommon.MOD_ID+":"+"warterbukett"),
//				new ItemStack(Items.WATER_BUCKET,1),
//				Ingredient.fromItems(Items.BUCKET),Ingredient.fromItem(ItemCore.item_waterball));
//		// 溶岩バケツ
//		addShapelessRecipe(event,new ResourceLocation(ModCommon.MOD_ID+":"+"lavabukett"),
//				new ResourceLocation(ModCommon.MOD_ID+":"+"lavabukett"),
//				new ItemStack(Items.LAVA_BUCKET,1),
//				Ingredient.fromItems(Items.BUCKET),Ingredient.fromItem(ItemCore.item_lavaball));
	}
}
