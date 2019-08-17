package mod.cvbox.item;

import java.util.Set;

import com.google.common.collect.Sets;

import mod.cvbox.core.Mod_ConvenienceBox;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.EnumActionResult;

public class ItemSickle extends ItemTool{
    private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(Blocks.MELON, Blocks.PUMPKIN, Blocks.CACTUS, Blocks.WHEAT, Blocks.POTATOES,Blocks.SUGAR_CANE, Blocks.CARROTS, Blocks.BEETROOTS, Blocks.NETHER_WART,
    		Blocks.CHORUS_FLOWER, Blocks.CHORUS_PLANT, Blocks.COCOA);
    private static final float[] ATTACK_DAMAGES = new float[] {6.0F, 8.0F, 8.0F, 8.0F, 6.0F};
    private static final float[] ATTACK_SPEEDS = new float[] { -3.2F, -3.2F, -3.1F, -3.0F, -3.0F};


    protected ItemSickle(IItemTier tier, int attackDamageIn, float attackSpeedIn, Item.Properties builder) {
        super((float)attackDamageIn, attackSpeedIn, tier, EFFECTIVE_ON,
        		builder.addToolType(net.minecraftforge.common.ToolType.PICKAXE, tier.getHarvestLevel())
        		.group(Mod_ConvenienceBox.tabFarmer));
     }

    @Override
    public float getDestroySpeed(ItemStack stack, IBlockState state)
    {
        Material material = state.getMaterial();
        return material != Material.WOOD && material != Material.PLANTS && material != Material.VINE ? super.getDestroySpeed(stack, state) : this.efficiency;
    }


    @Override
    public EnumActionResult onItemUse(ItemUseContext context)
    {
        ItemStack itemstack = context.getItem();

        IBlockState iblockstate = context.getWorld().getBlockState(context.getPos());
        Block block = iblockstate.getBlock();
        ItemStack drop =ItemStack.EMPTY;
        if (block == Blocks.MELON){
        	drop = new ItemStack(block,1);
        }else if (block == Blocks.PUMPKIN){
        	drop = new ItemStack(block,1);
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
        	drop = new ItemStack(Blocks.COCOA);
        }else if (block == Blocks.CHORUS_PLANT){
        	drop = new ItemStack(Blocks.CHORUS_FLOWER,4);
    		if (!context.getPlayer().inventory.addItemStackToInventory(drop)){
    			context.getPlayer().dropItem(drop, false);
        	}
    		context.getWorld().destroyBlock(context.getPos(), true);
    		itemstack.damageItem(1, context.getPlayer());
    		drop = ItemStack.EMPTY;
        }

        if (!drop.isEmpty()){
        	if (!context.getPlayer().inventory.addItemStackToInventory(drop)){
        		context.getPlayer().dropItem(drop, false);
        	}
        	context.getWorld().setBlockState(context.getPos(), Blocks.AIR.getDefaultState());
    	}
        return EnumActionResult.SUCCESS;
    }
}
