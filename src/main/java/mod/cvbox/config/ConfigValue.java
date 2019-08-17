package mod.cvbox.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigValue{
	public static final int MAX_FIELD = 5000;
	public static final int MAX_DISTANCE = 100;

	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General general = new General(BUILDER);
	public static final ExpBank expbank = new ExpBank(BUILDER);
	public static final ExEnchant exenchatn = new ExEnchant(BUILDER);
	public static final Planter planter = new Planter(BUILDER);
	public static final Harvester harvester = new Harvester(BUILDER);
	public static final WoodPlanter woodplanter = new WoodPlanter(BUILDER);
	public static final WoodHarvester woodharvester = new WoodHarvester(BUILDER);
	public static final IceMaker icemaker = new IceMaker(BUILDER);
	public static final LavaMaker lavamaker = new LavaMaker(BUILDER);
	public static final ExpCollector expcollector = new ExpCollector(BUILDER);
	public static final ANVIL anvil = new ANVIL(BUILDER);

	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General{
		public final ForgeConfigSpec.ConfigValue<String> ENCHANT_LIST;

		public General(ForgeConfigSpec.Builder builder){
			builder.push("General");
			ENCHANT_LIST = builder
					.comment("Enchant Name List")
					.define("ENCHANT_LIST", "");
			builder.pop();
		}
	}

	public static class ExpBank{
		public ExpBank(ForgeConfigSpec.Builder builder){
			builder.push("ExpBank");
			builder.pop();
		}
	}

	public static class ExEnchant{
		public final ForgeConfigSpec.ConfigValue<Integer> tributeItemLevel;
		public final ForgeConfigSpec.ConfigValue<Integer> trubuteItemCount;

		public ExEnchant(ForgeConfigSpec.Builder builder){
			builder.push("ExEnchant");
			tributeItemLevel = builder.comment("config.comment.exenchant.tributeitemlevel").defineInRange("TributeItemLevel", 2,1,3);
			trubuteItemCount = builder.comment("config.comment.exenchant.tributeitemcount").defineInRange("TrubuteItemCount", 10,10,64);
			builder.pop();
		}

		public int TributeItemLevel(){
			return this.tributeItemLevel.get();
		}

		public int TrubuteItemCount(){
			return this.trubuteItemCount.get();
		}
	}

	public static class Planter{
		public final ForgeConfigSpec.ConfigValue<Integer> maxDistance;
		public final ForgeConfigSpec.ConfigValue<Integer> maxHeight;
		public final ForgeConfigSpec.ConfigValue<Integer> oneTicPlant;
		public final ForgeConfigSpec.ConfigValue<Integer> execTimeSpan;
		public final ForgeConfigSpec.ConfigValue<Boolean> rightClick;
		public final ForgeConfigSpec.ConfigValue<String> targetItemIds;
		public final ForgeConfigSpec.ConfigValue<Boolean> canfarming;

		public Planter(ForgeConfigSpec.Builder builder){
			builder.push("Planter");
			maxDistance = builder.comment("Maximum Distance(x-z) 1-128").defineInRange("MaxDistance", 4, 1, 128);
			maxHeight = builder.comment("Maximum Distance(y) 1-128").defineInRange("MaxHeight", 3, 1, 128);
			oneTicPlant = builder.comment("Planting one tick 1-128").defineInRange("OneTicPlant", 1, 1, 128);
			execTimeSpan = builder.comment("Planting Time Span").defineInRange("ExecTimeSpan", 20, 1, 200);
			rightClick = builder.comment("right click possible [true/false]").define("RightClick",true);
			targetItemIds = builder.comment("enable item ids").define("TargetItemIds",
					"minecraft:potato,"
					+ "minecraft:carrot,"
					+ "minecraft:wheat_seeds,"
					+ "minecraft:melon_seeds,"
					+ "minecraft:pumpkin_seeds,"
					+ "minecraft:cocoa_beans,"
					+ "minecraft:beetroot_seeds,"
					+ "minecraft:cactus,"
					+ "minecraft:sugar_cane,"
					+ "minecraft:nether_wart,"
					+ "minecraft:chorus_flower");
			canfarming = builder.comment("farming dirt").define("CanFarming", true);
			builder.pop();
		}

		public int MaxDistance(){return this.maxDistance.get();}
		public int MaxHeight(){return this.maxHeight.get();}
		public int OneTicPlant(){return this.oneTicPlant.get();}
		public int ExecTimeSpan(){return this.execTimeSpan.get();}
		public boolean RightClick(){return this.rightClick.get();}
		public String TargetItemIds(){return this.targetItemIds.get();}
		public boolean CanFarming() {return this.canfarming.get();}


		private List<ItemStack> targetStacks = new ArrayList<ItemStack>();
		public void setItem(){
			targetStacks.clear();
			String regex="^[0-9]+$";
			Pattern p = Pattern.compile(regex);
			String[] targets = targetItemIds.get().split(",");
			for (String target : targets){
				ResourceLocation res = new ResourceLocation(target.toLowerCase());

				ItemStack w = ItemStack.EMPTY;
				try{
					w = new ItemStack(IRegistry.field_212630_s.func_212608_b(res));
				}catch(Exception ex){

				}
				if (w.isEmpty()){
					try{
						w = new ItemStack(IRegistry.field_212618_g.func_212608_b(res));
					}catch(Exception ex){

					}
				}
				if (!w.isEmpty()){
					targetStacks.add(w.copy());
				}
			}
		}

		public List<ItemStack> getItems() {
			if (targetStacks.size() == 0){
				setItem();
			}
			return targetStacks;
		}


	}

	public static class Harvester{
		public final ForgeConfigSpec.ConfigValue<Integer> maxDistance;
		public final ForgeConfigSpec.ConfigValue<Integer> maxHeight;
		public final ForgeConfigSpec.ConfigValue<Integer> oneTicPlant;
		public final ForgeConfigSpec.ConfigValue<Integer> execTimeSpan;
		public final ForgeConfigSpec.ConfigValue<Integer> searchPlanter;

		public Harvester(ForgeConfigSpec.Builder builder){
			builder.push("ExEnchant");
			maxDistance = builder.comment("Maximum Distance(x-z) 1-128").defineInRange("MaxDistance", 4,1,128);
			maxHeight = builder.comment("Maximum Distance(y) 1-128").defineInRange("MaxHeight", 3,1,128);
			oneTicPlant = builder.comment("Harvest one tick 1-128").define("OneTicPlant", 1);
			execTimeSpan = builder.comment("Planting Time Span").define("ExecTimeSpan", 20);
			searchPlanter = builder.comment("search planter block distance(x.y.z) 1-128").define("SearchPlanter", 3);
			builder.pop();
		}

		public int MaxDistance(){return maxDistance.get();}
		public int MaxHeight(){return maxHeight.get();}
		public int OneTicPlant(){return oneTicPlant.get();}
		public int ExecTimeSpan(){return execTimeSpan.get();}
		public int SearchPlanter(){return searchPlanter.get();}

	}

	public static class WoodPlanter{
		public final ForgeConfigSpec.ConfigValue<Integer> maxDistance;
		public final ForgeConfigSpec.ConfigValue<Integer> maxHeight;
		public final ForgeConfigSpec.ConfigValue<Integer> oneTicPlant;
		public final ForgeConfigSpec.ConfigValue<Integer> execTimeSpan;
		public final ForgeConfigSpec.ConfigValue<Boolean> rightClick;
		public final ForgeConfigSpec.ConfigValue<String> targetItemIds;

		public WoodPlanter(ForgeConfigSpec.Builder builder){
			builder.push("WoodPlanter");
			maxDistance = builder.comment("Maximum Distance(x-z) 1-128").defineInRange("MaxDistance", 9, 1, 128);
			maxHeight = builder.comment("Maximum Distance(y) 1-128").defineInRange("MaxHeight", 3, 1, 128);
			oneTicPlant = builder.comment("Planting one tick 1-128").defineInRange("OneTicPlant", 1, 1, 128);
			execTimeSpan = builder.comment("Planting Time Span").defineInRange("ExecTimeSpan", 20, 1, 200);
			rightClick = builder.comment("right click possible [true/false]").define("RightClick",true);
			targetItemIds = builder.comment("enable item ids").define("TargetItemIds",
					"oak_sapling,"
							+ "birch_sapling,"
							+ "jungle_sapling,"
							+ "acacia_sapling,"
							+ "dark_oak_sapling,");
			builder.pop();
		}

		public int MaxDistance(){return this.maxDistance.get();}
		public int MaxHeight(){return this.maxHeight.get();}
		public int OneTicPlant(){return this.oneTicPlant.get();}
		public int ExecTimeSpan(){return this.execTimeSpan.get();}
		public boolean RightClick(){return this.rightClick.get();}
		public String TargetItemIds(){return this.targetItemIds.get();}


		private List<ItemStack> targetStacks = new ArrayList<ItemStack>();
		public void setItem(){
			targetStacks.clear();
			String regex="^[0-9]+$";
			Pattern p = Pattern.compile(regex);
			String[] targets = targetItemIds.get().split(",");
			for (String target : targets){
				ResourceLocation res = new ResourceLocation(target.toLowerCase());

				ItemStack w = ItemStack.EMPTY;
				try{
					w = new ItemStack(IRegistry.field_212630_s.func_212608_b(res));
				}catch(Exception ex){

				}
				if (w.isEmpty()){
					try{
						w = new ItemStack(IRegistry.field_212618_g.func_212608_b(res));
					}catch(Exception ex){

					}
				}
				if (!w.isEmpty()){
					targetStacks.add(w.copy());
				}
			}
		}
	}

	public static class WoodHarvester{

		public final ForgeConfigSpec.ConfigValue<Integer> maxDistance;
		public final ForgeConfigSpec.ConfigValue<Integer> maxHeight;
		public final ForgeConfigSpec.ConfigValue<Integer> oneTicPlant;
		public final ForgeConfigSpec.ConfigValue<Integer> execTimeSpan;
		public final ForgeConfigSpec.ConfigValue<Integer> searchPlanter;

		public WoodHarvester(ForgeConfigSpec.Builder builder){
			builder.push("WoodHarvester");
			maxDistance = builder.comment("Maximum Distance(x-z) 1-128").defineInRange("MaxDistance", 9,1,128);
			maxHeight = builder.comment("Maximum Distance(y) 1-128").defineInRange("MaxHeight", 3,1,128);
			oneTicPlant = builder.comment("Harvest one tick 1-128").define("OneTicPlant", 1);
			execTimeSpan = builder.comment("Planting Time Span").define("ExecTimeSpan", 20);
			searchPlanter = builder.comment("search planter block distance(x.y.z) 1-128").define("SearchPlanter", 3);
			builder.pop();
		}

		public int MaxDistance(){return maxDistance.get();}
		public int MaxHeight(){return maxHeight.get();}
		public int OneTicPlant(){return oneTicPlant.get();}
		public int ExecTimeSpan(){return execTimeSpan.get();}
		public int SearchPlanter(){return searchPlanter.get();}

	}

	public static class IceMaker{
		public final ForgeConfigSpec.ConfigValue<Integer> execSec;

		public IceMaker(ForgeConfigSpec.Builder builder){
			builder.push("IceMaker");
			execSec = builder.comment("Exec Seconds").define("ExecSec", 1);
			builder.pop();
		}

		public int ExecSec(){return execSec.get();}
	}

	public static class LavaMaker{
		public final ForgeConfigSpec.ConfigValue<Integer> execSec;

		public LavaMaker(ForgeConfigSpec.Builder builder){
			builder.push("LavaMaker");
			execSec = builder.comment("Exec Seconds").define("ExecSec", 1);
			builder.pop();
		}

		public int ExecSec(){return execSec.get();}
	}

	public static class ExpCollector{
		public final ForgeConfigSpec.ConfigValue<Integer> areaSize;

		public ExpCollector(ForgeConfigSpec.Builder builder){
			builder.push("ExpCollector");
			areaSize = builder.comment("collectable area size x*y*z").define("AreaSize", 10);
			builder.pop();
		}
		public int AreaSize(){return areaSize.get();}
	}

	public static class ANVIL{
		public final ForgeConfigSpec.ConfigValue<Double> constMultiplaier;
		public final ForgeConfigSpec.ConfigValue<Integer> renameingCost;
		public final ForgeConfigSpec.ConfigValue<Integer> renamingRepairBonus;
		public final ForgeConfigSpec.ConfigValue<Double> mainRepairBonusPercent;
		public final ForgeConfigSpec.ConfigValue<Integer> repairCostPreItem;
		public final ForgeConfigSpec.ConfigValue<Integer> enchantCombineRepairCost;
		public final ForgeConfigSpec.ConfigValue<Integer> enchantTransferRepairCost;
		public final ForgeConfigSpec.ConfigValue<Integer> enchantCombineRepairBonus;
		public final ForgeConfigSpec.ConfigValue<Integer> enchantTransferRepairBonus;
		public final ForgeConfigSpec.ConfigValue<Integer> copyEnchantToBookCostMultiplier;
		public final ForgeConfigSpec.ConfigValue<Integer> copyEnchantToBookRepairBonus;
		public final ForgeConfigSpec.ConfigValue<Double> itemRepairAmount;
		public final ForgeConfigSpec.ConfigValue<String> enchantLimits;
		public final ForgeConfigSpec.ConfigValue<String> enchantBlackList;

		public ANVIL(ForgeConfigSpec.Builder builder){
			builder.push("ANVIL");
			constMultiplaier = builder.comment("maltiplay").define("constMultiplaier",1.0D);
			renameingCost = builder.comment("rname cost").define("renameingCost",5);
			renamingRepairBonus = builder.comment("rname bonus").define("renamingRepairBonus",1);
			mainRepairBonusPercent = builder.comment("repaier bonus").define("mainRepairBonusPercent",12.0D/100.0D);
			repairCostPreItem = builder.comment("repair cost").define("repairCostPreItem",3);
			enchantCombineRepairCost = builder.comment("enchant conbine repair cost").define("enchantCombineRepairCost",2);
			enchantTransferRepairCost = builder.comment("enchant transfer repair cost").define("enchantTransferRepairCost",1);
			enchantCombineRepairBonus = builder.comment("enchant conbine repair bonus").define("enchantCombineRepairBonus",2);
			enchantTransferRepairBonus = builder.comment("enchant transfer repair bonus").define("enchantTransferRepairBonus",1);
			copyEnchantToBookCostMultiplier =builder.comment("copy enchant book cost").define("copyEnchantToBookRepairBonus",2);
			copyEnchantToBookRepairBonus = builder.comment("copy enchant book repair bonus").define("copyEnchantToBookRepairBonus",1);
			itemRepairAmount = builder.comment("item repair").define("itemRepairAmount",25.0D/100.0D);
			enchantLimits = builder.comment("enchant limit list").define("enchantLimits","");
			enchantBlackList = builder.comment("enchant list").define("enchantBlackList","");
			builder.pop();
		}


		public double ConstMultiplaier(){return constMultiplaier.get();}
		public int RenameingCost(){return renameingCost.get();}
		public int RenamingRepairBonus(){return renamingRepairBonus.get();}
		public double MainRepairBonusPercent(){return mainRepairBonusPercent.get();}
		public int RepairCostPreItem(){return repairCostPreItem.get();}
		public int EnchantCombineRepairCost(){return enchantCombineRepairCost.get();}
		public int EnchantTransferRepairCost(){return enchantTransferRepairCost.get();}
		public int EnchantCombineRepairBonus(){return enchantCombineRepairBonus.get();}
		public int EnchantTransferRepairBonus(){return enchantTransferRepairBonus.get();}
		public int CopyEnchantToBookCostMultiplier(){return copyEnchantToBookCostMultiplier.get();}
		public int CopyEnchantToBookRepairBonus(){return copyEnchantToBookRepairBonus.get();}
		public double ItemRepairAmount(){return itemRepairAmount.get();}
		public String EnchantLimits(){return enchantLimits.get();}
		public String EnchantBlackList(){return enchantBlackList.get();}

		public Map<Integer,Integer> ENCHANT_LIMITS = new HashMap<Integer, Integer>();
		public Map<Integer,String[]> ENCHANT_BLACK_LIST = new HashMap<Integer, String[]>();

		private Map<String,Integer> list_enchLimits = new HashMap<String,Integer>();;
		private Map<String,String[]> list_enchBlack = new HashMap<String,String[]>();

		public void reLoade(){
			try{
				Map<String,Integer> workList = new HashMap<String,Integer>();
				StringBuilder works = new StringBuilder();
				String[] enchPair = enchantLimits.get().split(";");
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
				//enchantLimits = works.toString();
				list_enchLimits.clear();
				list_enchLimits.putAll(workList);

				Map<String,String[]> workList2 = new HashMap<String,String[]>();
				StringBuilder works2 = new StringBuilder();
				enchPair = enchantBlackList.get().split(";");
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
				//enchantBlackList = works2.toString();
				list_enchBlack.clear();
				list_enchBlack.putAll(workList2);

			}catch(Exception ex){

			}
		}

		public int EnchantmentLimits(String enchName, int defaultLimit) {
			if (list_enchLimits.containsKey(enchName)){
				return list_enchLimits.get(enchName);
			}
			return -1;
		}

		public String[] EnchantmentBlack(String enchName, String[] array) {
			if (list_enchBlack.containsKey(enchName)){
				return list_enchBlack.get(enchName);
			}
			return null;
		}
	}

}