package mod.cvbox.inventory;

import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;

public class InventoryRepair extends InventoryBasic {

    /** Container of this anvil's block. */
    private final mod.cvbox.inventory.ContainerRepair theContainer;

    public InventoryRepair(mod.cvbox.inventory.ContainerRepair containerRepairBA, String inventoryTitle, boolean isLocalized, int slotCount) {
        super(inventoryTitle, isLocalized, slotCount);
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
