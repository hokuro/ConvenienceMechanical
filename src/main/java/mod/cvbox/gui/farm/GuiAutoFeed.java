package mod.cvbox.gui.farm;

import mod.cvbox.gui.ab.GuiPowerBase;
import mod.cvbox.inventory.ab.ContainerPowerBase;
import mod.cvbox.network.MessageHandler;
import mod.cvbox.tileentity.ab.TileEntityPowerBase;
import mod.cvbox.tileentity.farm.TileEntityAutoFeed;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class GuiAutoFeed extends GuiPowerBase {

	private static final ResourceLocation tex = new ResourceLocation("cvbox:textures/gui/autofeed.png");

	public GuiAutoFeed(ContainerPowerBase container, PlayerInventory playerInv, ITextComponent titleIn){
		super(container, playerInv, titleIn);
	}

	protected ResourceLocation getTexture() {
		return tex;
	}

	@Override
	public void init(){
		super.init();
    	int x=(this.width - this.xSize) / 2;
    	int y=(this.height - this.ySize) / 2;
    	this.buttons.clear();
    	this.addButton(new Button(x+102, y+40,20,20,"-", (bt)->{actionPerformed(101, bt);}));
    	this.addButton(new Button(x+149, y+40,20,20,"+", (bt)->{actionPerformed(102, bt);}));
    	this.addButton(new Button(x+102, y+61,20,20,"-", (bt)->{actionPerformed(103, bt);}));
    	this.addButton(new Button(x+149, y+61,20,20,"+", (bt)->{actionPerformed(104, bt);}));
	}

    protected void actionPerformed(int id, Button button) {
    	int w = 0;
    	int d = 0;
    	switch(id){
	    	case 101:
	    		w = -1;
	    		break;
	    	case 102:
	    		w = +1;
	    		break;
	    	case 103:
	    		d = -1;
	    		break;
	    	case 104:
	    		d = +1;
	    		break;
    	}

    	if (w != 0 || d != 0){
        	MessageHandler.SendMessage_AutoFeed_AreaSizeUpdate(w,d);
    	}

    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.font.drawString("Auto Feed", 12, 5, 4210752);
        this.font.drawString("Inventory", 8, this.ySize - 96 + 2, 4210752);

        // エリアサイズ
        font.drawString("X", 90, 45, 0xFFFFFFFF);
        String s = "" + te.getField(TileEntityAutoFeed.FIELD_RANGEX);
        font.drawString(s, 125, 45, 0xFFFFFFFF);

        font.drawString("Z", 90, 66, 0xFFFFFFFF);
        s = "" + te.getField(TileEntityAutoFeed.FIELD_RANGEZ);
        font.drawString(s, 125, 66, 0xFFFFFFFF);

        font.drawString("NEXT", 90, 30, 0xFFFFFFFF);
        s = "" + (int)((((TileEntityPowerBase)te).getTickTime() - te.getField(TileEntityPowerBase.FIELD_TIMECNT))/20);
        font.drawString(s, 125, 30, 0xFF7F1F8F);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
    	super.drawGuiContainerBackgroundLayer(f, x, y);
    }
}
