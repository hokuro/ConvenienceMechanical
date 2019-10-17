package mod.cvbox.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemSpana extends Item {

	public ItemSpana(Item.Properties proeprty){
		super(proeprty);
	}

    public boolean isEnchantable(ItemStack stack){
        return false;
    }
}
