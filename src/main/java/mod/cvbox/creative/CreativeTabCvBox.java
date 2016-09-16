package mod.cvbox.creative;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CreativeTabCvBox extends CreativeTabs {
	public CreativeTabCvBox(String label){
		super(label);
	}

	@SideOnly(Side.CLIENT)
	public String getTranslatedTabLabel(){
		return "Furniture";
	}

	@Override
	public Item getTabIconItem() {
		return null;//ItemFurniture.item_zabuton;
	}

	@SideOnly(Side.CLIENT)
	public int getIconItemDamage(){
		return 8;
	}
}
