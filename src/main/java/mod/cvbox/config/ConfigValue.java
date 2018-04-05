package mod.cvbox.config;

import java.util.HashMap;
import java.util.Map;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ConfigValue{
	public static final int MAX_FIELD = 5000;
	public static final int MAX_DISTANCE = 100;
	private static final ModConfig config = new ModConfig();

	public static void init(FMLPreInitializationEvent event){
		config.init(new Class<?>[]{
//			ExpBank.class
			ExEnchant.class
		}, event);
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
		@ConfigProperty(comment="config.comment.exenchant.tributeitemlevel",max="3",min="1")
		public static int TributeItemLevel  = 2;
		@ConfigProperty(comment="config.comment.exenchant.tributeitemcount",max="64",min="10")
		public static int TrubuteItemCount = 10;
	}

	public static class Planter{
		@ConfigProperty(comment="Maximum farmland 1-5000",max="5000",min="1")
		public static int MaxFarmland = 1000;
		@ConfigProperty(comment="Maximum Distance 1-100" ,max="100",min="1")
		public static int MaxDistance = 30;
		@ConfigProperty(comment="right click possible [true/false]")
		public static boolean RightClick = true;
		@ConfigProperty(comment="enable item ids")
		public static String TargetItemIds = "";
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