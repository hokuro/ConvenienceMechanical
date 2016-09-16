package mod.cvbox.block;

import mod.cvbox.core.Mod_ConvenienceBox;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockSuperAnvil extends BlockAnvil{

	public BlockSuperAnvil(){
		setHardness(5.0F);
		setStepSound(SoundType.ANVIL);
		setResistance(2000.0F);
		setUnlocalizedName("anvil");
		setRegistryName("anvil");
	}

	@Override
	 public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ){
		playerIn.openGui(Mod_ConvenienceBox.instance, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}
}