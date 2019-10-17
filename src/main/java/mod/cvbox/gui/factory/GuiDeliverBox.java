package mod.cvbox.gui.factory;

import org.apache.commons.lang3.BooleanUtils;

import com.mojang.blaze3d.platform.GlStateManager;

import mod.cvbox.inventory.factory.ContainerDeliverBox;
import mod.cvbox.network.MessageHandler;
import mod.cvbox.tileentity.factory.TileEntityDeliverBox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class GuiDeliverBox extends ContainerScreen<ContainerDeliverBox> {
	private static final ResourceLocation tex = new ResourceLocation("cvbox", "textures/gui/deliverbox.png");

	public GuiDeliverBox(ContainerDeliverBox container, PlayerInventory inv, ITextComponent titleIn) {
		super(container, inv, titleIn);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int i, int j){
		font.drawString("DeliverBox",8,4,4210752);
        font.drawString("Inventory", 8, this.ySize - 96 + 2, 4210752);

        font.drawString("Match", 128, 59, 0xFFFFFFFF);
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks){
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
	}

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;

        double l = mouseX - (i + 122);
        double i1 = mouseY - (j + 54);

        if (l >= 0 && i1 >= 0 && l < 40 && i1 < 18) {
        	MessageHandler.SendMessage_DeliverBox_ModeChange();
        }
        return true;
    }


	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int x, int y){
		// 背景
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getInstance().getTextureManager().bindTexture(tex);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.blit(i, j, 0, 0, this.xSize, this.ySize);

        TileEntityDeliverBox te = container.getTileEntity();
        if (!BooleanUtils.toBoolean(te.getField(TileEntityDeliverBox.FIELD_ISMATCH))){
        	this.blit(i + 122 ,j + 54, 176, 0, 40, 18);
        }
	}
}
