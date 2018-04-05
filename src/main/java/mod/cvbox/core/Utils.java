package mod.cvbox.core;

import java.util.HashMap;
import java.util.Map;

import mod.cvbox.config.ConfigValue;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class Utils {
	public static CombinedEnchantments combine(Map<Enchantment, Integer> enchList1, Map<Enchantment, Integer> enchList2, ItemStack item){
		int repairCost = 0;
		double repairAmount = 0;
		Map<Enchantment, Integer> compatEnchList = new HashMap<Enchantment, Integer>();
		Map<Enchantment,Integer>inCompatEnchList = new HashMap<Enchantment,Integer>();
		if (enchList1 != null && enchList2 != null){
			compatEnchList.putAll(enchList1);
			for(Map.Entry<Enchantment,Integer> entry: enchList2.entrySet()){
				Enchantment id = entry.getKey();
				if(compatEnchList.containsKey(id)){
					int value = enchList2.get(id);
					int origVal = compatEnchList.get(id);
					int limit = ConfigValue.ANVIL.ENCHANT_LIMITS.get(id);
					if (origVal == value && origVal < limit){
						compatEnchList.put(id, value+1);
						repairCost += ConfigValue.ANVIL.enchantCombineRepairCost * value;
						repairAmount += ConfigValue.ANVIL.enchantCombineRepairBonus * value;
					}else if(origVal < value){
						compatEnchList.put(id, value);
						repairCost += ConfigValue.ANVIL.enchantTransferRepairCost * value;
						repairAmount += ConfigValue.ANVIL.enchantTransferRepairBonus * value;
					}
				}else if(item.getItem() == Items.ENCHANTED_BOOK || id.canApply(item)){
					//}else if(item.getItem() == Items.enchanted_book || Enchantment.enchantmentsBookList[id].canApply(item)){
					boolean found = false;
					for(Map.Entry<Enchantment,Integer> entry2: compatEnchList.entrySet()){
//						if(contains(ConfigValue.ANVIL.ENCHANT_BLACK_LIST.get(entry2.getKey()), getEnchName(id))) {
//							inCompatEnchList.put(id, entry.getValue());
//							found = true;
//							break;
//						}
					}
					if(!found){
						compatEnchList.put(id, entry.getValue());
						repairCost += ConfigValue.ANVIL.enchantTransferRepairCost * entry.getValue();
						repairAmount += ConfigValue.ANVIL.enchantTransferRepairBonus * entry.getValue();
					}
				}else{
					inCompatEnchList.put(id, entry.getValue());
				}
			}
		}
		return new CombinedEnchantments(repairCost, repairAmount, compatEnchList, inCompatEnchList);
	}

	public static String getEnchName(Enchantment ench){
		return net.minecraft.util.text.translation.I18n.translateToLocal(ench.getName());
	}

//	public static boolean contains(String[] array, String target){
//		if (array != null){
//			for (String value : array){
//				if (value != null && value.equals(target)){
//					return true;
//				}
//			}
//		}
//		return false;
//	}
//
//	public static boolean canApplyTogether(Enchantment ench1, Enchantment ench2){
//		return contains(ConfigValue.ANVIL.ENCHANT_BLACK_LIST.get(ench1.getEnchantmentID(ench1)), getEnchName(ench2));
//	}
}
