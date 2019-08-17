package mod.cvbox.block;

import java.util.Random;

import mod.cvbox.intaractionobject.IntaractionObjectStraw;
import mod.cvbox.item.ItemCore;
import mod.cvbox.tileentity.TileEntityStraw;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockStraw extends BlockContainer {

	public BlockStraw() {
		super(Block.Properties.create(Material.GROUND));
	}

	@Override
	public TileEntity createNewTileEntity(IBlockReader world) {
		TileEntityStraw ret = new TileEntityStraw();
		return ret;
	}

	@Override
    public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos,  EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		ItemStack mainstack = playerIn.getHeldItemMainhand();
		if (mainstack.getItem() == ItemCore.item_spana){
    		this.onReplaced(state, worldIn, pos, Blocks.AIR.getDefaultState(),false);
    		worldIn.setBlockState(pos,Blocks.AIR.getDefaultState());
        	InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(this,1));
		}
		else if (!worldIn.isRemote)
        {
			NetworkHooks.openGui((EntityPlayerMP)playerIn,
        			new IntaractionObjectStraw(pos),
        			(buf)->{
						buf.writeInt(pos.getX());
						buf.writeInt(pos.getY());
						buf.writeInt(pos.getZ());
					});
        	//playerIn.openGui(Mod_ConvenienceBox.instance, ModCommon.GUIID_STRAW, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }


	@Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public void onReplaced(IBlockState state, World worldIn, BlockPos pos, IBlockState newstate, boolean isMoving)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TileEntityStraw)
        {
            InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)tileentity);
            TileEntityStraw straw = (TileEntityStraw)tileentity;
            int tank = straw.getField(TileEntityStraw.FIELD_TANK);
            while(tank > 0){
        		InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(straw.getLiquid(),tank>64?64:tank));
            	tank -= 64;
            }
            worldIn.updateComparatorOutputLevel(pos, this);
        }
        super.onReplaced(state, worldIn, pos, newstate,isMoving);
    }

	@Override
    public int quantityDropped(IBlockState state, Random random)
    {
        return random.nextInt(3) + 1;
    }

	@Override
	 public IItemProvider getItemDropped(IBlockState state, World worldIn, BlockPos pos, int fortune)
   {
       return ItemCore.item_machinematter;
   }

}
