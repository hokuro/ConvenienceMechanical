package mod.cvbox.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class GuiRepair extends GuiContainer{// implements ICrafting {

    private mod.cvbox.inventory.ContainerRepair repairContainer;
    private GuiTextField itemNameField;
    private InventoryPlayer playerInventory;

    public GuiRepair(InventoryPlayer playerInventory, World world, int x, int y, int z)
    {
        super(new mod.cvbox.inventory.ContainerRepair(playerInventory, world, x, y, z, playerInventory.player));
        this.playerInventory = playerInventory;
        this.repairContainer = (mod.cvbox.inventory.ContainerRepair)this.inventorySlots;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        Keyboard.enableRepeatEvents(true);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.itemNameField = new GuiTextField(0,this.fontRenderer, x + 62, y + 24, 103, 12);
        this.itemNameField.setTextColor(-1);
        this.itemNameField.setDisabledTextColour(-1);
        this.itemNameField.setEnableBackgroundDrawing(false);
        this.itemNameField.setMaxStringLength(30);
//        this.inventorySlots.removeCraftingFromCrafters(this);
//        this.inventorySlots.onCraftGuiOpened(this);
    }

    @Override
    public void onGuiClosed()
    {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
//        this.inventorySlots.removeCraftingFromCrafters(this);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        GL11.glDisable(GL11.GL_LIGHTING);
        this.fontRenderer.drawString(I18n.translateToLocal("container.repair"), 60, 6, 4210752);

        if (this.repairContainer.maximumCost > 0 || this.repairContainer.isRenamingOnly)
        {
            int colour = 8453920;
            String s = I18n.translateToLocalFormatted("container.repair.cost", repairContainer.maximumCost);

            if (!this.repairContainer.getSlot(2).canTakeStack(this.playerInventory.player))
            {
                colour = 16736352;
            }

            if (this.repairContainer.hadOutput)
            {
                int finalColour = -16777216 | (colour & 16579836) >> 2 | colour & -16777216;
                int stringX = this.xSize - 8 - this.fontRenderer.getStringWidth(s);
                byte stringY = 67;

                if (this.fontRenderer.getUnicodeFlag())
                {
                    drawRect(stringX - 3, stringY - 2, this.xSize - 7, stringY + 10, -16777216);
                    drawRect(stringX - 2, stringY - 1, this.xSize - 8, stringY + 9, -12895429);
                }
                else
                {
                    this.fontRenderer.drawString(s, stringX, stringY + 1, finalColour);
                    this.fontRenderer.drawString(s, stringX + 1, stringY, finalColour);
                    this.fontRenderer.drawString(s, stringX + 1, stringY + 1, finalColour);
                }

                this.fontRenderer.drawString(s, stringX, stringY, colour);
            }
        }

        GL11.glEnable(GL11.GL_LIGHTING);
    }

    @Override
    protected void keyTyped(char character, int key)
    {
        if (this.itemNameField.textboxKeyTyped(character, key))
        {
            this.repairContainer.updateItemName(this.itemNameField.getText());
//            mc.thePlayer.sendQueue.addToSendQueue(new CPacketCustomPayload("MC|ItemName", new PacketBuffer(Unpooled.wrappedBuffer(itemNameField.getText().getBytes(Charsets.UTF_8)))));
        }
        else
        {
            try {
				super.keyTyped(character, key);
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
        }
    }

    @Override
    protected void mouseClicked(int x, int y, int z)
    {
        try {
			super.mouseClicked(x, y, z);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
        this.itemNameField.mouseClicked(x, y, z);
    }

    @Override
    public void drawScreen(int x, int y, float z)
    {
        super.drawScreen(x, y, z);
        GL11.glDisable(GL11.GL_LIGHTING);
        this.itemNameField.drawTextBox();
    }


//	@Override
//	public void updateCraftingInventory(Container containerToSend, List<ItemStack> itemsList) {
//		// TODO 自動生成されたメソッド・スタブ
//
//	}
//
//	@Override
//	public void sendSlotContents(Container containerToSend, int slotInd, ItemStack stack) {
//        if (slotInd == 0)
//        {
//            this.itemNameField.setText(stack == null ? "" : stack.getDisplayName());
//            this.itemNameField.setEnabled(stack != null); //set enabled
//
//            if (stack != null)
//            {
//                this.repairContainer.updateItemName(this.itemNameField.getText());
//                mc.thePlayer.sendQueue.addToSendQueue(new CPacketCustomPayload("MC|ItemName", new PacketBuffer(Unpooled.wrappedBuffer(itemNameField.getText().getBytes(Charsets.UTF_8)))));
//            }
//        }
//
//	}
//
//	@Override
//	public void sendProgressBarUpdate(Container containerIn, int varToUpdate, int newValue) {
//		// TODO 自動生成されたメソッド・スタブ
//
//	}
//
//	@Override
//	public void sendAllWindowProperties(Container p_175173_1_, IInventory p_175173_2_) {
//		// TODO 自動生成されたメソッド・スタブ
//
//	}


	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(new ResourceLocation("minecraft", "textures/gui/container/anvil.png"));
        int centerX = (this.width - this.xSize) / 2;
        int centerY = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(centerX, centerY, 0, 0, this.xSize, this.ySize);
        this.drawTexturedModalRect(centerX + 59, centerY + 20, 0, this.ySize + (this.repairContainer.getSlot(0).getHasStack() ? 0 : 16), 110, 16);

        if ((this.repairContainer.getSlot(0).getHasStack() || this.repairContainer.getSlot(1).getHasStack()) && !this.repairContainer.getSlot(2).getHasStack())
        {
            this.drawTexturedModalRect(centerX + 99, centerY + 45, this.xSize, 0, 28, 21);
        }
	}

}
