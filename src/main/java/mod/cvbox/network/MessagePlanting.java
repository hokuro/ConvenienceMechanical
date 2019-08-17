package mod.cvbox.network;

import java.util.function.Supplier;

import mod.cvbox.tileentity.TileEntityPlanter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessagePlanting {

	private int x;
	private int y;
	private int z;

	public MessagePlanting(int xx, int yy, int zz){
		x = xx;
		y = yy;
		z = zz;
	}

	public static void encode(MessagePlanting pkt, PacketBuffer buf)
	{
		buf.writeInt(pkt.x);
		buf.writeInt(pkt.y);
		buf.writeInt(pkt.z);
	}

	public static MessagePlanting decode(PacketBuffer buf)
	{
		return new MessagePlanting(buf.readInt(),buf.readInt(),buf.readInt());
	}

	public static class Handler
	{
		public static void handle(final MessagePlanting pkt, Supplier<NetworkEvent.Context> ctx)
		{
			ctx.get().enqueueWork(() -> {
				EntityPlayer player = ctx.get().getSender();
				TileEntity tilEntity = player.world.getTileEntity(new BlockPos(pkt.x,pkt.y,pkt.z));
				if ((tilEntity instanceof TileEntityPlanter)){
					TileEntityPlanter tileEntityNoop = (TileEntityPlanter) tilEntity;
				}
			});
			ctx.get().setPacketHandled(true);
		}
	}

}
