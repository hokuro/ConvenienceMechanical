package mod.cvbox.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ConfigValue{
	public static final int MAX_FIELD = 5000;
	public static final int MAX_DISTANCE = 100;
	private static final ModConfig config = new ModConfig();

	public static void init(FMLPreInitializationEvent event){
		config.init(new Class<?>[]{
			ExEnchant.class,
			Planter.class,
			Harvester.class,
			WoodPlanter.class,
			WoodHarvester.class,
			IceMaker.class,
			LavaMaker.class,
		}, event);
	}


	public static void setting() {
		Planter.setItem();
	}

	public static void save(){
		config.saveConfig();
	}

	public static class General{
		@ConfigProperty(comment="Maximum farmland 1-5000",max="5000",min="1")
		public static String[] ENCHANT_LIMITS;

	}

	public static class ExpBank{
	}

	public static class ExEnchant{
		@ConfigProperty(category="ExEnchant", comment="config.comment.exenchant.tributeitemlevel",max="3",min="1")
		public static int TributeItemLevel  = 2;
		@ConfigProperty(category="ExEnchant", comment="config.comment.exenchant.tributeitemcount",max="64",min="10")
		public static int TrubuteItemCount = 10;
	}

	public static class Planter{
		@ConfigProperty(category="Planter", comment="Maximum Distance(x-z) 1-128" ,max="128",min="1")
		public static int MaxDistance = 9;
		@ConfigProperty(category="Planter", comment="Maximum Distance(y) 1-128" ,max="128",min="1")
		public static int MaxHeight = 3;
		@ConfigProperty(category="Planter", comment="Planting one tick 1-128" ,max="128",min="1")
		public static int OneTicPlant = 1;
		@ConfigProperty(category="Planter", comment="Planting Time Span")
		public static final int ExecTimeSpan = 20;
		@ConfigProperty(category="Planter", comment="right click possible [true/false]")
		public static boolean RightClick = true;
		@ConfigProperty(category="Planter", comment="enable item ids")
		public static String TargetItemIds ="minecraft:potato,"
				+ "minecraft:carrot,"
				+ "minecraft:wheat_seeds,"
				+ "minecraft:melon_seeds,"
				+ "minecraft:pumpkin_seeds,"
				+ "minecraft:dye:3,"
				+ "minecraft:beetroot_seeds,"
				+ "minecraft:cactus,"
				+ "minecraft:reeds,"
				+ "minecraft:nether_wart,"
				+ "minecraft:chorus_flower";

		private static List<ItemStack> targetStacks = new ArrayList<ItemStack>();
		public static void setItem(){
			targetStacks.clear();
			String regex="^[0-9]+$";
			Pattern p = Pattern.compile(regex);
			String[] targets = TargetItemIds.split(",");
			for (String target : targets){
				String[] sep = target.trim().split(":");
				String domain = "minecraft";
				String name = "";
				int meta = 0;
				if (sep.length == 1){
					// アイテム名のみ
					name = sep[0];
				}else if (sep.length == 2 && p.matcher(sep[1]).find()){
					// アイテム名 + メタ値
					name = sep[0];
					meta = new Integer(sep[1]).intValue();
				}else if (sep.length == 2){
					// モッド名+アイテム名
					domain = sep[0];
					name = sep[1];
				}else if (sep.length == 3 && p.matcher(sep[2]).find()){
					// モッド名+アイテム名 +メタ値
					domain = sep[0];
					name = sep[1];
					meta = new Integer(sep[2]).intValue();
				}else{
					// 知らんがな
					break;
				}
				ItemStack w = ItemStack.EMPTY;
				try{
					w = new ItemStack(Item.getByNameOrId(domain+":"+name),1,meta);
				}catch(Exception ex){

				}
				if (w.isEmpty()){
					try{
						w = new ItemStack(Block.getBlockFromName(domain+":"+name),1,meta);
					}catch(Exception ex){

					}
				}
				if (!w.isEmpty()){
					targetStacks.add(w.copy());
				}
			}
		}
		public static List<ItemStack> getItems() {
			if (targetStacks.size() == 0){
				setItem();
			}
			return targetStacks;
		}
	}

	public static class Harvester{
		@ConfigProperty(category="Harvester", comment="Maximum Distance(x-z) 1-128" ,max="128",min="1")
		public static int MaxDistance = 9;
		@ConfigProperty(category="Harvester", comment="Maximum Distance(y) 1-128" ,max="128",min="1")
		public static int MaxHeight = 3;
		@ConfigProperty(category="Harvester", comment="Harvest one tick 1-128" ,max="128",min="1")
		public static int OneTicPlant = 1;
		@ConfigProperty(category="Harvester", comment="Planting Time Span")
		public static final int ExecTimeSpan = 20;
		@ConfigProperty(category="Harvester", comment="search planter block distance(x.y.z) 1-128" ,max="128",min="1")
		public static int SearchPlanter = 3;

	}


	public static class WoodPlanter{
		@ConfigProperty(category="WoodPlanter", comment="Maximum Distance(x-z) 1-128" ,max="128",min="1")
		public static int MaxDistance = 9;
		@ConfigProperty(category="WoodPlanter", comment="Maximum Distance(y) 1-128" ,max="128",min="1")
		public static int MaxHeight = 3;
		@ConfigProperty(category="WoodPlanter", comment="Planting one tick 1-128" ,max="128",min="1")
		public static int OneTicPlant = 1;
		@ConfigProperty(category="WoodPlanter", comment="Planting Time Span")
		public static final int ExecTimeSpan = 20;
		@ConfigProperty(category="WoodPlanter", comment="right click possible [true/false]")
		public static boolean RightClick = true;
		@ConfigProperty(category="WoodPlanter", comment="enable item ids")
		public static String TargetItemIds ="minecraft:potato,"
				+ "minecraft:carrot,"
				+ "minecraft:wheat_seeds,"
				+ "minecraft:melon_seeds,"
				+ "minecraft:pumpkin_seeds,"
				+ "minecraft:dye:3,"
				+ "minecraft:beetroot_seeds,"
				+ "minecraft:cactus,"
				+ "minecraft:reeds,"
				+ "minecraft:nether_wart,"
				+ "minecraft:chorus_flower";

		private static List<ItemStack> targetStacks = new ArrayList<ItemStack>();
		public static void setItem(){
			targetStacks.clear();
			String regex="^[0-9]+$";
			Pattern p = Pattern.compile(regex);
			String[] targets = TargetItemIds.split(",");
			for (String target : targets){
				String[] sep = target.trim().split(":");
				String domain = "minecraft";
				String name = "";
				int meta = 0;
				if (sep.length == 1){
					// アイテム名のみ
					name = sep[0];
				}else if (sep.length == 2 && p.matcher(sep[1]).find()){
					// アイテム名 + メタ値
					name = sep[0];
					meta = new Integer(sep[1]).intValue();
				}else if (sep.length == 2){
					// モッド名+アイテム名
					domain = sep[0];
					name = sep[1];
				}else if (sep.length == 3 && p.matcher(sep[2]).find()){
					// モッド名+アイテム名 +メタ値
					domain = sep[0];
					name = sep[1];
					meta = new Integer(sep[2]).intValue();
				}else{
					// 知らんがな
					break;
				}
				ItemStack w = ItemStack.EMPTY;
				try{
					w = new ItemStack(Item.getByNameOrId(domain+":"+name),1,meta);
				}catch(Exception ex){

				}
				if (w.isEmpty()){
					try{
						w = new ItemStack(Block.getBlockFromName(domain+":"+name),1,meta);
					}catch(Exception ex){

					}
				}
				if (!w.isEmpty()){
					targetStacks.add(w.copy());
				}
			}
		}
		public static List<ItemStack> getItems() {
			if (targetStacks.size() == 0){
				setItem();
			}
			return targetStacks;
		}
	}

	public static class WoodHarvester{
		@ConfigProperty(category="WoodHarvester", comment="Maximum Distance(x-z) 1-128" ,max="128",min="1")
		public static int MaxDistance = 9;
		@ConfigProperty(category="WoodHarvester", comment="Maximum Distance(y) 1-128" ,max="128",min="1")
		public static int MaxHeight = 3;
		@ConfigProperty(category="WoodHarvester", comment="Harvest one tick 1-128" ,max="128",min="1")
		public static int OneTicPlant = 1;
		@ConfigProperty(category="WoodHarvester", comment="Planting Time Span")
		public static final int ExecTimeSpan = 20;
		@ConfigProperty(category="WoodHarvester", comment="search planter block distance(x.y.z) 1-128" ,max="128",min="1")
		public static int SearchPlanter = 3;

	}

	public static class IceMaker{
		@ConfigProperty(category="IceMaker", comment="Exec Seconds")
		public static int ExecSec = 10;
	}


	public static class LavaMaker{
		@ConfigProperty(category="IceMaker", comment="Exec Seconds")
		public static int ExecSec = 10;
	}



	public static class ANVIL{
		@ConfigProperty(comment="maltiplay")
		public static double constMultiplaier = 1;
		@ConfigProperty(comment="rname cost")
		public static int renameingCost = 5;
		@ConfigProperty(comment="rname bonus")
		public static int renamingRepairBonus = 1;
		@ConfigProperty(comment="repaier bonus")
		public static double mainRepairBonusPercent = 12/100;
		@ConfigProperty(comment="repair cost")
		public static int repairCostPreItem = 3;
		@ConfigProperty(comment="enchant conbine repair cost")
		public static int enchantCombineRepairCost = 2;
		@ConfigProperty(comment="enchant transfer repair cost")
		public static int enchantTransferRepairCost = 1;
		@ConfigProperty(comment="enchant conbine repair bonus")
		public static int enchantCombineRepairBonus = 2;
		@ConfigProperty(comment="enchant transfer repair bonus")
		public static int enchantTransferRepairBonus = 1;
		@ConfigProperty(comment="copy enchant book cost")
		public static int copyEnchantToBookCostMultiplier =2;
		@ConfigProperty(comment="copy enchant book repair bonus")
		public static int copyEnchantToBookRepairBonus = 1;
		@ConfigProperty(comment="item repair")
		public static double itemRepairAmount = 25/100;
		@ConfigProperty(comment="enchant limit list",isSave=true)
		public static String enchantLimits = "";
		@ConfigProperty(comment="enchant list",isSave=true)
		public static String enchantBlackList = "";

		public static final Map<Integer,Integer> ENCHANT_LIMITS = new HashMap<Integer, Integer>();
		public static final Map<Integer,String[]> ENCHANT_BLACK_LIST = new HashMap<Integer, String[]>();

		private static Map<String,Integer> list_enchLimits = new HashMap<String,Integer>();;
		private static Map<String,String[]> list_enchBlack = new HashMap<String,String[]>();

		public static void reLoade(){
			try{
				Map<String,Integer> workList = new HashMap<String,Integer>();
				StringBuilder works = new StringBuilder();
				String[] enchPair = enchantLimits.split(";");
				for (String ench: enchPair){
					String[] limit = ench.split("=");
					if (limit.length != 2){continue;}
					try{
						workList.put(limit[0], Integer.parseInt(limit[1]));
						works.append(ench);
						works.append(";");
					}catch(NumberFormatException nfex){}
				}
				if (works.length() > 0){works.delete(works.length()-1, works.length()-1);}
				enchantLimits = works.toString();
				list_enchLimits.clear();
				list_enchLimits.putAll(workList);

				Map<String,String[]> workList2 = new HashMap<String,String[]>();
				StringBuilder works2 = new StringBuilder();
				enchPair = enchantBlackList.split(";");
				for (String ench: enchPair){
					String[] bList = ench.split("=");
					if (bList.length != 2){continue;}
					String[] name = bList[1].split(",");
					if (name.length < 1){continue;}
					workList2.put(bList[0], name);

					works2.append(bList[0]);
					works2.append("=");
					for (String str: name){
						works2.append(str);
						works2.append(",");
					}
					works2.delete(works2.length()-1, works2.length()-1);
					works2.append(";");
				}
				if (works2.length() > 0){works2.delete(works2.length()-1, works2.length()-1);}
				enchantBlackList = works2.toString();
				list_enchBlack.clear();
				list_enchBlack.putAll(workList2);

			}catch(Exception ex){

			}
		}

		public static int EnchantmentLimits(String enchName, int defaultLimit) {
			if (list_enchLimits.containsKey(enchName)){
				return list_enchLimits.get(enchName);
			}else{
				if( enchantLimits.length() != 0){
					enchantLimits += ";";
				}
				enchantLimits +=enchName + "=" + Integer.toString(defaultLimit);

			}
			return -1;
		}

		public static String[] EnchantmentBlack(String enchName, String[] array) {
			if (list_enchBlack.containsKey(enchName)){
				return list_enchBlack.get(enchName);
			}else{
				if (enchantBlackList.length() != 0){
					enchantBlackList += ";";
				}
				enchantBlackList += enchName + "=";
				for (int i = 0; i < array.length; i++){
					enchantBlackList += array[i];
					if ( i < array.length-1){
						enchantBlackList += ",";
					}
				}
			}
			return null;
		}

	}

	public static int EnchantmentLimits(String enchName, int defaultLimit) {
		int ret;
		if ((ret = ANVIL.EnchantmentLimits(enchName, defaultLimit))<0){
			save();
			ANVIL.reLoade();
			ret = defaultLimit;
		}
		return ret;
	}

	public static String[] EnchantmentBlack(String enchName, String[] array) {
		String[] ret = null;
		if ((ret = ANVIL.EnchantmentBlack(enchName, array)) == null){
			save();
			ANVIL.reLoade();
			ret = array;
		}
		return ret;
	}





}