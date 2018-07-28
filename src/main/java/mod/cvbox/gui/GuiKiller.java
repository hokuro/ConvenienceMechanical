package mod.cvbox.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.BooleanUtils;

import mod.cvbox.core.Mod_ConvenienceBox;
import mod.cvbox.inventory.ContainerKiller;
import mod.cvbox.network.MessageKiller_UpdateAreaSize;
import mod.cvbox.network.MessageKiller_UpdateTarget;
import mod.cvbox.network.Message_BoxSwitchChange;
import mod.cvbox.tileentity.TileEntityCompresser;
import mod.cvbox.tileentity.TileEntityKiller;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
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
		for (ResourceLocation name : EntityList.getEntityNameList()){
			if (EntityList.createEntityByIDFromName(name, world) instanceof EntityLivingBase){
				entityNames.add(name);
			}
		}
		xSize = 247;
    	index = 0;
    	target = EntityList.getTranslationName(entityNames.get(index));
    	te = (TileEntityKiller)containerinv;


	}

	public void initGui(){
		super.initGui();
    	int x=(this.width - this.xSize) / 2;
    	int y=(this.height - this.ySize) / 2;
    	this.buttonList.clear();
    	this.buttonList.add(new GuiButton(101,x+22,y+40,20,20,"←"));
    	this.buttonList.add(new GuiButton(102,x+135,y+40,20,20,"→"));
    	this.buttonList.add(new GuiButton(103,x+172, y+16,20,20,"-"));
    	this.buttonList.add(new GuiButton(104,x+219, y+16,20,20,"+"));
    	this.buttonList.add(new GuiButton(105,x+172, y+46,20,20,"-"));
    	this.buttonList.add(new GuiButton(106,x+219, y+46,20,20,"+"));
    	this.buttonList.add(new GuiButton(107,x+172, y+77,20,20,"-"));
    	this.buttonList.add(new GuiButton(108,x+219, y+77,20,20,"+"));

	}

    protected void actionPerformed(GuiButton button) throws IOException
    {
    	boolean send = false;
    	int size;
    	switch(button.id){
    	case 101:
    		index--;
    		if (index < 0){
    			index = entityNames.size()-1;
    		}
    		target = EntityList.getTranslationName(entityNames.get(index));
	    break;
    	case 102:
    		index++;
    		if (index >=entityNames.size()){
    			index = 0;
    		}
    		target = EntityList.getTranslationName(entityNames.get(index));
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
        	Mod_ConvenienceBox.Net_Instance.sendToServer(new MessageKiller_UpdateAreaSize(
        	    	te.getField(TileEntityKiller.FIELD_AREASIZEX),
        	    	te.getField(TileEntityKiller.FIELD_AREASIZEY),
        	    	te.getField(TileEntityKiller.FIELD_AREASIZEZ)
        			));
    	}

    }



    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;

        int l = mouseX - (i + 43);
        int i1 = mouseY - (j + 40);

        if (l >= 0 && i1 >= 0 && l < 90 && i1 < 19)
        {
            Mod_ConvenienceBox.Net_Instance.sendToServer(new MessageKiller_UpdateTarget(entityNames.get(this.index).toString()));
            te.updateTarget(entityNames.get(this.index).toString());
        }

        l = mouseX - (i + 144);
        i1 = mouseY - (j + 5);

        if (l >= 0 && i1 >= 0 && l < 26 && i1 < 18)
        {
        	Mod_ConvenienceBox.Net_Instance.sendToServer(new Message_BoxSwitchChange(!BooleanUtils.toBoolean(te.getField(TileEntityCompresser.FIELD_POWER))));
        }
    }

	    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	    {
	        this.fontRenderer.drawString("Killer", 12, 5, 4210752);
	        this.fontRenderer.drawString("Inventory", 8, this.ySize - 96 + 2, 4210752);
	    }

	    /**
	     * Draws the background layer of this container (behind the items).
	     */
	    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	    {
	        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
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
	        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
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

	    /**
	     * Draws the screen and all the components in it.
	     */
	    public void drawScreen(int mouseX, int mouseY, float partialTicks)
	    {
	        partialTicks = this.mc.getTickLength();
	        this.drawDefaultBackground();
	        super.drawScreen(mouseX, mouseY, partialTicks);
	        this.renderHoveredToolTip(mouseX, mouseY);
	    }

		protected int getBatteryBaar(){
			return 16-(int)(16.0F * (te.getField(TileEntityKiller.FIELD_BATTERYMAX) - te.getField(TileEntityKiller.FIELD_BATTERY))/te.getField(TileEntityKiller.FIELD_BATTERYMAX));
		}
}
