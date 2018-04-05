package mod.cvbox.block;

import mod.cvbox.core.ModCommon;
import mod.cvbox.core.Mod_ConvenienceBox;
import mod.cvbox.tileentity.TileEntityExEnchantmentTable;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockExEnchantmentTable extends BlockContainer {


    protected BlockExEnchantmentTable()
    {
        super(Material.ROCK, MapColor.RED);
        this.setLightOpacity(0);
        this.setCreativeTab(Mod_ConvenienceBox.tabWorker);
        this.setHardness(5.0F);
        this.setResistance(2000.0F);
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityExEnchantmentTable();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote)
        {
            return true;
        }
        else
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileEntityExEnchantmentTable)
            {
                playerIn.openGui(Mod_ConvenienceBox.instance, ModCommon.GUIID_EXENCHANTER, worldIn, pos.getX(), pos.getY(), pos.getZ());
            }

            return true;
        }
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);

        if (stack.hasDisplayName())
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileEntityExEnchantmentTable)
            {
                ((TileEntityExEnchantmentTable)tileentity).setCustomName(stack.getDisplayName());
            }
        }
    }
}
