package mod.cvbox.gui.factory;

import mod.cvbox.gui.ab.GuiPowerBase;
import mod.cvbox.inventory.ab.ContainerPowerBase;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class GuiVacumer extends GuiPowerBase {
	private static final ResourceLocation tex = new ResourceLocation("cvbox", "textures/gui/vacumer.png");

	public GuiVacumer(ContainerPowerBase container, PlayerInventory playerInv, ITextComponent titleIn){
		super(container, playerInv, titleIn);
	}

	protected ResourceLocation getTexture() {
		return tex;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int i, int j){
		font.drawString("Vacumer",8,4,4210752);
        font.drawString("Inventory", 8, this.ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int x, int y){
		super.drawGuiContainerBackgroundLayer(f, x, y);
	}
}
