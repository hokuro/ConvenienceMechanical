package mod.cvbox.gui.farm;

import com.mojang.blaze3d.platform.GlStateManager;

import mod.cvbox.gui.ab.GuiPowerBase;
import mod.cvbox.inventory.ab.ContainerPowerBase;
import mod.cvbox.network.MessageHandler;
import mod.cvbox.tileentity.ab.TileEntityPowerBase;
import mod.cvbox.tileentity.farm.TileEntityMillking;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;

public class GuiMillking extends GuiPowerBase {

	private static final ResourceLocation tex = new ResourceLocation("cvbox:textures/gui/millking.png");

	public GuiMillking(ContainerPowerBase container, PlayerInventory playerInv, ITextComponent titleIn){
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
    	this.addButton(new Button(x+42, y+51,40,20,"get all", (bt)->{actionPerformed(106, bt);}));
	}

    protected void actionPerformed(int id, Button button) {
    	int x = 0;
    	int z = 0;
    	switch(id){
	    	case 101:
	    		x = -1;
	    		break;
	    	case 102:
	    		x = +1;
	    		break;
	    	case 103:
	    		z = -1;
	    		break;
	    	case 104:
	    		z = +1;
	    		break;
	    	case 106:
	    		MessageHandler.SendMessage_Millking_GetAll();
	    		break;
    	}

    	if (x != 0 || z != 0){
        	MessageHandler.SendMessage_Millking_AreaSizeUpdate(x,z);
    	}

    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.font.drawString("Millking", 12, 5, 4210752);
        this.font.drawString("Inventory", 8, this.ySize - 96 + 2, 4210752);

        // エリアサイズ
        font.drawString("X", 90, 35, 0xFFFFFFFF);
        String s = "" +te.getField(TileEntityMillking.FIELD_RANGEX);
        font.drawString(s, 125, 35, 0xFFFFFFFF);

        font.drawString("Z", 90, 56, 0xFFFFFFFF);
        s = "" +te.getField(TileEntityMillking.FIELD_RANGEZ);
        font.drawString(s, 125, 56, 0xFFFFFFFF);

        font.drawString("NEXT", 25, 20, 0xFFFFFFFF);
        s = "" + (int)((((TileEntityPowerBase)te).getTickTime() - te.getField(TileEntityPowerBase.FIELD_TIMECNT))/20);
        font.drawString(s, 27, 30, 0xFF7F1F8F);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
    	super.drawGuiContainerBackgroundLayer(f, x, y);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;

        int tankLen =tankLength();
        this.blit(i+8, j+70-tankLen, 176, 34, 12, tankLen);
    }

    private int tankLength(){
    	return MathHelper.floor(50.0F *((float)te.getField(TileEntityMillking.FIELD_TANK)/(float)TileEntityMillking.MAX_TANK));
    }
}
