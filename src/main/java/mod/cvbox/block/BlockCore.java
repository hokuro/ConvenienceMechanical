package mod.cvbox.block;

import java.util.HashMap;
import java.util.Map;

import mod.cvbox.block.item.ItemExpBank;
import mod.cvbox.block.item.ItemPlanter;
import mod.cvbox.core.ModCommon;
import mod.cvbox.core.Mod_ConvenienceBox;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;

public class BlockCore{

	// 工房系
	public static final String NAME_EXPBANK = "expbank";			// 経験値貯金箱
	public static final String NAME_EXENCHANTER = "exenchanter";	// スーパーエンチャントテーブル
	public static final String NAME_EXANVIL ="exanvil";				// スーパーアンビル
	public static final String NAME_EXANVIL_CHIPPED = "exanvile_chipped";
	public static final String NAME_EXANVIL_DAMAGED = "exanvile_damaged";

	// 農業系
	public static final String NAME_PLANTER = "planter";			// 種まき機
	public static final String NAME_HARVESTER = "harvester";		//　収穫機
	private static final String NAME_WOODPLANTER ="woodplanter";	// 植林機
	private static final String NAME_WOODHARVESTER = "woodharvester";	// きこり機

	// 工業系
	public static final String NAME_LIQUIDMAKER = "liquidmaker";	// 製氷機
	public static final String NAME_DESTROYER = "destroyer";		// 破砕機
	public static final String NAME_SETTER = "setter";				// 配置機
	public static final String NAME_KILLER = "kirer";				// 屠殺機
	public static final String NAME_EXCOLLECTOR = "excollector";	// 経験値収集機
	public static final String NAME_CRUSSHER = "crussher";			// 粉砕機
	public static final String NAME_COMPLESSER = "commpleser";		// 圧縮機
	public static final String NAME_LIQUIDCSTRAWER = "straw";		// 給水機
	public static final String NAME_VACUMER = "vacumer";			// 吸引機

	搾乳機と羊毛刈り取り機とミルクボールとゲノム採取装置とゲノム解析機と万能細胞培養器とモブ複製機とゴルディオンハンマーを追加する

	// 経験値貯金箱
	public static Block block_expbank = new BlockExpBank(Material.ROCK)
			.setRegistryName(ModCommon.MOD_ID + ":" + NAME_EXPBANK);

	// エンチャント台
	public static Block block_exenchanter = new BlockExEnchantmentTable()
			.setRegistryName(ModCommon.MOD_ID + ":" + NAME_EXENCHANTER);

	// かなとこ
	public static Block block_exanvil = new BlockExAnvil()
	.setRegistryName(ModCommon.MOD_ID + ":" + NAME_EXANVIL);

	public static Block block_exanvil_chipped = new BlockExAnvil()
	.setRegistryName(ModCommon.MOD_ID + ":" + NAME_EXANVIL_CHIPPED);

	public static Block block_exanvil_damaged = new BlockExAnvil()
	.setRegistryName(ModCommon.MOD_ID + ":" + NAME_EXANVIL_DAMAGED);

	// 種まき機
	public static Block block_planter = new BlockPlanter()
			.setRegistryName(ModCommon.MOD_ID + ":" + NAME_PLANTER);
	// 収穫機
	public static Block block_harvester = new BlockHarvester()
			.setRegistryName(ModCommon.MOD_ID + ":" + NAME_HARVESTER);

	// 植林機
	public static Block block_woodplanter = new BlockWoodPlanter()
			.setRegistryName(ModCommon.MOD_ID + ":" + NAME_WOODPLANTER);

	// きこり機
	public static Block block_woodharvester = new BlockWoodHarvester()
			.setRegistryName(ModCommon.MOD_ID + ":" + NAME_WOODHARVESTER);

	// 製氷機
	public static Block block_liquidmaker = new BlockLiquidMaker()
			.setRegistryName(ModCommon.MOD_ID + ":" + NAME_LIQUIDMAKER);

	// 破砕機
	public static Block block_destroyer = new BlockDestroyer()
			.setRegistryName(ModCommon.MOD_ID + ":" + NAME_DESTROYER);

	// 配置機
	public static Block block_setter = new BlockSetter()
			.setRegistryName(ModCommon.MOD_ID + ":" + NAME_SETTER);

	// キラー
	public static Block block_killer = new BlockKiller()
			.setRegistryName(ModCommon.MOD_ID + ":" + NAME_KILLER);

	// 経験値収集機
	public static Block block_excollector = new BlockExpCollector()
			.setRegistryName(ModCommon.MOD_ID + ":" + NAME_EXCOLLECTOR);

	// 粉砕機
	public static Block block_crusher = new BlockCrusher()
			.setRegistryName(ModCommon.MOD_ID + ":" + NAME_CRUSSHER);

	// 圧縮機
	public static Block block_compresser = new BlockCompresser()
			.setRegistryName(ModCommon.MOD_ID + ":" + NAME_COMPLESSER);

	// ストロー
	public static Block block_straw = new BlockStraw()
			.setRegistryName(ModCommon.MOD_ID + ":" + NAME_LIQUIDCSTRAWER);

	// 吸引機
	public static Block block_vacumer = new BlockVacumer()
	.setRegistryName(ModCommon.MOD_ID + ":" + NAME_VACUMER);

	private static final String[] NAME_LIST = new String[]{
			 NAME_EXPBANK,
			 NAME_EXENCHANTER,
			 NAME_EXANVIL,
//			 NAME_EXANVIL_CHIPPED,
//			 NAME_EXANVIL_DAMAGED,

			 NAME_PLANTER,
			 NAME_HARVESTER,
			 NAME_WOODPLANTER,
			 NAME_WOODHARVESTER,

			 NAME_LIQUIDMAKER,
			 NAME_DESTROYER,
			 NAME_SETTER,
			 NAME_KILLER,
			 NAME_EXCOLLECTOR,
			 NAME_CRUSSHER,
			 NAME_COMPLESSER,
			 NAME_LIQUIDCSTRAWER,
			 NAME_VACUMER,
	};

	private static Map<String,Block> blockMap;
	private static Map<String,Item> itemMap;
	private static Map<String,ResourceLocation[]> resourceMap;

	private static void init(){



		blockMap = new HashMap<String,Block>(){
			{put(NAME_EXPBANK,block_expbank);}
			{put(NAME_EXENCHANTER,block_exenchanter);}
			{put(NAME_EXANVIL,block_exanvil);}
			{put(NAME_EXANVIL_CHIPPED,block_exanvil_chipped);}
			{put(NAME_EXANVIL_DAMAGED,block_exanvil_damaged);}

			{put(NAME_PLANTER,block_planter);}
			{put(NAME_HARVESTER,block_harvester);}
			{put(NAME_WOODPLANTER,block_woodplanter);}
			{put(NAME_WOODHARVESTER,block_woodharvester);}

			{put(NAME_LIQUIDMAKER,block_liquidmaker);}
			{put(NAME_DESTROYER,block_destroyer);}
			{put(NAME_SETTER,block_setter);}
			{put(NAME_KILLER,block_killer);}
			{put(NAME_EXCOLLECTOR,block_excollector);}
			{put(NAME_CRUSSHER,block_crusher);}
			{put(NAME_COMPLESSER,block_compresser);}
			{put(NAME_LIQUIDCSTRAWER,block_straw);}
			{put(NAME_VACUMER,block_vacumer);}
		};

		itemMap = new HashMap<String,Item>(){
			{put(NAME_EXPBANK,new ItemExpBank(block_expbank).setRegistryName(ModCommon.MOD_ID + ":" + NAME_EXPBANK));}
			{put(NAME_EXENCHANTER, new ItemBlock(block_exenchanter, new Item.Properties().group(Mod_ConvenienceBox.tabWorker)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_EXENCHANTER));}
			{put(NAME_EXANVIL, new ItemBlock(block_exanvil,new Item.Properties().group(Mod_ConvenienceBox.tabWorker)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_EXANVIL));}
			{put(NAME_EXANVIL_CHIPPED, new ItemBlock(block_exanvil_chipped,new Item.Properties().group(Mod_ConvenienceBox.tabWorker)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_EXANVIL_CHIPPED));}
			{put(NAME_EXANVIL_DAMAGED, new ItemBlock(block_exanvil_damaged,new Item.Properties().group(Mod_ConvenienceBox.tabWorker)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_EXANVIL_DAMAGED));}

			{put(NAME_PLANTER, new ItemPlanter(block_planter).setRegistryName(ModCommon.MOD_ID + ":" + NAME_PLANTER));}
			{put(NAME_HARVESTER, new ItemBlock(block_harvester, new Item.Properties().group(Mod_ConvenienceBox.tabFarmer)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_HARVESTER));}
			{put(NAME_WOODPLANTER, new ItemPlanter(block_woodplanter).setRegistryName(ModCommon.MOD_ID + ":" + NAME_WOODPLANTER));}
			{put(NAME_WOODHARVESTER, new ItemBlock(block_woodharvester,new Item.Properties().group(Mod_ConvenienceBox.tabFarmer)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_WOODHARVESTER));}

			{put(NAME_LIQUIDMAKER, new ItemBlock(block_liquidmaker,new Item.Properties().group(Mod_ConvenienceBox.tabFactory)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_LIQUIDMAKER));}
			{put(NAME_DESTROYER, new ItemBlock(block_destroyer,new Item.Properties().group(Mod_ConvenienceBox.tabFactory)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_DESTROYER));}
			{put(NAME_SETTER, new ItemBlock(block_setter,new Item.Properties().group(Mod_ConvenienceBox.tabFactory)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_SETTER));}
			{put(NAME_KILLER, new ItemBlock(block_killer,new Item.Properties().group(Mod_ConvenienceBox.tabFactory)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_KILLER));}
			{put(NAME_EXCOLLECTOR, new ItemBlock(block_excollector,new Item.Properties().group(Mod_ConvenienceBox.tabFactory)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_EXCOLLECTOR));}
			{put(NAME_CRUSSHER, new ItemBlock(block_crusher,new Item.Properties().group(Mod_ConvenienceBox.tabFactory)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_CRUSSHER));}
			{put(NAME_COMPLESSER, new ItemBlock(block_compresser,new Item.Properties().group(Mod_ConvenienceBox.tabFactory)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_COMPLESSER));}
			{put(NAME_LIQUIDCSTRAWER, new ItemBlock(block_straw,new Item.Properties().group(Mod_ConvenienceBox.tabFactory)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_LIQUIDCSTRAWER));}
			{put(NAME_VACUMER, new ItemBlock(block_vacumer,new Item.Properties().group(Mod_ConvenienceBox.tabFactory)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_VACUMER));}
		};


		resourceMap = new HashMap<String,ResourceLocation[]>(){
			{put(NAME_EXPBANK, new ResourceLocation[]{new ResourceLocation(ModCommon.MOD_ID + ":" + NAME_EXPBANK)});}
			{put(NAME_EXENCHANTER, new ResourceLocation[]{new ResourceLocation(ModCommon.MOD_ID+":"+NAME_EXENCHANTER)});}
			{put(NAME_EXANVIL, new ResourceLocation[]{new ResourceLocation(ModCommon.MOD_ID+":"+NAME_EXANVIL)});}

			{put(NAME_PLANTER, new ResourceLocation[]{new ResourceLocation(ModCommon.MOD_ID+":"+NAME_PLANTER)});}
			{put(NAME_HARVESTER, new ResourceLocation[]{new ResourceLocation(ModCommon.MOD_ID+":"+NAME_HARVESTER)});}
			{put(NAME_WOODPLANTER, new ResourceLocation[]{new ResourceLocation(ModCommon.MOD_ID+":"+NAME_WOODPLANTER)});}
			{put(NAME_WOODHARVESTER, new ResourceLocation[]{new ResourceLocation(ModCommon.MOD_ID+":"+NAME_WOODHARVESTER)});}

			{put(NAME_LIQUIDMAKER, new ResourceLocation[]{new ResourceLocation(ModCommon.MOD_ID+":"+NAME_LIQUIDMAKER)});}
			{put(NAME_DESTROYER, new ResourceLocation[]{new ResourceLocation(ModCommon.MOD_ID+":"+NAME_DESTROYER)});}
			{put(NAME_SETTER, new ResourceLocation[]{new ResourceLocation(ModCommon.MOD_ID+":"+NAME_SETTER)});}
			{put(NAME_KILLER, new ResourceLocation[]{new ResourceLocation(ModCommon.MOD_ID+":"+NAME_KILLER)});}
			{put(NAME_EXCOLLECTOR, new ResourceLocation[]{new ResourceLocation(ModCommon.MOD_ID+":"+NAME_EXCOLLECTOR)});}
			{put(NAME_CRUSSHER, new ResourceLocation[]{new ResourceLocation(ModCommon.MOD_ID+":"+NAME_CRUSSHER)});}
			{put(NAME_COMPLESSER, new ResourceLocation[]{new ResourceLocation(ModCommon.MOD_ID+":"+NAME_COMPLESSER)});}
			{put(NAME_LIQUIDCSTRAWER, new ResourceLocation[]{new ResourceLocation(ModCommon.MOD_ID+":"+NAME_LIQUIDCSTRAWER)});}
			{put(NAME_VACUMER, new ResourceLocation[]{new ResourceLocation(ModCommon.MOD_ID+":"+NAME_VACUMER)});}
		};
	}


	public static void registerBlock(final RegistryEvent.Register<Block> event){
		if (blockMap == null){ init();}
		for (String name : NAME_LIST){
			event.getRegistry().register(blockMap.get(name));
		}
	}

	public static void registerItemBlock(final RegistryEvent.Register<Item> event){
		if (blockMap == null){ init();}
		for (String name : NAME_LIST){
			event.getRegistry().register(itemMap.get(name));
		}
	}
}