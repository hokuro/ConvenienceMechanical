package mod.cvbox.block.item;

import mod.cvbox.tileentity.work.TileEntityExpBank;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;

public class ItemExpBank extends BlockItem {
	public ItemExpBank(Block block, Item.Properties property){
		super(block,property);
	}

	@Override
	public boolean placeBlock(BlockItemUseContext context, BlockState state){
		if(super.placeBlock(context,state)){
			TileEntityExpBank tile = (TileEntityExpBank) context.getWorld().getTileEntity(context.getPos());
			if(tile == null){
				return false;
			}
			int face = context.getPlayer().getHorizontalFacing().getOpposite().getIndex();
			if(face < 2){
				face = 2;
			}
			tile.set_face(face);
			return true;
		}
		return false;
	}
}
