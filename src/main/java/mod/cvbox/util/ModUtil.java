package mod.cvbox.util;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ModUtil {
	public static enum CompaierLevel{
		LEVEL_EQUAL_ITEM,
		LEVEL_EQUAL_META,
		LEVEL_EQUAL_COUNT,
		LEVEL_EQUAL_ALL
	};

	public static boolean compareItemStacks(ItemStack stack1, ItemStack stack2 ){
		if (stack1 == null){return false;}
		if (stack2 == null){return false;}
		if (stack1.isEmpty() && stack2.isEmpty()){return true;}
		return (stack2.getItem() == stack1.getItem());
	}

	public static boolean compareItemStacks(ItemStack stack1, ItemStack stack2, CompaierLevel level){
		if (stack1 == null){return false;}
		if (stack2 == null){return false;}
		if (stack1.isEmpty() && stack2.isEmpty()){return true;}

		boolean ret = false;
		switch(level){
		case LEVEL_EQUAL_ALL:
			ret = ((stack1.getItem() == stack2.getItem()) &&
					stack1.getCount() == stack2.getCount());
			break;
		case LEVEL_EQUAL_ITEM:
			ret = ((stack1.getItem() == stack2.getItem()));
			break;
		default:
			break;
		}

		return ret;
	}


	public static boolean containItemStack(ItemStack checkItem,List<ItemStack> itemArray, CompaierLevel leve){
		ItemStack[] stacks = itemArray.toArray(new ItemStack[itemArray.size()]);
		return containItemStack(checkItem,stacks,leve);
	}

	public static boolean containItemStack(ItemStack checkItem, ItemStack[] itemArray, CompaierLevel level) {
		boolean ret = false;
		for (ItemStack item: itemArray){
			if (compareItemStacks(checkItem, item,level)){
				ret = true;
				break;
			}
		}
		return ret;
	}

    public static void spawnItemStack(World worldIn, double x, double y, double z, ItemStack stack, Random RANDOM)
    {
        float f = RANDOM.nextFloat() * 0.8F + 0.1F;
        float f1 = RANDOM.nextFloat() * 0.8F + 0.1F;
        float f2 = RANDOM.nextFloat() * 0.8F + 0.1F;

        while (!stack.isEmpty())
        {
            ItemEntity entityitem = new ItemEntity(worldIn, x + (double)f, y + (double)f1, z + (double)f2, stack.split(RANDOM.nextInt(21) + 10));
            float f3 = 0.05F;
            entityitem.setMotion(RANDOM.nextGaussian() * 0.05000000074505806D,
            						RANDOM.nextGaussian() * 0.05000000074505806D + 0.20000000298023224D,
            						RANDOM.nextGaussian() * 0.05000000074505806D);
            worldIn.addEntity(entityitem);
        }
    }

    public static void addParticleTypes(World worldIn, BlockPos pos, IParticleData part) {
        Random random = worldIn.rand;
        double d0 = 0.0625D;
        for(int i = 0; i < 10; ++i) {
        	double d3 = random.nextGaussian() * 0.02D;
        	double d4 = random.nextGaussian() * 0.02D;
            double d5 = random.nextGaussian() * 0.02D;
            worldIn.addParticle(part, (double)pos.getX() + (double)0.13125F + (double)0.7375F * (double)random.nextFloat(), (double)pos.getY() + d0 + (double)random.nextFloat() * (1.0D - d0), (double)pos.getZ() + (double)0.13125F + (double)0.7375F * (double)random.nextFloat(), d3, d4, d5);
        }
    }

}
