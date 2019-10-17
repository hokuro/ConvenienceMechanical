package mod.cvbox.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public class ItemMobGenom extends Item{

	public ItemMobGenom(Item.Properties property) {
		super(property);
	}

	@Override
	public String getTranslationKey(ItemStack stack) {
		String entityName = this.getMob(stack);
		entityName = I18n.format(Registry.ENTITY_TYPE.getOrDefault(new ResourceLocation(entityName)).getTranslationKey());
		return I18n.format(this.getTranslationKey()) + " " + entityName;
	}

	public static ItemStack setMob(ItemStack stack, String target) {
		CompoundNBT nbt = stack.getOrCreateTag();
		nbt.putString("genom", target);
		stack.setTag(nbt);
		return stack;
	}

	public static String getMob(ItemStack stack) {
		CompoundNBT nbt = stack.getOrCreateTag();
		return Registry.ENTITY_TYPE.getOrDefault(new ResourceLocation(nbt.getString("genom"))).getRegistryName().toString();
	}
}
