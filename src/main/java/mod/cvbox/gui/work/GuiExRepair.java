package mod.cvbox.gui.work;

import com.mojang.blaze3d.platform.GlStateManager;

import mod.cvbox.inventory.work.ContainerExRepair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CRenameItemPacket;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class GuiExRepair extends ContainerScreen<ContainerExRepair> implements IContainerListener{

	private static final ResourceLocation ANVIL_RESOURCE = new ResourceLocation("textures/gui/container/anvil.png");
	private TextFieldWidget nameField;

	public GuiExRepair(ContainerExRepair container, PlayerInventory playerInv, ITextComponent titleIn){
		super(container, playerInv, titleIn);
	}

	@Override
	public void init() {
		super.init();
	    Minecraft.getInstance().keyboardListener.enableRepeatEvents(true);
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		this.nameField = new TextFieldWidget(this.font, i + 62, j + 24, 103, 12, "");
		this.nameField.setCanLoseFocus(false);
		this.nameField.changeFocus(true);
		this.nameField.setTextColor(-1);
		this.nameField.setDisabledTextColour(-1);
		this.nameField.setEnableBackgroundDrawing(false);
		this.nameField.setMaxStringLength(35);
		this.nameField.func_212954_a(this::func_214075_a);
		this.children.add(this.nameField);
		this.container.addListener(this);
		this.func_212928_a(this.nameField);
		this.container.removeListener(this);
		this.container.addListener(this);
	}

	@Override
	public void onClose() {
		super.onClose();
		Minecraft.getInstance().keyboardListener.enableRepeatEvents(false);
		this.container.removeListener(this);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		GlStateManager.disableLighting();
		GlStateManager.disableBlend();
		this.font.drawString(net.minecraft.client.resources.I18n.format("container.repair"), 60, 6, 4210752);

		if (this.container.getMaximumCost() > 0) {
			int i = 8453920;
			boolean flag = true;
			String s = net.minecraft.client.resources.I18n.format("container.repair.cost", this.container.getMaximumCost());

			if (!this.container.getSlot(2).getHasStack()) {
				flag = false;
			} else if (!this.container.getSlot(2).canTakeStack(this.playerInventory.player)) {
				i = 16736352;
			}

			if (flag) {
				int j = -16777216 | (i & 16579836) >> 2 | i & -16777216;
				int k = this.xSize - 8 - this.font.getStringWidth(s);
				int l = 67;

				fill(k - 3, 65, this.xSize - 7, 77, -16777216);
				fill(k - 2, 66, this.xSize - 8, 76, -12895429);
				this.font.drawString(s, k, 67, i);
			}
		}
		GlStateManager.enableLighting();
	}

	@Override
	public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
		if (super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_)) {
			return true;
		} else if (p_keyPressed_1_ != 257 && p_keyPressed_1_ != 335) {
			return false;
		}
		return true;
	}

	private void renameItem() {
		String s = this.nameField.getText();
		Slot slot = this.container.getSlot(0);

		if (slot != null && slot.getHasStack() && !slot.getStack().hasDisplayName() && s.equals(slot.getStack().getDisplayName())) {
			s = "";
		}

		this.container.updateItemName(s);
        this.minecraft.player.connection.sendPacket(new CRenameItemPacket(s));
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		this.nameField.mouseClicked(mouseX, mouseY, mouseButton);
		return true;
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		this.renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
		GlStateManager.disableLighting();
		GlStateManager.disableBlend();
		this.nameField.render(mouseX, mouseY, partialTicks);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		Minecraft.getInstance().getTextureManager().bindTexture(ANVIL_RESOURCE);
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		this.blit(i, j, 0, 0, this.xSize, this.ySize);
		this.blit(i + 59, j + 20, 0, this.ySize + (this.container.getSlot(0).getHasStack() ? 0 : 16), 110, 16);

		if ((this.container.getSlot(0).getHasStack() || this.container.getSlot(1).getHasStack()) && !this.container.getSlot(2).getHasStack())
		{
			this.blit(i + 99, j + 45, this.xSize, 0, 28, 21);
		}
	}

	@Override
	public void sendAllContents(Container containerToSend, NonNullList<ItemStack> itemsList) {
		this.sendSlotContents(containerToSend, 0, containerToSend.getSlot(0).getStack());
	}

	@Override
	public void sendSlotContents(Container containerToSend, int slotInd, ItemStack stack) {
		if (slotInd == 0) {
			this.nameField.setText(stack.isEmpty() ? "" : stack.getDisplayName().getFormattedText());
			this.nameField.setEnabled(!stack.isEmpty());
			if (!stack.isEmpty()) {
				this.renameItem();
			}
		}
	}


	@Override
	public void sendWindowProperty(Container containerIn, int varToUpdate, int newValue) {

	}


	private void func_214075_a(String p_214075_1_) {
		if (!p_214075_1_.isEmpty()) {
			String s = p_214075_1_;
			Slot slot = this.container.getSlot(0);
			if (slot != null && slot.getHasStack() && !slot.getStack().hasDisplayName() && p_214075_1_.equals(slot.getStack().getDisplayName().getString())) {
				s = "";
			}

			this.container.updateItemName(s);
			this.minecraft.player.connection.sendPacket(new CRenameItemPacket(s));
		}
	}

}
