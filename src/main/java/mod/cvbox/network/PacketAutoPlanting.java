package mod.cvbox.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import mod.cvbox.core.ModCommon;
import mod.cvbox.entity.TileEntityAutoPlanting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PacketAutoPlanting extends AbstractPacket{
	private int buttonId;
	public ItemStack[] viewItemStack = new ItemStack[ModCommon.PLANTER_MAX_SLOT];
	public int[] itemList = new int[ModCommon.PLANTER_MAX_SLOT];
	public int[] metaList = new int[ModCommon.PLANTER_MAX_SLOT];

	private int x = 0;
	private int y = 0;
	private int z = 0;

	public PacketAutoPlanting(){}

	public PacketAutoPlanting(int[] it, int[] mt, int xx, int yy, int zz){
		itemList =it;
		metaList = mt;
		x = xx;
		y = yy;
		z = zz;
	}

	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer){
		buffer.writeInt(x);
		buffer.writeInt(y);
		buffer.writeInt(z);
		for ( int i = 0; i <ModCommon.PLANTER_MAX_SLOT; i++){
			buffer.writeInt(itemList[i]);
		}
		for (int i = 0; i< ModCommon.PLANTER_MAX_SLOT; i++){
			buffer.writeInt(metaList[i]);
		}
	}

	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer){
		x = buffer.readInt();
		y = buffer.readInt();
		z = buffer.readInt();
		for (int i = 0; i< ModCommon.PLANTER_MAX_SLOT; i++){
			itemList[i] = buffer.readInt();
		}
		for ( int i = 0; i< ModCommon.PLANTER_MAX_SLOT; i++){
			itemList[i] = buffer.readInt();
		}
	}

	public void handleClientSide(EntityPlayer player){
		World world = player.worldObj;

		TileEntity tilEntity = world.getTileEntity(new BlockPos(x,y,z));
		if ((tilEntity instanceof TileEntityAutoPlanting)){
			TileEntityAutoPlanting tileEntityNoop = (TileEntityAutoPlanting) tilEntity;
	//		tileEntityNoop.setViewItemList(itemList, metaList);
		}
	}

	public void handleServerSide(EntityPlayer player){
	}

//	public void sendPacketToServer(PacketGuiButton packetGuiButton) {
//		// TODO 自動生成されたメソッド・スタブ
//
//	}
}