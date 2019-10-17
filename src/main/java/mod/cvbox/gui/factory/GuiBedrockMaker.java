package mod.cvbox.gui.factory;

import com.mojang.blaze3d.platform.GlStateManager;

import mod.cvbox.gui.ab.GuiPowerBase;
import mod.cvbox.inventory.ab.ContainerPowerBase;
import mod.cvbox.tileentity.factory.TileEntityBedrockMaker;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class GuiBedrockMaker extends GuiPowerBase {
	private static final ResourceLocation tex = new ResourceLocation("cvbox", "textures/gui/bedrockmaker.png");

	public GuiBedrockMaker(ContainerPowerBase container, PlayerInventory playerInv, ITextComponent titleIn){
		super(container, playerInv, titleIn);
	}

	protected ResourceLocation getTexture() {
		return tex;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int i, int j){
		font.drawString("Bedrock Maker",8,4,4210752);
        font.drawString("Inventory", 8, this.ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
		super.drawGuiContainerBackgroundLayer(f, x, y);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        int l = getCookProgressionScaled();
		this.blit(i + 79, j + 34, 176, 34, l + 1, 16);
	}


	@OnlyIn(Dist.CLIENT)
	public int getCookProgressionScaled() {
		int i = te.getField(TileEntityBedrockMaker.FIELD_COUNT);
		int j = TileEntityBedrockMaker.WORK_TIME;
		return j != 0 && i != 0 ? i * 24 / j : 0;
	}
}
