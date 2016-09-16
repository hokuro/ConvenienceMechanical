package mod.cvbox.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityExpBank extends TileEntity{
	private int input_exp = 0;
	private int box_exp = 0;
	private String player_name = "";
	private int face = 0;
	private boolean cover_open = false;
	private float angle = -2.1F;

	public TileEntityExpBank(){
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

	public void readFromNBT(NBTTagCompound nbt){
		super.readFromNBT(nbt);;
		input_exp = nbt.getInteger("input_exp");
		box_exp = nbt.getInteger("box_exp");
		player_name = nbt.getString("player");
		face = nbt.getInteger("face");
		cover_open = nbt.getBoolean("cover");
		angle = nbt.getFloat("angle");
	}

	public void writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		nbt.setInteger("input_exp", input_exp);
		nbt.setInteger("box_exp", box_exp);
		nbt.setString("player", player_name);
		nbt.setInteger("face", face);
		nbt.setBoolean("cover", cover_open);
		nbt.setFloat("angle", angle);

	}

	public Packet getDescriptionPacket(){
		NBTTagCompound nbtTagCompound = new NBTTagCompound();
		writeToNBT(nbtTagCompound);
		return new SPacketUpdateTileEntity(pos, 1, nbtTagCompound);
	}

	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt){
		readFromNBT(pkt.getNbtCompound());
	}


	public void update(){
		if((cover_open) && (angle < 0.0F)){
			angle +=0.1F;
			if(angle >= 0.0F){
				angle = 0.0F;
			}
			send_packet(pos.getX(), pos.getY(), pos.getZ(), cover_open);
		}
		if((!cover_open) && (angle > -2.1F)){
			angle -= 0.1F;
			if(angle >= 0.0F){
				angle = 0.0F;
			}
			send_packet(pos.getX(),pos.getY(),pos.getZ(),cover_open);
		}
	}

	public void send_packet(int x, int y, int z, boolean flag){
//		ArrayList<EntityPlayerMP> allp = new ArrayList<EntityPlayerMP>();
//		for (int i = 0; i < MinecraftServer.getServer().worldServers.length; i++) {
//			ListIterator itl = MinecraftServer.getServer().worldServers[i].playerEntities.listIterator();
//			while (itl.hasNext()) {
//				allp.add((EntityPlayerMP) itl.next());
//			}
//		}
//		for (EntityPlayerMP player : allp) {
//			ExpBox.INSTANCE.sendTo(new Message2(x, y, z), player);
//		}
	}

}
