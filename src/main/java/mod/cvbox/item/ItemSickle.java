package mod.cvbox.item;

import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemSickle extends ItemTool{
    private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(Blocks.MELON_BLOCK, Blocks.PUMPKIN, Blocks.CACTUS, Blocks.REEDS, Blocks.WHEAT, Blocks.POTATOES, Blocks.CARROTS, Blocks.BEETROOTS, Blocks.WOODEN_BUTTON, Blocks.NETHER_WART,
    		Blocks.CHORUS_FLOWER, Blocks.CHORUS_PLANT, Blocks.COCOA);
    private static final float[] ATTACK_DAMAGES = new float[] {6.0F, 8.0F, 8.0F, 8.0F, 6.0F};
    private static final float[] ATTACK_SPEEDS = new float[] { -3.2F, -3.2F, -3.1F, -3.0F, -3.0F};


    public ItemSickle(Item.ToolMaterial material){
        super(material, EFFECTIVE_ON);
        this.attackDamage = ATTACK_DAMAGES[material.ordinal()];
        this.attackSpeed = ATTACK_SPEEDS[material.ordinal()];
        this.setCreativeTab(CreativeTabs.TOOLS);
        this.setHarvestLevel("sickle", 1);

	}

    protected ItemSickle(Item.ToolMaterial material, float damage, float speed)
    {
        this(material);
        this.attackDamage = damage;
        this.attackSpeed = speed;
        this.setCreativeTab(CreativeTabs.TOOLS);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, IBlockState state)
    {
        Material material = state.getMaterial();
        return material != Material.WOOD && material != Material.PLANTS && material != Material.VINE ? super.getDestroySpeed(stack, state) : this.efficiency;
    }

    @Override
    public boolean canItemEditBlocks()
    {
        return true;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack itemstack = player.getHeldItem(hand);

        IBlockState iblockstate = worldIn.getBlockState(pos);
        Block block = iblockstate.getBlock();
        ItemStack drop =ItemStack.EMPTY;
        if (block == Blocks.MELON_BLOCK){
        	drop = new ItemStack(block,1,block.getMetaFromState(iblockstate));
        }else if (block == Blocks.PUMPKIN){
        	drop = new ItemStack(block,1,block.getMetaFromState(iblockstate));
        }else if (block == Blocks.WHEAT){
        	drop = new ItemStack(Items.WHEAT,16);
        }else if (block == Blocks.POTATOES){
        	drop = new ItemStack(Items.POTATO,16);
        }else if (block == Blocks.CARROTS){
        	drop = new ItemStack(Items.CARROT,16);
        }else if (block == Blocks.BEETROOTS){
        	drop = new ItemStack(Items.BEETROOT,16);
        }else if (block == Blocks.NETHER_WART){
        	drop = new ItemStack(Items.NETHER_WART,16);
        }else if (block == Blocks.COCOA){
        	drop = new ItemStack(Items.DYE,16,EnumDyeColor.BROWN.getDyeDamage());
        }else if (block == Blocks.REEDS){
        	BlockPos nextPos = pos;
        	while(block == Blocks.REEDS){
        		drop = new ItemStack(Items.REEDS,2);
        		if (!player.inventory.addItemStackToInventory(drop)){
            		player.dropItem(drop, false);
            	}
        		worldIn.setBlockState(nextPos, Blocks.AIR.getDefaultState());
        		nextPos = nextPos.add(0, 1 ,0);
        		block =  worldIn.getBlockState(pos).getBlock();
        	}
    		itemstack.damageItem(1, player);
        	drop = ItemStack.EMPTY;
        }else if (block == Blocks.CHORUS_PLANT){
        	drop = new ItemStack(Blocks.CHORUS_FLOWER,4);
    		if (!player.inventory.addItemStackToInventory(drop)){
        		player.dropItem(drop, false);
        	}
    		worldIn.destroyBlock(pos, true);
    		itemstack.damageItem(1, player);
    		drop = ItemStack.EMPTY;
        }

        if (!drop.isEmpty()){
        	if (!player.inventory.addItemStackToInventory(drop)){
        		player.dropItem(drop, false);
        	}
        	worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
    	}
        return EnumActionResult.SUCCESS;
    }
}
