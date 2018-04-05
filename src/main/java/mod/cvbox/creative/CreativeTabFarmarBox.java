package mod.cvbox.creative;

import mod.cvbox.block.BlockCore;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CreativeTabFarmarBox extends CreativeTabs {
	public CreativeTabFarmarBox(String label){
		super(label);
	}

	@SideOnly(Side.CLIENT)
	public String getTranslatedTabLabel(){
		return "farmer box";
	}

	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(BlockCore.block_planter);
	}
}