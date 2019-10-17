package mod.cvbox.gui.factory;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.BooleanUtils;

import com.mojang.blaze3d.platform.GlStateManager;

import mod.cvbox.inventory.factory.ContainerKiller;
import mod.cvbox.network.MessageHandler;
import mod.cvbox.tileentity.ab.TileEntityPowerBase;
import mod.cvbox.tileentity.factory.TileEntityKiller;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;

public class GuiKiller extends ContainerScreen<ContainerKiller> {

	private static final ResourceLocation tex = new ResourceLocation("cvbox:textures/gui/killer.png");
	private int index;
	private String target;
	private List<EntityType> entityNames;

	private TileEntityKiller te;
	public GuiKiller(ContainerKiller container, PlayerInventory playerInv, ITextComponent titleIn){
		super(container, playerInv, titleIn);
		entityNames = new ArrayList<EntityType>();
		Registry.ENTITY_TYPE.forEach((ent)->{
			if (ent.create(playerInv.player.world) instanceof LivingEntity){
				entityNames.add(ent);
			}
		});
		xSize = 247;
    	index = 0;
    	target = I18n.format(entityNames.get(index).getTranslationKey());
    	te = container.getTileEntity();
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
    	this.addButton(new Button(x+22,y+40,20,20,"←", (bt)->{actionPerformed(101, bt);}));
    	this.addButton(new Button(x+135,y+40,20,20,"→", (bt)->{actionPerformed(102, bt);}));
    	this.addButton(new Button(x+172, y+16,20,20,"-", (bt)->{actionPerformed(103, bt);}));
    	this.addButton(new Button(x+219, y+16,20,20,"+", (bt)->{actionPerformed(104, bt);}));
    	this.addButton(new Button(x+172, y+46,20,20,"-", (bt)->{actionPerformed(105, bt);}));
    	this.addButton(new Button(x+219, y+46,20,20,"+", (bt)->{actionPerformed(106, bt);}));
    	this.addButton(new Button(x+172, y+77,20,20,"-", (bt)->{actionPerformed(107, bt);}));
    	this.addButton(new Button(x+219, y+77,20,20,"+", (bt)->{actionPerformed(108, bt);}));
	}

    protected void actionPerformed(int id,Button button) {
    	boolean send = false;
    	int size;
    	switch(id){
    	case 101:
    		index--;
    		if (index < 0){
    			index = entityNames.size()-1;
    		}
    		target =  I18n.format(entityNames.get(index).getTranslationKey());
    		break;
    	case 102:
    		index++;
    		if (index >=entityNames.size()){
    			index = 0;
    		}
    		target = I18n.format(entityNames.get(index).getTranslationKey());
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
    	}

    }
	@Override
	public void render(int mouseX, int mouseY, float partialTicks){
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
	}

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;

        double l = mouseX - (i + 43);
        double i1 = mouseY - (j + 40);

        if (l >= 0 && i1 >= 0 && l < 90 && i1 < 19) {
        	MessageHandler.sendMessage_Killer_UpdateTarget(entityNames.get(this.index).getRegistryName().toString());
            ((TileEntityKiller)te).updateTarget(entityNames.get(this.index).getRegistryName().toString());
        }

        l = mouseX - (i + 146);
        i1 = mouseY - (j + 5);

        if (l >= 0 && i1 >= 0 && l < 26 && i1 < 18) {
        	MessageHandler.SendMessage_BoxSwitchChante(!BooleanUtils.toBoolean(te.getField(TileEntityPowerBase.FIELD_POWER)));
        }
        return true;
    }

    @Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    	this.font.drawString("Killer", 12, 5, 4210752);
    	this.font.drawString("Inventory", 8, this.ySize - 96 + 2, 4210752);

        FontRenderer fontrenderer = Minecraft.getInstance().fontRenderer;
        fontrenderer.drawString(target, 47, 47, 0xFFFFFFFF);


        fontrenderer.drawString("X size", 182, 8, 0xFFFFFFFF);
        String s = "" +te.getField(TileEntityKiller.FIELD_AREASIZEX);
        fontrenderer.drawString(s, 195, 21, 0xFFFFFFFF);

        fontrenderer.drawString("Y size", 182, 38, 0xFFFFFFFF);
        s = "" +te.getField(TileEntityKiller.FIELD_AREASIZEY);
        fontrenderer.drawString(s, 195, 52, 0xFFFFFFFF);

        fontrenderer.drawString("Z size", 182, 68, 0xFFFFFFFF);
        s = "" +te.getField(TileEntityKiller.FIELD_AREASIZEZ);
        fontrenderer.drawString(s, 195, 82, 0xFFFFFFFF);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		// 背景
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getInstance().getTextureManager().bindTexture(getTexture());
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.blit(i, j, 0, 0, this.xSize, this.ySize);
        // COUNTER
        this.blit(i+142, j+6, 108, 166, 2, getBatteryBaar());
        if (!BooleanUtils.toBoolean(te.getField(TileEntityPowerBase.FIELD_POWER))){
        	this.blit(i+146, j+5, 108, 182, 26, 18);
        }

		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		if (((TileEntityKiller)te).ContainTarget(entityNames.get(index).getRegistryName())){
			this.blit(i+43, j+40, 0, 185, 108, 19);
		}
	}

	protected int getBatteryBaar(){
		return 16-(int)(16.0F * (te.getField(TileEntityPowerBase.FIELD_BATTERYMAX) - te.getField(TileEntityPowerBase.FIELD_BATTERY))/te.getField(TileEntityPowerBase.FIELD_BATTERYMAX));
	}

}
