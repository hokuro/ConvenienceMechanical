package mod.cvbox.render;

import mod.cvbox.block.BlockCore;
import mod.cvbox.model.ModelExpBox;
import mod.cvbox.tileentity.TileEntityExpBank;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class TileEntityEcpBankRender extends TileEntitySpecialRenderer {

	private static final ResourceLocation mainResourcey = new ResourceLocation("cvbox:textures/tileentity/tileentity_expbank.png");
	private ModelExpBox mdl;

	public TileEntityEcpBankRender() {
	}

	public void renderExPContainer(TileEntityExpBank entity, double posX, double posY, double posZ, float per8, int per9) {
		BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
		IBlockState modelState = BlockCore.block_expbank.getStateFromMeta(entity.getBlockMetadata());
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) posX , (float) posY, (float) posZ);
		GlStateManager.scale(0.0625F, 0.0625F, 0.0625F);

		GlStateManager.enableCull();
		GlStateManager.enableRescaleNormal();

		bindTexture(mainResourcey);

		this.mdl.render(0.5F);

		GlStateManager.popMatrix();
	}

	@Override
	public void renderTileEntityAt(TileEntity te, double posX, double posZ, double posY, float rot_rot, int p_180535_9_) {
		renderExPContainer((TileEntityExpBank) te, posX, posZ, posY, rot_rot, p_180535_9_);
	}
}
