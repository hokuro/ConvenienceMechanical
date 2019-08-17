package mod.cvbox.gui;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.BooleanUtils;

import mod.cvbox.inventory.ContainerKiller;
import mod.cvbox.network.MessageHandler;
import mod.cvbox.tileentity.TileEntityCompresser;
import mod.cvbox.tileentity.TileEntityKiller;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.IRegistry;
import net.minecraft.world.World;

public class GuiKiller extends GuiContainer {

	private static final ResourceLocation TEXTURE = new ResourceLocation("cvbox:textures/gui/killer.png");
	private BlockPos pos;
	private World world;
	private int index;
	private String target;
	private List<ResourceLocation> entityNames;
	private TileEntityKiller te;

	public GuiKiller(IInventory playerinv, IInventory containerinv, World world, BlockPos pos){
		super(new ContainerKiller(playerinv, containerinv,world,pos));
		this.pos = pos;;
		this.world = world;
		entityNames = new ArrayList<ResourceLocation>();
		IRegistry.field_212629_r.forEach((ent)->{
			if (ent.create(world) instanceof EntityLivingBase){
				entityNames.add(ent.getRegistryName());
			}
		});
		xSize = 247;
    	index = 0;
    	target = IRegistry.field_212629_r.func_212608_b(entityNames.get(index)).getEntityClass().getSimpleName();
    	te = (TileEntityKiller)containerinv;


	}

	@Override
	public void initGui(){
		super.initGui();
    	int x=(this.width - this.xSize) / 2;
    	int y=(this.height - this.ySize) / 2;
    	this.buttons.clear();
    	GuiButton b1 = new GuiButton(101,x+22,y+40,20,20,"←"){
    		@Override
    		public void onClick(double mouseX, double mouseY){
    			actionPerformed(this);
    		}
    	};
    	GuiButton b2 = new GuiButton(102,x+135,y+40,20,20,"→"){
    		@Override
    		public void onClick(double mouseX, double mouseY){
    			actionPerformed(this);
    		}
    	};
    	GuiButton b3 = new GuiButton(103,x+172, y+16,20,20,"-"){
    		@Override
    		public void onClick(double mouseX, double mouseY){
    			actionPerformed(this);
    		}
    	};
    	GuiButton b4 = new GuiButton(104,x+219, y+16,20,20,"+"){
    		@Override
    		public void onClick(double mouseX, double mouseY){
    			actionPerformed(this);
    		}
    	};
    	GuiButton b5 = new GuiButton(105,x+172, y+46,20,20,"-"){
    		@Override
    		public void onClick(double mouseX, double mouseY){
    			actionPerformed(this);
    		}
    	};
    	GuiButton b6 = new GuiButton(106,x+219, y+46,20,20,"+"){
    		@Override
    		public void onClick(double mouseX, double mouseY){
    			actionPerformed(this);
    		}
    	};
    	GuiButton b7 = new GuiButton(107,x+172, y+77,20,20,"-"){
    		@Override
    		public void onClick(double mouseX, double mouseY){
    			actionPerformed(this);
    		}
    	};
    	GuiButton b8 = new GuiButton(108,x+219, y+77,20,20,"+"){
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
    	this.buttons.add(b7);
    	this.buttons.add(b8);
    	this.children.addAll(buttons);

	}

    protected void actionPerformed(GuiButton button)
    {
    	boolean send = false;
    	int size;
    	switch(button.id){
    	case 101:
    		index--;
    		if (index < 0){
    			index = entityNames.size()-1;
    		}
    		target =  I18n.format(IRegistry.field_212629_r.func_212608_b(entityNames.get(index)).getTranslationKey());
	    break;
    	case 102:
    		index++;
    		if (index >=entityNames.size()){
    			index = 0;
    		}
    		target = I18n.format(IRegistry.field_212629_r.func_212608_b(entityNames.get(index)).getTranslationKey());
	    break;
    	case 103:
    		size = te.getField(TileEntityKiller.FIELD_AREASIZEX) - 1;
    		if (size < 0){size = 0;}
    		else{
    			te.setField(TileEntityKiller.FIELD_AREASIZEX,size);
    			send=true;
    			}
    		break;
    	case 104:
    		size = te.getField(TileEntityKiller.FIELD_AREASIZEX) + 1;
    		if (size > 255){size = 255;}
    		else{
    			te.setField(TileEntityKiller.FIELD_AREASIZEX,size);
    			send=true;
    			}
    		break;

    	case 105:
    		size = te.getField(TileEntityKiller.FIELD_AREASIZEY) - 1;
    		if (size < 0){size = 0;}
    		else{
    			te.setField(TileEntityKiller.FIELD_AREASIZEY,size);
    			send=true;
    			}
    		break;
    	case 106:
    		size = te.getField(TileEntityKiller.FIELD_AREASIZEY) + 1;
    		if (size > 255){size = 255;}
    		else{
    			te.setField(TileEntityKiller.FIELD_AREASIZEY,size);
    			send=true;
    			}
    		break;

    	case 107:
    		size = te.getField(TileEntityKiller.FIELD_AREASIZEZ) - 1;
    		if (size < 0){size = 0;}
    		else{
    			te.setField(TileEntityKiller.FIELD_AREASIZEZ,size);
    			send=true;
    			}
    		break;
    	case 108:
    		size = te.getField(TileEntityKiller.FIELD_AREASIZEZ) + 1;
    		if (size > 255){size = 255;}
    		else{
    			te.setField(TileEntityKiller.FIELD_AREASIZEZ,size);
    			send=true;
    			}
    		break;
    	}

    	if (send){
    		MessageHandler.SendMessage_Killer_UpdateAreaSize(
    				te.getField(TileEntityKiller.FIELD_AREASIZEX),
        	    	te.getField(TileEntityKiller.FIELD_AREASIZEY),
        	    	te.getField(TileEntityKiller.FIELD_AREASIZEZ));
//        	Mod_ConvenienceBox.Net_Instance.sendToServer(new MessageKiller_UpdateAreaSize(
//        	    	te.getField(TileEntityKiller.FIELD_AREASIZEX),
//        	    	te.getField(TileEntityKiller.FIELD_AREASIZEY),
//        	    	te.getField(TileEntityKiller.FIELD_AREASIZEZ)
//        			));
    	}

    }



    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;

        double l = mouseX - (i + 43);
        double i1 = mouseY - (j + 40);

        if (l >= 0 && i1 >= 0 && l < 90 && i1 < 19)
        {
        	MessageHandler.sendMessage_Killer_UpdateTarget(entityNames.get(this.index).toString());
            //Mod_ConvenienceBox.Net_Instance.sendToServer(new MessageKiller_UpdateTarget(entityNames.get(this.index).toString()));
            te.updateTarget(entityNames.get(this.index).toString());
        }

        l = mouseX - (i + 144);
        i1 = mouseY - (j + 5);

        if (l >= 0 && i1 >= 0 && l < 26 && i1 < 18)
        {
        	MessageHandler.SendMessage_BoxSwitchChante(!BooleanUtils.toBoolean(te.getField(TileEntityCompresser.FIELD_POWER)));
        	//Mod_ConvenienceBox.Net_Instance.sendToServer(new Message_BoxSwitchChange(!BooleanUtils.toBoolean(te.getField(TileEntityCompresser.FIELD_POWER))));
        }
        return true;
    }
    	@Override
	    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	    {
	        this.fontRenderer.drawString("Killer", 12, 5, 4210752);
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

            // COUNTER
            this.drawTexturedModalRect(i+139, j+6, 108, 166, 2, getBatteryBaar());

            if (!BooleanUtils.toBoolean(te.getField(TileEntityKiller.FIELD_POWER))){
            	this.drawTexturedModalRect(i+144, j+6, 108, 183, 26, 18);
            }

	        int i1 = i + 60;
	        this.zLevel = 0.0F;
	        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
	        if (te.ContainTarget(entityNames.get(index))){
	        	this.drawTexturedModalRect(i+43, j+40, 0, 185, 108, 19);
	        }
            FontRenderer fontrenderer = this.mc.fontRenderer;
            fontrenderer.drawString(target, i+47, j+47, 0xFFFFFFFF);


            fontrenderer.drawString("X size", i+182, j+8, 0xFFFFFFFF);
            String s = "" +te.getField(TileEntityKiller.FIELD_AREASIZEX);
            fontrenderer.drawString(s, i+195, j+21, 0xFFFFFFFF);

            fontrenderer.drawString("Y size", i+182, j+38, 0xFFFFFFFF);
            s = "" +te.getField(TileEntityKiller.FIELD_AREASIZEY);
            fontrenderer.drawString(s, i+195, j+52, 0xFFFFFFFF);

            fontrenderer.drawString("Z size", i+182, j+68, 0xFFFFFFFF);
            s = "" +te.getField(TileEntityKiller.FIELD_AREASIZEZ);
            fontrenderer.drawString(s, i+195, j+82, 0xFFFFFFFF);


	    }

	   @Override
	    public void render(int mouseX, int mouseY, float partialTicks)
	    {
	        partialTicks = this.mc.getTickLength();
	        this.drawDefaultBackground();
	        super.render(mouseX, mouseY, partialTicks);
	        this.renderHoveredToolTip(mouseX, mouseY);
	    }

		protected int getBatteryBaar(){
			return 16-(int)(16.0F * (te.getField(TileEntityKiller.FIELD_BATTERYMAX) - te.getField(TileEntityKiller.FIELD_BATTERY))/te.getField(TileEntityKiller.FIELD_BATTERYMAX));
		}
}
