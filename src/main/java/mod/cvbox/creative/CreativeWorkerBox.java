package mod.cvbox.creative;

import mod.cvbox.block.BlockCore;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class CreativeWorkerBox extends ItemGroup {
	public CreativeWorkerBox(String label){
		super(label);
	}

	@Override
	public ItemStack createIcon() {
		return new ItemStack(BlockCore.block_exanvil);
	}

	public String getTranslationKey() {
		return this.getTabLabel();
	}

}
