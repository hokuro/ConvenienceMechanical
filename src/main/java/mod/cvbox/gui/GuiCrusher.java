package mod.cvbox.gui;

import java.io.IOException;

import mod.cvbox.core.Mod_ConvenienceBox;
import mod.cvbox.inventory.ContainerCrusher;
import mod.cvbox.network.MessageCrusher_ChangeSelect;
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

	private TileEntityCrusher crusher;


	public GuiCrusher(EntityPlayer player, IInventory tileEntity){
		super(new ContainerCrusher(player.inventory, tileEntity));
		ySize = 222;
		crusher = (TileEntityCrusher)tileEntity;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int i, int j){
		fontRenderer.drawString("Crusher",8,4,4210752);
		fontRenderer.drawString("Inventory", 8, ySize - 96, 4210752);
	}

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
   {
       super.mouseClicked(mouseX, mouseY, mouseButton);
       int i = (this.width - this.xSize) / 2;
       int j = (this.height - this.ySize) / 2;

       int sel = crusher.getField(TileEntityCrusher.FIELD_SELECT);
       int sel2 = -1;
       // Dirt
       int l = mouseX - (i + 27);
       int i1 = mouseY - (j + 40);
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
    	   Mod_ConvenienceBox.Net_Instance.sendToServer(new MessageCrusher_ChangeSelect(sel2));
    	   crusher.setField(TileEntityCrusher.FIELD_SELECT, sel2);
       }
   }

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks){
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);

        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        if (!crusher.inBuket()){
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
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(tex);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);

        int sel = crusher.getField(TileEntityCrusher.FIELD_SELECT);
        int dirt = crusher.getField(TileEntityCrusher.FIELD_DIRT);
        int grave = crusher.getField(TileEntityCrusher.FIELD_GRAVE);
        int sand = crusher.getField(TileEntityCrusher.FIELD_SAND);
        int count = crusher.getField(TileEntityCrusher.FIELD_COUNT);
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
	}

	protected int getDustBar(int id){
		return (int)(20.0F * crusher.getField(id)/100.0F);
	}

	protected int getPlogresBar(){
		return 16-(int)(16.0F * crusher.getField(TileEntityCrusher.FIELD_COUNT)/TileEntityCrusher.CRUSH_TIME);
	}



    protected void drawSlot(ItemStack stackin, int x, int y)
    {
        this.zLevel = 100.0F;
        this.itemRender.zLevel = 100.0F;

        GlStateManager.enableDepth();
        this.itemRender.renderItemAndEffectIntoGUI(this.mc.player, stackin, x, y);
        this.itemRender.zLevel = 0.0F;
        this.zLevel = 0.0F;
    }
}
