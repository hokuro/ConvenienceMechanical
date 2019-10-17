package mod.cvbox.gui.ab;

import org.apache.commons.lang3.BooleanUtils;

import com.mojang.blaze3d.platform.GlStateManager;

import mod.cvbox.inventory.ab.ContainerPowerBase;
import mod.cvbox.network.MessageHandler;
import mod.cvbox.tileentity.ab.ITileEntityParameter;
import mod.cvbox.tileentity.ab.TileEntityPowerBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public abstract class GuiPowerBase extends ContainerScreen<ContainerPowerBase> {

	protected final ITileEntityParameter te;
	public GuiPowerBase(ContainerPowerBase container, PlayerInventory inv, ITextComponent titleIn) {
		super(container, inv, titleIn);
		te = (ITileEntityParameter)container.getTileEntity();
	}

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;

        double l = mouseX - (i + 146);
        double i1 = mouseY - (j + 5);

        if (l >= 0 && i1 >= 0 && l < 26 && i1 < 18) {
        	MessageHandler.SendMessage_BoxSwitchChante(!BooleanUtils.toBoolean(te.getField(TileEntityPowerBase.FIELD_POWER)));
        }
        return true;
    }

	@Override
	public void render(int mouseX, int mouseY, float partialTicks){
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int x, int y){
		// 背景
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getInstance().getTextureManager().bindTexture(getTexture());
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.blit(i, j, 0, 0, this.xSize, this.ySize);
        // COUNTER
        this.blit(i+142, j+6, 176, 0, 2, getBatteryBaar());
        if (!BooleanUtils.toBoolean(te.getField(TileEntityPowerBase.FIELD_POWER))){
        	this.blit(i+146, j+5, 176, 16, 26, 18);
        }
	}

	protected int getBatteryBaar(){
		return 16-(int)(16.0F * (te.getField(TileEntityPowerBase.FIELD_BATTERYMAX) - te.getField(TileEntityPowerBase.FIELD_BATTERY))/te.getField(TileEntityPowerBase.FIELD_BATTERYMAX));
	}

	protected abstract ResourceLocation getTexture();
}
