package mod.cvbox.inventory;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public class InventoryRepair extends Inventory {

    /** Container of this anvil's block. */
    private final mod.cvbox.inventory.work.ContainerExRepair theContainer;

    public InventoryRepair(mod.cvbox.inventory.work.ContainerExRepair containerRepairBA, String inventoryTitle, boolean isLocalized, int slotCount) {
        super(slotCount);
        this.theContainer = containerRepairBA;
    }

    /**
     * Called when an the contents of an Inventory change, usually
     */
    @Override
    public void markDirty() {
        super.markDirty();
        this.theContainer.onCraftMatrixChanged(this);
    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
     */
    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
        return true;
    }
}
