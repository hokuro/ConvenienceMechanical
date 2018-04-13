package mod.cvbox.block;

import java.util.HashMap;
import java.util.Map;

import mod.cvbox.block.item.ItemExpBank;
import mod.cvbox.block.item.ItemPlanter;
import mod.cvbox.core.ModCommon;
import mod.cvbox.core.Mod_ConvenienceBox;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class BlockCore{

	// 工房系
	public static final String NAME_EXPBANK = "expbank";			// 経験値貯金箱
	public static final String NAME_EXENCHANTER = "exenchanter";	// スーパーエンチャントテーブル
	public static final String NAME_EXANVIL ="exanvil";				// スーパーアンビル

	// 農業系
	public static final String NAME_PLANTER = "planter";			// 種まき機
	public static final String NAME_HARVESTER = "harvester";		//　収穫機
	private static final String NAME_WOODPLANTER ="woodplanter";	// 植林機
	private static final String NAME_WOODHARVESTER = "woodharvester";	// きこり機

	// 工業系
	public static final String NAME_ICEMAKER = "icemaker";			// 製氷機
	public static final String NAME_LAVAMAKER = "lavamaker";		// 溶解機
	public static final String NAME_DESTROYER = "destroyer";		// 破砕機
	public static final String NAME_SETTER = "setter";				// 配置機
	public static final String NAME_KILLER = "kirer";				// 屠殺機
	public static final String NAME_EXCOLLECTOR = "excollector";	// 経験値収集機
	public static final String NAME_CRUSSHER = "crussher";			// 粉砕機
	public static final String NAME_COMPLESSER = "commpleser";		// 圧縮機
	public static final String NAME_LIQUIDCSTRAWER = "straw";		// 給水機




	public static Block block_expbank;
	public static Block block_exenchanter;
	public static Block block_exanvil;

	public static Block block_planter;
	public static Block block_harvester;
	public static Block block_woodplanter;
	public static Block block_woodharvester;

	public static Block block_icemaker;
	public static Block block_lavamaker;
	public static Block block_destroyer;
	public static Block block_setter;
	public static Block block_killer;
	public static Block block_excollector;
	public static Block block_crusher;
	public static Block block_compresser;
	public static Block block_straw;

	private static final String[] NAME_LIST = new String[]{
			 NAME_EXPBANK,
			 NAME_EXENCHANTER,
			 NAME_EXANVIL,

			 NAME_PLANTER,
			 NAME_HARVESTER,
			 NAME_WOODPLANTER,
			 NAME_WOODHARVESTER,

			 NAME_ICEMAKER,
			 NAME_LAVAMAKER,
			 NAME_DESTROYER,
			 NAME_SETTER,
			 NAME_KILLER,
			 NAME_EXCOLLECTOR,
			 NAME_CRUSSHER,
			 NAME_COMPLESSER,
			 NAME_LIQUIDCSTRAWER,
	};

	private static Map<String,Block> blockMap;
	private static Map<String,Item> itemMap;
	private static Map<String,ResourceLocation[]> resourceMap;

	private static void init(){
		// 経験値貯金箱
		block_expbank = new BlockExpBank(Material.ROCK)
				.setRegistryName(ModCommon.MOD_ID + ":" + NAME_EXPBANK)
				.setUnlocalizedName(NAME_EXPBANK)
				.setCreativeTab(Mod_ConvenienceBox.tabWorker);

		// エンチャント台
		block_exenchanter = new BlockExEnchantmentTable()
				.setRegistryName(ModCommon.MOD_ID + ":" + NAME_EXENCHANTER)
				.setUnlocalizedName(NAME_EXENCHANTER)
				.setCreativeTab(Mod_ConvenienceBox.tabWorker);

		// かなとこ
		block_exanvil = new BlockExAnvil()
		.setRegistryName(ModCommon.MOD_ID + ":" + NAME_EXANVIL)
		.setUnlocalizedName(NAME_EXANVIL)
		.setCreativeTab(Mod_ConvenienceBox.tabWorker);

		// 種まき機
		block_planter = new BlockPlanter()
				.setRegistryName(ModCommon.MOD_ID + ":" + NAME_PLANTER)
				.setUnlocalizedName(NAME_PLANTER)
				.setCreativeTab(Mod_ConvenienceBox.tabFarmer);
		// 収穫機
		block_harvester = new BlockHarvester()
				.setRegistryName(ModCommon.MOD_ID + ":" + NAME_HARVESTER)
				.setUnlocalizedName(NAME_HARVESTER)
				.setCreativeTab(Mod_ConvenienceBox.tabFarmer);

		// 植林機
		block_woodplanter = new BlockWoodPlanter()
				.setRegistryName(ModCommon.MOD_ID + ":" + NAME_WOODPLANTER)
				.setUnlocalizedName(NAME_WOODPLANTER)
				.setCreativeTab(Mod_ConvenienceBox.tabFarmer);

		// きこり機
		block_woodharvester = new BlockWoodHarvester()
				.setRegistryName(ModCommon.MOD_ID + ":" + NAME_WOODHARVESTER)
				.setUnlocalizedName(NAME_WOODHARVESTER)
				.setCreativeTab(Mod_ConvenienceBox.tabFarmer);

		// 製氷機
		block_icemaker = new BlockIceMaker(Material.IRON)
				.setRegistryName(ModCommon.MOD_ID + ":" + NAME_ICEMAKER)
				.setUnlocalizedName(NAME_ICEMAKER);

		// 溶解機
		block_lavamaker = new BlockLavaMaker(Material.IRON)
				.setRegistryName(ModCommon.MOD_ID + ":" + NAME_LAVAMAKER)
				.setUnlocalizedName(NAME_LAVAMAKER);

		// 破砕機
		block_destroyer = new BlockDestroyer(Material.IRON)
				.setRegistryName(ModCommon.MOD_ID + ":" + NAME_DESTROYER)
				.setUnlocalizedName(NAME_DESTROYER);

		// 配置機
		block_setter = new BlockSetter(Material.IRON)
				.setRegistryName(ModCommon.MOD_ID + ":" + NAME_SETTER)
				.setUnlocalizedName(NAME_SETTER);

		// キラー
		block_killer = new BlockKiller(Material.IRON)
				.setRegistryName(ModCommon.MOD_ID + ":" + NAME_KILLER)
				.setUnlocalizedName(NAME_KILLER);

		// 経験値収集機
		block_excollector = new BlockExpCollector(Material.IRON)
				.setRegistryName(ModCommon.MOD_ID + ":" + NAME_EXCOLLECTOR)
				.setUnlocalizedName(NAME_EXCOLLECTOR);

		block_crusher = new BlockIceMaker(Material.IRON)
				.setRegistryName(ModCommon.MOD_ID + ":" + NAME_CRUSSHER)
				.setUnlocalizedName(NAME_CRUSSHER);

		block_compresser = new BlockIceMaker(Material.IRON)
				.setRegistryName(ModCommon.MOD_ID + ":" + NAME_COMPLESSER)
				.setUnlocalizedName(NAME_COMPLESSER);

		block_straw = new BlockIceMaker(Material.IRON)
				.setRegistryName(ModCommon.MOD_ID + ":" + NAME_LIQUIDCSTRAWER)
				.setUnlocalizedName(NAME_LIQUIDCSTRAWER);



		blockMap = new HashMap<String,Block>(){
			{put(NAME_EXPBANK,block_expbank);}
			{put(NAME_EXENCHANTER,block_exenchanter);}
			{put(NAME_EXANVIL,block_exanvil);}

			{put(NAME_PLANTER,block_planter);}
			{put(NAME_HARVESTER,block_harvester);}
			{put(NAME_WOODPLANTER,block_woodplanter);}
			{put(NAME_WOODHARVESTER,block_woodharvester);}

			{put(NAME_ICEMAKER,block_icemaker);}
			{put(NAME_LAVAMAKER,block_lavamaker);}
			{put(NAME_DESTROYER,block_destroyer);}
			{put(NAME_SETTER,block_setter);}
			{put(NAME_KILLER,block_killer);}
			{put(NAME_EXCOLLECTOR,block_excollector);}
			{put(NAME_CRUSSHER,block_crusher);}
			{put(NAME_COMPLESSER,block_compresser);}
			{put(NAME_LIQUIDCSTRAWER,block_straw);}
		};

		itemMap = new HashMap<String,Item>(){
			{put(NAME_EXPBANK,new ItemExpBank(block_expbank).setRegistryName(ModCommon.MOD_ID + ":" + NAME_EXPBANK));}
			{put(NAME_EXENCHANTER, new ItemBlock(block_exenchanter).setRegistryName(ModCommon.MOD_ID + ":" + NAME_EXENCHANTER));}
			{put(NAME_EXANVIL, new ItemBlock(block_exanvil).setRegistryName(ModCommon.MOD_ID + ":" + NAME_EXANVIL));}

			{put(NAME_PLANTER, new ItemPlanter(block_planter).setRegistryName(ModCommon.MOD_ID + ":" + NAME_PLANTER));}
			{put(NAME_HARVESTER, new ItemBlock(block_harvester).setRegistryName(ModCommon.MOD_ID + ":" + NAME_HARVESTER));}
			{put(NAME_WOODPLANTER, new ItemPlanter(block_woodplanter).setRegistryName(ModCommon.MOD_ID + ":" + NAME_WOODPLANTER));}
			{put(NAME_WOODHARVESTER, new ItemBlock(block_woodharvester).setRegistryName(ModCommon.MOD_ID + ":" + NAME_WOODHARVESTER));}

			{put(NAME_ICEMAKER, new ItemBlock(block_icemaker).setRegistryName(ModCommon.MOD_ID + ":" + NAME_ICEMAKER));}
			{put(NAME_LAVAMAKER, new ItemBlock(block_lavamaker).setRegistryName(ModCommon.MOD_ID + ":" + NAME_LAVAMAKER));}
			{put(NAME_DESTROYER, new ItemBlock(block_destroyer).setRegistryName(ModCommon.MOD_ID + ":" + NAME_DESTROYER));}
			{put(NAME_SETTER, new ItemBlock(block_setter).setRegistryName(ModCommon.MOD_ID + ":" + NAME_SETTER));}
			{put(NAME_KILLER, new ItemBlock(block_killer).setRegistryName(ModCommon.MOD_ID + ":" + NAME_KILLER));}
			{put(NAME_EXCOLLECTOR, new ItemBlock(block_excollector).setRegistryName(ModCommon.MOD_ID + ":" + NAME_EXCOLLECTOR));}
			{put(NAME_CRUSSHER, new ItemBlock(block_crusher).setRegistryName(ModCommon.MOD_ID + ":" + NAME_CRUSSHER));}
			{put(NAME_COMPLESSER, new ItemBlock(block_compresser).setRegistryName(ModCommon.MOD_ID + ":" + NAME_COMPLESSER));}
			{put(NAME_LIQUIDCSTRAWER, new ItemBlock(block_straw).setRegistryName(ModCommon.MOD_ID + ":" + NAME_LIQUIDCSTRAWER));}
		};


		resourceMap = new HashMap<String,ResourceLocation[]>(){
			{put(NAME_EXPBANK, new ResourceLocation[]{new ResourceLocation(ModCommon.MOD_ID + ":" + NAME_EXPBANK)});}
			{put(NAME_EXENCHANTER, new ResourceLocation[]{new ResourceLocation(ModCommon.MOD_ID+":"+NAME_EXENCHANTER)});}
			{put(NAME_EXANVIL, new ResourceLocation[]{new ResourceLocation(ModCommon.MOD_ID+":"+NAME_EXANVIL)});}

			{put(NAME_PLANTER, new ResourceLocation[]{new ResourceLocation(ModCommon.MOD_ID+":"+NAME_PLANTER)});}
			{put(NAME_HARVESTER, new ResourceLocation[]{new ResourceLocation(ModCommon.MOD_ID+":"+NAME_HARVESTER)});}
			{put(NAME_WOODPLANTER, new ResourceLocation[]{new ResourceLocation(ModCommon.MOD_ID+":"+NAME_WOODPLANTER)});}
			{put(NAME_WOODHARVESTER, new ResourceLocation[]{new ResourceLocation(ModCommon.MOD_ID+":"+NAME_WOODHARVESTER)});}

			{put(NAME_ICEMAKER, new ResourceLocation[]{new ResourceLocation(ModCommon.MOD_ID+":"+NAME_ICEMAKER)});}
			{put(NAME_LAVAMAKER, new ResourceLocation[]{new ResourceLocation(ModCommon.MOD_ID+":"+NAME_LAVAMAKER)});}
			{put(NAME_DESTROYER, new ResourceLocation[]{new ResourceLocation(ModCommon.MOD_ID+":"+NAME_DESTROYER)});}
			{put(NAME_SETTER, new ResourceLocation[]{new ResourceLocation(ModCommon.MOD_ID+":"+NAME_SETTER)});}
			{put(NAME_KILLER, new ResourceLocation[]{new ResourceLocation(ModCommon.MOD_ID+":"+NAME_KILLER)});}
			{put(NAME_EXCOLLECTOR, new ResourceLocation[]{new ResourceLocation(ModCommon.MOD_ID+":"+NAME_EXCOLLECTOR)});}
			{put(NAME_CRUSSHER, new ResourceLocation[]{new ResourceLocation(ModCommon.MOD_ID+":"+NAME_CRUSSHER)});}
			{put(NAME_COMPLESSER, new ResourceLocation[]{new ResourceLocation(ModCommon.MOD_ID+":"+NAME_COMPLESSER)});}
			{put(NAME_LIQUIDCSTRAWER, new ResourceLocation[]{new ResourceLocation(ModCommon.MOD_ID+":"+NAME_LIQUIDCSTRAWER)});}
		};
	}


	public static void register(FMLPreInitializationEvent event){
		init();
		for (String key: NAME_LIST){
			ForgeRegistries.BLOCKS.register(blockMap.get(key));
			ForgeRegistries.ITEMS.register(itemMap.get(key));
		}

		if (event.getSide().isClient()){
			for (String key : NAME_LIST){
				Item witem = itemMap.get(key);
				ResourceLocation[] wresource = resourceMap.get(key);
				if (wresource.length > 1){
					ModelLoader.registerItemVariants(witem, wresource);
				}
				for (int i = 0; i < wresource.length; i++){
					ModelLoader.setCustomModelResourceLocation(witem, i,
							new ModelResourceLocation(wresource[i], "inventory"));
				}
			}
		}
	}
}