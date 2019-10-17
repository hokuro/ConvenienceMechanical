package mod.cvbox.creative;

import mod.cvbox.block.BlockCore;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class CreativeTabFarmarBox extends ItemGroup {
	public CreativeTabFarmarBox(String label){
		super(label);
	}

	@Override
	public ItemStack createIcon() {
		return new ItemStack(BlockCore.block_farming);
	}

	public String getTranslationKey() {
		return this.getTabLabel();
	}
}