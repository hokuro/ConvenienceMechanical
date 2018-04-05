package mod.cvbox.inventory;
import mod.cvbox.block.BlockSuperAnvil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ContainerRepair extends net.minecraft.inventory.ContainerRepair {
	 /** Here comes out item you merged and/or renamed. */
    private IInventory outputSlot = new InventoryCraftResult();

    /**
     * The 2slots where you put your items in that you want to merge and/or rename.
     */
    private IInventory inputSlots = new mod.cvbox.inventory.InventoryRepair(this, "Repair", true, 2);
    private World theWorld;
    private BlockPos blockPos;

    /** determined by damage of input item and stackSize of repair materials */
    private int stackSizeToBeUsedInRepair = 0;
    public ItemStack resultInputStack = (ItemStack)null;
    public ItemStack resultInputStack1 = (ItemStack)null;
    private String repairedItemName;

    /** The player that has this container open. */
    private final EntityPlayer thePlayer;

    //Currently renaming only
    public boolean isRenamingOnly = false;
    public boolean hadOutput = false;

    public ContainerRepair(InventoryPlayer inventoryPlayer, World world, int x, int y, int z, EntityPlayer entityPlayer) {
        super(inventoryPlayer, world, entityPlayer);
    	this.theWorld = world;
    	blockPos = new BlockPos(x,y,z);
        this.thePlayer = entityPlayer;

        this.inventorySlots.clear();
        this.inventoryItemStacks.clear();
        this.addSlotToContainer(new Slot(this.inputSlots, 0, 27, 47));
        this.addSlotToContainer(new Slot(this.inputSlots, 1, 76, 47));
        this.addSlotToContainer(new mod.cvbox.inventory.SlotRepair(this, this.outputSlot, 2, 134, 47, world, x, y, z));
        int l;

        for (l = 0; l < 3; ++l) {
            for (int i1 = 0; i1 < 9; ++i1) {
                this.addSlotToContainer(new Slot(inventoryPlayer, i1 + l * 9 + 9, 8 + i1 * 18, 84 + l * 18));
            }
        }

        for (l = 0; l < 9; ++l) {
            this.addSlotToContainer(new Slot(inventoryPlayer, l, 8 + l * 18, 142));
        }
    }

    /**
     * Callback for when the crafting matrix is changed.
     */
    @Override
    public void onCraftMatrixChanged(IInventory iInventory) {
        super.onCraftMatrixChanged(iInventory);
        if (iInventory == this.inputSlots) {
            this.updateRepairOutput();
        }
    }

    @SuppressWarnings({ "unchecked" })
    @Override
    public void updateRepairOutput() {
//        this.isRenamingOnly = false;
//        this.hadOutput = false;
//        this.resultInputStack = null;
//        this.resultInputStack1 = null;
//        ItemStack stack1 = this.inputSlots.getStackInSlot(0);
//        ItemStack stack2 = this.inputSlots.getStackInSlot(1);
//        double repairCost = 0;
//        double repairAmount = 0;
//        if(stack1 == null) {
//            this.outputSlot.setInventorySlotContents(0, null);
//            this.maximumCost = 0;
//        } else {
//            ItemStack workStack = stack1.copy();
//            //Combine enchantments
//            if(stack2 != null) {
//                Map<Enchantment, Integer> enchantments1 = EnchantmentHelper.getEnchantments(stack1);
//                Map<Enchantment, Integer> enchantments2 = EnchantmentHelper.getEnchantments(stack2);
//                //Used for checking if the stack can be enchanted
//                ItemStack notEnchanted = stack1.copy();
//                EnchantmentHelper.setEnchantments(new HashMap<Enchantment, Integer>(), notEnchanted);
//                if(stack1.getItem() == stack2.getItem()) {
//                    //Enchanted item + same enchanted item = item with incompatible enchantments and item with compatible enchantments
//                    CombinedEnchantments combined = Utils.combine(enchantments1, enchantments2, stack1);
//                    repairCost = combined.repairCost;
//                    repairAmount = combined.repairAmount;
//                    EnchantmentHelper.setEnchantments(combined.compatEnchList, workStack);
//                    if(combined.incompatEnchList.size() != 0) {
//                    	this.resultInputStack = stack2.copy();
//                        EnchantmentHelper.setEnchantments(combined.incompatEnchList, this.resultInputStack);
//                        this.resultInputStack.setItemDamage(this.resultInputStack.getMaxDamage() - combined.incompatEnchList.size());
//                        if(this.resultInputStack.getItem() == Items.enchanted_book && combined.incompatEnchList.isEmpty()) {
//                            this.resultInputStack = new ItemStack(Items.book);
//                        }
//                    }
//                } else if(stack1.getItem() == Items.book && stack2.getItem() == Items.enchanted_book) {
//                    //Copy an enchanted book
//                    if(!enchantments2.isEmpty()) {
//                        this.resultInputStack = stack2.copy();
//                        for(Map.Entry<Enchantment, Integer> entry: enchantments2.entrySet()) {
//                            repairCost += entry.getValue() * ConfigValue.ANVIL.copyEnchantToBookCostMultiplier;
//                            repairAmount += ConfigValue.ANVIL.copyEnchantToBookRepairBonus;
//                        }
//                        workStack = stack2.copy();
//                        if(stack1.stackSize == 1) {
//                        	this.resultInputStack1 = null;
//                        } else {
//                        	ItemStack resultInput = stack1.copy();
//                        	resultInput.stackSize -= 1;
//                        	this.resultInputStack1 = resultInput;
//                        }
//
//                    }
//                } else if((stack1.getItem()== Items.book || stack1.getItem() == Items.enchanted_book) && stack2.isItemEnchanted()) {
//                    //add first enchantment from item to book
//                    Iterator<Map.Entry<Enchantment, Integer>> it = enchantments2.entrySet().iterator();
//                    if(it.hasNext()) {
//                        Map.Entry<Enchantment, Integer> ench = it.next();
//                        enchantments1.put(ench.getKey(), ench.getValue());
//                        repairCost += ench.getValue() * ConfigValue.ANVIL.copyEnchantToBookCostMultiplier;
//                        repairAmount += ConfigValue.ANVIL.copyEnchantToBookRepairBonus;
//                        enchantments2.remove(ench.getKey());
//                    }
//                    workStack = new ItemStack(Items.enchanted_book);
//                    EnchantmentHelper.setEnchantments(enchantments1, workStack);
//                    ItemStack resultInput = stack2.copy();
//                    EnchantmentHelper.setEnchantments(enchantments2, resultInput);
//                    this.resultInputStack = resultInput;
//                    if(stack1.getItem() == Items.book) {
//                    	resultInput = stack1.copy();
//                    	resultInput.stackSize -= 1;
//                        if(resultInput.stackSize == 0) {
//                        	resultInput = null;
//                        }
//                    } else {
//                    	resultInput = null;
//                    }
//                    this.resultInputStack1 = resultInput;
//                } else if(notEnchanted.isItemEnchantable() && stack2.getItem() == Items.enchanted_book) {
//                	CombinedEnchantments combined = Utils.combine(enchantments1, enchantments2, stack1);
//                	repairCost = combined.repairCost;
//                    repairAmount = combined.repairAmount;
//                    EnchantmentHelper.setEnchantments(combined.compatEnchList, workStack);
//                    if(combined.incompatEnchList.size() != 0) {
//                    	this.resultInputStack = new ItemStack(Items.enchanted_book);
//                        EnchantmentHelper.setEnchantments(combined.incompatEnchList, this.resultInputStack);
//                    } else {
//                    	this.resultInputStack = new ItemStack(Items.book);
//                    }
//                }
//            } else {
//            	this.outputSlot.setInventorySlotContents(0, null);
//            }
//            //Rename
//            if (this.repairedItemName != null && this.repairedItemName.length() > 0 && !this.repairedItemName.equals(stack1.getDisplayName())) {
//                workStack.setStackDisplayName(this.repairedItemName);
//                repairCost += ConfigValue.ANVIL.renameingCost;
//                this.isRenamingOnly = repairCost == ConfigValue.ANVIL.renameingCost;
//                if (stack1.getItem().isRepairable()) repairAmount += ConfigValue.ANVIL.renamingRepairBonus;
//            }
//            //Repair
//            if(stack2 != null && stack1.getItem() == stack2.getItem() && stack1.getItem().isRepairable()) {
//                double amount = stack2.getMaxDamage() - stack2.getItemDamage() + ((double)stack1.getMaxDamage() * ConfigValue.ANVIL.mainRepairBonusPercent);
//                repairAmount += amount;
//                repairCost += amount / 100;
//            } else if(stack2 != null && stack1.getItem().getIsRepairable(stack1, stack2)) {
//                double orig = stack1.getMaxDamage() - stack1.getItemDamage() - repairAmount;
//                double damage = orig;
//                int max = workStack.getMaxDamage();
//                int amount = 0;
//                for(int i = 0; i < stack2.stackSize && damage < max; i++) {
//                    damage = Math.min(damage + (max * ConfigValue.ANVIL.itemRepairAmount), max);
//                    amount++;
//                }
//                this.resultInputStack = stack2.copy();
//                this.resultInputStack.stackSize = stack2.stackSize - amount;
//                if(this.resultInputStack.stackSize == 0) {
//                	this.resultInputStack = null;
//                }
//                repairAmount += Math.round(damage) - orig;
//                repairCost += amount * ConfigValue.ANVIL.repairCostPreItem;
//            }
//            //Set outputs
//            workStack.setItemDamage((int)Math.round(workStack.getItemDamage() - repairAmount));
//            this.maximumCost = (int) Math.round(repairCost * ConfigValue.ANVIL.constMultiplaier);
//            if(this.maximumCost > 0 || this.isRenamingOnly) {
//                this.outputSlot.setInventorySlotContents(0, workStack);
//                this.hadOutput = true;
//            }
//            if(!this.getSlot(2).canTakeStack(thePlayer)) {
//            	this.outputSlot.setInventorySlotContents(0, (ItemStack)null);
//            }
//        }
    }

    /**
     * Callback for when the crafting gui is closed.
     */
    @Override
    public void onContainerClosed(EntityPlayer entityPlayer) {
        super.onContainerClosed(entityPlayer);
        if (!this.theWorld.isRemote) {
            for (int i = 0; i < this.inputSlots.getSizeInventory(); ++i) {
                ItemStack itemstack = this.inputSlots.getStackInSlot(i);
                if (itemstack != null) {
                    entityPlayer.entityDropItem(itemstack, theWorld.rand.nextFloat() + 0.05F);
                }
            }
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return theWorld.getBlockState(blockPos).getBlock() instanceof BlockSuperAnvil &&
        		entityPlayer.getDistanceSq((double)this.blockPos.getX() + 0.5D, (double)this.blockPos.getY() + 0.5D, (double)this.blockPos.getZ() + 0.5D) <= 64.0D;
    }

    /**
     * used by the Anvil GUI to update the Item Name being typed by the player
     */
    @Override
    public void updateItemName(String name) {
        this.repairedItemName = name;

        if (this.getSlot(2).getHasStack()) {
            this.getSlot(2).getStack().setStackDisplayName(this.repairedItemName);
        }

        this.updateRepairOutput();
    }

    public static IInventory getRepairInputInventory(mod.cvbox.inventory.ContainerRepair containerRepairBA) {
        return containerRepairBA.inputSlots;
    }

    public static int getStackSizeUsedInRepair(mod.cvbox.inventory.ContainerRepair containerRepairBA) {
        return containerRepairBA.stackSizeToBeUsedInRepair;
    }

}
