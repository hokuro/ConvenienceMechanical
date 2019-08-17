package mod.cvbox.item;

import mod.cvbox.core.Mod_ConvenienceBox;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemSpana extends Item {

	public ItemSpana(){
		super(new Item.Properties().defaultMaxDamage(120).group(Mod_ConvenienceBox.tabFactory));
	}

    public boolean isEnchantable(ItemStack stack)
    {
        return false;
    }
}
