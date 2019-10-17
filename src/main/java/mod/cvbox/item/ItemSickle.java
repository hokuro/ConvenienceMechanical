package mod.cvbox.item;

import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.item.ToolItem;
import net.minecraft.util.ActionResultType;

public class ItemSickle extends ToolItem{
    private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(Blocks.MELON, Blocks.PUMPKIN, Blocks.CACTUS, Blocks.WHEAT, Blocks.POTATOES,Blocks.SUGAR_CANE, Blocks.CARROTS, Blocks.BEETROOTS, Blocks.NETHER_WART,
    		Blocks.CHORUS_FLOWER, Blocks.CHORUS_PLANT, Blocks.COCOA);
    private static final float[] ATTACK_DAMAGES = new float[] {6.0F, 8.0F, 8.0F, 8.0F, 6.0F};
    private static final float[] ATTACK_SPEEDS = new float[] { -3.2F, -3.2F, -3.1F, -3.0F, -3.0F};


    protected ItemSickle(IItemTier tier, int attackDamageIn, float attackSpeedIn, Item.Properties builder) {
        super((float)attackDamageIn, attackSpeedIn, tier, EFFECTIVE_ON,
        		builder.addToolType(net.minecraftforge.common.ToolType.PICKAXE, tier.getHarvestLevel()));
     }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        Material material = state.getMaterial();
        return material != Material.WOOD && material != Material.PLANTS && material != Material.TALL_PLANTS ? super.getDestroySpeed(stack, state) : this.efficiency;
    }


    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        ItemStack itemstack = context.getItem();

        BlockState iblockstate = context.getWorld().getBlockState(context.getPos());
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
    		itemstack.damageItem(1, context.getPlayer(), (player)->{player.sendBreakAnimation(context.getHand());});
    		drop = ItemStack.EMPTY;
        }

        if (!drop.isEmpty()){
        	if (!context.getPlayer().inventory.addItemStackToInventory(drop)){
        		context.getPlayer().dropItem(drop, false);
        	}
        	context.getWorld().setBlockState(context.getPos(), Blocks.AIR.getDefaultState());
    	}
        return ActionResultType.SUCCESS;
    }
}
