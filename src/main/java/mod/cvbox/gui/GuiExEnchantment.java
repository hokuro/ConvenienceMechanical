package mod.cvbox.gui;

import mod.cvbox.core.Mod_ConvenienceBox;
import mod.cvbox.core.Utils;
import mod.cvbox.inventory.ContainerExEnchantment;
import mod.cvbox.network.MessageHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiExEnchantment  extends GuiContainer
{
    private static final ResourceLocation ENCHANTMENT_TABLE_GUI_TEXTURE = new ResourceLocation("cvbox:textures/gui/exenchanting_table.png");
    private final InventoryPlayer playerInventory;
    private final ContainerExEnchantment container;

    private ItemStack last = ItemStack.EMPTY;

    public GuiExEnchantment(InventoryPlayer inventory, World worldIn)
    {
        super( new ContainerExEnchantment(inventory, worldIn));
        this.playerInventory = inventory;
        this.container = (ContainerExEnchantment)this.inventorySlots;
    }

    @Override
    public void initGui(){
        super.initGui();
    	int x=this.width/2;
    	int y=this.height/2;
    	this.buttons.clear();
    	GuiButton b1 = new GuiButton(101,x+42,y-80,20,20,"←"){
    		@Override
    		public void onClick(double mouseX, double mosueY){
    			actionPerformed(this);
    		}
    	};
    	this.buttons.add(b1);

    	GuiButton b2 = new GuiButton(102,x+62,y-80,20,20,"→"){
    		@Override
    		public void onClick(double mouseX, double mosueY){
    			actionPerformed(this);
    		}
    	};
    	this.buttons.add(b2);

    	GuiButton b3 = new GuiButton(103,x+40,y-40,20,20,"+"){
    		@Override
    		public void onClick(double mouseX, double mosueY){
    			actionPerformed(this);
    		}
    	};
    	this.buttons.add(b3);

    	GuiButton b4 = new GuiButton(104,x+40,y-20,20,20,"-"){
    		@Override
    		public void onClick(double mouseX, double mosueY){
    			actionPerformed(this);
    		}
    	};
    	this.buttons.add(b4);

    	this.children.addAll(buttons);
    	container.setEnchantInfo(-1, 0);
    }

    protected void actionPerformed(GuiButton button)
    {
    	boolean send_message = false;
    	int before_idx = -1;
    	int enc_index = container.getEnchantIndex();
    	int enc_level = container.getEnchantLevel();

    	switch(button.id){
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
    		//Mod_ConvenienceBox.Net_Instance.sendToServer(new MessageExEnchant_UpdateParameter(enc_index, enc_level));
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

        if (l >= 0 && i1 >= 0 && l < 108 && i1 < 19 && this.container.enchantItem(this.mc.player))
        {
        	MessageHandler.SendMessage_ExEnchant_ExEnchant();
            //Mod_ConvenienceBox.Net_Instance.sendToServer(new MessageExEnchant_ExecEnchant());
        }
        return true;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.fontRenderer.drawString("Enchant", 12, 5, 4210752);
        this.fontRenderer.drawString("Inventory", 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(ENCHANTMENT_TABLE_GUI_TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);


        int i1 = i + 60;
        this.zLevel = 0.0F;
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        int enc_index = container.getEnchantIndex();
        int enc_level = container.getEnchantLevel();
        Enchantment enchant = enc_index >= 0 ? Mod_ConvenienceBox.encList.get(enc_index):null;

        if (enchant == null)
        {
            this.drawTexturedModalRect(i1, j + 24, 0, 185, 108, 19);
        }
        else
        {
        	String enc_name = Utils.getEnchName(enchant);
        	int need = container.getNeedLevel();
        	String s = "" + need;
        	String sl = "" + enc_level;
            FontRenderer fontrenderer = this.mc.fontRenderer;
            int i2 = 16777215;//6839882;

            if (((this.mc.player.experienceLevel < need)  && !this.mc.player.abilities.isCreativeMode) ||
            		!container.checkTributeItemCnt()){
                this.drawTexturedModalRect(i1, j + 24, 0, 185, 108, 19);
                fontrenderer.drawString(enc_name, i1+5, j + 26, (i2 & 16711422) >> 1);
                i2 = 4226832;
            }
            else
            {
                int j2 = mouseX - (i + 60);
                int k2 = mouseY - (j + 23);

                if (j2 >= 0 && k2 >= 0 && j2 < 108 && k2 < 19)
                {
                    this.drawTexturedModalRect(i1, j + 24, 0, 204, 108, 19);
                    i2 = 16777088;
                }
                else
                {
                    this.drawTexturedModalRect(i1, j + 24, 0, 166, 108, 19);
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
        partialTicks = this.mc.getTickLength();
        this.drawDefaultBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
}
