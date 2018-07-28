package mod.cvbox.gui;

import java.io.IOException;

import org.apache.commons.lang3.BooleanUtils;

import mod.cvbox.core.Mod_ConvenienceBox;
import mod.cvbox.inventory.ContainerLiquidMaker;
import mod.cvbox.network.Message_BoxSwitchChange;
import mod.cvbox.tileentity.TileEntityLiquidMaker;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiLiquidMaker extends GuiContainer {
	private static final ResourceLocation tex = new ResourceLocation("cvbox", "textures/gui/liquidmaker.png");


	private TileEntityLiquidMaker te;


	public GuiLiquidMaker(EntityPlayer player, IInventory tileEntity){
		super(new ContainerLiquidMaker(player.inventory, tileEntity));
		te = (TileEntityLiquidMaker)tileEntity;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int i, int j){
		fontRenderer.drawString("LiquidMaker",8,4,4210752);
        fontRenderer.drawString("Inventory", 8, this.ySize - 96 + 2, 4210752);

	}

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;

        int l = mouseX - (i + 146);
        int i1 = mouseY - (j + 5);

        if (l >= 0 && i1 >= 0 && l < 26 && i1 < 18)
        {
        	Mod_ConvenienceBox.Net_Instance.sendToServer(new Message_BoxSwitchChange(!BooleanUtils.toBoolean(te.getField(TileEntityLiquidMaker.FIELD_POWER))));
        }
    }

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks){
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int x, int y){
		// 背景
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(tex);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);

        // COUNTER
        this.drawTexturedModalRect(i+142, j+6, 176, 26, 2, getBatteryBaar());

        if (!BooleanUtils.toBoolean(te.getField(TileEntityLiquidMaker.FIELD_POWER))){
        	this.drawTexturedModalRect(i+146, j+5, 176, 42, 26, 18);
        }
	}


	protected int getBatteryBaar(){
		return 16-(int)(16.0F * (te.getField(TileEntityLiquidMaker.FIELD_BATTERYMAX) - te.getField(TileEntityLiquidMaker.FIELD_BATTERY))/te.getField(TileEntityLiquidMaker.FIELD_BATTERYMAX));
	}
}
