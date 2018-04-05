package mod.cvbox.block.item;

import java.util.List;

import mod.cvbox.tileentity.TileEntityExpBank;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemExpBank extends ItemBlock {
	public ItemExpBank(Block block){
		super(block);
		setHasSubtypes(true);
        this.setMaxDamage(0);
	}

	@Override
	public int getMetadata(int damage){
		return damage;
	}

	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState){
		if(super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState)){
			TileEntityExpBank tile = (TileEntityExpBank) world.getTileEntity(pos);
			if(tile == null){
				return false;
			}
			int face = player.getHorizontalFacing().getOpposite().getIndex();
			if(face < 2){
				face = 2;
			}
			tile.set_face(face);
			return true;
		}
		return false;
	}

	@SideOnly(Side.CLIENT)
	public void getSubItems( CreativeTabs tab, List<ItemStack> subItems){
		if (tab == this.getCreativeTab()){
			for (int n = 1; n < 15; n++){
				subItems.add(new ItemStack(this, 1, n));
			}
		}
	}
}
