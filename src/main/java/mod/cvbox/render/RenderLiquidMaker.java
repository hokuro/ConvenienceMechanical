package mod.cvbox.render;

import mod.cvbox.model.ModelLiquidMaker;
import mod.cvbox.tileentity.TileEntityLiquidMaker;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.util.ResourceLocation;

public class RenderLiquidMaker   extends TileEntityRenderer<TileEntityLiquidMaker> {
	private static ResourceLocation tex_none = new ResourceLocation("cvbox:textures/entity/liquidmaker_none.png");
	private static ResourceLocation tex_ice = new ResourceLocation("cvbox:textures/entity/liquidmaker_ice.png");
	private static ResourceLocation tex_lava = new ResourceLocation("cvbox:textures/entity/liquidmaker_lava.png");

	private ModelLiquidMaker mainModel = new ModelLiquidMaker();

	@Override
	public void render(TileEntityLiquidMaker te, double x, double y, double z, float partialTicks, int destroyStage) {
		dorender((TileEntityLiquidMaker)te,x,y,z,partialTicks,destroyStage);
	}

	public void dorender(TileEntityLiquidMaker te, double x, double y, double z, float partialTicks, int destroyStage) {
		double sx,sy,sz = 0.0D;
		double tx,ty,tz = 0.0D;
		sx = sy = sz = 0.03125D;
		tx = ty = tz = 0.5D;


        if (destroyStage >= 0)
        {
            this.bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scalef(5.0F, 4.0F, 1.0F);
            GlStateManager.translatef(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
        }


		GlStateManager.pushMatrix();
		GlStateManager.translatef((float) x + 0.5F , (float) y +  0.94F, (float) z + 0.5F);
		GlStateManager.scaled(0.0625D,0.0625D,0.0625D);
		GlStateManager.rotatef(180,0F,0F,1F);
		GlStateManager.color4f(1.0F, 1.0F,1.0F,1.0F);

		int mode = te.getMode();
		switch(mode){
		case TileEntityLiquidMaker.MODE_NONE:
    		this.bindTexture(tex_none);
			break;
		case TileEntityLiquidMaker.MODE_ICE:
    		this.bindTexture(tex_ice);
			break;
		case TileEntityLiquidMaker.MODE_LAVA:
    		this.bindTexture(tex_lava);
			break;
			default:
				this.bindTexture(tex_none);
				break;

		}
		this.mainModel.render(te,1.0F);
		GlStateManager.popMatrix();



        if (destroyStage >= 0)
        {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
	}
}
