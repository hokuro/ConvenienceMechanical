package mod.cvbox.block;

import mod.cvbox.intaractionobject.IntaractionObjectExAnvil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockExAnvil extends BlockAnvil {

    protected BlockExAnvil()
    {
        super(Block.Properties.create(Material.ANVIL));
    }


    @Override
    public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote)
        {
        	NetworkHooks.openGui((EntityPlayerMP)playerIn,
        			new IntaractionObjectExAnvil(),
        			(buf)->{
						buf.writeInt(pos.getX());
						buf.writeInt(pos.getY());
						buf.writeInt(pos.getZ());
					});
        	//playerIn.openGui(Mod_ConvenienceBox.instance, ModCommon.GUIID_EXANVIL, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }

        return true;
    }

}
