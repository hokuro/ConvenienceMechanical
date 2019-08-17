package mod.cvbox.gui;

import org.apache.commons.lang3.BooleanUtils;

import mod.cvbox.inventory.ContainerAutoHarvest;
import mod.cvbox.network.MessageHandler;
import mod.cvbox.tileentity.TileEntityHarvester;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GuiAutoHarvest extends GuiContainer {
	private static final ResourceLocation tex = new ResourceLocation("cvbox", "textures/gui/harvester.png");
	private int x1;
	private int y1;
	private int z1;
	private TileEntityHarvester te;
	private BlockPos pos;


	public GuiAutoHarvest(EntityPlayer player, TileEntityHarvester tileEntity, World world, int x, int y, int z){
		super(new ContainerAutoHarvest(player, tileEntity, world, x, y, z));
		xSize = 200;
		ySize = 222;
		te = tileEntity;
	}

	@Override
	public void initGui(){
		super.initGui();
    	int x=(this.width - this.xSize) / 2;
    	int y=(this.height - this.ySize) / 2;
    	this.buttons.clear();
    	GuiButton b1 = new GuiButton(101,x+7,y+14,40,20,"reset"){
    		@Override
    		public void onClick(double mouseX, double moudeY){
    			MessageHandler.SendMessage_RestWork();
        		//Mod_ConvenienceBox.Net_Instance.sendToServer(new Message_ResetWork());
    		}
    	};
    	this.buttons.add(b1);
		this.children.addAll(this.buttons);

	}

//    protected void actionPerformed(GuiButton button) throws IOException
//    {
//    	boolean send = false;
//    	int size;
//    	switch(button.id){
//    	case 101:
//	    break;
//	    }
//    }


	@Override
	protected void drawGuiContainerForegroundLayer(int i, int j){
		fontRenderer.drawString("AutoHarvest",8,4,4210752);
		fontRenderer.drawString("Inventory", 8, ySize - 96, 4210752);
		fontRenderer.drawString("Deliver", xSize/2, ySize - 96, 4210752);

		int x = te.getField(TileEntityHarvester.FIELD_NEXT_X);
		int z = te.getField(TileEntityHarvester.FIELD_NEXT_Z);
		String nextPos = String.valueOf(x) + "," + String.valueOf(z);
		fontRenderer.drawString(nextPos, 53, 20, 16777215);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
   {
       super.mouseClicked(mouseX, mouseY, mouseButton);
       int i = (this.width - this.xSize) / 2;
       int j = (this.height - this.ySize) / 2;

       double l = mouseX - (i + 138);
       double i1 = mouseY - (j + 126);

       if (l >= 0 && i1 >= 0 && l < 32 && i1 < 19)
       {
    	   boolean deliver = te.canDeliver();
    	   MessageHandler.SendMessage_Farmer_UpdateDelivery(te.getPos(), !deliver);
    	   //Mod_ConvenienceBox.Net_Instance.sendToServer(new MessageFarmer_UpdateDelivery(te.getPos(), !deliver));
    	   te.setField(1, BooleanUtils.toInteger(!deliver));
       }

       l = mouseX - (i + 146);
       i1 = mouseY - (j + 5);

       if (l >= 0 && i1 >= 0 && l < 26 && i1 < 18)
       {
    	   MessageHandler.SendMessage_BoxSwitchChante(!BooleanUtils.toBoolean(te.getField(TileEntityHarvester.FIELD_POWER)));
       		//Mod_ConvenienceBox.Net_Instance.sendToServer(new Message_BoxSwitchChange(!BooleanUtils.toBoolean(te.getField(TileEntityHarvester.FIELD_POWER))));
       }
       return true;
   }

	@Override
	public void render(int mouseX, int mouseY, float partialTicks){
        this.drawDefaultBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int x, int y){
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(tex);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);

        if (te.canDeliver()){
        	this.drawTexturedModalRect(i + 138, j + 126, 200, 12, 33, 12);
        }else{
        	this.drawTexturedModalRect(i + 138, j + 126, 200, 0, 33, 12);
        }
        this.drawTexturedModalRect(i+176, j+17, 200, 24, 18,18);

        // COUNTER
        this.drawTexturedModalRect(i+142, j+6, 200, 61, 2, getBatteryBaar());

        if (!BooleanUtils.toBoolean(te.getField(TileEntityHarvester.FIELD_POWER))){
        	this.drawTexturedModalRect(i+146, j+5, 200, 76, 26, 18);
        }
	}

	protected int getBatteryBaar(){
		return 16-(int)(16.0F * (te.getField(TileEntityHarvester.FIELD_BATTERYMAX) - te.getField(TileEntityHarvester.FIELD_BATTERY))/te.getField(TileEntityHarvester.FIELD_BATTERYMAX));
	}
}
