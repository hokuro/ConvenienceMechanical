package mod.cvbox.block.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class ItemPlanter extends ItemBlock{
	public ItemPlanter(Block i){
		super(i);
		setMaxDamage(0);
	}

	@Override
	public int getMetadata(int meta){
		return meta;
	}

}