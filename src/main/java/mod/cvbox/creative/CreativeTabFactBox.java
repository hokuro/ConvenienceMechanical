package mod.cvbox.creative;

import mod.cvbox.item.ItemCore;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CreativeTabFactBox  extends CreativeTabs {
	public CreativeTabFactBox(String label){
		super(label);
	}

	@SideOnly(Side.CLIENT)
	public String getTranslatedTabLabel(){
		return "factory box";
	}

	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(ItemCore.item_machinematter);
	}

	@SideOnly(Side.CLIENT)
	public int getIconItemDamage(){
		return 8;
	}
}