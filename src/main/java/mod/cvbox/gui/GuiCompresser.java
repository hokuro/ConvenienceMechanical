package mod.cvbox.gui;

import org.apache.commons.lang3.BooleanUtils;

import mod.cvbox.inventory.ContainerComplesser;
import mod.cvbox.network.MessageHandler;
import mod.cvbox.tileentity.TileEntityCompresser;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiCompresser extends GuiContainer {
	private static final ResourceLocation tex = new ResourceLocation("cvbox", "textures/gui/complesser.png");


	private TileEntityCompresser te;


	public GuiCompresser(EntityPlayer player, IInventory tileEntity){
		super(new ContainerComplesser(player.inventory, tileEntity));
		te = (TileEntityCompresser)tileEntity;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int i, int j){
		fontRenderer.drawString("Complesser",8,4,4210752);
        fontRenderer.drawString("Inventory", 8, this.ySize - 96 + 2, 4210752);

	}

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;

        double l = mouseX - (i + 146);
        double i1 = mouseY - (j + 5);

        if (l >= 0 && i1 >= 0 && l < 26 && i1 < 18)
        {
        	MessageHandler.SendMessage_BoxSwitchChante(!BooleanUtils.toBoolean(te.getField(TileEntityCompresser.FIELD_POWER)));
        	//Mod_ConvenienceBox.Net_Instance.sendToServer(new Message_BoxSwitchChange(!BooleanUtils.toBoolean(te.getField(TileEntityCompresser.FIELD_POWER))));
        }
        return true;
    }

	@Override
	public void render(int mouseX, int mouseY, float partialTicks){
        this.drawDefaultBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int x, int y){
		// 背景
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(tex);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);

        this.drawTexturedModalRect(i+12, j+60, 176, 18, getDustBar(TileEntityCompresser.FIELD_IRON), 8);
        this.drawTexturedModalRect(i+39, j+60, 176, 18, getDustBar(TileEntityCompresser.FIELD_GOLD), 8);
        this.drawTexturedModalRect(i+66, j+60, 176, 18, getDustBar(TileEntityCompresser.FIELD_DIAMOND), 8);
        this.drawTexturedModalRect(i+93, j+60, 176, 18, getDustBar(TileEntityCompresser.FIELD_EMERALD), 8);
        this.drawTexturedModalRect(i+120, j+60, 176, 18, getDustBar(TileEntityCompresser.FIELD_REDSTONE), 8);
        this.drawTexturedModalRect(i+147, j+60, 176, 18, getDustBar(TileEntityCompresser.FIELD_LAPIS), 8);

        // COUNTER
        this.drawTexturedModalRect(i+99, j+17, 176, 26, 2, getPlogresBar());
        // COUNTER
        this.drawTexturedModalRect(i+142, j+6, 176, 26, 2, getBatteryBaar());

        if (!BooleanUtils.toBoolean(te.getField(TileEntityCompresser.FIELD_POWER))){
        	this.drawTexturedModalRect(i+146, j+5, 176, 42, 26, 18);
        }
	}

	protected int getDustBar(int id){
		return (int)(20.0F * (float)te.getField(id)/100.0F);
	}

	protected int getPlogresBar(){
		return 16-(int)(16.0F * te.getField(TileEntityCompresser.FIELD_COUNT)/TileEntityCompresser.CRUSH_TIME);
	}

	protected int getBatteryBaar(){
		return 16-(int)(16.0F * (te.getField(TileEntityCompresser.FIELD_BATTERYMAX) - te.getField(TileEntityCompresser.FIELD_BATTERY))/te.getField(TileEntityCompresser.FIELD_BATTERYMAX));
	}
}
