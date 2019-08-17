package mod.cvbox.item;

import mod.cvbox.core.Mod_ConvenienceBox;
import net.minecraft.item.Item;

public class ItemBattery extends Item {
	public ItemBattery(int cap){
		super(new Item.Properties()
				.maxStackSize(1)
				.defaultMaxDamage(cap+1)
				.group(Mod_ConvenienceBox.tabFactory));
	}
}
