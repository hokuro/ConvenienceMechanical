package mod.cvbox.tileentity.work;

import mod.cvbox.entity.EntityCore;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;

public class TileEntityExpBank extends TileEntity{
	public static final String NAME = "expbanck";
	private int input_exp = 0;
	private int box_exp = 0;
	private String player_name = "";
	private int face = 0;
	private boolean cover_open = false;
	private float angle = -2.1F;

	public TileEntityExpBank(){
		super(EntityCore.ExpBank);
	}

	public void set_face(int face){
		this.face = face;
	}

	public int get_face(){
		return this.face;
	}

	public float get_angle(){
		return this.angle;
	}

	public boolean get_cover(){
		return this.cover_open;
	}

	public void set_cover(boolean flag){
		this.cover_open = flag;
	}

	public void set_input_exp(int exp){
		this.input_exp = exp;
	}

	public void set_box_exp(int exp){
		this.box_exp = exp;
	}

	public String get_player(){
		return this.player_name;
	}

	public int get_input_exp(){
		return this.input_exp;
	}

	public int get_box_exp(){
		return this.box_exp;
	}

	@Override
	public void read(CompoundNBT nbt){
		super.read(nbt);;
		input_exp = nbt.getInt("input_exp");
		box_exp = nbt.getInt("box_exp");
		player_name = nbt.getString("player");
		face = nbt.getInt("face");
		cover_open = nbt.getBoolean("cover");
		angle = nbt.getFloat("angle");
	}

	@Override
	public CompoundNBT write(CompoundNBT nbt){
		super.write(nbt);
		nbt.putInt("input_exp", input_exp);
		nbt.putInt("box_exp", box_exp);
		nbt.putString("player", player_name);
		nbt.putInt("face", face);
		nbt.putBoolean("cover", cover_open);
		nbt.putFloat("angle", angle);
		return nbt;

	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket(){
		CompoundNBT CompoundNBT = new CompoundNBT();
		write(CompoundNBT);
		return new SUpdateTileEntityPacket(pos, 1, CompoundNBT);
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt){
		read(pkt.getNbtCompound());
	}



	@Override
	public CompoundNBT serializeNBT() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		// TODO 自動生成されたメソッド・スタブ

	}
}
