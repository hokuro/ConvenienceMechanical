package mod.cvbox.entity;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class TileEntityAutoPlantingRender extends TileEntitySpecialRenderer{

	private static final ResourceLocation mainResource = new ResourceLocation("autoplanting:textures/blocks/tileentity_planting.png");

	private final Random random = new Random();
	private ItemRenderer itemr;
	private int cou = 1;
	private ModelAutoPlant mdl;

	public TileEntityAutoPlantingRender(){
	}

	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTicks, int destroyStage) {
		TileEntityAutoPlanting tile = (TileEntityAutoPlanting) te;
		mdl = new ModelAutoPlant();
		GL11.glPushMatrix();
		GL11.glTranslatef((float)x, (float)y, (float) z);
		GlStateManager.enableCull();
		GlStateManager.enableRescaleNormal();
		bindTexture(mainResource);
		mdl.render();
		GL11.glPopMatrix();
	}
}