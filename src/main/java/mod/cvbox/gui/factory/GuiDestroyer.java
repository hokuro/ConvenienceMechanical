package mod.cvbox.gui.factory;

import org.apache.commons.lang3.BooleanUtils;

import mod.cvbox.gui.ab.GuiPowerBase;
import mod.cvbox.inventory.ab.ContainerPowerBase;
import mod.cvbox.network.MessageHandler;
import mod.cvbox.tileentity.factory.TileEntityDestroyer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class GuiDestroyer extends GuiPowerBase {
	private static final ResourceLocation tex = new ResourceLocation("cvbox", "textures/gui/destroyer.png");
	private Button reset;
	private Button inc1msec;
	private Button dec1msec;
	private Button inc1sec;
	private Button dec1sec;

	public GuiDestroyer(ContainerPowerBase container, PlayerInventory playerInv, ITextComponent titleIn){
		super(container, playerInv, titleIn);
	}

	protected ResourceLocation getTexture() {
		return tex;
	}

	public void init(){
		super.init();
    	int x=(this.width - this.xSize) / 2;
    	int y=(this.height - this.ySize) / 2;
    	this.buttons.clear();

    	this.addButton(new Button(x+7,y+25,40,20,"mode", (bt)->{actionPerformed(101, bt);}));
    	inc1msec = new Button(x+148,y+25,20,20,"+", (bt)->{actionPerformed(102, bt);});
    	dec1msec = new Button(x+91,y+25,20,20,"-", (bt)->{actionPerformed(103, bt);});
    	inc1sec = new Button(x+148,y+46,20,20,"+", (bt)->{actionPerformed(104, bt);});
    	dec1sec = new Button(x+91,y+46,20,20,"-", (bt)->{actionPerformed(105, bt);});
    	reset = new Button(x+49,y+25,40,20,"reset", (bt)->{actionPerformed(106, bt);});

    	this.addButton(inc1msec);
    	this.addButton(dec1msec);
    	this.addButton(inc1sec);
    	this.addButton(dec1sec);
    	this.addButton(reset);
	}

    protected void actionPerformed(int id, Button button){
    	boolean send = false;
    	int size;
    	int mode = te.getField(TileEntityDestroyer.FIELD_MODE);
    	float time = te.getField(TileEntityDestroyer.FIELD_TIME)/20.0F;
    	switch(id){
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
		MessageHandler.SendMessage_UpdateDestroy(mode,time);
    }

	@Override
	protected void drawGuiContainerForegroundLayer(int i, int j){
		font.drawString("Destroyer",8,4,4210752);
        font.drawString("Inventory", 8, this.ySize - 96 + 2, 4210752);
        font.drawString(((te.getField(TileEntityDestroyer.FIELD_MODE)==TileEntityDestroyer.MODE_TIME)?"With Time":"With Change"), 9, 50, 4210752);
        font.drawString("100ms", 113, 30, 4210752);
        font.drawString("1sec", 113, 51, 4210752);
        font.drawString(String.valueOf(te.getField(TileEntityDestroyer.FIELD_TIME)/20.0F)+" Sec", 101, this.ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int x, int y){
		super.drawGuiContainerBackgroundLayer(f, x, y);

        if (reset.active && BooleanUtils.toBoolean(te.getField(TileEntityDestroyer.FIELD_ISRUN))){
        	reset.active=false;
        	inc1msec.active=false;
        	dec1msec.active=false;
        	inc1sec.active=false;
        	dec1sec.active=false;
        }else if (!reset.active && !BooleanUtils.toBoolean(te.getField(TileEntityDestroyer.FIELD_ISRUN))){
        	reset.active=true;
        	inc1msec.active=true;
        	dec1msec.active=true;
        	inc1sec.active=true;
        	dec1sec.active=true;
        }
	}
}
