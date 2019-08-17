package mod.cvbox.inventory;

import mod.cvbox.config.ConfigValue;
import mod.cvbox.util.ModUtil;
import mod.cvbox.util.ModUtil.CompaierLevel;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotPlant extends Slot {

	public SlotPlant(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}

    public boolean isItemValid(ItemStack stack)
    {
    	if (checkExtends(stack)){
    		return true;
    	}
    	return false;
    }

    public static boolean checkExtends(ItemStack stack){
    	return ModUtil.containItemStack(stack, ConfigValue.planter.getItems(), CompaierLevel.LEVEL_EQUAL_ITEM);
    }

}
