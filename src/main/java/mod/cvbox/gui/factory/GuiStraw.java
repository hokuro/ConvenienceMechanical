package mod.cvbox.gui.factory;

import com.mojang.blaze3d.platform.GlStateManager;

import mod.cvbox.gui.ab.GuiPowerBase;
import mod.cvbox.inventory.ab.ContainerPowerBase;
import mod.cvbox.network.MessageHandler;
import mod.cvbox.tileentity.factory.TileEntityStraw;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;

public class GuiStraw extends GuiPowerBase {

	private static final ResourceLocation tex = new ResourceLocation("cvbox:textures/gui/straw.png");
	public GuiStraw(ContainerPowerBase container, PlayerInventory playerInv, ITextComponent titleIn){
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
    	this.addButton(new Button(x+102, y+30,20,20,"-", (bt)->{actionPerformed(101, bt);}));
    	this.addButton(new Button(x+149, y+30,20,20,"+", (bt)->{actionPerformed(102, bt);}));
    	this.addButton(new Button(x+102, y+51,20,20,"-", (bt)->{actionPerformed(103, bt);}));
    	this.addButton(new Button(x+149, y+51,20,20,"+", (bt)->{actionPerformed(104, bt);}));
    	this.addButton(new Button(x+69, y+19,20,20,"C", (bt)->{actionPerformed(105, bt);}));
    	this.addButton(new Button(x+42, y+51,40,20,"get all", (bt)->{actionPerformed(106, bt);}));
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
	    	case 105:
	    		MessageHandler.SendMessage_Straw_CreaArea();
	    		break;

	    	case 106:
	    		MessageHandler.SendMessage_Straw_GetAll();
	    		break;
    	}

    	if (w != 0 || d != 0){
        	MessageHandler.SendMessage_Straw_AreaSizeUpdate(w,d);
    	}

    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.font.drawString("Liquid Container", 12, 5, 4210752);
        this.font.drawString("Inventory", 8, this.ySize - 96 + 2, 4210752);

        // エリアサイズ
        font.drawString("W", 90, 35, 0xFFFFFFFF);
        String s = "" +te.getField(TileEntityStraw.FIELD_WIDTH);
        font.drawString(s, 125, 35, 0xFFFFFFFF);

        font.drawString("D", 90, 56, 0xFFFFFFFF);
        s = "" +te.getField(TileEntityStraw.FIELD_DEPTH);
        font.drawString(s, 125, 56, 0xFFFFFFFF);

        s = "" +te.getField(TileEntityStraw.FIELD_SIZE) +
        		"("+te.getField(TileEntityStraw.FIELD_X)+","
        		+te.getField(TileEntityStraw.FIELD_Y)+","
        		+te.getField(TileEntityStraw.FIELD_Z)+")";
        font.drawString(s, 25, 24, 0xFFFFFFFF);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
    	super.drawGuiContainerBackgroundLayer(f, x, y);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;

        int tankLen =tankLength();
        if (te.getField(TileEntityStraw.FIELD_KIND) == 1){
        	this.blit(i+8, j+70-tankLen, 192, 34, 12, tankLen);
        }else{
        	this.blit(i+8, j+70-tankLen, 176, 34, 12, tankLen);
        }
    }

    private int tankLength(){
    	return MathHelper.floor(50.0F *((float)te.getField(TileEntityStraw.FIELD_TANK)/(float)TileEntityStraw.MAX_TANK));
    }
}
