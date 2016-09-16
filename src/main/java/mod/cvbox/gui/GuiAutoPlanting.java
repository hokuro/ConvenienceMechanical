package mod.cvbox.gui;

import org.lwjgl.opengl.GL11;

import mod.cvbox.core.ModCommon;
import mod.cvbox.inventory.ContainerAutoPlanting;
import mod.cvbox.tileentity.TileEntityPlanter;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiAutoPlanting extends GuiContainer {
	private static final ResourceLocation gui = new ResourceLocation("autoplanting", "textures/gui/plant.png");
	private int x1;
	private int y1;
	private int z1;
	private TileEntityPlanter tile;
	private BlockPos pos;


	public GuiAutoPlanting(EntityPlayer player, TileEntityPlanter tileEntity, World world, int x, int y, int z){
		super(new ContainerAutoPlanting(player, tileEntity, world, x, y, z));
		xSize = 222;
		ySize = xSize - 108;

		x1 = x;
		y1 = y;
		z1 = z;
		pos = new BlockPos(x,y,z);
		tile = tileEntity;

		ySize = ySize + ModCommon.PLANTER_MAX_ROW_SLOT * 18;
	}

	public void initGui(){
		super.initGui();
		int i = this.width - this.xSize >> 1;
		int j = this.height - this.ySize >> 1;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int i, int j){
		fontRendererObj.drawString("AutoPlanting",8,4,4210752);
		fontRendererObj.drawString("Inventory", 8, ySize - 98, 42510752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j){
		GL11.glColor4f(1.0F,1.0F,1.0F,1.0F);
		mc.getTextureManager().bindTexture(gui);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
        this.drawTexturedModalRect(x, y-4, 0, 0, this.xSize, ModCommon.PLANTER_MAX_ROW_SLOT * 18 + 17);
        this.drawTexturedModalRect(x, y + ModCommon.PLANTER_MAX_ROW_SLOT * 18 + 13, 0, 126, this.xSize, 96);
	}
}