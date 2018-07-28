package mod.cvbox.item;

import java.util.HashMap;
import java.util.Map;

import mod.cvbox.core.ModCommon;
import mod.cvbox.core.Mod_ConvenienceBox;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ItemCore {
	public static final String NAME_WOODSICKLE = "sickle_wood";
	public static final String NAME_STONESICKLE = "sickle_stone";
	public static final String NAME_IRONSICKLE ="sickle_iron";
	public static final String NAME_GOLDSICKLE = "sickle_gold";
	public static final String NAME_DIAMONDSICKLE = "sickle_diamond";
	public static final String NAME_MACHINEMATTER = "machinematter";
	public static final String NAME_WATERBALL = "waterball";
	public static final String NAME_LAVABALL = "lavaball";
	public static final String NAME_SPANA = "spana";
	public static final String NAME_BATTERY = "battery";
	public static final String NAME_EXBATTERY = "battery_extra";
	public static final String NAME_LARGEBATTERY ="battery_large";
	public static final String NAME_HUGEBATTERY = "battery_huge";


	public static final String[] NAME_LIST = new String[]{
			NAME_WOODSICKLE,
			NAME_STONESICKLE,
			NAME_IRONSICKLE,
			NAME_GOLDSICKLE,
			NAME_DIAMONDSICKLE,
			NAME_MACHINEMATTER,
			NAME_WATERBALL,
			NAME_LAVABALL,
			NAME_SPANA,
			NAME_BATTERY,
			NAME_EXBATTERY,
			NAME_LARGEBATTERY,
			NAME_HUGEBATTERY,
	};

	public static Item item_sickle_wood;
	public static Item item_sickle_stone;
	public static Item item_sickle_iron;
	public static Item item_sickle_gold;
	public static Item item_sickle_diamond;
	public static Item item_machinematter;
	public static Item item_waterball;
	public static Item item_lavaball;
	public static Item item_spana;
	public static Item item_battery;
	public static Item item_exbattery;
	public static Item largebattery;
	public static Item hugebattery;

	private static Map<String,Item> itemMap;
	private static Map<String,ModelResourceLocation[]> resourceMap;

	private static void init(){
		item_sickle_wood = new ItemSickle(Item.ToolMaterial.WOOD).setRegistryName(NAME_WOODSICKLE).setUnlocalizedName(NAME_WOODSICKLE);
		item_sickle_stone = new ItemSickle(Item.ToolMaterial.STONE).setRegistryName(NAME_STONESICKLE).setUnlocalizedName(NAME_STONESICKLE);
		item_sickle_iron = new ItemSickle(Item.ToolMaterial.IRON).setRegistryName(NAME_IRONSICKLE).setUnlocalizedName(NAME_IRONSICKLE);
		item_sickle_gold = new ItemSickle(Item.ToolMaterial.GOLD).setRegistryName(NAME_GOLDSICKLE).setUnlocalizedName(NAME_GOLDSICKLE);
		item_sickle_diamond = new ItemSickle(Item.ToolMaterial.DIAMOND).setRegistryName(NAME_DIAMONDSICKLE).setUnlocalizedName(NAME_DIAMONDSICKLE);
		item_machinematter = new Item().setCreativeTab(Mod_ConvenienceBox.tabFactory).setRegistryName(NAME_MACHINEMATTER).setUnlocalizedName(NAME_MACHINEMATTER);
		item_waterball = new ItemLiquidBall(Blocks.FLOWING_WATER).setCreativeTab(Mod_ConvenienceBox.tabFactory).setRegistryName(NAME_WATERBALL).setUnlocalizedName(NAME_WATERBALL);
		item_lavaball =  new ItemLiquidBall(Blocks.FLOWING_LAVA).setCreativeTab(Mod_ConvenienceBox.tabFactory).setRegistryName(NAME_LAVABALL).setUnlocalizedName(NAME_LAVABALL);
		item_spana = new ItemSpana()
				.setMaxDamage(120)
				.setMaxStackSize(1)
				.setCreativeTab(Mod_ConvenienceBox.tabFactory).setRegistryName(NAME_SPANA).setUnlocalizedName(NAME_SPANA);
		item_battery = new ItemBattery(8400).setCreativeTab(Mod_ConvenienceBox.tabFactory).setRegistryName(NAME_BATTERY).setUnlocalizedName(NAME_BATTERY);
		item_exbattery = new ItemBattery(36000).setCreativeTab(Mod_ConvenienceBox.tabFactory).setRegistryName(NAME_EXBATTERY).setUnlocalizedName(NAME_EXBATTERY);
		largebattery = new ItemBattery(438000).setCreativeTab(Mod_ConvenienceBox.tabFactory).setRegistryName(NAME_LARGEBATTERY).setUnlocalizedName(NAME_LARGEBATTERY);
		hugebattery = new ItemBattery(4380000).setCreativeTab(Mod_ConvenienceBox.tabFactory).setRegistryName(NAME_HUGEBATTERY).setUnlocalizedName(NAME_HUGEBATTERY);

		itemMap = new HashMap<String,Item>(){
			{put(NAME_WOODSICKLE,item_sickle_wood);}
			{put(NAME_STONESICKLE,item_sickle_stone);}
			{put(NAME_IRONSICKLE,item_sickle_iron);}
			{put(NAME_GOLDSICKLE,item_sickle_gold);}
			{put(NAME_DIAMONDSICKLE,item_sickle_diamond);}
			{put(NAME_MACHINEMATTER,item_machinematter);}
			{put(NAME_WATERBALL,item_waterball);}
			{put(NAME_LAVABALL,item_lavaball);}
			{put(NAME_SPANA,item_spana);}
			{put(NAME_BATTERY,item_battery);}
			{put(NAME_EXBATTERY,item_exbattery);}
			{put(NAME_LARGEBATTERY,largebattery);}
			{put(NAME_HUGEBATTERY,hugebattery);}
		};

		resourceMap = new HashMap<String,ModelResourceLocation[]>(){
			{put(NAME_WOODSICKLE,new ModelResourceLocation[]{new ModelResourceLocation(ModCommon.MOD_ID + ":" + NAME_WOODSICKLE, "inventory")});}
			{put(NAME_STONESICKLE,new ModelResourceLocation[]{new ModelResourceLocation(ModCommon.MOD_ID + ":" + NAME_STONESICKLE, "inventory")});}
			{put(NAME_IRONSICKLE,new ModelResourceLocation[]{new ModelResourceLocation(ModCommon.MOD_ID + ":" + NAME_IRONSICKLE, "inventory")});}
			{put(NAME_GOLDSICKLE,new ModelResourceLocation[]{new ModelResourceLocation(ModCommon.MOD_ID + ":" + NAME_GOLDSICKLE, "inventory")});}
			{put(NAME_DIAMONDSICKLE,new ModelResourceLocation[]{new ModelResourceLocation(ModCommon.MOD_ID + ":" + NAME_DIAMONDSICKLE, "inventory")});}
			{put(NAME_MACHINEMATTER,new ModelResourceLocation[]{new ModelResourceLocation(ModCommon.MOD_ID + ":" + NAME_MACHINEMATTER, "inventory")});}
			{put(NAME_WATERBALL,new ModelResourceLocation[]{new ModelResourceLocation(ModCommon.MOD_ID + ":" + NAME_WATERBALL, "inventory")});}
			{put(NAME_LAVABALL,new ModelResourceLocation[]{new ModelResourceLocation(ModCommon.MOD_ID + ":" + NAME_LAVABALL, "inventory")});}
			{put(NAME_SPANA,new ModelResourceLocation[]{new ModelResourceLocation(ModCommon.MOD_ID + ":" + NAME_SPANA, "inventory")});}
			{put(NAME_BATTERY,new ModelResourceLocation[]{new ModelResourceLocation(ModCommon.MOD_ID + ":" + NAME_BATTERY, "inventory")});}
			{put(NAME_EXBATTERY,new ModelResourceLocation[]{new ModelResourceLocation(ModCommon.MOD_ID + ":" + NAME_EXBATTERY, "inventory")});}
			{put(NAME_LARGEBATTERY,new ModelResourceLocation[]{new ModelResourceLocation(ModCommon.MOD_ID + ":" + NAME_LARGEBATTERY, "inventory")});}
			{put(NAME_HUGEBATTERY,new ModelResourceLocation[]{new ModelResourceLocation(ModCommon.MOD_ID + ":" + NAME_HUGEBATTERY, "inventory")});}
		};
	}

	public static void register(FMLPreInitializationEvent event) {
		init();
		for (String key : NAME_LIST){
			ForgeRegistries.ITEMS.register(itemMap.get(key));
		}

        //テクスチャ・モデル指定JSONファイル名の登録。
        if (event.getSide().isClient()) {
        	for (String key : NAME_LIST){
        		//1IDで複数モデルを登録するなら、上のメソッドで登録した登録名を指定する。
        		int cnt = 0;
        		for (ModelResourceLocation rc : resourceMap.get(key)){
        			ModelLoader.setCustomModelResourceLocation(itemMap.get(key), cnt, rc);
        			cnt++;
        		}
        	}
        }
	}
}
