package mod.cvbox.gui.factory;

import com.mojang.blaze3d.platform.GlStateManager;

import mod.cvbox.gui.ab.GuiPowerBase;
import mod.cvbox.inventory.ab.ContainerPowerBase;
import mod.cvbox.network.MessageHandler;
import mod.cvbox.tileentity.factory.TileEntityCrusher;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class GuiCrusher extends GuiPowerBase {
	private static final ResourceLocation tex = new ResourceLocation("cvbox", "textures/gui/crusher.png");
	private static final ItemStack dirt = new ItemStack(Blocks.DIRT);
	private static final ItemStack grave = new ItemStack(Blocks.GRAVEL);
	private static final ItemStack sand = new ItemStack(Blocks.SAND);
	private static final ItemStack clay = new ItemStack(Items.CLAY_BALL);

	public GuiCrusher(ContainerPowerBase container, PlayerInventory playerInv, ITextComponent titleIn){
		super(container, playerInv, titleIn);
		ySize = 222;
	}

	protected ResourceLocation getTexture() {
		return tex;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int i, int j){
		font.drawString("Crusher",8,4,4210752);
		font.drawString("Inventory", 8, ySize - 96, 4210752);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
       super.mouseClicked(mouseX, mouseY, mouseButton);
       int i = (this.width - this.xSize) / 2;
       int j = (this.height - this.ySize) / 2;

       int sel = te.getField(TileEntityCrusher.FIELD_SELECT);
       int sel2 = -1;
       // Dirt
       double l = mouseX - (i + 27);
       double i1 = mouseY - (j + 40);
       if (l >= 0 && i1 >= 0 && l <  15 && i1 < 15) {
    	   sel2 = sel!=1?1:0;
       }

       // Grave
       l = mouseX - (i + 80);
       i1 = mouseY - (j + 40);
       if (l >= 0 && i1 >= 0 && l < 15 && i1 < 15) {
    	   sel2 = sel!=2?2:0;
       }

       // Sand
       l = mouseX - (i + 134);
       i1 = mouseY - (j + 40);
       if (l >= 0 && i1 >= 0 && l <  15 && i1 < 15) {
    	   sel2 = sel!=3?3:0;
       }

       if (sel2 >= 0){
    	   MessageHandler.SendMessage_ChangeSelect(sel2);
    	   te.setField(TileEntityCrusher.FIELD_SELECT, sel2);
       }
       return true;
   }

	@Override
	public void render(int mouseX, int mouseY, float partialTicks){
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);

        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        if (!((TileEntityCrusher)te).inBuket()){
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
		super.drawGuiContainerBackgroundLayer(f, x, y);
		// 背景
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;

        int sel = te.getField(TileEntityCrusher.FIELD_SELECT);
        int dirt = te.getField(TileEntityCrusher.FIELD_DIRT);
        int grave = te.getField(TileEntityCrusher.FIELD_GRAVE);
        int sand = te.getField(TileEntityCrusher.FIELD_SAND);
        int count = te.getField(TileEntityCrusher.FIELD_COUNT);
        // DIRT
        if (sel != 1){
            this.blit(i+25, j+39, 176, 42, 18, 18);
        }
        this.blit(i+24, j+60, 176, 34, getDustBar(TileEntityCrusher.FIELD_DIRT), 8);

        // GRAVE
        if (sel != 2){
            this.blit(i+79, j+39, 176, 42, 18, 18);
        }
        this.blit(i+78, j+60, 176, 34, getDustBar(TileEntityCrusher.FIELD_GRAVE), 8);

        // SAND
        if (sel != 3){
            this.blit(i+133, j+39, 176, 42, 18, 18);
        }
        this.blit(i+132, j+60, 176, 34, getDustBar(TileEntityCrusher.FIELD_SAND), 8);

        // COUNTER
        this.blit(i+99, j+17, 176, 0, 2, getPlogresBar());
	}

	protected int getDustBar(int id){
		return (int)(20.0F * te.getField(id)/100.0F);
	}

	protected int getPlogresBar(){
		return 16-(int)(16.0F * te.getField(TileEntityCrusher.FIELD_COUNT)/TileEntityCrusher.WORK_TIME);
	}

    protected void drawSlot(ItemStack stackin, int x, int y) {
        this.blitOffset = 100;
        this.itemRenderer.zLevel  = 100.0F;

        GlStateManager.enableDepthTest();
        this.itemRenderer.renderItemAndEffectIntoGUI(Minecraft.getInstance().player, stackin, x, y);
        this.itemRenderer.zLevel = 0.0F;
        this.blitOffset = 0;
    }
}
