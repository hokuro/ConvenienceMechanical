package mod.cvbox.model;

import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.tileentity.TileEntity;

public class ModelLiquidMaker extends Model{
    RendererModel Shape1;
	public ModelLiquidMaker() {
	textureWidth = 64;
	textureHeight = 32;

	Shape1 = new RendererModel(this, 0, 0);
	Shape1.addBox(-8F, -8F, -8F, 16, 16, 16);
	Shape1.setRotationPoint(0F, 7F, 0F);
	Shape1.setTextureSize(64, 32);
	Shape1.mirror = true;
	}

	public void render(TileEntity entity, float scale){
		this.Shape1.render(scale);
	}
}
