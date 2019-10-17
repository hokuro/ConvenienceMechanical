package mod.cvbox.item;

import java.util.HashMap;
import java.util.Map;

import mod.cvbox.core.Mod_ConvenienceBox;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemTier;
import net.minecraftforge.event.RegistryEvent;

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
	public static final String NAME_TISSUESCISSOR = "tissuescissor";
	public static final String NAME_MOBTISSUE = "mobtissue";
	public static final String NAME_MOBGENOM = "mobgenom";
	public static final String NAME_IPSCELL = "ipscell";
	public static final String NAME_MILLKBALL = "milkball";
	public static final String NAME_GOLDIONHAMMER = "goldionhammer";


	public static final String[] NAME_LIST = new String[]{
			NAME_WOODSICKLE,
			NAME_STONESICKLE,
			NAME_IRONSICKLE,
			NAME_GOLDSICKLE,
			NAME_DIAMONDSICKLE,
			NAME_SPANA,
			NAME_GOLDIONHAMMER,
			NAME_MACHINEMATTER,
			NAME_WATERBALL,
			NAME_LAVABALL,
			NAME_MILLKBALL,
			NAME_BATTERY,
			NAME_EXBATTERY,
			NAME_LARGEBATTERY,
			NAME_HUGEBATTERY,
			NAME_TISSUESCISSOR,
			NAME_MOBTISSUE,
			NAME_MOBGENOM,
			NAME_IPSCELL
	};

	public static Item item_sickle_wood = new ItemSickle(ItemTier.WOOD,6,-3.2F, new Item.Properties().group(ItemGroup.TOOLS)).setRegistryName(NAME_WOODSICKLE);
	public static Item item_sickle_stone = new ItemSickle(ItemTier.STONE,8,-3.2F,new Item.Properties().group(ItemGroup.TOOLS)).setRegistryName(NAME_STONESICKLE);
	public static Item item_sickle_iron = new ItemSickle(ItemTier.IRON,8,-3.1F,new Item.Properties().group(ItemGroup.TOOLS)).setRegistryName(NAME_IRONSICKLE);
	public static Item item_sickle_gold = new ItemSickle(ItemTier.GOLD,8,-3.0F,new Item.Properties().group(ItemGroup.TOOLS)).setRegistryName(NAME_GOLDSICKLE);
	public static Item item_sickle_diamond = new ItemSickle(ItemTier.DIAMOND,6,-3.0F,new Item.Properties().group(ItemGroup.TOOLS)).setRegistryName(NAME_DIAMONDSICKLE);;
	public static Item item_machinematter = new Item(new Item.Properties().group(Mod_ConvenienceBox.tabFactory)).setRegistryName(NAME_MACHINEMATTER);
	public static Item item_waterball = new ItemLiquidBall(Fluids.WATER, new Item.Properties().group(Mod_ConvenienceBox.tabFactory)).setRegistryName(NAME_WATERBALL);
	public static Item item_lavaball =  new ItemLiquidBall(Fluids.LAVA, new Item.Properties().group(Mod_ConvenienceBox.tabFactory)).setRegistryName(NAME_LAVABALL);
	public static Item item_spana = new ItemSpana(new Item.Properties().maxDamage(128).group(Mod_ConvenienceBox.tabFactory)).setRegistryName(NAME_SPANA);
	public static Item item_battery = new ItemBattery(new Item.Properties().maxDamage(8400).group(Mod_ConvenienceBox.tabFactory)).setRegistryName(NAME_BATTERY);
	public static Item item_exbattery = new ItemBattery(new Item.Properties().maxDamage(36000).group(Mod_ConvenienceBox.tabFactory)).setRegistryName(NAME_EXBATTERY);
	public static Item item_largebattery = new ItemBattery(new Item.Properties().maxDamage(438000).group(Mod_ConvenienceBox.tabFactory)).setRegistryName(NAME_LARGEBATTERY);
	public static Item item_hugebattery = new ItemBattery(new Item.Properties().maxDamage(4380000).group(Mod_ConvenienceBox.tabFactory)).setRegistryName(NAME_HUGEBATTERY);
	public static Item item_tissuesissor = new ItemTissueSissor(new Item.Properties().maxDamage(128).group(Mod_ConvenienceBox.tabFactory)).setRegistryName(NAME_TISSUESCISSOR);
	public static Item item_mobtissue = new ItemMobGenom(new Item.Properties().group(Mod_ConvenienceBox.tabFactory)).setRegistryName(NAME_MOBTISSUE);
	public static Item item_mobgenom = new ItemMobGenom(new Item.Properties().group(Mod_ConvenienceBox.tabFactory)).setRegistryName(NAME_MOBGENOM);
	public static Item item_ipscell = new Item(new Item.Properties().group(Mod_ConvenienceBox.tabFactory)).setRegistryName(NAME_IPSCELL);
	public static Item item_millkball = new ItemMillkBall(new Item.Properties().group(Mod_ConvenienceBox.tabFarmer)).setRegistryName(NAME_MILLKBALL);
	public static Item item_goldionhammer = new ItemGoldionHammer(new Item.Properties().maxDamage(128).group(Mod_ConvenienceBox.tabFactory)).setRegistryName(NAME_GOLDIONHAMMER);

	private static Map<String,Item> itemMap = null;
	public static void init(){
		if (itemMap != null){return;}
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
			{put(NAME_LARGEBATTERY,item_largebattery);}
			{put(NAME_HUGEBATTERY,item_hugebattery);}
			{put(NAME_TISSUESCISSOR,item_tissuesissor);}
			{put(NAME_MOBTISSUE,item_mobtissue);}
			{put(NAME_MOBGENOM,item_mobgenom);}
			{put(NAME_IPSCELL,item_ipscell);}
			{put(NAME_MILLKBALL,item_millkball);}
			{put(NAME_GOLDIONHAMMER,item_goldionhammer);}
		};
	}

	public static void register(final RegistryEvent.Register<Item> event) {
		for (String key : NAME_LIST){
			if (itemMap.containsKey(key)) {
				event.getRegistry().register(itemMap.get(key));
			}
		}
	}
}
