package mod.cvbox.render;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import mod.cvbox.model.ModelPlanter;
import mod.cvbox.tileentity.TileEntityPlanter;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;

public class TileEntityPlanterRender extends TileEntitySpecialRenderer<TileEntityPlanter>{

	private static final ResourceLocation mainResource = new ResourceLocation("cvbox:textures/blocks/tileentity_planting.png");

	private final Random random = new Random();
	private ModelPlanter mdl;

	public TileEntityPlanterRender(){
	}

	@Override
	public void render(TileEntityPlanter te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
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