package mod.cvbox.gui.factory;

import com.mojang.blaze3d.platform.GlStateManager;

import mod.cvbox.gui.ab.GuiPowerBase;
import mod.cvbox.inventory.ab.ContainerPowerBase;
import mod.cvbox.tileentity.factory.TileEntityGenomAnalyser;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class GuiGenomAnalyser extends GuiPowerBase {
	private static final ResourceLocation tex = new ResourceLocation("cvbox", "textures/gui/genomanalyser.png");

	public GuiGenomAnalyser(ContainerPowerBase container, PlayerInventory playerInv, ITextComponent titleIn){
		super(container, playerInv, titleIn);
	}

	protected ResourceLocation getTexture() {
		return tex;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int i, int j){
		font.drawString("Genom Analyser",8,4,4210752);
        font.drawString("Inventory", 8, this.ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
		super.drawGuiContainerBackgroundLayer(f, x, y);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;

        int l = this.getCrushProgressScaled(16);
        this.blit(i + 79, j + 34, 176, 34, 20, l);
	}

    private int getCrushProgressScaled(int pixels) {
        int i = te.getField(TileEntityGenomAnalyser.FIELD_COUNT);
	    return i == 0?0:i * pixels / TileEntityGenomAnalyser.WORK_TIME;
    }
}
