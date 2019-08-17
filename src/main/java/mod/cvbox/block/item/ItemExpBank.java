package mod.cvbox.block.item;

import mod.cvbox.core.Mod_ConvenienceBox;
import mod.cvbox.tileentity.TileEntityExpBank;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class ItemExpBank extends ItemBlock {
	public ItemExpBank(Block block){
		super(block,new Item.Properties()
				.group(Mod_ConvenienceBox.tabWorker)
				.defaultMaxDamage(0));
	}

	@Override
	public boolean placeBlock(BlockItemUseContext context, IBlockState state){
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
