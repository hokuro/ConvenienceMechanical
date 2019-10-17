package mod.cvbox.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemGoldionHammer extends Item {

	public ItemGoldionHammer(Item.Properties property) {
		super(property);
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		World world = context.getWorld();
		BlockPos pos = context.getPos();
		ItemStack stack = context.getItem();
		Hand hand = context.getHand();

		// 岩盤を削り取る
		BlockState state = world.getBlockState(pos);
		if (state.getBlock() == Blocks.BEDROCK && pos.getY() > 0) {
			if (!world.isRemote) {
				// 岩盤のスポーン
				double x = pos.getX() + this.random.nextDouble() - 0.5D;
				double y = pos.getY() + 1 + (this.random.nextDouble() - 0.5D);
				double z = pos.getZ() + this.random.nextDouble() - 0.5D;
 				Entity spawn = new ItemEntity(world, x, y, z, new ItemStack(Blocks.BEDROCK,1));
 				world.addEntity(spawn);

 				// 岩盤を消去
 				world.removeBlock(pos, false);

 				// ツールにダメージ
 				if (!context.getPlayer().abilities.isCreativeMode) {
 					stack.damageItem(1, context.getPlayer(), (player)->{
 						player.sendBreakAnimation(hand);
 					});
 				}
			}else {
				world.playSound(context.getPlayer(), pos, SoundEvents.BLOCK_STONE_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F+this.random.nextFloat());
			}
		}
		return ActionResultType.PASS;
	}
}
