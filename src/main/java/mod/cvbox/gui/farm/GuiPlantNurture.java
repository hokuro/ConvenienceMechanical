package mod.cvbox.gui.farm;

import org.apache.commons.lang3.BooleanUtils;

import com.mojang.blaze3d.platform.GlStateManager;

import mod.cvbox.inventory.farm.ContainerPlantNurture;
import mod.cvbox.network.MessageHandler;
import mod.cvbox.tileentity.ab.TileEntityPlantNurtureBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;

public class GuiPlantNurture extends ContainerScreen<ContainerPlantNurture> {
	private final ResourceLocation tex = new ResourceLocation("cvbox", "textures/gui/plantnurture.png");
	private int x1;
	private int y1;
	private int z1;
	private TileEntityPlantNurtureBase te;
	private BlockPos pos;

	public GuiPlantNurture(ContainerPlantNurture container, PlayerInventory playerInv, ITextComponent titleIn){
		super(container, playerInv, titleIn);
		xSize = 200;
		ySize = 222;
		te = container.getTileEntity();
	}

	@Override
	public void init(){
		super.init();
    	int x=(this.width - this.xSize) / 2;
    	int y=(this.height - this.ySize) / 2;
    	this.buttons.clear();
    	this.addButton(new Button(x+7,y+14,40,20,"reset", (bt)->{MessageHandler.SendMessage_RestWork();}));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int i, int j){
		font.drawString(title.getFormattedText(),8,4,4210752);
		font.drawString("Inventory", 8, ySize - 96, 4210752);
		font.drawString("Deliver", xSize/2, ySize - 96, 4210752);

		int x = te.getField(TileEntityPlantNurtureBase.FIELD_NEXT_X);
		int z = te.getField(TileEntityPlantNurtureBase.FIELD_NEXT_Z);
		String nextPos = String.valueOf(x) + "," + String.valueOf(z);
		font.drawString(nextPos, 53, 20, 16777215);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
       super.mouseClicked(mouseX, mouseY, mouseButton);
       int i = (this.width - this.xSize) / 2;
       int j = (this.height - this.ySize) / 2;

       double l = mouseX - (i + 138);
       double i1 = mouseY - (j + 126);

       if (l >= 0 && i1 >= 0 && l < 32 && i1 < 19) {
    	   boolean deliver = te.canDeliver();
    	   MessageHandler.SendMessage_Farmer_UpdateDelivery(te.getPos(), !deliver);
    	   te.setField(1, BooleanUtils.toInteger(!deliver));
       }

       l = mouseX - (i + 146);
       i1 = mouseY - (j + 5);

       if (l >= 0 && i1 >= 0 && l < 26 && i1 < 18) {
    	   MessageHandler.SendMessage_BoxSwitchChante(!BooleanUtils.toBoolean(te.getField(TileEntityPlantNurtureBase.FIELD_POWER)));
       }
       return true;
   }

	@Override
	public void render(int mouseX, int mouseY, float partialTicks){
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int x, int y){
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getInstance().getTextureManager().bindTexture(tex);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.blit(i, j, 0, 0, this.xSize, this.ySize);

        if (te.canDeliver()){
        	this.blit(i + 138, j + 126, 200, 12, 33, 12);
        }else{
        	this.blit(i + 138, j + 126, 200, 0, 33, 12);
        }
        if (te.getKind() == ContainerPlantNurture.NURTUREKIND.SAPLING) {
        	this.blit(i+175, j+35, 200, 42, 18,18);
        }else {
        	this.blit(i+175, j+35, 200, 24, 18,18);
        }

        // COUNTER
        this.blit(i+142, j+6, 200, 60, 2, getBatteryBaar());

        if (!BooleanUtils.toBoolean(te.getField(TileEntityPlantNurtureBase.FIELD_POWER))){
        	this.blit(i+146, j+5, 200, 76, 26, 18);
        }
	}

	protected int getBatteryBaar(){
		return 16-(int)(16.0F * (te.getField(TileEntityPlantNurtureBase.FIELD_BATTERYMAX) - te.getField(TileEntityPlantNurtureBase.FIELD_BATTERY))/te.getField(TileEntityPlantNurtureBase.FIELD_BATTERYMAX));
	}
}
