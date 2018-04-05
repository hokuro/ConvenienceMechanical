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

	// 工業系
	public static final String NAME_ICEMAKER = "icemaker";			// 製氷機
	public static final String NAME_LAVAMAKER = "lavamaker";		// 溶解機
	public static final String NAME_DESTROYER = "destroyer";		// 破砕機
	public static final String NAME_SETTER = "setter";				// 配置機
	public static final String NAME_KIRER = "kirer";				// 屠殺機
	public static final String NAME_EXCOLLECTOR = "excollector";	// 経験値収集機
	public static final String NAME_CRUSSHER = "crussher";			// 粉砕機
	public static final String NAME_COMPLESSER = "commpleser";		// 圧縮機




	public static Block block_expbank;
	public static Block block_exenchanter;
	public static Block block_exanvil;

	public static Block block_planter;
	public static Block block_harvester;

	public static Block block_icemaker;
	public static Block block_lavamaker;
	public static Block block_destroyer;
	public static Block block_setter;
	public static Block block_kirer;
	public static Block block_excollector;
	public static Block block_crusher;
	public static Block block_compresser;

	private static final String[] NAME_LIST = new String[]{
			 NAME_EXPBANK,
			 NAME_EXENCHANTER,
//			 NAME_EXANVIL,

			 NAME_PLANTER,
			 NAME_HARVESTER,

//			 NAME_ICEMAKER,
//			 NAME_LAVAMAKER,
//			 NAME_DESTROYER,
//			 NAME_SETTER,
//			 NAME_KIRER,
//			 NAME_EXCOLLECTOR,
//			 NAME_CRUSSHER,
//			 NAME_COMPLESSER,
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

		block_exanvil = new Block(Material.ANVIL)
		.setRegistryName(ModCommon.MOD_ID + ":" + NAME_EXANVIL)
		.setUnlocalizedName(NAME_EXANVIL)
		.setCreativeTab(Mod_ConvenienceBox.tabWorker);

		// 種まき機
		block_planter = new BlockPlanter()
				.setRegistryName(ModCommon.MOD_ID + ":" + NAME_PLANTER)
				.setUnlocalizedName(NAME_PLANTER)
				.setCreativeTab(Mod_ConvenienceBox.tabWorker);
		// 収穫機
		block_harvester = new Block(Material.ROCK)
				.setRegistryName(ModCommon.MOD_ID + ":" + NAME_HARVESTER)
				.setUnlocalizedName(NAME_HARVESTER)
				.setCreativeTab(Mod_ConvenienceBox.tabWorker);

		block_icemaker = new Block(Material.IRON)
				.setRegistryName(ModCommon.MOD_ID + ":" + NAME_ICEMAKER)
				.setUnlocalizedName(NAME_ICEMAKER)

				.setCreativeTab(Mod_ConvenienceBox.tabWorker);
		block_lavamaker = new Block(Material.IRON)
				.setRegistryName(ModCommon.MOD_ID + ":" + NAME_LAVAMAKER)
				.setUnlocalizedName(NAME_LAVAMAKER)
				.setCreativeTab(Mod_ConvenienceBox.tabWorker);

		block_destroyer = new Block(Material.IRON)
				.setRegistryName(ModCommon.MOD_ID + ":" + NAME_DESTROYER)
				.setUnlocalizedName(NAME_DESTROYER)
				.setCreativeTab(Mod_ConvenienceBox.tabWorker);

		block_setter = new Block(Material.IRON)
				.setRegistryName(ModCommon.MOD_ID + ":" + NAME_SETTER)
				.setUnlocalizedName(NAME_SETTER)
				.setCreativeTab(Mod_ConvenienceBox.tabWorker);

		block_kirer = new Block(Material.IRON)
				.setRegistryName(ModCommon.MOD_ID + ":" + NAME_KIRER)
				.setUnlocalizedName(NAME_KIRER)
				.setCreativeTab(Mod_ConvenienceBox.tabWorker);

		block_excollector = new Block(Material.IRON)
				.setRegistryName(ModCommon.MOD_ID + ":" + NAME_EXCOLLECTOR)
				.setUnlocalizedName(NAME_EXCOLLECTOR)
				.setCreativeTab(Mod_ConvenienceBox.tabWorker);

		block_crusher = new Block(Material.IRON)
				.setRegistryName(ModCommon.MOD_ID + ":" + NAME_CRUSSHER)
				.setUnlocalizedName(NAME_CRUSSHER)
				.setCreativeTab(Mod_ConvenienceBox.tabWorker);

		block_compresser = new Block(Material.IRON)
				.setRegistryName(ModCommon.MOD_ID + ":" + NAME_COMPLESSER)
				.setUnlocalizedName(NAME_COMPLESSER)
				.setCreativeTab(Mod_ConvenienceBox.tabWorker);



		blockMap = new HashMap<String,Block>(){
			{put(NAME_EXPBANK,block_expbank);}
			{put(NAME_EXENCHANTER,block_exenchanter);}
			{put(NAME_EXANVIL,block_exanvil);}

			{put(NAME_PLANTER,block_planter);}
			{put(NAME_HARVESTER,block_harvester);}

			{put(NAME_ICEMAKER,block_icemaker);}
			{put(NAME_LAVAMAKER,block_lavamaker);}
			{put( NAME_DESTROYER,block_destroyer);}
			{put(NAME_SETTER,block_setter);}
			{put(NAME_KIRER,block_kirer);}
			{put(NAME_EXCOLLECTOR,block_excollector);}
			{put(NAME_CRUSSHER,block_crusher);}
			{put(NAME_COMPLESSER,block_compresser);}
		};

		itemMap = new HashMap<String,Item>(){
			{put(NAME_EXPBANK,new ItemExpBank(block_expbank).setRegistryName(ModCommon.MOD_ID + ":" + NAME_EXPBANK));}
			{put(NAME_EXENCHANTER, new ItemBlock(block_exenchanter).setRegistryName(ModCommon.MOD_ID + ":" + NAME_EXENCHANTER));}
			{put(NAME_EXANVIL, new ItemBlock(block_exanvil).setRegistryName(ModCommon.MOD_ID + ":" + NAME_EXANVIL));}

			{put(NAME_PLANTER, new ItemPlanter(block_planter).setRegistryName(ModCommon.MOD_ID + ":" + NAME_PLANTER));}
			{put(NAME_HARVESTER, new ItemBlock(block_harvester).setRegistryName(ModCommon.MOD_ID + ":" + NAME_HARVESTER));}

			{put(NAME_ICEMAKER, new ItemBlock(block_icemaker).setRegistryName(ModCommon.MOD_ID + ":" + NAME_ICEMAKER));}
			{put(NAME_LAVAMAKER, new ItemBlock(block_lavamaker).setRegistryName(ModCommon.MOD_ID + ":" + NAME_LAVAMAKER));}
			{put(NAME_DESTROYER, new ItemBlock(block_destroyer).setRegistryName(ModCommon.MOD_ID + ":" + NAME_DESTROYER));}
			{put(NAME_SETTER, new ItemBlock(block_setter).setRegistryName(ModCommon.MOD_ID + ":" + NAME_SETTER));}
			{put(NAME_KIRER, new ItemBlock(block_kirer).setRegistryName(ModCommon.MOD_ID + ":" + NAME_KIRER));}
			{put(NAME_EXCOLLECTOR, new ItemBlock(block_excollector).setRegistryName(ModCommon.MOD_ID + ":" + NAME_EXCOLLECTOR));}
			{put(NAME_CRUSSHER, new ItemBlock(block_crusher).setRegistryName(ModCommon.MOD_ID + ":" + NAME_CRUSSHER));}
			{put(NAME_COMPLESSER, new ItemBlock(block_compresser).setRegistryName(ModCommon.MOD_ID + ":" + NAME_COMPLESSER));}
		};


		resourceMap = new HashMap<String,ResourceLocation[]>(){
			{put(NAME_EXPBANK, new ResourceLocation[]{new ResourceLocation(ModCommon.MOD_ID + ":" + "expbank")});}

			{put(NAME_EXENCHANTER, new ResourceLocation[]{new ResourceLocation(ModCommon.MOD_ID+":"+NAME_EXENCHANTER)});}
			{put(NAME_EXANVIL, new ResourceLocation[]{new ResourceLocation(ModCommon.MOD_ID+":"+NAME_EXANVIL)});}

			{put(NAME_PLANTER, new ResourceLocation[]{new ResourceLocation(ModCommon.MOD_ID+":"+NAME_PLANTER)});}
			{put(NAME_HARVESTER, new ResourceLocation[]{new ResourceLocation(ModCommon.MOD_ID+":"+NAME_HARVESTER)});}

			{put(NAME_ICEMAKER, new ResourceLocation[]{new ResourceLocation(ModCommon.MOD_ID+":"+NAME_ICEMAKER)});}
			{put(NAME_LAVAMAKER, new ResourceLocation[]{new ResourceLocation(ModCommon.MOD_ID+":"+NAME_LAVAMAKER)});}
			{put(NAME_DESTROYER, new ResourceLocation[]{new ResourceLocation(ModCommon.MOD_ID+":"+NAME_DESTROYER)});}
			{put(NAME_SETTER, new ResourceLocation[]{new ResourceLocation(ModCommon.MOD_ID+":"+NAME_SETTER)});}
			{put(NAME_KIRER, new ResourceLocation[]{new ResourceLocation(ModCommon.MOD_ID+":"+NAME_KIRER)});}
			{put(NAME_EXCOLLECTOR, new ResourceLocation[]{new ResourceLocation(ModCommon.MOD_ID+":"+NAME_EXCOLLECTOR)});}
			{put(NAME_CRUSSHER, new ResourceLocation[]{new ResourceLocation(ModCommon.MOD_ID+":"+NAME_CRUSSHER)});}
			{put(NAME_COMPLESSER, new ResourceLocation[]{new ResourceLocation(ModCommon.MOD_ID+":"+NAME_COMPLESSER)});}
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