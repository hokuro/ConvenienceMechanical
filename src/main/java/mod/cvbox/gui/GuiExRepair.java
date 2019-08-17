package mod.cvbox.gui;

import mod.cvbox.inventory.ContainerExRepair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.play.client.CPacketRenameItem;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GuiExRepair extends GuiContainer implements IContainerListener{

	 private static final ResourceLocation ANVIL_RESOURCE = new ResourceLocation("textures/gui/container/anvil.png");
	 private final ContainerExRepair anvil;
	 private GuiTextField nameField;
	 private final InventoryPlayer playerInventory;

	 public GuiExRepair(InventoryPlayer inventoryIn, World worldIn)
	 {
		 super(new ContainerExRepair(inventoryIn, worldIn, Minecraft.getInstance().player));
		 this.playerInventory = inventoryIn;
		 this.anvil = (ContainerExRepair)this.inventorySlots;
	 }


	 @Override
	 public void initGui()
	 {
		 super.initGui();
	     this.mc.keyboardListener.enableRepeatEvents(true);
		 int i = (this.width - this.xSize) / 2;
		 int j = (this.height - this.ySize) / 2;
		 this.nameField = new GuiTextField(0, this.fontRenderer, i + 62, j + 24, 103, 12);
		 this.nameField.setTextColor(-1);
		 this.nameField.setDisabledTextColour(-1);
		 this.nameField.setEnableBackgroundDrawing(false);
		 this.nameField.setMaxStringLength(35);
	     this.nameField.setTextAcceptHandler(this::func_195393_a);
	     this.children.add(this.nameField);
		 this.inventorySlots.removeListener(this);
		 this.inventorySlots.addListener(this);
	 }

	 @Override
	 public void onGuiClosed()
	 {
		 super.onGuiClosed();
			this.mc.keyboardListener.enableRepeatEvents(false);
	     this.inventorySlots.removeListener(this);
	 }

	 @Override
	 protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	 {
		 GlStateManager.disableLighting();
		 GlStateManager.disableBlend();
		 this.fontRenderer.drawString(net.minecraft.client.resources.I18n.format("container.repair"), 60, 6, 4210752);

		 if (this.anvil.maximumCost > 0)
		 {
			 int i = 8453920;
	         boolean flag = true;
	         String s = net.minecraft.client.resources.I18n.format("container.repair.cost", this.anvil.maximumCost);

	         if (!this.anvil.getSlot(2).getHasStack())
	         {
	             flag = false;
	         }
	         else if (!this.anvil.getSlot(2).canTakeStack(this.playerInventory.player))
	         {
	             i = 16736352;
	         }

	         if (flag)
	         {
	             int j = -16777216 | (i & 16579836) >> 2 | i & -16777216;
	             int k = this.xSize - 8 - this.fontRenderer.getStringWidth(s);
	             int l = 67;
	             //if (this.fontRenderer.)
	             //{
	            	 drawRect(k - 3, 65, this.xSize - 7, 77, -16777216);
	                 drawRect(k - 2, 66, this.xSize - 8, 76, -12895429);
	             //}else{
	           // 	 this.fontRenderer.drawString(s, k, 68, j);
	             //    this.fontRenderer.drawString(s, k + 1, 67, j);
	             //    this.fontRenderer.drawString(s, k + 1, 68, j);
	             //}
	             this.fontRenderer.drawString(s, k, 67, i);
	         }
		 }
		 GlStateManager.enableLighting();
	 }

	 @Override
	 public boolean charTyped(char typedChar, int keyCode)
	 {
		 if (this.nameField.charTyped(typedChar, keyCode))
	     {
			 this.renameItem();
	     }
	     else
	     {
	         super.charTyped(typedChar, keyCode);
	     }
		 return true;
	 }

	 private void renameItem()
	 {
		 String s = this.nameField.getText();
	     Slot slot = this.anvil.getSlot(0);

	     if (slot != null && slot.getHasStack() && !slot.getStack().hasDisplayName() && s.equals(slot.getStack().getDisplayName()))
	     {
	         s = "";
	     }

	     this.anvil.updateItemName(s);
	     this.mc.player.connection.sendPacket(new CPacketCustomPayload());
	 }

	 @Override
	 public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
	 {
		 super.mouseClicked(mouseX, mouseY, mouseButton);
	     this.nameField.mouseClicked(mouseX, mouseY, mouseButton);
	     return true;
	 }

	 @Override
	 public void render(int mouseX, int mouseY, float partialTicks)
	 {
		 this.drawDefaultBackground();
		 super.render(mouseX, mouseY, partialTicks);
		 this.renderHoveredToolTip(mouseX, mouseY);
		 GlStateManager.disableLighting();
		 GlStateManager.disableBlend();
		 this.nameField.drawTextField(mouseX, mouseY, partialTicks);
	 }

	 @Override
	 protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	 {
		 GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		 this.mc.getTextureManager().bindTexture(ANVIL_RESOURCE);
		 int i = (this.width - this.xSize) / 2;
		 int j = (this.height - this.ySize) / 2;
		 this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
		 this.drawTexturedModalRect(i + 59, j + 20, 0, this.ySize + (this.anvil.getSlot(0).getHasStack() ? 0 : 16), 110, 16);

		 if ((this.anvil.getSlot(0).getHasStack() || this.anvil.getSlot(1).getHasStack()) && !this.anvil.getSlot(2).getHasStack())
		 {
			 this.drawTexturedModalRect(i + 99, j + 45, this.xSize, 0, 28, 21);
		 }
	 }

	 @Override
	 public void sendAllContents(Container containerToSend, NonNullList<ItemStack> itemsList)
	 {
		 this.sendSlotContents(containerToSend, 0, containerToSend.getSlot(0).getStack());
	 }

	 @Override
	 public void sendSlotContents(Container containerToSend, int slotInd, ItemStack stack)
	 {
		 if (slotInd == 0)
		 {
			 this.nameField.setText(stack.isEmpty() ? "" : stack.getDisplayName().getFormattedText());
			 this.nameField.setEnabled(!stack.isEmpty());
			 if (!stack.isEmpty())
			 {
				 this.renameItem();
			 }
		 }
	 }


	@Override
	public void sendWindowProperty(Container containerIn, int varToUpdate, int newValue) {

	}


	@Override
	public void sendAllWindowProperties(Container containerIn, IInventory inventory) {

	}

	   private void func_195393_a(int p_195393_1_, String p_195393_2_) {
		      if (!p_195393_2_.isEmpty()) {
		         String s = p_195393_2_;
		         Slot slot = this.anvil.getSlot(0);
		         if (slot != null && slot.getHasStack() && !slot.getStack().hasDisplayName() && p_195393_2_.equals(slot.getStack().getDisplayName().getString())) {
		            s = "";
		         }

		         this.anvil.updateItemName(s);
		         this.mc.player.connection.sendPacket(new CPacketRenameItem(s));
		      }
		   }

}
