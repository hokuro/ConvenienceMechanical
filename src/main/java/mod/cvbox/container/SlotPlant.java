package mod.cvbox.container;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSeeds;
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
    	if ( (stack.getItem() instanceof ItemFood) || (stack.getItem() instanceof ItemSeeds)){
    		return true;
    	}
    	return false;
    }

}
