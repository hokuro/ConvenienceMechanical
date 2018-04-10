package mod.cvbox.gui;

import java.io.IOException;

import org.apache.commons.lang3.BooleanUtils;

import mod.cvbox.core.Mod_ConvenienceBox;
import mod.cvbox.inventory.ContainerAutoHarvest;
import mod.cvbox.network.MessageFarmer_UpdateDelivery;
import mod.cvbox.tileentity.TileEntityHarvester;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GuiAutoHarvest extends GuiContainer {
	private static final ResourceLocation tex = new ResourceLocation("cvbox", "textures/gui/harvester.png");
	private int x1;
	private int y1;
	private int z1;
	private TileEntityHarvester planter;
	private BlockPos pos;


	public GuiAutoHarvest(EntityPlayer player, TileEntityHarvester tileEntity, World world, int x, int y, int z){
		super(new ContainerAutoHarvest(player, tileEntity, world, x, y, z));
		xSize = 200;
		ySize = 222;
		planter = tileEntity;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int i, int j){
		fontRenderer.drawString("AutoHarvest",8,4,4210752);
		fontRenderer.drawString("Inventory", 8, ySize - 96, 4210752);
		fontRenderer.drawString("Deliver", xSize/2, ySize - 96, 4210752);
	}

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
   {
       super.mouseClicked(mouseX, mouseY, mouseButton);
       int i = (this.width - this.xSize) / 2;
       int j = (this.height - this.ySize) / 2;

       int l = mouseX - (i + 138);
       int i1 = mouseY - (j + 126);

       if (l >= 0 && i1 >= 0 && l < 32 && 12 < 19)
       {
    	   boolean deliver = planter.canDeliver();
    	   Mod_ConvenienceBox.Net_Instance.sendToServer(new MessageFarmer_UpdateDelivery(planter.getPos(), !deliver));
    	   planter.setField(1, BooleanUtils.toInteger(!deliver));
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
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(tex);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);

        if (planter.canDeliver()){
        	this.drawTexturedModalRect(i + 138, j + 126, 200, 13, 32, 12);
        }else{
        	this.drawTexturedModalRect(i + 138, j + 126, 200, 0, 32, 12);
        }
        this.drawTexturedModalRect(i+176, j+17, 200, 24, 18,18);
	}
}
