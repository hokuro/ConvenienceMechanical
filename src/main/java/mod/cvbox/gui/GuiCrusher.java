package mod.cvbox.gui;

import org.apache.commons.lang3.BooleanUtils;

import mod.cvbox.inventory.ContainerCrusher;
import mod.cvbox.network.MessageHandler;
import mod.cvbox.tileentity.TileEntityCrusher;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiCrusher extends GuiContainer {
	private static final ResourceLocation tex = new ResourceLocation("cvbox", "textures/gui/crusher.png");
	private static final ItemStack dirt = new ItemStack(Blocks.DIRT);
	private static final ItemStack grave = new ItemStack(Blocks.GRAVEL);
	private static final ItemStack sand = new ItemStack(Blocks.SAND);
	private static final ItemStack clay = new ItemStack(Items.CLAY_BALL);

	private TileEntityCrusher te;


	public GuiCrusher(EntityPlayer player, IInventory tileEntity){
		super(new ContainerCrusher(player.inventory, tileEntity));
		ySize = 222;
		te = (TileEntityCrusher)tileEntity;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int i, int j){
		fontRenderer.drawString("Crusher",8,4,4210752);
		fontRenderer.drawString("Inventory", 8, ySize - 96, 4210752);
	}

	@Override
   public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
   {
       super.mouseClicked(mouseX, mouseY, mouseButton);
       int i = (this.width - this.xSize) / 2;
       int j = (this.height - this.ySize) / 2;

       int sel = te.getField(TileEntityCrusher.FIELD_SELECT);
       int sel2 = -1;
       // Dirt
       double l = mouseX - (i + 27);
       double i1 = mouseY - (j + 40);
       if (l >= 0 && i1 >= 0 && l <  15 && i1 < 15)
       {
    	   sel2 = sel!=1?1:0;
       }

       // Grave
       l = mouseX - (i + 80);
       i1 = mouseY - (j + 40);
       if (l >= 0 && i1 >= 0 && l < 15 && i1 < 15)
       {
    	   sel2 = sel!=2?2:0;
       }

       // Sand
       l = mouseX - (i + 134);
       i1 = mouseY - (j + 40);
       if (l >= 0 && i1 >= 0 && l <  15 && i1 < 15)
       {
    	   sel2 = sel!=3?3:0;
       }

       if (sel2 >= 0){
    	   MessageHandler.SendMessage_ChangeSelect(sel2);
    	   //Mod_ConvenienceBox.Net_Instance.sendToServer(new MessageCrusher_ChangeSelect(sel2));
    	   te.setField(TileEntityCrusher.FIELD_SELECT, sel2);
       }
       l = mouseX - (i + 146);
       i1 = mouseY - (j + 5);
       if (l >= 0 && i1 >= 0 && l < 26 && i1 < 18)
       {
    	   MessageHandler.SendMessage_BoxSwitchChante(!BooleanUtils.toBoolean(te.getField(TileEntityCrusher.FIELD_POWER)));
           //Mod_ConvenienceBox.Net_Instance.sendToServer(new Message_BoxSwitchChange(!BooleanUtils.toBoolean(te.getField(TileEntityCrusher.FIELD_POWER))));
       }
       return true;
   }

	@Override
	public void render(int mouseX, int mouseY, float partialTicks){
        this.drawDefaultBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);

        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        if (!te.inBuket()){
            // Dirt
            drawSlot(dirt, i+26, j+40);
        }else{
            // Dirt
            drawSlot(clay, i+26, j+40);
        }
        // Grave
        drawSlot(grave, i+80, j+40);
        // Sand
        drawSlot(sand, i+134, j+40);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int x, int y){
		// 背景
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(tex);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);

        int sel = te.getField(TileEntityCrusher.FIELD_SELECT);
        int dirt = te.getField(TileEntityCrusher.FIELD_DIRT);
        int grave = te.getField(TileEntityCrusher.FIELD_GRAVE);
        int sand = te.getField(TileEntityCrusher.FIELD_SAND);
        int count = te.getField(TileEntityCrusher.FIELD_COUNT);
        // DIRT
        if (sel != 1){
            this.drawTexturedModalRect(i+25, j+39, 176, 0, 18, 18);
        }
        this.drawTexturedModalRect(i+24, j+60, 176, 18, getDustBar(TileEntityCrusher.FIELD_DIRT), 8);

        // GRAVE
        if (sel != 2){
            this.drawTexturedModalRect(i+79, j+39, 176, 0, 18, 18);
        }
        this.drawTexturedModalRect(i+78, j+60, 176, 18, getDustBar(TileEntityCrusher.FIELD_GRAVE), 8);

        // SAND
        if (sel != 3){
            this.drawTexturedModalRect(i+133, j+39, 176, 0, 18, 18);
        }
        this.drawTexturedModalRect(i+132, j+60, 176, 18, getDustBar(TileEntityCrusher.FIELD_SAND), 8);

        // COUNTER
        this.drawTexturedModalRect(i+99, j+17, 176, 26, 2, getPlogresBar());

        // COUNTER
        this.drawTexturedModalRect(i+142, j+6, 176, 26, 2, getBatteryBaar());

        if (!BooleanUtils.toBoolean(te.getField(TileEntityCrusher.FIELD_POWER))){
        	this.drawTexturedModalRect(i+146, j+5, 176, 42, 26, 18);
        }
	}

	protected int getDustBar(int id){
		return (int)(20.0F * te.getField(id)/100.0F);
	}

	protected int getPlogresBar(){
		return 16-(int)(16.0F * te.getField(TileEntityCrusher.FIELD_COUNT)/TileEntityCrusher.CRUSH_TIME);
	}

	protected int getBatteryBaar(){
		return 16-(int)(16.0F * (te.getField(TileEntityCrusher.FIELD_BATTERYMAX) - te.getField(TileEntityCrusher.FIELD_BATTERY))/te.getField(TileEntityCrusher.FIELD_BATTERYMAX));
	}

    protected void drawSlot(ItemStack stackin, int x, int y)
    {
        this.zLevel = 100.0F;
        this.itemRender.zLevel = 100.0F;

        GlStateManager.enableDepthTest();
        this.itemRender.renderItemAndEffectIntoGUI(this.mc.player, stackin, x, y);
        this.itemRender.zLevel = 0.0F;
        this.zLevel = 0.0F;
    }
}
