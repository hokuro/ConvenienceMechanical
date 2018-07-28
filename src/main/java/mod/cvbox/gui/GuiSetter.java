package mod.cvbox.gui;

import java.io.IOException;

import org.apache.commons.lang3.BooleanUtils;

import mod.cvbox.core.Mod_ConvenienceBox;
import mod.cvbox.inventory.ContainerSetter;
import mod.cvbox.network.Message_BoxSwitchChange;
import mod.cvbox.tileentity.TileEntitySetter;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiSetter extends GuiContainer
{
    private static final ResourceLocation GUI_TEXTURES = new ResourceLocation("cvbox:textures/gui/setter.png");
    private final InventoryPlayer playerInventory;
    public IInventory te;

    public GuiSetter(InventoryPlayer playerInv, IInventory setterInv)
    {
        super(new ContainerSetter(playerInv, setterInv));
        this.playerInventory = playerInv;
        this.te = setterInv;
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
        	Mod_ConvenienceBox.Net_Instance.sendToServer(new Message_BoxSwitchChange(!BooleanUtils.toBoolean(te.getField(TileEntitySetter.FIELD_POWER))));
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        String s = this.te.getDisplayName().getUnformattedText();
        this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
        this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Draws the background layer of this container (behind the items).
     */
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(GUI_TEXTURES);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);

        // COUNTER
        this.drawTexturedModalRect(i+142, j+6, 176, 26, 2, getBatteryBaar());

        if (!BooleanUtils.toBoolean(te.getField(TileEntitySetter.FIELD_POWER))){
        	this.drawTexturedModalRect(i+146, j+5, 176, 42, 26, 18);
        }
    }

	protected int getBatteryBaar(){
		return 16-(int)(16.0F * (te.getField(TileEntitySetter.FIELD_BATTERYMAX) - te.getField(TileEntitySetter.FIELD_BATTERY))/te.getField(TileEntitySetter.FIELD_BATTERYMAX));
	}
}