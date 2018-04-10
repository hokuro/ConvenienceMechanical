package mod.cvbox.gui;

import java.io.IOException;

import org.apache.commons.lang3.BooleanUtils;

import mod.cvbox.core.Mod_ConvenienceBox;
import mod.cvbox.inventory.ContainerAutoPlanting;
import mod.cvbox.network.MessageFarmer_UpdateDelivery;
import mod.cvbox.tileentity.TileEntityPlanter;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiAutoPlanting extends GuiContainer {
	private static final ResourceLocation tex = new ResourceLocation("cvbox", "textures/gui/planter.png");
	private int x1;
	private int y1;
	private int z1;
	private BlockPos pos;
	TileEntityPlanter planter;


	public GuiAutoPlanting(EntityPlayer player, TileEntityPlanter tileEntity, World world, int x, int y, int z){
		super(new ContainerAutoPlanting(player, tileEntity, world, x, y, z));
		ySize = 222;
		planter = (TileEntityPlanter)tileEntity;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int i, int j){
		fontRenderer.drawString("AutoPlanting",8,4,4210752);
		fontRenderer.drawString("Inventory", 8, ySize - 96, 4210752);
		fontRenderer.drawString("Deliver", xSize/2+10, ySize - 96, 4210752);
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
        	this.drawTexturedModalRect(i + 138, j + 126, 177, 13, 32, 12);
        }else{
        	this.drawTexturedModalRect(i + 138, j + 126, 177, 0, 32, 12);
        }
	}
}