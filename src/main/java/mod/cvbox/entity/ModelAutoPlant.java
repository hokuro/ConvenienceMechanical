package mod.cvbox.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelAutoPlant extends ModelBase {
	ModelRenderer bottomBoard;
	ModelRenderer topBoard;

	public ModelAutoPlant() {
		this.textureWidth = 128;
		this.textureHeight = 64;

		this.bottomBoard = new ModelRenderer(this, 0, 0);
		this.bottomBoard.addBox(0.0F, 0.0F, 0.0F, 32, 32, 32, 0.0F);
		this.bottomBoard.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.bottomBoard.setTextureSize(128, 64);
		this.bottomBoard.mirror = true;
	}

	public void render() {
		this.bottomBoard.rotateAngleX = bottomBoard.rotateAngleX;
		this.bottomBoard.render(0.03125F);
	}
}
