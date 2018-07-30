package mod.cvbox.item;

import net.minecraft.item.Item;

public class ItemBattery extends Item {
	public ItemBattery(int cap){
		this.setHasSubtypes(false);
		this.setMaxDamage(cap+1);
		this.setMaxStackSize(1);
	}
}
