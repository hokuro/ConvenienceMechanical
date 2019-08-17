package mod.cvbox.creative;

import mod.cvbox.item.ItemCore;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class CreativeTabFactBox  extends ItemGroup {
	public CreativeTabFactBox(String label){
		super(label);
	}

	@Override
	public ItemStack createIcon() {
		return new ItemStack(ItemCore.item_machinematter);
	}

	public String getTranslationKey() {
		return this.getTabLabel();
	}

}