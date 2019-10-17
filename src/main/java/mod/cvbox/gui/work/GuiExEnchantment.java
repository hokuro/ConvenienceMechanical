package mod.cvbox.gui.work;

import com.mojang.blaze3d.platform.GlStateManager;

import mod.cvbox.core.Mod_ConvenienceBox;
import mod.cvbox.core.Utils;
import mod.cvbox.inventory.work.ContainerExEnchantment;
import mod.cvbox.network.MessageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiExEnchantment  extends ContainerScreen<ContainerExEnchantment>
{
    private static final ResourceLocation ENCHANTMENT_TABLE_GUI_TEXTURE = new ResourceLocation("cvbox:textures/gui/exenchanting_table.png");


    private ItemStack last = ItemStack.EMPTY;
	public GuiExEnchantment(ContainerExEnchantment container, PlayerInventory playerInv, ITextComponent titleIn){
		super(container, playerInv, titleIn);
	}

    @Override
    public void init(){
        super.init();
    	int x=this.width/2;
    	int y=this.height/2;
    	this.buttons.clear();
    	this.addButton(new Button(x+42,y-80,20,20,"←", (bt)->{actionPerformed(101,bt);}));
    	this.addButton(new Button(x+62,y-80,20,20,"→", (bt)->{actionPerformed(102,bt);}));
    	this.addButton(new Button(x+40,y-40,20,20,"+", (bt)->{actionPerformed(103,bt);}));
    	this.addButton(new Button(x+40,y-20,20,20,"-", (bt)->{actionPerformed(104,bt);}));

    	container.setEnchantInfo(-1, 0);
    }

    protected void actionPerformed(int id, Button button) {
    	boolean send_message = false;
    	int before_idx = -1;
    	int enc_index = container.getEnchantIndex();
    	int enc_level = container.getEnchantLevel();

    	switch(id){
    	case 101:
    		before_idx = enc_index<0?0:enc_index;
    		do{
        		enc_index--;
        		if (enc_index <0){
        			enc_index = Mod_ConvenienceBox.encList.size()-1;
        		}
        		if (this.container.checkEnchantable(enc_index)){
            		enc_level = 1;
            		send_message = true;
        		}
    		}while(enc_index != before_idx && !send_message);
    		break;
    	case 102:
    		before_idx = enc_index<0?Mod_ConvenienceBox.encList.size()-1:enc_index;
    		do{
        		enc_index++;
        		if (enc_index >= Mod_ConvenienceBox.encList.size()){
        			enc_index = 0;
        		}
        		if (this.container.checkEnchantable(enc_index)){
            		enc_level = 1;
            		send_message = true;
        		}
    		}while(enc_index != before_idx && !send_message);
    		break;
    	case 103:
    		if (enc_index >= 0 && enc_level < Mod_ConvenienceBox.encList.get(enc_index).getMaxLevel()){
    			enc_level++;
    		}
    		send_message = true;
    		break;
    	case 104:
    		if (enc_index > 0 && enc_level <=  Mod_ConvenienceBox.encList.get(enc_index).getMaxLevel()){
    			enc_level--;
    		}
    		send_message = true;
    		break;
    	}
    	if (send_message){
    		MessageHandler.SendMessage_ExEnchant_UpdateParameter(enc_index, enc_level);
    		container.setEnchantInfo(enc_index, enc_level);
    	}else{
        	if (enc_index == before_idx){
        		container.setEnchantInfo(-1, 0);
        	}
    	}
    }


    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;

        double l = mouseX - (i + 60);
        double i1 = mouseY - (j + 24);

        if (l >= 0 && i1 >= 0 && l < 108 && i1 < 19 && this.container.enchantItem(Minecraft.getInstance().player)) {
        	MessageHandler.SendMessage_ExEnchant_ExEnchant();
        }
        return true;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.font.drawString("Enchant", 12, 5, 4210752);
        this.font.drawString("Inventory", 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getInstance().getTextureManager().bindTexture(ENCHANTMENT_TABLE_GUI_TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.blit(i, j, 0, 0, this.xSize, this.ySize);


        int i1 = i + 60;
        this.blitOffset = 0;
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        int enc_index = container.getEnchantIndex();
        int enc_level = container.getEnchantLevel();
        Enchantment enchant = enc_index >= 0 ? Mod_ConvenienceBox.encList.get(enc_index):null;

        if (enchant == null)
        {
            this.blit(i1, j + 24, 0, 185, 108, 19);
        }
        else
        {
        	String enc_name = Utils.getEnchName(enchant);
        	int need = container.getNeedLevel();
        	String s = "" + need;
        	String sl = "" + enc_level;
            FontRenderer fontrenderer = Minecraft.getInstance().fontRenderer;
            int i2 = 16777215;//6839882;

            if (((Minecraft.getInstance().player.experienceLevel < need)  && !Minecraft.getInstance().player.abilities.isCreativeMode) ||
            		!container.checkTributeItemCnt()){
                this.blit(i1, j + 24, 0, 185, 108, 19);
                fontrenderer.drawString(enc_name, i1+5, j + 26, (i2 & 16711422) >> 1);
                i2 = 4226832;
            }
            else
            {
                int j2 = mouseX - (i + 60);
                int k2 = mouseY - (j + 23);

                if (j2 >= 0 && k2 >= 0 && j2 < 108 && k2 < 19)
                {
                    this.blit(i1, j + 24, 0, 204, 108, 19);
                    i2 = 16777088;
                }
                else
                {
                    this.blit(i1, j + 24, 0, 166, 108, 19);
                }

                fontrenderer.drawString(enc_name, i1+5, j + 25, i2);
                i2 = 8453920;
            }
            fontrenderer.drawStringWithShadow(s, (float)(i1 + 106 - fontrenderer.getStringWidth(s)), (float)(j + 26 + 7), i2);
            fontrenderer.drawStringWithShadow(sl, (float)(i1 + 106 - fontrenderer.getStringWidth(s)), (float)(j + 46 + 7), i2);
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        partialTicks = Minecraft.getInstance().getTickLength();
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
}
