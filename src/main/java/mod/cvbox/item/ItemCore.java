package mod.cvbox.item;

import java.util.HashMap;
import java.util.Map;

import mod.cvbox.core.Mod_ConvenienceBox;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
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

	public static Item item_sickle_wood = new ItemSickle(ItemTier.WOOD,6,-3.2F, new Item.Properties()).setRegistryName(NAME_WOODSICKLE);
	public static Item item_sickle_stone = new ItemSickle(ItemTier.STONE,8,-3.2F,new Item.Properties()).setRegistryName(NAME_STONESICKLE);
	public static Item item_sickle_iron = new ItemSickle(ItemTier.IRON,8,-3.1F,new Item.Properties()).setRegistryName(NAME_IRONSICKLE);
	public static Item item_sickle_gold = new ItemSickle(ItemTier.GOLD,8,-3.0F,new Item.Properties()).setRegistryName(NAME_GOLDSICKLE);
	public static Item item_sickle_diamond = new ItemSickle(ItemTier.DIAMOND,6,-3.0F,new Item.Properties()).setRegistryName(NAME_DIAMONDSICKLE);;
	public static Item item_machinematter = new Item(new Item.Properties().group(Mod_ConvenienceBox.tabFactory)).setRegistryName(NAME_MACHINEMATTER);
	public static Item item_waterball = new ItemLiquidBall(Blocks.WATER).setRegistryName(NAME_WATERBALL);
	public static Item item_lavaball =  new ItemLiquidBall(Blocks.LAVA).setRegistryName(NAME_LAVABALL);
	public static Item item_spana = new ItemSpana().setRegistryName(NAME_SPANA);
	public static Item item_battery = new ItemBattery(8400).setRegistryName(NAME_BATTERY);
	public static Item item_exbattery = new ItemBattery(36000).setRegistryName(NAME_EXBATTERY);
	public static Item largebattery = new ItemBattery(438000).setRegistryName(NAME_LARGEBATTERY);
	public static Item hugebattery = new ItemBattery(4380000).setRegistryName(NAME_HUGEBATTERY);

	private static Map<String,Item> itemMap = null;
	private static void init(){
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
			{put(NAME_LARGEBATTERY,largebattery);}
			{put(NAME_HUGEBATTERY,hugebattery);}
		};
	}

	public static void register(final RegistryEvent.Register<Item> event) {
		init();
		for (String key : NAME_LIST){
			event.getRegistry().register(itemMap.get(key));
		}
	}
}
