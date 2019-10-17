package mod.cvbox.gui.factory;

import com.mojang.blaze3d.platform.GlStateManager;

import mod.cvbox.gui.ab.GuiPowerBase;
import mod.cvbox.inventory.ab.ContainerPowerBase;
import mod.cvbox.tileentity.factory.TileEntityCompresser;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class GuiCompresser extends GuiPowerBase {
	private static final ResourceLocation tex = new ResourceLocation("cvbox", "textures/gui/complesser.png");

	public GuiCompresser(ContainerPowerBase container, PlayerInventory playerInv, ITextComponent titleIn){
		super(container, playerInv, titleIn);
	}

	protected ResourceLocation getTexture() {
		return tex;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int i, int j){
		font.drawString("Complesser",8,4,4210752);
        font.drawString("Inventory", 8, this.ySize - 96 + 2, 4210752);

	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int x, int y){
		super.drawGuiContainerBackgroundLayer(f, x, y);
		// 背景
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;

        this.blit(i+12, j+60, 176, 34, getDustBar(TileEntityCompresser.FIELD_IRON), 8);
        this.blit(i+39, j+60, 176, 34, getDustBar(TileEntityCompresser.FIELD_GOLD), 8);
        this.blit(i+66, j+60, 176, 34, getDustBar(TileEntityCompresser.FIELD_DIAMOND), 8);
        this.blit(i+93, j+60, 176, 34, getDustBar(TileEntityCompresser.FIELD_EMERALD), 8);
        this.blit(i+120, j+60, 176, 34, getDustBar(TileEntityCompresser.FIELD_REDSTONE), 8);
        this.blit(i+147, j+60, 176, 34, getDustBar(TileEntityCompresser.FIELD_LAPIS), 8);

        // COUNTER
        this.blit(i+99, j+17, 176, 0, 2, getPlogresBar());
	}

	protected int getDustBar(int id){
		return (int)(20.0F * (float)te.getField(id)/100.0F);
	}

	protected int getPlogresBar(){
		return 16-(int)(16.0F * te.getField(TileEntityCompresser.FIELD_COUNT)/TileEntityCompresser.WORK_TIME);
	}
}
