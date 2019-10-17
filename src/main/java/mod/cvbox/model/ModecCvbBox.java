package mod.cvbox.model;

import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.Model;

public class ModecCvbBox extends Model{
	RendererModel box;

	public ModecCvbBox() {
		this.textureWidth = 128;
		this.textureHeight = 64;

		this.box = new RendererModel(this, 0, 0);
		this.box.addBox(0.0F, 0.0F, 0.0F, 32, 32, 32);
		this.box.setTextureSize(128, 64);
		this.box.mirror = true;
	}

	public void render(float f) {
		this.box.render(f);
	}
}
