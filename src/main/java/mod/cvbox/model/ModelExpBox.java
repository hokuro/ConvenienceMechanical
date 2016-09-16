package mod.cvbox.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelExpBox extends ModelBase{
	ModelRenderer bottomBoard;

	public ModelExpBox() {
		this.textureWidth = 128;
		this.textureHeight = 64;
	}

	public void render(float f) {
		this.bottomBoard.rotateAngleX = bottomBoard.rotateAngleX;
		this.bottomBoard.render(f);
	}
}
