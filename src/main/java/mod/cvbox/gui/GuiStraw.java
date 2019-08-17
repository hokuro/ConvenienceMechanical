package mod.cvbox.gui;

import org.apache.commons.lang3.BooleanUtils;

import mod.cvbox.inventory.ContainerStraw;
import mod.cvbox.network.MessageHandler;
import mod.cvbox.tileentity.TileEntityStraw;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class GuiStraw extends GuiContainer {

	private static final ResourceLocation TEXTURE = new ResourceLocation("cvbox:textures/gui/straw.png");
	private TileEntityStraw te;

	public GuiStraw(IInventory playerinv, IInventory containerinv){
		super(new ContainerStraw(playerinv, containerinv));
    	te = (TileEntityStraw)containerinv;
	}

	@Override
	public void initGui(){
		super.initGui();
    	int x=(this.width - this.xSize) / 2;
    	int y=(this.height - this.ySize) / 2;
    	this.buttons.clear();
    	GuiButton b1 = new GuiButton(101,x+102, y+16,20,20,"-"){
    		@Override
    		public void onClick(double mouseX, double mouseY){
    			actionPerformed(this);
    		}
    	};
    	GuiButton b2 = new GuiButton(102,x+149, y+16,20,20,"+"){
    		@Override
    		public void onClick(double mouseX, double mouseY){
    			actionPerformed(this);
    		}
    	};
    	GuiButton b3 = new GuiButton(103,x+102, y+37,20,20,"-"){
    		@Override
    		public void onClick(double mouseX, double mouseY){
    			actionPerformed(this);
    		}
    	};
    	GuiButton b4 = new GuiButton(104,x+149, y+37,20,20,"+"){
    		@Override
    		public void onClick(double mouseX, double mouseY){
    			actionPerformed(this);
    		}
    	};
    	GuiButton b5 = new GuiButton(106,x+149, y+58,20,20,"clear"){
    		@Override
    		public void onClick(double mouseX, double mouseY){
    			actionPerformed(this);
    		}
    	};
    	GuiButton b6 = new GuiButton(107,x+42, y+51,40,20,"get all"){
    		@Override
    		public void onClick(double mouseX, double mouseY){
    			actionPerformed(this);
    		}
    	};


    	this.buttons.add(b1);
    	this.buttons.add(b2);
    	this.buttons.add(b3);
    	this.buttons.add(b4);
    	this.buttons.add(b5);
    	this.buttons.add(b6);

    	this.children.addAll(buttons);
	}

    protected void actionPerformed(GuiButton button)
    {
    	int w = 0;
    	int d = 0;
    	switch(button.id){
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

    	case 106:
    		MessageHandler.SendMessage_Straw_CreaArea();
    		//Mod_ConvenienceBox.Net_Instance.sendToServer(new MessageStraw_ClearArea());
    		break;

    	case 107:
    		MessageHandler.SendMessage_Straw_GetAll();
    		//Mod_ConvenienceBox.Net_Instance.sendToServer(new MessageStraw_GetAll());
    		break;
    	}

    	if (w != 0 || d != 0){
        	MessageHandler.SendMessage_Straw_AreaSizeUpdate(w,d);
    		//Mod_ConvenienceBox.Net_Instance.sendToServer(new MessageStraw_AreaSizeUpdate(w,d));
    	}

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;

        double l = mouseX - (i + 46);
        double i1 = mouseY - (j + 19);

        if (l >= 0 && i1 >= 0 && l < 26 && i1 < 18)
        {
        	MessageHandler.SendMessage_BoxSwitchChante(!BooleanUtils.toBoolean(te.getField(TileEntityStraw.FIELD_POWER)));
        	//Mod_ConvenienceBox.Net_Instance.sendToServer(new Message_BoxSwitchChange(!BooleanUtils.toBoolean(te.getField(TileEntityStraw.FIELD_POWER))));
        }
        return true;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.fontRenderer.drawString("Liquid Container", 12, 5, 4210752);
        this.fontRenderer.drawString("Inventory", 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);

        int i1 = i + 60;
        this.zLevel = 0.0F;

        int tankLen =tankLength();
        if (te.getField(TileEntityStraw.FIELD_KIND) == 1){
        	this.drawTexturedModalRect(i+8, j+70-tankLen, 192, 0, 12, tankLen);
        }else{
        	this.drawTexturedModalRect(i+8, j+70-tankLen, 176, 0, 12, tankLen);
        }
        // COUNTER
        this.drawTexturedModalRect(i+42, j+20, 176, 50, 2, getBatteryBaar());

        if (!BooleanUtils.toBoolean(te.getField(TileEntityStraw.FIELD_POWER))){
        	this.drawTexturedModalRect(i+46, j+19, 176, 65, 26, 18);
        }

        // エリアサイズ
        fontRenderer.drawString("Wid", i+84, j+21, 0xFFFFFFFF);
        String s = "" +te.getField(TileEntityStraw.FIELD_WIDTH);
        fontRenderer.drawString(s, i+125, j+21, 0xFFFFFFFF);

        fontRenderer.drawString("Dep", i+84, j+42, 0xFFFFFFFF);
        s = "" +te.getField(TileEntityStraw.FIELD_DEPTH);
        fontRenderer.drawString(s, i+125, j+42, 0xFFFFFFFF);

        s = "" +te.getField(TileEntityStraw.FIELD_SIZE) +
        		"("+te.getField(TileEntityStraw.FIELD_X)+","
        		+te.getField(TileEntityStraw.FIELD_Y)+","
        		+te.getField(TileEntityStraw.FIELD_Z)+")";
        fontRenderer.drawString(s, i+105, j+63, 0xFFFFFFFF);
    }

    private int tankLength(){
    	return MathHelper.floor(50.0F *((float)te.getField(TileEntityStraw.FIELD_TANK)/(float)TileEntityStraw.MAX_TANK));
    }

    protected int getBatteryBaar(){
		return 16-(int)(16.0F * (te.getField(TileEntityStraw.FIELD_BATTERYMAX) - te.getField(TileEntityStraw.FIELD_BATTERY))/te.getField(TileEntityStraw.FIELD_BATTERYMAX));
	}
}
