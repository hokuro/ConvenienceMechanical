package mod.cvbox.gui;

import java.io.IOException;

import org.apache.commons.lang3.BooleanUtils;

import mod.cvbox.core.Mod_ConvenienceBox;
import mod.cvbox.inventory.ContainerDestroyer;
import mod.cvbox.network.Message_BoxSwitchChange;
import mod.cvbox.network.Message_UpdateDestroy;
import mod.cvbox.tileentity.TileEntityDestroyer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiDestroyer extends GuiContainer {
	private static final ResourceLocation tex = new ResourceLocation("cvbox", "textures/gui/destroyer.png");
	private TileEntityDestroyer te;
	private GuiButton reset;
	private GuiButton inc1msec;
	private GuiButton dec1msec;
	private GuiButton inc1sec;
	private GuiButton dec1sec;


	public GuiDestroyer(EntityPlayer player, IInventory tileEntity){
		super(new ContainerDestroyer(player.inventory, tileEntity));
		te = (TileEntityDestroyer)tileEntity;
	}

	public void initGui(){
		super.initGui();
    	int x=(this.width - this.xSize) / 2;
    	int y=(this.height - this.ySize) / 2;
    	this.buttonList.clear();
    	this.buttonList.add(new GuiButton(101,x+7,y+25,40,20,"mode"));

    	inc1msec =new GuiButton(102,x+148,y+25,20,20,"+");
    	this.buttonList.add(inc1msec);

    	dec1msec =new GuiButton(103,x+91,y+25,20,20,"-");
    	this.buttonList.add(dec1msec);

    	inc1sec =new GuiButton(104,x+148,y+46,20,20,"+");
    	this.buttonList.add(inc1sec);

    	dec1sec =new GuiButton(105,x+91,y+46,20,20,"-");
    	this.buttonList.add(dec1sec);

    	reset = new GuiButton(106,x+49,y+25,40,20,"reset");
    	this.buttonList.add(reset);
	}

    protected void actionPerformed(GuiButton button) throws IOException
    {
    	boolean send = false;
    	int size;
    	int mode = te.getField(TileEntityDestroyer.FIELD_MODE);
    	float time = te.getField(TileEntityDestroyer.FIELD_TIME)/10.0F;
    	switch(button.id){
    	case 101:
    		if (mode == TileEntityDestroyer.MODE_TIME){
    			mode = TileEntityDestroyer.MODE_CHANGE;
    		}else{
    			mode = TileEntityDestroyer.MODE_TIME;
    		}
	    break;
    	case 102:
    		time += 0.1;
	    break;
    	case 103:
    		time -=0.1;
	    break;
    	case 104:
    		time += 1;
	    break;
    	case 105:
    		time -= 1;
	    break;
    	case 106:
    		time = 0.1F;
    		break;
	    }
    	if (time < 0.1F){time = 0.1F;}
		Mod_ConvenienceBox.Net_Instance.sendToServer(new Message_UpdateDestroy(mode,time));
    }

	@Override
	protected void drawGuiContainerForegroundLayer(int i, int j){
		fontRenderer.drawString("Destroyer",8,4,4210752);
        fontRenderer.drawString("Inventory", 8, this.ySize - 96 + 2, 4210752);
        fontRenderer.drawString(((te.getField(TileEntityDestroyer.FIELD_MODE)==TileEntityDestroyer.MODE_TIME)?"With Time":"With Change")
        		, 9, 50, 4210752);
        fontRenderer.drawString("100ms", 113, 30, 4210752);
        fontRenderer.drawString("1sec", 113, 51, 4210752);
        fontRenderer.drawString(String.valueOf(te.getField(TileEntityDestroyer.FIELD_TIME)/10.0F)+" Sec", 101, this.ySize - 96 + 2, 4210752);
	}

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;

        int l = mouseX - (i + 146);
        int i1 = mouseY - (j + 5);

        if (l >= 0 && i1 >= 0 && l < 26 && i1 < 18)
        {
        	Mod_ConvenienceBox.Net_Instance.sendToServer(new Message_BoxSwitchChange(!BooleanUtils.toBoolean(te.getField(TileEntityDestroyer.FIELD_POWER))));
        }
    }

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks){
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int x, int y){
		// 背景
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(tex);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);

        // COUNTER
        this.drawTexturedModalRect(i+142, j+6, 176, 26, 2, getBatteryBaar());

        if (!BooleanUtils.toBoolean(te.getField(TileEntityDestroyer.FIELD_POWER))){
        	this.drawTexturedModalRect(i+146, j+5, 176, 42, 26, 18);
        }

        if (reset.enabled && BooleanUtils.toBoolean(te.getField(TileEntityDestroyer.FIELD_ISRUN))){
        	reset.enabled=false;
        	inc1msec.enabled=false;
        	dec1msec.enabled=false;
        	inc1sec.enabled=false;
        	dec1sec.enabled=false;
        }else if (!reset.enabled && !BooleanUtils.toBoolean(te.getField(TileEntityDestroyer.FIELD_ISRUN))){
        	reset.enabled=true;
        	inc1msec.enabled=true;
        	dec1msec.enabled=true;
        	inc1sec.enabled=true;
        	dec1sec.enabled=true;
        }


	}

	protected int getBatteryBaar(){
		return 16-(int)(16.0F * (te.getField(TileEntityDestroyer.FIELD_BATTERYMAX) - te.getField(TileEntityDestroyer.FIELD_BATTERY))/te.getField(TileEntityDestroyer.FIELD_BATTERYMAX));
	}
}
