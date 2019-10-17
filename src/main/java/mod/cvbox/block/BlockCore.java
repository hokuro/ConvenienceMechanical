package mod.cvbox.block;

import java.util.HashMap;
import java.util.Map;

import mod.cvbox.block.factory.BlockBedrockMaker;
import mod.cvbox.block.factory.BlockCompresser;
import mod.cvbox.block.factory.BlockCrusher;
import mod.cvbox.block.factory.BlockDeliverBox;
import mod.cvbox.block.factory.BlockDestroyer;
import mod.cvbox.block.factory.BlockExpCollector;
import mod.cvbox.block.factory.BlockGenomAnalyser;
import mod.cvbox.block.factory.BlockIPSCellMaker;
import mod.cvbox.block.factory.BlockKiller;
import mod.cvbox.block.factory.BlockLiquidMaker;
import mod.cvbox.block.factory.BlockLivingCreator;
import mod.cvbox.block.factory.BlockSetter;
import mod.cvbox.block.factory.BlockStraw;
import mod.cvbox.block.factory.BlockVacumer;
import mod.cvbox.block.farm.BlockAfforestation;
import mod.cvbox.block.farm.BlockAutoFeed;
import mod.cvbox.block.farm.BlockFarming;
import mod.cvbox.block.farm.BlockMillking;
import mod.cvbox.block.farm.BlockWoolCutting;
import mod.cvbox.block.item.ItemExpBank;
import mod.cvbox.block.work.BlockExAnvil;
import mod.cvbox.block.work.BlockExEnchantmentTable;
import mod.cvbox.block.work.BlockExpBank;
import mod.cvbox.core.ModCommon;
import mod.cvbox.core.Mod_ConvenienceBox;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;

public class BlockCore{

	// 工房系
	public static final String NAME_EXPBANK = "expbank";			// 経験値貯金箱
	public static final String NAME_EXENCHANTER = "exenchanter";	// スーパーエンチャントテーブル
	public static final String NAME_EXANVIL ="exanvil";				// スーパーアンビル

	// 農業系
	private static final String NAME_AFFORESTATION = "afforestation";// 植林機
	private static final String NAME_FARMING = "farming";			// 農耕機
	private static final String NAME_AUTOFEED = "autofeed";			// 餌やり機
	private static final String NAME_MILLKING = "millking";			// 搾乳機
	private static final String NAME_WOOLCUTTING = "woolcutting";	// 毛刈り機

	// 工業系
	public static final String NAME_LIQUIDMAKER = "liquidmaker";	//
	public static final String NAME_DESTROYER = "destroyer";		// 破砕機
	public static final String NAME_SETTER = "setter";				// 配置機
	public static final String NAME_KILLER = "kirer";				// 屠殺機
	public static final String NAME_EXCOLLECTOR = "excollector";	// 経験値収集機
	public static final String NAME_CRUSSHER = "crussher";			// 粉砕機
	public static final String NAME_COMPLESSER = "commpleser";		// 圧縮機
	public static final String NAME_LIQUIDCSTRAWER = "straw";		// 給水機
	public static final String NAME_VACUMER = "vacumer";			// 吸引機
	public static final String NAME_GENOMANALYSER = "genomanalyser"; // ゲノム解析機
	public static final String NAME_IPSCELLMAKER = "ipscellmaker";   // IPS細胞生成機
	public static final String NAME_LIVINGCREATOR = "livingcreator"; // 生物培養器
	public static final String NAME_BEDROCKMAKER = "bedrockmaker";	// 岩盤生成期
	public static final String NAME_DELIVERBOX = "deliverbox";		// 輸送管

	public static Block block_expbank = new BlockExpBank(Block.Properties.create(Material.IRON).hardnessAndResistance(1.0F, 2000.0F).harvestTool(ToolType.PICKAXE).sound(SoundType.METAL)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_EXPBANK);
	public static Block block_exenchanter = new BlockExEnchantmentTable(Block.Properties.create(Material.ROCK, MaterialColor.RED).hardnessAndResistance(5.0F, 1200.0F)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_EXENCHANTER);
	public static Block block_exanvil = new BlockExAnvil(Block.Properties.create(Material.ANVIL, MaterialColor.IRON).hardnessAndResistance(5.0F, 1200.0F).sound(SoundType.ANVIL)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_EXANVIL);

	public static Block block_afforestation = new BlockAfforestation(Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(1.0F, 3.0F).sound(SoundType.WOOD)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_AFFORESTATION);
	public static Block block_farming = new BlockFarming(Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(1.0F, 3.0F).sound(SoundType.WOOD)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_FARMING);
	public static Block block_millking = new BlockMillking(Block.Properties.create(Material.EARTH, MaterialColor.IRON).hardnessAndResistance(5.0F, 2000.0F).sound(SoundType.METAL)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_MILLKING);
	public static Block block_woolcutting = new BlockWoolCutting(Block.Properties.create(Material.EARTH, MaterialColor.IRON).hardnessAndResistance(5.0F, 2000.0F).sound(SoundType.METAL)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_WOOLCUTTING);
	public static Block block_autofeed = new BlockAutoFeed(Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(1.0F, 3.0F).sound(SoundType.WOOD)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_AUTOFEED);

	public static Block block_liquidmaker = new BlockLiquidMaker(Block.Properties.create(Material.EARTH, MaterialColor.IRON).hardnessAndResistance(5.0F, 2000.0F).sound(SoundType.METAL)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_LIQUIDMAKER);
	public static Block block_destroyer = new BlockDestroyer(Block.Properties.create(Material.EARTH, MaterialColor.IRON).hardnessAndResistance(5.0F, 2000.0F).sound(SoundType.METAL)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_DESTROYER);
	public static Block block_setter = new BlockSetter(Block.Properties.create(Material.EARTH, MaterialColor.IRON).hardnessAndResistance(5.0F, 2000.0F).sound(SoundType.METAL)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_SETTER);
	public static Block block_killer = new BlockKiller(Block.Properties.create(Material.EARTH, MaterialColor.IRON).hardnessAndResistance(5.0F, 2000.0F).sound(SoundType.METAL)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_KILLER);
	public static Block block_excollector = new BlockExpCollector(Block.Properties.create(Material.EARTH, MaterialColor.IRON).hardnessAndResistance(5.0F, 2000.0F).sound(SoundType.METAL)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_EXCOLLECTOR);
	public static Block block_crusher = new BlockCrusher(Block.Properties.create(Material.EARTH, MaterialColor.IRON).hardnessAndResistance(5.0F, 2000.0F).sound(SoundType.METAL)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_CRUSSHER);
	public static Block block_compresser = new BlockCompresser(Block.Properties.create(Material.EARTH, MaterialColor.IRON).hardnessAndResistance(5.0F, 2000.0F).sound(SoundType.METAL)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_COMPLESSER);
	public static Block block_straw = new BlockStraw(Block.Properties.create(Material.EARTH, MaterialColor.IRON).hardnessAndResistance(5.0F, 2000.0F).sound(SoundType.METAL)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_LIQUIDCSTRAWER);
	public static Block block_vacumer = new BlockVacumer(Block.Properties.create(Material.EARTH, MaterialColor.IRON).hardnessAndResistance(5.0F, 2000.0F).sound(SoundType.METAL)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_VACUMER);
	public static Block block_genomanalyser = new BlockGenomAnalyser(Block.Properties.create(Material.EARTH, MaterialColor.IRON).hardnessAndResistance(5.0F, 2000.0F).sound(SoundType.METAL)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_GENOMANALYSER);
	public static Block block_ipscellmaker = new BlockIPSCellMaker(Block.Properties.create(Material.EARTH, MaterialColor.IRON).hardnessAndResistance(5.0F, 2000.0F).sound(SoundType.METAL)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_IPSCELLMAKER);
	public static Block block_livingcreator = new BlockLivingCreator(Block.Properties.create(Material.EARTH, MaterialColor.IRON).hardnessAndResistance(5.0F, 2000.0F).sound(SoundType.METAL)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_LIVINGCREATOR);
	public static Block block_bedrockmaker = new BlockBedrockMaker(Block.Properties.create(Material.EARTH, MaterialColor.IRON).hardnessAndResistance(5.0F, 2000.0F).sound(SoundType.METAL)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_BEDROCKMAKER);
	public static Block block_deliverbox = new BlockDeliverBox(Block.Properties.create(Material.EARTH, MaterialColor.IRON).hardnessAndResistance(5.0F, 2000.0F).sound(SoundType.METAL)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_DELIVERBOX);

	private static final String[] NAME_LIST = new String[]{
			NAME_EXPBANK,
			NAME_EXENCHANTER,
			NAME_EXANVIL,

			NAME_AFFORESTATION,
			NAME_FARMING,
			NAME_MILLKING,
			NAME_WOOLCUTTING,
			NAME_AUTOFEED,

			NAME_LIQUIDMAKER,
			NAME_DESTROYER,
			NAME_SETTER,
			NAME_KILLER,
			NAME_EXCOLLECTOR,
			NAME_CRUSSHER,
			NAME_COMPLESSER,
			NAME_LIQUIDCSTRAWER,
			NAME_VACUMER,
			NAME_GENOMANALYSER,
			NAME_IPSCELLMAKER,
			NAME_LIVINGCREATOR,
			NAME_BEDROCKMAKER,
			NAME_DELIVERBOX
	};

	private static Map<String,Block> blockMap;
	private static Map<String,Item> itemMap;

	public static void init(){
		blockMap = new HashMap<String,Block>(){
			{put(NAME_EXPBANK,block_expbank);}
			{put(NAME_EXENCHANTER,block_exenchanter);}
			{put(NAME_EXANVIL,block_exanvil);}

			{put(NAME_AFFORESTATION, block_afforestation);}
			{put(NAME_FARMING, block_farming);}
			{put(NAME_MILLKING, block_millking);}
			{put(NAME_WOOLCUTTING, block_woolcutting);}
			{put(NAME_AUTOFEED, block_autofeed);}

			{put(NAME_LIQUIDMAKER,block_liquidmaker);}
			{put(NAME_DESTROYER,block_destroyer);}
			{put(NAME_SETTER,block_setter);}
			{put(NAME_KILLER,block_killer);}
			{put(NAME_EXCOLLECTOR,block_excollector);}
			{put(NAME_CRUSSHER,block_crusher);}
			{put(NAME_COMPLESSER,block_compresser);}
			{put(NAME_LIQUIDCSTRAWER,block_straw);}
			{put(NAME_VACUMER,block_vacumer);}
			{put(NAME_GENOMANALYSER, block_genomanalyser);}
			{put(NAME_IPSCELLMAKER, block_ipscellmaker);}
			{put(NAME_LIVINGCREATOR, block_livingcreator);}
			{put(NAME_BEDROCKMAKER, block_bedrockmaker);}
			{put(NAME_DELIVERBOX,block_deliverbox);}
		};

		itemMap = new HashMap<String,Item>(){
			{put(NAME_EXPBANK,new ItemExpBank(block_expbank, new Item.Properties().group(Mod_ConvenienceBox.tabWorker)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_EXPBANK));}
			{put(NAME_EXENCHANTER, new BlockItem(block_exenchanter, new Item.Properties().group(Mod_ConvenienceBox.tabWorker)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_EXENCHANTER));}
			{put(NAME_EXANVIL, new BlockItem(block_exanvil,new Item.Properties().group(Mod_ConvenienceBox.tabWorker)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_EXANVIL));}

			{put(NAME_AFFORESTATION, new BlockItem(block_afforestation,new Item.Properties().group(Mod_ConvenienceBox.tabFarmer)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_AFFORESTATION));}
			{put(NAME_FARMING, new BlockItem(block_farming,new Item.Properties().group(Mod_ConvenienceBox.tabFarmer)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_FARMING));}
			{put(NAME_MILLKING, new BlockItem(block_millking,new Item.Properties().group(Mod_ConvenienceBox.tabFarmer)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_MILLKING));}
			{put(NAME_WOOLCUTTING, new BlockItem(block_woolcutting,new Item.Properties().group(Mod_ConvenienceBox.tabFarmer)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_WOOLCUTTING));}
			{put(NAME_AUTOFEED, new BlockItem(block_autofeed,new Item.Properties().group(Mod_ConvenienceBox.tabFarmer)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_AUTOFEED));}

			{put(NAME_LIQUIDMAKER, new BlockItem(block_liquidmaker,new Item.Properties().group(Mod_ConvenienceBox.tabFactory)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_LIQUIDMAKER));}
			{put(NAME_DESTROYER, new BlockItem(block_destroyer,new Item.Properties().group(Mod_ConvenienceBox.tabFactory)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_DESTROYER));}
			{put(NAME_SETTER, new BlockItem(block_setter,new Item.Properties().group(Mod_ConvenienceBox.tabFactory)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_SETTER));}
			{put(NAME_KILLER, new BlockItem(block_killer,new Item.Properties().group(Mod_ConvenienceBox.tabFactory)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_KILLER));}
			{put(NAME_EXCOLLECTOR, new BlockItem(block_excollector,new Item.Properties().group(Mod_ConvenienceBox.tabFactory)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_EXCOLLECTOR));}
			{put(NAME_CRUSSHER, new BlockItem(block_crusher,new Item.Properties().group(Mod_ConvenienceBox.tabFactory)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_CRUSSHER));}
			{put(NAME_COMPLESSER, new BlockItem(block_compresser,new Item.Properties().group(Mod_ConvenienceBox.tabFactory)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_COMPLESSER));}
			{put(NAME_LIQUIDCSTRAWER, new BlockItem(block_straw,new Item.Properties().group(Mod_ConvenienceBox.tabFactory)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_LIQUIDCSTRAWER));}
			{put(NAME_VACUMER, new BlockItem(block_vacumer,new Item.Properties().group(Mod_ConvenienceBox.tabFactory)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_VACUMER));}
			{put(NAME_GENOMANALYSER, new BlockItem(block_genomanalyser,new Item.Properties().group(Mod_ConvenienceBox.tabFactory)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_GENOMANALYSER));}
			{put(NAME_IPSCELLMAKER, new BlockItem(block_ipscellmaker,new Item.Properties().group(Mod_ConvenienceBox.tabFactory)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_IPSCELLMAKER));}
			{put(NAME_LIVINGCREATOR, new BlockItem(block_livingcreator,new Item.Properties().group(Mod_ConvenienceBox.tabFactory)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_LIVINGCREATOR));}
			{put(NAME_BEDROCKMAKER, new BlockItem(block_bedrockmaker, new Item.Properties().group(Mod_ConvenienceBox.tabFactory)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_BEDROCKMAKER));}
			{put(NAME_DELIVERBOX, new BlockItem(block_deliverbox, new Item.Properties().group(Mod_ConvenienceBox.tabFactory)).setRegistryName(ModCommon.MOD_ID + ":" + NAME_DELIVERBOX));}
		};
	}


	public static void registerBlock(final RegistryEvent.Register<Block> event){
		for (String name : NAME_LIST){
			if (blockMap.containsKey(name)) {
				event.getRegistry().register(blockMap.get(name));
			}
		}
	}

	public static void registerBlockItem(final RegistryEvent.Register<Item> event){
		for (String name : NAME_LIST){
			if (itemMap.containsKey(name)) {
				event.getRegistry().register(itemMap.get(name));
			}
		}
	}
}
