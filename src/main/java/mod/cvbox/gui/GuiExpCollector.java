package mod.cvbox.gui;

import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import mod.cvbox.core.ModCommon;
import mod.cvbox.core.Mod_ConvenienceBox;
import mod.cvbox.core.PlayerExpBank;
import mod.cvbox.inventory.ContainerExpCollector;
import mod.cvbox.network.MessageEXPCollector_LevelUp;
import mod.cvbox.tileentity.TileEntityExpCollector;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

public class GuiExpCollector  extends GuiContainer{

	public static ArrayList<String> player_name = new ArrayList();
	public static ArrayList<Integer> box_exp = new ArrayList();
	public static ArrayList<Integer> player_exp = new ArrayList();
	private int input_exp = 0;
	private int place = 7;
	private int[] counterA = new int[this.place];
	private int[] counterA_x = new int[this.place];
	private int[] counterA_y = new int[this.place];
	private int[] counterB = new int[this.place];
	private int[] counterB_x = new int[this.place];
	private int[] counterB_y = new int[this.place];
	private int[] counterC = new int[this.place];
	private int[] counterC_x = new int[this.place];
	private int[] counterC_y = new int[this.place];

	private EntityPlayer player;
	private TileEntityExpCollector collector;

	public GuiExpCollector(EntityPlayer player, IInventory collector){
		super(new ContainerExpCollector(player.inventory, collector));
		this.player = player;
		this.collector = (TileEntityExpCollector)collector;
		this.xSize = 220;
	}

	public static void getPacket(String name, int exp2, int exp3){
		int list_no = -1;
		for (int n = 0; n < player_name.size(); n++){
			if (((String)player_name.get(n)).equals(name)){
				list_no = n;
				box_exp.set(n,  Integer.valueOf(exp2));
				player_exp.set(n, Integer.valueOf(exp3));
				break;
			}
		}
		if(list_no == -1){
			player_name.add(name);
			box_exp.add(Integer.valueOf(exp2));
			player_exp.add(Integer.valueOf(exp3));
			list_no = player_name.size() -1;
		}
	}

	public int search_user(String name){
		int list_no = -1;
		for (int n = 0; n < player_name.size(); n++){
			if(((String)player_name.get(n)).equals(name)){
				list_no = n;
				break;
			}
		}
		if(list_no == -1){
			player_name.add(name);
			box_exp.add(Integer.valueOf(0));
			player_exp.add(Integer.valueOf(0));
			list_no = player_name.size()-1;
		}
		return list_no;
	}

	public void initGui(){
		super.initGui();

		cal_counter();
		show_counter();

		int x = ((this.width-xSize)/2);
		int y = ((this.height-ySize)/2);

		int button_width = 20;
		int button_height = 20;

		this.buttonList.add(new GuiButton(1, x + 172, y + 7, button_width, button_height,
				I18n.translateToLocal("gui.button_1")));
		this.buttonList.add(new GuiButton(2, x + 172, y + 29, button_width, button_height,
				I18n.translateToLocal("gui.button_2")));
		this.buttonList.add(new GuiButton(3, x + 172, y + 51, button_width, button_height,
				I18n.translateToLocal("gui.button_3")));
		this.buttonList.add(new GuiButton(4, x + 172, y + 73, button_width, button_height,
				I18n.translateToLocal("gui.button_4")));
		this.buttonList.add(new GuiButton(5, x + 172, y + 94, button_width, button_height,
				I18n.translateToLocal("gui.button_5")));
		this.buttonList.add(new GuiButton(6, x + 193, y + 7, button_width, button_height,
				I18n.translateToLocal("gui.button_6")));
		this.buttonList.add(new GuiButton(7, x + 193, y + 29, button_width, button_height,
				I18n.translateToLocal("gui.button_7")));
		this.buttonList.add(new GuiButton(8, x + 193, y + 51, button_width, button_height,
				I18n.translateToLocal("gui.button_8")));
		this.buttonList.add(new GuiButton(9, x + 193, y + 73, button_width, button_height,
				I18n.translateToLocal("gui.button_9")));
		this.buttonList.add(new GuiButton(0, x + 193, y + 94, button_width, button_height,
				I18n.translateToLocal("gui.button_0")));

		this.buttonList.add(new GuiButton(20, x + 172, y + 119, 41,
				button_height, I18n.translateToLocal("gui.button_del")));
		this.buttonList.add(new GuiButton(21, x + 172, y + 141, 41, button_height,
				I18n.translateToLocal("gui.button_get")));
		this.buttonList.add(new GuiButton(22, x + 135, y + 40, 34, 20,
				I18n.translateToLocal("gui.button_all")));
	}

	public void drawScreen(int p1, int p2, float p3){
		super.drawScreen(p1, p2, p3);;
	}

	public void keyTyped(char p1, int p2){
		try{
			super.keyTyped(p1, p2);
		}catch(IOException e){
		}
		if (PlayerExpBank.use_keyboard){
			if ((p2 == 11) || (p2 == 82)) {
				push_exp(0);
			}
			if ((p2 == 2) || (p2 == 79)) {
				push_exp(1);
			}
			if ((p2 == 3) || (p2 == 80)) {
				push_exp(2);
			}
			if ((p2 == 4) || (p2 == 81)) {
				push_exp(3);
			}
			if ((p2 == 5) || (p2 == 75)) {
				push_exp(4);
			}
			if ((p2 == 6) || (p2 == 76)) {
				push_exp(5);
			}
			if ((p2 == 7) || (p2 == 77)) {
				push_exp(6);
			}
			if ((p2 == 8) || (p2 == 71)) {
				push_exp(7);
			}
			if ((p2 == 9) || (p2 == 72)) {
				push_exp(8);
			}
			if ((p2 == 10) || (p2 == 73)) {
				push_exp(9);
			}
			if ((p2 == 14) || (p2 == 211)) {
				this.input_exp = 0;
			}
		}
	}

	protected void drawGuiContainerForegroundLayer(int param1, int param2) {
		this.fontRenderer.drawString(I18n.translateToLocal("gui.title.expcollector"), 8, 4, 4210752);
	}



	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		ResourceLocation GUI_RESOURCE_LOCATION = new ResourceLocation(ModCommon.MOD_ID+":"+"textures/gui/expcollector.png");
		this.mc.renderEngine.bindTexture(GUI_RESOURCE_LOCATION);
		int x = ((this.width - xSize) / 2);
		int y = ((this.height - ySize) / 2);
		drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

		cal_counter();
		show_counter();
		drawTexturedModalRect(x + 56 + 0, y + 40, this.counterA_x[0], this.counterA_y[0], 8, 12);
		drawTexturedModalRect(x + 56 + 8, y + 40, this.counterA_x[1], this.counterA_y[1], 8, 12);
		drawTexturedModalRect(x + 56 + 16, y + 40, this.counterA_x[2], this.counterA_y[2], 8, 12);
		drawTexturedModalRect(x + 56 + 24, y + 40, this.counterA_x[3], this.counterA_y[3], 8, 12);
		drawTexturedModalRect(x + 56 + 32, y + 40, this.counterA_x[4], this.counterA_y[4], 8, 12);
		drawTexturedModalRect(x + 56 + 40, y + 40, this.counterA_x[5], this.counterA_y[5], 8, 12);
		drawTexturedModalRect(x + 56 + 48, y + 40, this.counterA_x[6], this.counterA_y[6], 8, 12);

		drawTexturedModalRect(x + 112, y + 40, 231, 167, 15, 12);

		drawTexturedModalRect(x + 56 + 0, y + 54, this.counterC_x[0], this.counterC_y[0], 8, 12);
		drawTexturedModalRect(x + 56 + 8, y + 54, this.counterC_x[1], this.counterC_y[1], 8, 12);
		drawTexturedModalRect(x + 56 + 16, y + 54, this.counterC_x[2], this.counterC_y[2], 8, 12);
		drawTexturedModalRect(x + 56 + 24, y + 54, this.counterC_x[3], this.counterC_y[3], 8, 12);
		drawTexturedModalRect(x + 56 + 32, y + 54, this.counterC_x[4], this.counterC_y[4], 8, 12);
		drawTexturedModalRect(x + 56 + 40, y + 54, this.counterC_x[5], this.counterC_y[5], 8, 12);
		drawTexturedModalRect(x + 56 + 48, y + 54, this.counterC_x[6], this.counterC_y[6], 8, 12);

//		drawTexturedModalRect(x + 116, y + 42, 123, 205, 15, 12);
	}

	public void actionPerformed(GuiButton guibutton) {
		push_button(guibutton.id);
	}


	public boolean push_button(int button_id) {
		int exp;
		switch (button_id) {
		case 0:
			push_exp(0);
			return true;
		case 1:
			push_exp(1);
			return true;
		case 2:
			push_exp(2);
			return true;
		case 3:
			push_exp(3);
			return true;
		case 4:
			push_exp(4);
			return true;
		case 5:
			push_exp(5);
			return true;
		case 6:
			push_exp(6);
			return true;
		case 7:
			push_exp(7);
			return true;
		case 8:
			push_exp(8);
			return true;
		case 9:
			push_exp(9);
			return true;
		case 20:
			this.input_exp = 0;
			return true;
		case 21:
			Mod_ConvenienceBox.Net_Instance.sendToServer(new MessageEXPCollector_LevelUp(this.input_exp));
			collector.setField(TileEntityExpCollector.FIELD_EXPVALUE, 0);
			this.input_exp = 0;
			return true;
		case 22:
			Mod_ConvenienceBox.Net_Instance.sendToServer(new MessageEXPCollector_LevelUp(-1));
			this.input_exp = 0;
			return true;
		}
		return false;
	}

	public void push_exp(int num) {
		if (num < 0) {
			return;
		}
		if (this.counterC[0] > 0) {
			for (int i = 0; i < this.place; i++) {
				this.counterC[i] = 9;
			}
		} else {
			for (int i = 0; i < this.place - 1; i++) {
				if ((this.counterC[i] < 0) && (this.counterC[(i + 1)] >= 0)) {
					this.counterC[i] = this.counterC[(i + 1)];
					this.counterC[(i + 1)] = -1;
				}
			}
			this.counterC[(this.place - 1)] = num;
		}
		if (this.counterC[0] > 0) {
			this.input_exp = (this.counterC[0] * 1000000 + this.counterC[1] * 100000 + this.counterC[2] * 10000
					+ this.counterC[3] * 1000 + this.counterC[4] * 100 + this.counterC[5] * 10 + this.counterC[6]);
		} else if (this.counterC[1] > 0) {
			this.input_exp = (this.counterC[1] * 100000 + this.counterC[2] * 10000 + this.counterC[3] * 1000
					+ this.counterC[4] * 100 + this.counterC[5] * 10 + this.counterC[6]);
		} else if (this.counterC[2] > 0) {
			this.input_exp = (this.counterC[2] * 10000 + this.counterC[3] * 1000 + this.counterC[4] * 100
					+ this.counterC[5] * 10 + this.counterC[6]);
		} else if (this.counterC[3] > 0) {
			this.input_exp = (this.counterC[3] * 1000 + this.counterC[4] * 100 + this.counterC[5] * 10
					+ this.counterC[6]);
		} else if (this.counterC[4] > 0) {
			this.input_exp = (this.counterC[4] * 100 + this.counterC[5] * 10 + this.counterC[6]);
		} else if (this.counterC[5] > 0) {
			this.input_exp = (this.counterC[5] * 10 + this.counterC[6]);
		} else {
			this.input_exp = this.counterC[6];
		}
	}

	public void cal_counter() {
		int exp_value = collector.getField(TileEntityExpCollector.FIELD_EXPVALUE);
		this.counterA[0] = (exp_value / 1000000);
		if (this.counterA[0] <= 0) {
			this.counterA[0] = -1;
		}
		this.counterA[1] = (exp_value % 1000000 / 100000);
		if ((this.counterA[1] <= 0) && (this.counterA[0] <= 0)) {
			this.counterA[1] = -1;
		}
		this.counterA[2] = (exp_value % 1000000 % 100000 / 10000);
		if ((this.counterA[2] <= 0) && (this.counterA[1] <= 0) && (this.counterA[0] <= 0)) {
			this.counterA[2] = -1;
		}
		this.counterA[3] = (exp_value % 1000000 % 100000 % 10000 / 1000);
		if ((this.counterA[3] <= 0) && (this.counterA[2] <= 0) && (this.counterA[1] <= 0)
				&& (this.counterA[0] <= 0)) {
			this.counterA[3] = -1;
		}
		this.counterA[4] = (exp_value % 1000000 % 100000 % 10000 % 1000 / 100);
		if ((this.counterA[4] <= 0) && (this.counterA[3] <= 0) && (this.counterA[2] <= 0) && (this.counterA[1] <= 0)
				&& (this.counterA[0] <= 0)) {
			this.counterA[4] = -1;
		}
		this.counterA[5] = (exp_value % 1000000 % 100000 % 10000 % 1000 % 100
				/ 10);
		if ((this.counterA[5] <= 0) && (this.counterA[4] <= 0) && (this.counterA[3] <= 0) && (this.counterA[2] <= 0)
				&& (this.counterA[1] <= 0) && (this.counterA[0] <= 0)) {
			this.counterA[5] = -1;
		}
		this.counterA[6] = (exp_value % 1000000 % 100000 % 10000 % 1000 % 100 % 10);

		if (this.input_exp <= 0) {
			this.input_exp = 0;
			for (int i = 0; i < this.place - 1; i++) {
				this.counterC[i] = -1;
			}
			this.counterC[(this.place - 1)] = 0;
		} else {
			this.counterC[0] = (this.input_exp / 1000000);
			if (this.counterC[0] <= 0) {
				this.counterC[0] = -1;
			}
			this.counterC[1] = (this.input_exp % 1000000 / 100000);
			if ((this.counterC[1] <= 0) && (this.counterC[0] <= 0)) {
				this.counterC[1] = -1;
			}
			this.counterC[2] = (this.input_exp % 1000000 % 100000 / 10000);
			if ((this.counterC[2] <= 0) && (this.counterC[1] <= 0) && (this.counterC[0] <= 0)) {
				this.counterC[2] = -1;
			}
			this.counterC[3] = (this.input_exp % 1000000 % 100000 % 10000 / 1000);
			if ((this.counterC[3] <= 0) && (this.counterC[2] <= 0) && (this.counterC[1] <= 0)
					&& (this.counterC[0] <= 0)) {
				this.counterC[3] = -1;
			}
			this.counterC[4] = (this.input_exp % 1000000 % 100000 % 10000 % 1000 / 100);
			if ((this.counterC[4] <= 0) && (this.counterC[3] <= 0) && (this.counterC[2] <= 0) && (this.counterC[1] <= 0)
					&& (this.counterC[0] <= 0)) {
				this.counterC[4] = -1;
			}
			this.counterC[5] = (this.input_exp % 1000000 % 100000 % 10000 % 1000 % 100 / 10);
			if ((this.counterC[5] <= 0) && (this.counterC[4] <= 0) && (this.counterC[3] <= 0) && (this.counterC[2] <= 0)
					&& (this.counterC[1] <= 0) && (this.counterC[0] <= 0)) {
				this.counterC[5] = -1;
			}
			this.counterC[6] = (this.input_exp % 1000000 % 100000 % 10000 % 1000 % 100 % 10);
		}
	}

	public void show_counter() {
		for (int i = 0; i < this.place; i++) {
			if (this.counterA[i] < 0) {
				this.counterA_x[i] = 220;
				this.counterA_y[i] = 167;
			} else {
				this.counterA_x[i] = (110 + 11 * this.counterA[i]);
				this.counterA_y[i] = 167;
			}
		}

		for (int i = 0; i < this.place; i++) {
			if (this.counterC[i] < 0) {
				this.counterC_x[i] = 220;
				this.counterC_y[i] = 197;
			} else {
				this.counterC_x[i] = (110 + 11 * this.counterC[i]);
				this.counterC_y[i] = 197;
			}
		}
	}
}