package mod.cvbox.tileentity;

import mod.cvbox.model.ModelExpBank;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class TileEntityEcpBankRender extends TileEntitySpecialRenderer {

	private static final ResourceLocation mainResource_gray = new ResourceLocation(
			"expbox:textures/tileentity/tileentity_expbank_gray.png");
	private static final ResourceLocation mainResource_lightgray = new ResourceLocation(
			"expbox:textures/tileentity/tileentity_expbank_lightgray.png");
	private static final ResourceLocation mainResource_green = new ResourceLocation(
			"expbox:textures/tileentity/tileentity_expbank_green.png");
	private static final ResourceLocation mainResource_black = new ResourceLocation(
			"expbox:textures/tileentity/tileentity_expbank_black.png");
	private static final ResourceLocation mainResource_blue = new ResourceLocation(
			"expbox:textures/tileentity/tileentity_expbank_blue.png");
	private static final ResourceLocation mainResource_cyan = new ResourceLocation(
			"expbox:textures/tileentity/tileentity_expbank_cyan.png");
	private static final ResourceLocation mainResource_purple = new ResourceLocation(
			"expbox:textures/tileentity/tileentity_expbank_purple.png");
	private static final ResourceLocation mainResource_orange = new ResourceLocation(
			"expbox:textures/tileentity/tileentity_expbank_orange.png");
	private static final ResourceLocation mainResource_red = new ResourceLocation(
			"expbox:textures/tileentity/tileentity_expbank_red.png");
	private static final ResourceLocation mainResource_brown = new ResourceLocation(
			"expbox:textures/tileentity/tileentity_expbank_brown.png");
	private static final ResourceLocation mainResource_pink = new ResourceLocation(
			"expbox:textures/tileentity/tileentity_expbank_pink.png");
	private static final ResourceLocation mainResource_yellow = new ResourceLocation(
			"expbox:textures/tileentity/tileentity_expbank_yellow.png");
	private static final ResourceLocation mainResource_lime = new ResourceLocation(
			"expbox:textures/tileentity/tileentity_expbank_lime.png");
	private static final ResourceLocation mainResource_lightblue = new ResourceLocation(
			"expbox:textures/tileentity/tileentity_expbank_lightblue.png");
	private static final ResourceLocation mainResource_magenta = new ResourceLocation(
			"expbox:textures/tileentity/tileentity_expbank_magenta.png");
	private ModelExpBank mainmodel = new ModelExpBank();

	public TileEntityEcpBankRender() {
	}

	public void renderExPContainer(TileEntityExpBank te, double posX, double posY, double posZ, float per8, int per9) {
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) posX , (float) posY, (float) posZ);
		GlStateManager.scale(0.0625F, 0.0625F, 0.0625F);

		GlStateManager.enableCull();
		GlStateManager.enableRescaleNormal();

		int meta = te.getBlockMetadata();
		switch (meta) {
		case 15:
			bindTexture(mainResource_black);
			break;
		case 14:
			bindTexture(mainResource_red);
			break;
		case 13:
			bindTexture(mainResource_green);
			break;
		case 12:
			bindTexture(mainResource_brown);
			break;
		case 11:
			bindTexture(mainResource_blue);
			break;
		case 10:
			bindTexture(mainResource_purple);
			break;
		case 9:
			bindTexture(mainResource_cyan);
			break;
		case 8:
			bindTexture(mainResource_lightgray);
			break;
		case 7:
			bindTexture(mainResource_gray);
			break;
		case 6:
			bindTexture(mainResource_pink);
			break;
		case 5:
			bindTexture(mainResource_lime);
			break;
		case 4:
			bindTexture(mainResource_yellow);
			break;
		case 3:
			bindTexture(mainResource_lightblue);
			break;
		case 2:
			bindTexture(mainResource_magenta);
			break;
		case 1:
			bindTexture(mainResource_orange);
			break;
		default:
			bindTexture(mainResource_gray);
		}
		this.mainmodel.render(0.5F);

		GlStateManager.popMatrix();
	}

	@Override
	public void renderTileEntityAt(TileEntity te, double posX, double posZ, double posY, float rot_rot, int p_180535_9_) {
		renderExPContainer((TileEntityExpBank) te, posX, posZ, posY, rot_rot, p_180535_9_);
	}
}
