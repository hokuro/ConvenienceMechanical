package mod.cvbox.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.tileentity.TileEntity;

public class ModelLiquidMaker extends ModelBase{
    ModelRenderer Shape1;
	public ModelLiquidMaker() {
	    textureWidth = 64;
	    textureHeight = 32;

	      Shape1 = new ModelRenderer(this, 0, 0);
	      Shape1.addBox(-8F, -8F, -8F, 16, 16, 16);
	      Shape1.setRotationPoint(0F, -7F, 0F);
	      Shape1.setTextureSize(64, 32);
	      Shape1.mirror = true;
	}

	public void render(TileEntity entity, float scale){
		this.Shape1.render(scale);
	}
}
