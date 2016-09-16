package mod.cvbox.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelPlanter extends ModelBase {
	ModelRenderer bottomBoard;

	public ModelPlanter() {
		this.textureWidth = 128;
		this.textureHeight = 64;
	}

	public void render(float f) {
		this.bottomBoard.render(f);
	}
}
