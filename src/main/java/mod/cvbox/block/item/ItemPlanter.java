package mod.cvbox.block.item;

import mod.cvbox.core.Mod_ConvenienceBox;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class ItemPlanter extends ItemBlock{
	public ItemPlanter(Block i){
		super(i,new Item.Properties()
				.group(Mod_ConvenienceBox.tabFarmer)
				.defaultMaxDamage(0));
	}
}