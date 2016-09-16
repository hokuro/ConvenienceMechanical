package mod.cvbox.render;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import mod.cvbox.model.ModelPlanter;
import mod.cvbox.tileentity.TileEntityPlanter;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class TileEntityPlanterRender extends TileEntitySpecialRenderer{

	private static final ResourceLocation mainResource = new ResourceLocation("cvbox:textures/blocks/tileentity_planting.png");

	private final Random random = new Random();
	private ModelPlanter mdl;

	public TileEntityPlanterRender(){
	}

	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTicks, int destroyStage) {
		TileEntityPlanter tile = (TileEntityPlanter) te;
		mdl = new ModelPlanter();
		GL11.glPushMatrix();
		GL11.glTranslatef((float)x, (float)y, (float) z);
		GlStateManager.enableCull();
		GlStateManager.enableRescaleNormal();
		bindTexture(mainResource);
		mdl.render(0.5f);
		GL11.glPopMatrix();
	}
}