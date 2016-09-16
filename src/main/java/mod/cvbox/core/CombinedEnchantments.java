package mod.cvbox.core;

import java.util.Map;

import net.minecraft.enchantment.Enchantment;

public class CombinedEnchantments {
	public final int repairCost;
	public final double repairAmount;
	public final Map<Enchantment,Integer> compatEnchList, incompatEnchList;

	public CombinedEnchantments(int repairCost, double repairAmount, Map<Enchantment, Integer> compatEnchList, Map<Enchantment, Integer> incompatEnchList){
		this.repairCost = repairCost;
		this.repairAmount = repairAmount;
		this.compatEnchList = compatEnchList;
		this.incompatEnchList = incompatEnchList;
	}
}
