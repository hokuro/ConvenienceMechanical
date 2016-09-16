package mod.cvbox.block.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class ItemCvbPlanter extends ItemBlock{
	public ItemCvbPlanter(Block i){
		super(i);
		setHasSubtypes(true);
		setMaxDamage(0);
	}

	@Override
	public int getMetadata(int meta){
		return meta;
	}

}