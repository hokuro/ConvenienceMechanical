package mod.cvbox.item;

import java.util.HashMap;
import java.util.Map;

import mod.cvbox.core.ModCommon;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
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

	public static final String[] NAME_LIST = new String[]{
			NAME_WOODSICKLE,
			NAME_STONESICKLE,
			NAME_IRONSICKLE,
			NAME_GOLDSICKLE,
			NAME_DIAMONDSICKLE,
	};

	public static Item item_sickle_wood;
	public static Item item_sickle_stone;
	public static Item item_sickle_iron;
	public static Item item_sickle_gold;
	public static Item item_sickle_diamond;


	private static Map<String,Item> itemMap;
	private static Map<String,ModelResourceLocation[]> resourceMap;

	private static void init(){
		item_sickle_wood = new ItemSickle(Item.ToolMaterial.WOOD).setRegistryName(NAME_WOODSICKLE).setUnlocalizedName(NAME_WOODSICKLE);
		item_sickle_stone = new ItemSickle(Item.ToolMaterial.STONE).setRegistryName(NAME_STONESICKLE).setUnlocalizedName(NAME_STONESICKLE);
		item_sickle_iron = new ItemSickle(Item.ToolMaterial.IRON).setRegistryName(NAME_IRONSICKLE).setUnlocalizedName(NAME_IRONSICKLE);
		item_sickle_gold = new ItemSickle(Item.ToolMaterial.GOLD).setRegistryName(NAME_GOLDSICKLE).setUnlocalizedName(NAME_GOLDSICKLE);
		item_sickle_diamond = new ItemSickle(Item.ToolMaterial.DIAMOND).setRegistryName(NAME_DIAMONDSICKLE).setUnlocalizedName(NAME_DIAMONDSICKLE);
		itemMap = new HashMap<String,Item>(){
			{put(NAME_WOODSICKLE,item_sickle_wood);}
			{put(NAME_STONESICKLE,item_sickle_stone);}
			{put(NAME_IRONSICKLE,item_sickle_iron);}
			{put(NAME_GOLDSICKLE,item_sickle_gold);}
			{put(NAME_DIAMONDSICKLE,item_sickle_diamond);}
		};


		resourceMap = new HashMap<String,ModelResourceLocation[]>(){
			{put(NAME_WOODSICKLE,new ModelResourceLocation[]{new ModelResourceLocation(ModCommon.MOD_ID + ":" + NAME_WOODSICKLE, "inventory")});}
			{put(NAME_STONESICKLE,new ModelResourceLocation[]{new ModelResourceLocation(ModCommon.MOD_ID + ":" + NAME_STONESICKLE, "inventory")});}
			{put(NAME_IRONSICKLE,new ModelResourceLocation[]{new ModelResourceLocation(ModCommon.MOD_ID + ":" + NAME_IRONSICKLE, "inventory")});}
			{put(NAME_GOLDSICKLE,new ModelResourceLocation[]{new ModelResourceLocation(ModCommon.MOD_ID + ":" + NAME_GOLDSICKLE, "inventory")});}
			{put(NAME_DIAMONDSICKLE,new ModelResourceLocation[]{new ModelResourceLocation(ModCommon.MOD_ID + ":" + NAME_DIAMONDSICKLE, "inventory")});}
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
