package mod.cvbox.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public class ItemTissueSissor extends Item{

	public ItemTissueSissor(Item.Properties property) {
		super(property);
	}

	@Override
	public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		if (!attacker.world.isRemote) {
			String key = target.getType().getRegistryName().toString();
			ItemStack genom = new ItemStack(ItemCore.item_mobtissue);
			ItemMobGenom.setMob(genom, key);
			attacker.entityDropItem(genom);
			if (attacker instanceof PlayerEntity) {
				if (((PlayerEntity)attacker).abilities.isCreativeMode) {
					stack.damageItem(1, attacker, (entity)->{entity.sendBreakAnimation(Hand.MAIN_HAND);});
				}
			}
		}
		return false;
	}
}
