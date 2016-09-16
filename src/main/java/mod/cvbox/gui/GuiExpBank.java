package mod.cvbox.gui;

import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import basashi.expbox.core.ExpBox;
import mod.cvbox.core.PlayerExpBank;
import mod.cvbox.network.Message0;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class GuiExpBank extends GuiContainer{
	private EntityPlayer player;
	private int xCoord;
	private int yCoord;
	private int zCoord;
	private int x = 0;
	private int y = 0;
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

	public GuiExpBank(EntityPlayer player, World world, int x, int y, int z){
		super(new ContainerExpBank(player, world, x, y, z));
		xCoord = x;
		yCoord = y;
		zCoord = z;
		this.player = player;
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
		this.xSize = 176;
		this.ySize = 166;
		super.initGui();

		cal_counter();
		show_counter();
		if(this.x == 0){
			this.x = ((this.width-this.xSize)/2);
		}
		if(this.y==0){
			this.y = ((this.height-this.ySize)/2);
		}
		this.x = ((this.width-this.xSize)/2);
		this.y = ((this.height-this.ySize)/2);

		int button_width = 20;
		int button_height = 20;

		int tmp_x = 13;
		int tmp_y = 62;

		this.buttonList.add(new GuiButton(1, this.x + tmp_x, this.y + tmp_y, button_width, button_height,
				I18n.translateToLocal("gui.button_1")));
		this.buttonList.add(new GuiButton(2, this.x + tmp_x + 25, this.y + tmp_y, button_width, button_height,
				I18n.translateToLocal("gui.button_2")));
		this.buttonList.add(new GuiButton(3, this.x + tmp_x + 50, this.y + tmp_y, button_width, button_height,
				I18n.translateToLocal("gui.button_3")));
		this.buttonList.add(new GuiButton(4, this.x + tmp_x, this.y + tmp_y + 25, button_width, button_height,
				I18n.translateToLocal("gui.button_4")));
		this.buttonList.add(new GuiButton(5, this.x + tmp_x + 25, this.y + tmp_y + 25, button_width, button_height,
				I18n.translateToLocal("gui.button_5")));
		this.buttonList.add(new GuiButton(6, this.x + tmp_x + 50, this.y + tmp_y + 25, button_width, button_height,
				I18n.translateToLocal("gui.button_6")));
		this.buttonList.add(new GuiButton(7, this.x + tmp_x, this.y + tmp_y + 50, button_width, button_height,
				I18n.translateToLocal("gui.button_7")));
		this.buttonList.add(new GuiButton(8, this.x + tmp_x + 25, this.y + tmp_y + 50, button_width, button_height,
				I18n.translateToLocal("gui.button_8")));
		this.buttonList.add(new GuiButton(9, this.x + tmp_x + 50, this.y + tmp_y + 50, button_width, button_height,
				I18n.translateToLocal("gui.button_9")));
		this.buttonList.add(new GuiButton(0, this.x + tmp_x, this.y + tmp_y + 75, button_width, button_height,
				I18n.translateToLocal("gui.button_0")));
		this.buttonList.add(new GuiButton(10, this.x + tmp_x + 25, this.y + tmp_y + 75, button_width * 2 + 5,
				button_height, I18n.translateToLocal("gui.button_del")));

		this.buttonList.add(new GuiButton(11, this.x + tmp_x + 80, this.y + tmp_y, 75, button_height,
				I18n.translateToLocal("gui.button_alldep")));
		this.buttonList.add(new GuiButton(12, this.x + tmp_x + 80, this.y + tmp_y + 25, 75, button_height,
				I18n.translateToLocal("gui.button_allwd")));
		this.buttonList.add(new GuiButton(13, this.x + tmp_x + 80, this.y + tmp_y + 75, 35, button_height,
				I18n.translateToLocal("gui.button_dep")));
		this.buttonList.add(new GuiButton(14, this.x + tmp_x + 120, this.y + tmp_y + 75, 35, button_height,
				I18n.translateToLocal("gui.button_wd")));
		this.buttonList.add(new GuiButton(17, this.x + tmp_x + 80, this.y + tmp_y + 50, 75, button_height,
				I18n.translateToLocal("gui.button_setlv")));
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
			if (p2 == 28) {
				push_button(17);
			}
		}
	}

	protected void drawGuiContainerForegroundLayer(int param1, int param2) {
		this.fontRendererObj.drawString(I18n.translateToLocal("gui.title"), 8, 4, 4210752);
	}



	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		ResourceLocation GUI_RESOURCE_LOCATION = new ResourceLocation("expbox","textures/gui/gui_expbank.png");
		this.mc.renderEngine.bindTexture(GUI_RESOURCE_LOCATION);
		this.x = ((this.width - this.xSize) / 2);
		this.y = ((this.height - this.ySize) / 2);
		drawTexturedModalRect(this.x, this.y, 0, 0, this.xSize, this.ySize);

		cal_counter();
		show_counter();
		drawTexturedModalRect(this.x + 59 + 0, this.y + 14, this.counterA_x[0], this.counterA_y[0], 8, 12);
		drawTexturedModalRect(this.x + 59 + 8, this.y + 14, this.counterA_x[1], this.counterA_y[1], 8, 12);
		drawTexturedModalRect(this.x + 59 + 16, this.y + 14, this.counterA_x[2], this.counterA_y[2], 8, 12);
		drawTexturedModalRect(this.x + 59 + 24, this.y + 14, this.counterA_x[3], this.counterA_y[3], 8, 12);
		drawTexturedModalRect(this.x + 59 + 32, this.y + 14, this.counterA_x[4], this.counterA_y[4], 8, 12);
		drawTexturedModalRect(this.x + 59 + 40, this.y + 14, this.counterA_x[5], this.counterA_y[5], 8, 12);
		drawTexturedModalRect(this.x + 59 + 48, this.y + 14, this.counterA_x[6], this.counterA_y[6], 8, 12);

		drawTexturedModalRect(this.x + 116, this.y + 14, 123, 175, 15, 12);

		drawTexturedModalRect(this.x + 59 + 0, this.y + 28, this.counterB_x[0], this.counterB_y[0], 8, 12);
		drawTexturedModalRect(this.x + 59 + 8, this.y + 28, this.counterB_x[1], this.counterB_y[1], 8, 12);
		drawTexturedModalRect(this.x + 59 + 16, this.y + 28, this.counterB_x[2], this.counterB_y[2], 8, 12);
		drawTexturedModalRect(this.x + 59 + 24, this.y + 28, this.counterB_x[3], this.counterB_y[3], 8, 12);
		drawTexturedModalRect(this.x + 59 + 32, this.y + 28, this.counterB_x[4], this.counterB_y[4], 8, 12);
		drawTexturedModalRect(this.x + 59 + 40, this.y + 28, this.counterB_x[5], this.counterB_y[5], 8, 12);
		drawTexturedModalRect(this.x + 59 + 48, this.y + 28, this.counterB_x[6], this.counterB_y[6], 8, 12);

		drawTexturedModalRect(this.x + 116, this.y + 28, 123, 190, 15, 12);

		drawTexturedModalRect(this.x + 59 + 0, this.y + 42, this.counterC_x[0], this.counterC_y[0], 8, 12);
		drawTexturedModalRect(this.x + 59 + 8, this.y + 42, this.counterC_x[1], this.counterC_y[1], 8, 12);
		drawTexturedModalRect(this.x + 59 + 16, this.y + 42, this.counterC_x[2], this.counterC_y[2], 8, 12);
		drawTexturedModalRect(this.x + 59 + 24, this.y + 42, this.counterC_x[3], this.counterC_y[3], 8, 12);
		drawTexturedModalRect(this.x + 59 + 32, this.y + 42, this.counterC_x[4], this.counterC_y[4], 8, 12);
		drawTexturedModalRect(this.x + 59 + 40, this.y + 42, this.counterC_x[5], this.counterC_y[5], 8, 12);
		drawTexturedModalRect(this.x + 59 + 48, this.y + 42, this.counterC_x[6], this.counterC_y[6], 8, 12);

		drawTexturedModalRect(this.x + 116, this.y + 42, 123, 205, 15, 12);
	}

	public void actionPerformed(GuiButton guibutton) {
		push_button(guibutton.id);
	}

	public boolean push_button(int button_id) {
		int list_no = search_user(this.player.getPersistentID().toString());
		if (list_no < 0) {
			return false;
		}
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
		case 10:
			this.input_exp = 0;
			return true;
		case 11:
			ExpBox.INSTANCE.sendToServer(new Message0(2, this.xCoord, this.yCoord, this.zCoord,
					((Integer) player_exp.get(list_no)).intValue()));
			this.input_exp = 0;
			return true;
		case 12:
			if (((Integer) box_exp.get(list_no)).intValue() > PlayerExpBank.MAX_EXP) {
				exp = PlayerExpBank.MAX_EXP;
			} else {
				exp = ((Integer) box_exp.get(list_no)).intValue();
			}
			ExpBox.INSTANCE.sendToServer(new Message0(1, this.xCoord, this.yCoord, this.zCoord, exp));
			this.input_exp = 0;
			return true;
		case 13:
			if (this.input_exp <= 0) {
				return false;
			}
			if (this.input_exp > ((Integer) player_exp.get(list_no)).intValue()) {
				exp = ((Integer) player_exp.get(list_no)).intValue();
			} else {
				exp = this.input_exp;
			}
			ExpBox.INSTANCE.sendToServer(new Message0(4, this.xCoord, this.yCoord, this.zCoord, exp));
			this.input_exp = 0;
			return true;
		case 14:
			if (this.input_exp <= 0) {
				return false;
			}
			exp = this.input_exp;
			ExpBox.INSTANCE.sendToServer(new Message0(3, this.xCoord, this.yCoord, this.zCoord, exp));
			this.input_exp = 0;
			return true;
		case 17:
			if (this.input_exp <= 0) {
				return false;
			}
			exp = this.input_exp;
			ExpBox.INSTANCE.sendToServer(new Message0(7, this.xCoord, this.yCoord, this.zCoord, exp));
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
		int list_no = search_user(this.player.getPersistentID().toString());
		if (list_no < 0) {
			for (int i = 0; i < this.place - 1; i++) {
				this.counterA[i] = -1;
			}
			this.counterA[(this.place - 1)] = 0;
			return;
		}
		if (((Integer) box_exp.get(list_no)).intValue() <= 0) {
			box_exp.set(list_no, Integer.valueOf(0));
			for (int i = 0; i < this.place - 1; i++) {
				this.counterA[i] = -1;
			}
			this.counterA[(this.place - 1)] = 0;
		} else if (((Integer) box_exp.get(list_no)).intValue() >= PlayerExpBank.MAX_EXP) {
			for (int i = 0; i < this.place; i++) {
				this.counterA[i] = 9;
			}
		} else {
			this.counterA[0] = (((Integer) box_exp.get(list_no)).intValue() / 1000000);
			if (this.counterA[0] <= 0) {
				this.counterA[0] = -1;
			}
			this.counterA[1] = (((Integer) box_exp.get(list_no)).intValue() % 1000000 / 100000);
			if ((this.counterA[1] <= 0) && (this.counterA[0] <= 0)) {
				this.counterA[1] = -1;
			}
			this.counterA[2] = (((Integer) box_exp.get(list_no)).intValue() % 1000000 % 100000 / 10000);
			if ((this.counterA[2] <= 0) && (this.counterA[1] <= 0) && (this.counterA[0] <= 0)) {
				this.counterA[2] = -1;
			}
			this.counterA[3] = (((Integer) box_exp.get(list_no)).intValue() % 1000000 % 100000 % 10000 / 1000);
			if ((this.counterA[3] <= 0) && (this.counterA[2] <= 0) && (this.counterA[1] <= 0)
					&& (this.counterA[0] <= 0)) {
				this.counterA[3] = -1;
			}
			this.counterA[4] = (((Integer) box_exp.get(list_no)).intValue() % 1000000 % 100000 % 10000 % 1000 / 100);
			if ((this.counterA[4] <= 0) && (this.counterA[3] <= 0) && (this.counterA[2] <= 0) && (this.counterA[1] <= 0)
					&& (this.counterA[0] <= 0)) {
				this.counterA[4] = -1;
			}
			this.counterA[5] = (((Integer) box_exp.get(list_no)).intValue() % 1000000 % 100000 % 10000 % 1000 % 100
					/ 10);
			if ((this.counterA[5] <= 0) && (this.counterA[4] <= 0) && (this.counterA[3] <= 0) && (this.counterA[2] <= 0)
					&& (this.counterA[1] <= 0) && (this.counterA[0] <= 0)) {
				this.counterA[5] = -1;
			}
			this.counterA[6] = (((Integer) box_exp.get(list_no)).intValue() % 1000000 % 100000 % 10000 % 1000 % 100
					% 10);
		}
		if (((Integer) player_exp.get(list_no)).intValue() <= 0) {
			player_exp.set(list_no, Integer.valueOf(0));
			for (int i = 0; i < this.place - 1; i++) {
				this.counterB[i] = -1;
			}
			this.counterB[(this.place - 1)] = 0;
		} else if (((Integer) player_exp.get(list_no)).intValue() >= PlayerExpBank.MAX_EXP) {
			for (int i = 0; i < this.place; i++) {
				this.counterB[i] = 9;
			}
		} else {
			this.counterB[0] = (((Integer) player_exp.get(list_no)).intValue() / 1000000);
			if (this.counterB[0] <= 0) {
				this.counterB[0] = -1;
			}
			this.counterB[1] = (((Integer) player_exp.get(list_no)).intValue() % 1000000 / 100000);
			if ((this.counterB[1] <= 0) && (this.counterB[0] <= 0)) {
				this.counterB[1] = -1;
			}
			this.counterB[2] = (((Integer) player_exp.get(list_no)).intValue() % 1000000 % 100000 / 10000);
			if ((this.counterB[2] <= 0) && (this.counterB[1] <= 0) && (this.counterB[0] <= 0)) {
				this.counterB[2] = -1;
			}
			this.counterB[3] = (((Integer) player_exp.get(list_no)).intValue() % 1000000 % 100000 % 10000 / 1000);
			if ((this.counterB[3] <= 0) && (this.counterB[2] <= 0) && (this.counterB[1] <= 0)
					&& (this.counterB[0] <= 0)) {
				this.counterB[3] = -1;
			}
			this.counterB[4] = (((Integer) player_exp.get(list_no)).intValue() % 1000000 % 100000 % 10000 % 1000 / 100);
			if ((this.counterB[4] <= 0) && (this.counterB[3] <= 0) && (this.counterB[2] <= 0) && (this.counterB[1] <= 0)
					&& (this.counterB[0] <= 0)) {
				this.counterB[4] = -1;
			}
			this.counterB[5] = (((Integer) player_exp.get(list_no)).intValue() % 1000000 % 100000 % 10000 % 1000 % 100
					/ 10);
			if ((this.counterB[5] <= 0) && (this.counterB[4] <= 0) && (this.counterB[3] <= 0) && (this.counterB[2] <= 0)
					&& (this.counterB[1] <= 0) && (this.counterB[0] <= 0)) {
				this.counterB[5] = -1;
			}
			this.counterB[6] = (((Integer) player_exp.get(list_no)).intValue() % 1000000 % 100000 % 10000 % 1000 % 100
					% 10);
		}
		if (this.input_exp <= 0) {
			this.input_exp = 0;
			for (int i = 0; i < this.place - 1; i++) {
				this.counterC[i] = -1;
			}
			this.counterC[(this.place - 1)] = 0;
		} else if (this.input_exp >= PlayerExpBank.MAX_EXP) {
			for (int i = 0; i < this.place; i++) {
				this.counterC[i] = 9;
			}
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
				this.counterA_x[i] = 112;
				this.counterA_y[i] = 175;
			} else {
				this.counterA_x[i] = (2 + 11 * this.counterA[i]);
				this.counterA_y[i] = 175;
			}
		}
		for (int i = 0; i < this.place; i++) {
			if (this.counterB[i] < 0) {
				this.counterB_x[i] = 112;
				this.counterB_y[i] = 190;
			} else {
				this.counterB_x[i] = (2 + 11 * this.counterB[i]);
				this.counterB_y[i] = 190;
			}
		}
		for (int i = 0; i < this.place; i++) {
			if (this.counterC[i] < 0) {
				this.counterC_x[i] = 112;
				this.counterC_y[i] = 205;
			} else {
				this.counterC_x[i] = (2 + 11 * this.counterC[i]);
				this.counterC_y[i] = 205;
			}
		}
	}
}
